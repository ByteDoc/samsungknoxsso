package org.apache.cordova.plugin;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.content.Context;

import com.sec.android.service.authentication.*;
import com.sec.android.service.singlesignon.aidls.*;


import java.util.*;
import android.util.Log;

public class SamsungKnoxSSO extends CordovaPlugin {
    public enum CordovaAction {
        GET_TOKEN, GENERIC_CALL
    }

	JSONObject argsObject;
    JSONArray argsArray;
    CallbackContext callbackContext;
    CordovaAction action;
	private String authenticationDomain;
    private String logTag = "SamsungKnoxSSO";
	
    private void logInfo(String message) {
        Log.i(logTag, message);
    }
    private void logError(String message) {
        Log.e(logTag, message);
    }
    
    @Override
    public boolean execute(String actionString, JSONArray args, CallbackContext callbackContext) throws JSONException {
        logInfo("execute called for action " + actionString);
        this.callbackContext = callbackContext;
        // read argument object, expected as first entry in args array
        try {
            argsArray = args;
            argsObject = argsArray.getJSONObject(0);
        } catch (JSONException e){
            logError("Error: JSONException " + e + " was thrown. No or bad argument object supplied!");
            callbackContext.error(e.getMessage());
            return false;
        }
        
        
        try {
            action = CordovaAction.valueOf(actionString);
        } catch (IllegalArgumentException e) {
            logError("Error: JSONException " + e + " was thrown. No valid action supplied!");
            callbackContext.error(e.getMessage());
            return false;
        }


        switch (action) {

            case GET_TOKEN:
                getToken();
                return true;
            
            case GENERIC_CALL:
                return genericCall();
        }
        
        return false;
    }
			
	private void getToken() {
		AsyncTask loadTask = new AsyncTask<Void, Void, Void>() {

			//@Override
			protected String doInBackground(Void... params) {
                EnterpriseAuthentication enterpriseAuth = null;
				SecurityToken securityToken = null;
				String mRequestedServiceAccessToken = null;
                
                Context context = cordova.getActivity().getApplicationContext();
                
                try {
                    authenticationDomain = argsObject.getString("authenticationDomain");
                } catch (JSONException e){
                    logError("Error: JSONException " + e + " was thrown. Parameter authenticationDomain not supplied!");
                    callbackContext.error(e.getMessage());
                    return e.getMessage();
                }
                

				try {
                    enterpriseAuth = EnterpriseAuthentication.getInstance(context);
					securityToken = enterpriseAuth.getSecurityToken(
						context,
						"HTTP@"+authenticationDomain,		// Service URL
						SingleSignOnTokenType.KERBEROS
					);
					if ( securityToken != null ) {
						//mRequestedServiceAccessToken can be sent in HTTP authorization header
						mRequestedServiceAccessToken = securityToken.getNegotiateToken();
						
						try {
                            argsObject.put("securityToken", mRequestedServiceAccessToken);
                        } catch (JSONException e){
                            logError("Error: JSONException " + e + " was thrown. argsObject not updated with securityToken!");
                            callbackContext.error(e.getMessage());
                            return e.getMessage();
                        }
                        
						callbackContext.success(argsArray);
					} else {
						callbackContext.error("securityToken received is null");
					}
				} catch (NotAuthenticatedException ex) {
					callbackContext.error("Exception: [" + ex.getMessage()+"]");
				} catch (SecurityProviderNotFoundException ex) {
					callbackContext.error("Exception: [" + ex.getMessage()+"]");
				} 
				return null;
			}
		};
		loadTask.execute();
			
	}
    
    private boolean genericCall() {
        
    }
	
}

