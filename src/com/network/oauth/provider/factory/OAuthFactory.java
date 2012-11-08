package com.network.oauth.provider.factory;

import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;

import android.content.Context;

import com.network.oauth.provider.factory.OAuthFactoryConstants.OAuthDefaults;
import com.network.oauth.provider.factory.OAuthFactoryConstants.OAuthProviderTypes;

/**
 * Class for generating OAuthFactory of a particular type
 * 
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
	 * One of the OAUTH_TYPE constants defined in OAuthTypes. Used to determine
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
	private String authorizationWebsiteUrl;

	/**
	 * Callback Url
	 */
	private String callbackUrl;

	/**
	 * OAuth Provider
	 */
	private CommonsHttpOAuthProvider oAuthProvider = null;

	/**
	 * OAuth Consumer
	 */
	private CommonsHttpOAuthConsumer oAuthConsumer = null;
	
	/**
	 * Context
	 */
	private Context context;
	
	/**
	 * Access Token
	 */
	private AccessToken accessToken = null;

	/**
	 * @param consumerKey OAuth App Consumer Key
	 * @param consumerSecret OAuth App Consumer Secret
	 * @param oauthType One of the OAUTH_TYPE constants defined in OAuthTypes
	 * @param oauthProviderType One of the PROVIDER constants. Use PROVIDER_CUSTOM to define your own, One of the others to automatically configure it for you
	 * @param context Activity context
	 * @throws OAuthFactoryException
	 */
	public OAuthFactory(String consumerKey, String consumerSecret, int oauthType, int oauthProviderType, Context context) throws OAuthFactoryException {

		this.context = context;
		this.consumerKey = consumerKey;
		this.consumerSecret = consumerSecret;
		this.oauthType = oauthType;
		this.oauthProviderType = oauthProviderType;
		
		oAuthConsumer = new CommonsHttpOAuthConsumer(consumerKey, consumerSecret);
		setOAuthProviderType(oauthProviderType);
		setCallbackUrl(null);

	}

	private void setOAuthProviderType(int providerType)
			throws OAuthFactoryException {

		switch (providerType) {

		case OAuthProviderTypes.PROVIDER_FACEBOOK:
			// Init with Facebook Urls
			break;

		case OAuthProviderTypes.PROVIDER_TWITTER:
			// Init with Twitter Urls
			break;

		case OAuthProviderTypes.PROVIDER_CUSTOM:
			// Init other stuff if needed
			break;

		default:
			throw new OAuthFactoryException(
					OAuthFactoryException.OAuthExceptionMessages.OAUTH_UNRECOGNIZED_PROVIDER);
		}
	}

	/**
	 * Set callback url
	 * 
	 * @param callbackUrl
	 */
	public void setCallbackUrl(String callbackUrl) {
		
		if(callbackUrl == null)
			callbackUrl = OAuthDefaults.DEFAULT_OAUTH_PROVIDER_CALLBACK_URL;
		else
			this.callbackUrl = callbackUrl;
	}

	public void initOAuthProvider(String requestTokenEndpointUrl,
			String accessTokenEndpointUrl, String authorizationWebsiteUrl) {

		this.requestTokenEndpointUrl = requestTokenEndpointUrl;
		this.accessTokenEndpointUrl = accessTokenEndpointUrl;
		this.authorizationWebsiteUrl = authorizationWebsiteUrl;

		oAuthProvider = new CommonsHttpOAuthProvider(requestTokenEndpointUrl,
				accessTokenEndpointUrl, authorizationWebsiteUrl);
	}
	
	/**
	 * Interface that specifies 
	 * callback methods
	 * to be called
	 * after the OAuth flow 
	 * 
	 * @author Vinay S Shenoy, Nov 8 2012
	 *
	 */
	public static interface OAuthAuthorizationCallback {
		
		/**
		 * Callback method when authorization was unsuccessful
		 * @param errorCode Error code
		 * @param message Error message
		 */
		public void onOAuthAuthorizationError(int errorCode, String message);
		
		public void onOAuthAuthorizationComplete(AccessToken accessToken);
	}

}
