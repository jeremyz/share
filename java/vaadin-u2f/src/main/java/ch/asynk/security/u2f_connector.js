window.ch_asynk_security_U2fConnector = function() {

    var connector = this;

    this.register = function(requestJson) {
        var request = JSON.parse(requestJson);
        // alert(JSON.stringify(request));
        setTimeout(function() {
            u2f.register(request.registerRequests[0].appId, request.registerRequests, request.authenticateRequests, function(data) {
                if(data.errorCode)
                    connector.onRegisterResponse(data.errorCode, request);
                else
                    connector.onRegisterResponse(JSON.stringify(data));
            });
        }, 5000);
    }

    this.authenticate = function(requestJson) {
        var request = JSON.parse(requestJson);
        // alert(JSON.stringify(request));
        setTimeout(function() {
            u2f.sign(request.authenticateRequests[0].appId, request.authenticateRequests[0].challenge, request.authenticateRequests, function(data) {
                if(data.errorCode)
                    connector.onAuthenticateResponse(data.errorCode, request);
                else
                    connector.onAuthenticateResponse(JSON.stringify(data));
            });
        }, 5000);
    }
}
