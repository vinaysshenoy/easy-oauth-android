/*Copyright 2012 Vinay S Shenoy

Licensed under the Apache License, Version 2.0 (the "License"); 
you may not use this file except in compliance with the License. 
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, 
software distributed under the License is distributed on 
an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 
either express or implied. See the License for the specific language 
governing permissions and limitations under the License.
 */
package com.vinaysshenoy.easyoauth.factory;

public class OAuthFactoryException extends Exception {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -6011434830021144460L;

	public OAuthFactoryException(String message) {

		super(message);
	}

	static class OAuthExceptionMessages {

		static final String OAUTH_UNRECOGNIZED_PROVIDER = "Unrecognized OAuth Provider. Provide one of the OAuthProvider constants or your own Custom provider config";
		static final String OAUTH_MISSING_CONFIG = "OAuth config cannot be null if using custom provider";
		static final String OAUTH_NULL_CONSUMER_KEY_OR_SECRET = "Consumer Key and Secret cannot be null";
		static final String INCORRECT_OAUTH_TYPE = "Incorrect OAuth Type. Use OAuthTypes.OAUTH_TYPE_1_0_A or OAUTH_TYPE_2_0";
		static final String OAUTH_NULL_ENDPOINT_URLS = "Request token url/access token url/authorization url cannot be null";
		static final String OAUTH_NULL_AUTHORIZATION_URL = "Authorization url cannot be null";
		static final String OAUTH_NOT_AUTHORIZED = "Access token is not set";
		static final String UNSUPPORTED_METHOD = "Only HTTP GET and POST methods are supported";
	}
}
