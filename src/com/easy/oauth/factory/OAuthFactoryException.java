package com.easy.oauth.factory;

public class OAuthFactoryException extends Exception {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -6011434830021144460L;

	public OAuthFactoryException(String message) {
		
		super(message);
	}
	
	static class OAuthExceptionMessages {
		
		static final String OAUTH_UNRECOGNIZED_PROVIDER = "Unrecognized OAuth Provider. Provide one of the OAuthProvider constants";
	}
}
