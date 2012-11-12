package com.easy.oauth.factory;

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
