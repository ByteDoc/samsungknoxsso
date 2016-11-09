package org.apache.cordova.plugin;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sec.android.service.authentication.*;
import com.sec.android.service.singlesignon.aidls.*;


import java.util.*;

public class SamsungKnoxSSO extends CordovaPlugin {

	private JSONArray argsArray;
	private JSONObject argsObject;
	private CallbackContext callbackContext;
	private String authenticationDomain;
	
	@Override
	public boolean execute(String action, JSONArray args, CallbackContext context) throws JSONException {
		argsArray = args;
		callbackContext = context;

		// get all required parameters
		try{
			argsObject = argsArray.getJSONObject(0);
			
			//authenticationDomain = argsObject.getString("authenticationDomain");
			authenticationDomain = "emm.samsungknox.com";
		}catch(JSONException e){
			callbackContext.error(e.getMessage());
		}	

		
		if (action.equals("getToken")) {
			getToken();
		} else {
			callbackContext.error("Unknow action '" + action + "' in plugin SamsungKnoxSSO");
		}
	}
			
	private void getToken() {
		AsyncTask loadTask = new AsyncTask<Void, Void, Void>() {

			@Override
			protected String doInBackground(Void... params) {
				SecurityToken securityToken = null;
				String mRequestedServiceAccessToken = null;

				try {
					securityToken = EnterpriseAuthentication.getInstance(SamsungKnoxSSO.this).getSecurityToken(
						SamsungKnoxSSO.this,
						"HTTP@"+authenticationDomain,		// Service URL
						SingleSignOnTokenType.KERBEROS
					);
					if ( securityToken != null ) {
						//mRequestedServiceAccessToken can be sent in HTTP authorization header
						mRequestedServiceAccessToken = securityToken.getNegotiateToken();
						JSONObject cbObject = new JSONObject();
						cbObject.put("token", mRequestedServiceAccessToken);
						JSONArray cbArray = new JSONArray();
						cbArray.put(cbOject);
						callbackContext.success(cbArray);
					} else {
						callbackContext.error("Token received is null");
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
	
}