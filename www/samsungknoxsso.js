var SamsungKnoxSsoPlugin = (function() {
    "use strict";
    var CORDOVA_PLUGIN_NAME = "SamsungKnoxSSO",
		CORDOVA_ACTION_GET_TOKEN = "GET_TOKEN",
        CORDOVA_ACTION_GENERIC_CALL = "GENERIC_CALL",
        argsObject = {},
		argsArray = [],
        successCallback,
        errorCallback;
    function debugLog(message) {
        console.log("samsungknoxsso.js: " + message);
    }
    function isSet(checkVar) {
        return typeof (checkVar) != "undefined" && checkVar !== null && checkVar !== "";
    }
    
    /**
     * ensure that needed values are set in the argsObject
     * and set default values if initial or bad value ...
     */
    function checkArgsObject() {
        argsObject = argsArray[0];

    }
    function getArgsArray(args) {
        // args auf erlaubten typ/inhalt prüfen
        // nur ein Object erlaubt, kein Array!
        if (typeof (args) != "object" || args === null || Array.isArray(args)) {
            args = {};
        }
        return [args];  // Array erstellen
    }
    function init(args, cbSuccess, cbError) {
        debugLog("args before init: " + JSON.stringify(args));
        argsArray = getArgsArray(args);
        checkArgsObject();
        successCallback = cbSuccess;
        errorCallback = cbError;
        debugLog("argsObject at the end of init: " + JSON.stringify(argsObject));
    }
    
    /**
     * === GET_TOKEN ===
     *   - acquire the credential token from the API
     */
    function cordovaExecGetToken() {
        cordova.exec(
            successCallback,
            errorCallback,
            CORDOVA_PLUGIN_NAME,
            CORDOVA_ACTION_GET_TOKEN,
            argsArray
        );
    }
    
    /**
     * === GENERIC_CALL ===
     *   - do a generic call, testing parameters
     */
    function cordovaExecGenericCall() {
        cordova.exec(
            successCallback,
            errorCallback,
            CORDOVA_PLUGIN_NAME,
            CORDOVA_ACTION_GENERIC_CALL,
            argsArray
        );
    }
    
    /**
     *  PUBLIC FUNCTIONS for the plugin
     */
    function getToken(args, successCallback, errorCallback) {
        debugLog("starting getToken");
        // init the plugin class
        init(args, successCallback, errorCallback);
        // call the API
        cordovaExecGetToken();
    }
    function genericCall(args, successCallback, errorCallback) {
        debugLog("starting genericCall");
        // init the plugin class
        init(args, successCallback, errorCallback);
        // call the API
        cordovaExecGenericCall();
    }
    return {
        getToken: getToken,
        genericCall: genericCall
    };

}());

module.exports = {
    getToken: SamsungKnoxSsoPlugin.getToken,
    genericCall: SamsungKnoxSsoPlugin.genericCall
};



