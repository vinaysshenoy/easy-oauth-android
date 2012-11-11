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

	/**
	 * Get the OAuth token
	 * @return OAuth token, null if not authenticated
	 */
	public String getToken() {
		
		return token;
	}

	/**
	 * Get the OAuth token secret
	 * @return OAuth token secret, null if 
	 * not authenticated or OAuth 2.0
	 */
	public String getTokenSecret() {
		
		return tokenSecret;
	}
	


}
