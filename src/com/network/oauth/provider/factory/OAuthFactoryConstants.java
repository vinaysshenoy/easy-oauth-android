package com.network.oauth.provider.factory;

/**
 * Class that holds some OAuth Constants
 * @author Vinay S Shenoy
 *
 */
public class OAuthFactoryConstants {
	
	/**
	 * Class that holds OAuth type constants
	 * @author Vinay S Shenoy
	 *
	 */
	public static class OAuthTypes {
		
		/**
		 * Implement OAuth 1.0a Authentication
		 */
		public static final int OAUTH_TYPE_1_0_A = 100;
		
		/**
		 * Implement OAuth 2.0 Authentication
		 */
		public static final int OAUTH_TYPE_2_0 = 101;
	}
	
	
	/**
	 * Class that holds some OAuth Provider constants
	 * @author Vinay S Shenoy
	 *
	 */
	public static class OAuthProviderTypes {
		
		/**
		 * Custom OAuth Provider
		 */
		public static final int PROVIDER_CUSTOM = 100;
		
		/**
		 * Facebook OAuth Provider - Currently not functional. Use Custom OAuth
		 */
		public static final int PROVIDER_FACEBOOK = 101;
		
		/**
		 * Twitter OAuth Provider - Currently not functional. Use Custom OAuth
		 */
		public static final int PROVIDER_TWITTER = 102;
	}
	
}
