package ch.asynk.security;

import java.util.HashMap;
import java.util.Map;

import com.vaadin.annotations.JavaScript;
import com.vaadin.server.AbstractJavaScriptExtension;
import com.vaadin.ui.UI;
import com.vaadin.ui.JavaScriptFunction;
import com.yubico.u2f.U2F;
import com.yubico.u2f.data.DeviceRegistration;
import com.yubico.u2f.data.messages.AuthenticateRequestData;
import com.yubico.u2f.data.messages.AuthenticateResponse;
import com.yubico.u2f.data.messages.RegisterRequestData;
import com.yubico.u2f.data.messages.RegisterResponse;
import com.yubico.u2f.exceptions.DeviceCompromisedException;
import com.yubico.u2f.exceptions.NoEligibleDevicesException;
import elemental.json.JsonArray;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@JavaScript({"u2f-api.js", "u2f_connector.js"})
public class U2fConnector extends AbstractJavaScriptExtension
{
    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(U2fConnector.class);

    // appId MUST match the URL  - https://developers.yubico.com/U2F/App_ID.html
    private final String appId = "https://localhost:8666";

    private final U2F u2f = new U2F();
    // private final U2F u2f = U2F.withoutAppIdValidation();

    // https://developers.yubico.com/U2F/Libraries/Client_error_codes.html
    private static String U2F_ERRORS[] = {"","OTHER_ERROR","BAD_REQUEST","CONFIGURATION_UNSUPPORTED","DEVICE_INELIGIBLE","TIMEOUT","DEVICE_COMPROMISED"};
    public String getErrorMsg(int i) { return U2F_ERRORS[i]; }

    private final U2fService u2fService = new U2fServiceImpl();

    class Request
    {
        String userId;
        String data;
        U2fListener listener;
        public Request(String userId, String data, U2fListener listener)
        {
            this.userId = userId;
            this.data = data;
            this.listener = listener;
        }
    }

    private final Map<String, Request> requests = new HashMap<>();

    public enum U2fAction {
        REGISTRATION_PENDING,
        REGISTRATION_SUCCESS,
        REGISTRATION_FAILURE,
        AUTHENTICATION_PENDING,
        AUTHENTICATION_FAILURE,
        AUTHENTICATION_SUCCESS
    };

    public interface U2fListener
    {
        public void u2fCallback(U2fAction action, String msg);
    }

    public U2fConnector() {
        extend(UI.getCurrent());
        addFunction("onRegisterResponse", new JavaScriptFunction() {
            private static final long serialVersionUID = 1L;
            @Override
            public void call(final JsonArray arguments) {
                onRegisterResponse(arguments);
            }
        });
        addFunction("onAuthenticateResponse", new JavaScriptFunction() {
            private static final long serialVersionUID = 1L;
            @Override
            public void call(final JsonArray arguments) {
                onAuthenticateResponse(arguments);
            }
        });
    }

    public void sendRegisterRequest(final String userId, final U2fListener listener)
    {
        final RegisterRequestData registerRequestData = u2f.startRegistration(appId, u2fService.getDeviceRegistrations(userId));
        final String registerRequestDataJson = registerRequestData.toJson();
        final String registerRequestDataId = registerRequestData.getRequestId();
        requests.put(registerRequestDataId, new Request(userId, registerRequestDataJson, listener));
        callFunction("register", registerRequestDataJson);
        listener.u2fCallback(U2fAction.REGISTRATION_PENDING, null);
        logger.debug(String.format("register[%s] : %s", userId, registerRequestDataJson));
    }

    public void onRegisterResponse(JsonArray arguments)
    {
        if (arguments.get(0) instanceof elemental.json.impl.JreJsonNumber) {
            int errorCode = (int) arguments.getNumber(0);
            // FIXME why does it not work ??
            // final RegisterRequestData registerRequestData = RegisterRequestData.fromJson(arguments.getString(1));
            final String registerRequestDataJson = arguments.getObject(1).toString();
            final RegisterRequestData registerRequestData = RegisterRequestData.fromJson(registerRequestDataJson);
            final Request request = requests.remove(registerRequestData.getRequestId());
            request.listener.u2fCallback(U2fAction.REGISTRATION_FAILURE, getErrorMsg(errorCode));
            logger.debug(String.format("failure[%d] : %s", errorCode, registerRequestDataJson));
        } else {
            final String registerResponseJson = arguments.getString(0);
            final RegisterResponse registerResponse = RegisterResponse.fromJson(registerResponseJson);
            final Request request = requests.remove(registerResponse.getRequestId());
            final RegisterRequestData registerRequestData = RegisterRequestData.fromJson(request.data);
            u2fService.addDeviceRegistration(request.userId, u2f.finishRegistration(registerRequestData, registerResponse));
            request.listener.u2fCallback(U2fAction.REGISTRATION_SUCCESS, null);
            logger.debug(String.format("success : %s", registerResponseJson));
        }
    }

    public void startAuthentication(final String userId, final U2fListener listener)
    {
        try {
            final AuthenticateRequestData authenticateRequestData = u2f.startAuthentication(appId, u2fService.getDeviceRegistrations(userId));
            final String authenticateRequestDataJson = authenticateRequestData.toJson();
            final String authenticateRequestDataId = authenticateRequestData.getRequestId();
            requests.put(authenticateRequestDataId, new Request(userId, authenticateRequestDataJson, listener));
            callFunction("authenticate", authenticateRequestDataJson, userId);
            listener.u2fCallback(U2fAction.AUTHENTICATION_PENDING, null);
            logger.debug(String.format("authenticate[%s] : %s", userId, authenticateRequestDataJson));
        } catch (NoEligibleDevicesException e) {
            listener.u2fCallback(U2fAction.AUTHENTICATION_FAILURE, getErrorMsg(4));
        }
    }

    public void onAuthenticateResponse(JsonArray arguments)
    {
        if (arguments.get(0) instanceof elemental.json.impl.JreJsonNumber) {
            int errorCode = (int) arguments.getNumber(0);
            // FIXME why does it not work ??
            // final AuthenticateRequestData authenticateRequestData = AuthenticateRequestData.fromJson(arguments.getString(1));
            final String authenticateRequestDataJson = arguments.getObject(1).toString();
            final AuthenticateRequestData authenticateRequestData = AuthenticateRequestData.fromJson(authenticateRequestDataJson);
            final Request request = requests.remove(authenticateRequestData.getRequestId());
            request.listener.u2fCallback(U2fAction.AUTHENTICATION_FAILURE, getErrorMsg(errorCode));
            logger.debug(String.format("failure[%d] : %s", errorCode, authenticateRequestDataJson));
        } else {
            final String authenticateResponseJson = arguments.getString(0);
            final AuthenticateResponse authenticateResponse = AuthenticateResponse.fromJson(authenticateResponseJson);
            final Request request = requests.remove(authenticateResponse.getRequestId());
            final AuthenticateRequestData authenticateRequestData = AuthenticateRequestData.fromJson(request.data);
            DeviceRegistration registration = null;
            try {
                registration = u2f.finishAuthentication(authenticateRequestData, authenticateResponse, u2fService.getDeviceRegistrations(request.userId));
            } catch (final DeviceCompromisedException e) {
                request.listener.u2fCallback(U2fAction.AUTHENTICATION_FAILURE, getErrorMsg(6));
                logger.error(String.format("device compromised: %s", request.data));
            }
            request.listener.u2fCallback(U2fAction.AUTHENTICATION_SUCCESS, null);
            logger.debug(String.format("success : %s", authenticateResponseJson));
        }
    }
}
