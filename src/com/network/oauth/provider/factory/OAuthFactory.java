package com.network.oauth.provider.factory;

import com.network.oauth.provider.factory.OAuthFactoryConstants.OAuthProviderTypes;

/**
 * Class for generating OAuthFactory of a particular type
 * @author Vinay S Shenoy
 *
 */
public class OAuthFactory {
	
	/**
	 * Consumer key
	 */
	private String consumerKey;
	
	/**
	 * Consumer Secret
	 */
	private String consumerSecret;
	
	/**
	 * One of the OAUTH_TYPE constants defined
	 * in OAuthTypes. Used to determine
	 * The request methods signing methodology
	 */
	private int oauthType;
	
	/**
	 * One of the PROVIDER constants. Use PROVIDER_CUSTOM to define your own,
	 * One of the others to automatically configure it for you
	 */
	private int oauthProviderType;
	
	/**
	 * Endpoint for fetching request tokens
	 */
	private String requestTokenEndpointUrl;
	
	/**
	 * Endpoint for fetching access tokens
	 */
	private String accessTokenEndpointUrl;
	
	/**
	 * Endpoint for authorizing the user
	 */
	private String authorizeWebsiteUrl;
	
	/**
	 * Callback Url
	 */
	private String callbackUrl;
	
	public OAuthFactory(String consumerKey, String consumerSecret, int oauthType, int oauthProviderType) {
		
		this.consumerKey = consumerKey;
		this.consumerSecret = consumerSecret;
		this.oauthType = oauthType;
		this.oauthProviderType = oauthProviderType;
		
		initOAuthProvider(oauthProviderType);
	}

	private void initOAuthProvider(int providerType) {
		
		switch (providerType) {
		
		case OAuthProviderTypes.PROVIDER_FACEBOOK:
			//Init with Facebook Urls
			break;
			
		case OAuthProviderTypes.PROVIDER_TWITTER:
			//Init with Twitter Urls
			break;

		default:
			break;
		}
	}
	
	

}
