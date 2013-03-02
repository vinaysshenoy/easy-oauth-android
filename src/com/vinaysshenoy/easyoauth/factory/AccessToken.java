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

/**
 * Class that represents an access token
 * @author Vinay S Shenoy
 *
 */
public class AccessToken {
	
	/**
	 * OAuth token
	 */
	private String token;
	
	/**
	 * OAuth token secret
	 * 
	 */
	private String tokenSecret;
	
	public AccessToken(String token, String tokenSecret) {
		
		this.token = token;
		this.tokenSecret = tokenSecret;
	}

	public String getToken() {
		
		return token;
	}

	public String getTokenSecret() {
		
		return tokenSecret;
	}

	void setToken(String token) {
		this.token = token;
	}

	void setTokenSecret(String tokenSecret) {
		this.tokenSecret = tokenSecret;
	}

}
