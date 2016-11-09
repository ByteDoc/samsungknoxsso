function getArgsArray(args) {
	// args auf erlaubten typ/inhalt prüfen
	// nur ein Object erlaubt, kein Array! 
	if (typeof(args) != "object" || args == null || Array.isArray(args)) args = {};
	return [args];	// Array erstellen
}

module.exports = {
	getToken: function (args, successCallback, errorCallback) {
		var argsArray = getArgsArray(args);
		cordova.exec(successCallback, errorCallback, "SamsungKnoxSSO", "getToken", argsArray);
	}
};