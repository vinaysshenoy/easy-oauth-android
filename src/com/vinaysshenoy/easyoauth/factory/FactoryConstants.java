package com.vinaysshenoy.easyoauth.factory;

/**
 * Class that holds some OAuth Constants
 * @author Vinay S Shenoy
 *
 */
public class FactoryConstants {

	public static final int FAILURE = 602;
	public static final int SUCCESS = 601;
	
	public enum HttpRequestTypes {
		
		GET,
		POST
	}

	public enum OAuthType {
		
		OAUTH_1_0_A,
		OAUTH_2_0
	}

	/**
	 * Different API Providers. Use {@link Provider#CUSTOM} to implement your own,
	 * one of the others to to generate it for you
	 * @author Vinay S Shenoy
	 *
	 */
	public enum Provider {
		
		CUSTOM,
		FACEBOOK,
		TWITTER
	}
	
}


