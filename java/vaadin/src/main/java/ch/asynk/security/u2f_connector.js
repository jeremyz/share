window.ch_asynk_security_U2fConnector = function() {

    var connector = this;

    this.register = function(requestJson) {
        var request = JSON.parse(requestJson);
        // alert(JSON.stringify(request));
        setTimeout(function() {
            u2f.register(request.registerRequests, request.authenticateRequests, function(data) {
                if(data.errorCode)
                    connector.onRegisterResponse(data.errorCode, request);
                else
                    connector.onRegisterResponse(JSON.stringify(data));
            });
        }, 500);
    }

    this.authenticate = function(requestJson) {
        var request = JSON.parse(requestJson);
        //alert(JSON.stringify(request));
        setTimeout(function() {
            u2f.sign(request.authenticateRequests, function(data) {
                if(data.errorCode)
                    connector.onAuthenticateResponse(data.errorCode, request);
                else
                    connector.onAuthenticateResponse(JSON.stringify(data));
            });
        }, 500);
    }
}
