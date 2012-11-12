package com.easy.oauth.factory;

import com.easy.oauth.http.HttpConfig;

/**
 * Class for OAuth configuration
 * 
 * @author Vinay S Shenoy, Nov 11, 2012
 * 
 */
public class OAuthConfig {

	/**
	 * One of the OAUTH_TYPE constants defined in OAuthTypes. Used to determine
	 * The request methods signing methodology
	 */
	public int oAuthType;

	/**
	 * Endpoint for fetching request tokens
	 */
	public String requestTokenEndpointUrl;

	/**
	 * Endpoint for fetching access tokens
	 */
	public String accessTokenEndpointUrl;

	/**
	 * Endpoint for authorizing the user
	 */
	public String authorizationWebsiteUrl;

	/**
	 * Callback Url to be called after authorization request
	 */
	public String callbackUrl;

	/**
	 * Config for the Http client
	 */
	public HttpConfig httpConfig;
	
	/**
	 * The key for the oAuth verifier, oAuth 1.0a only, default "oauth_verifier"
	 */
	public String oAuthVerifier;
	
	/**
	 * The key used to detect when the user denies the app permissions, default "denied"
	 */
	public String oAuthDenied;
	
	/**
	 * The key used to append the access token when making http requests, OAuth 2.0 only, default "oauth_token"
	 * You can ignore this parameter if you will be making the API requests yourself
	 */
	public String oAuthToken;

	/**
	 * @param oAuthType
	 *            decides the OAuth type. Must be one of the OAuthType constants
	 * @param requestTokenEndpointUrl
	 *            The endpoint for fetching request tokens for the first step of
	 *            OAuth sign in, cannot be null for Oauth 1.0a
	 * @param accessTokenEndpointUrl
	 *            The endpoint for fetching the access token after getting a
	 *            request token, cannot be null for OAuth 1.0a
	 * @param authorizationWebsiteUrl
	 *            The endpoint for authorizing the user, cannot be null for either OAuth 1.0a or 2.0
	 * @throws OAuthFactoryException
	 * @throws NullPointerException
	 */
	public OAuthConfig(int oAuthType, String requestTokenEndpointUrl,
			String accessTokenEndpointUrl, String authorizationWebsiteUrl)
			throws OAuthFactoryException, NullPointerException {

		this.oAuthType = oAuthType;
		this.requestTokenEndpointUrl = requestTokenEndpointUrl;
		this.accessTokenEndpointUrl = accessTokenEndpointUrl;
		this.authorizationWebsiteUrl = authorizationWebsiteUrl;

		switch (this.oAuthType) {

		case OAuthTypes.OAUTH_TYPE_1_0_A:

			//All three URLs are needed
			if (requestTokenEndpointUrl == null
					|| accessTokenEndpointUrl == null
					|| authorizationWebsiteUrl == null) {
				throw new NullPointerException(
						OAuthFactoryException.OAuthExceptionMessages.OAUTH_NULL_ENDPOINT_URLS);
			}
			break;

		case OAuthTypes.OAUTH_TYPE_2_0:
			//Only authorizationWebsiteUrl is needed
			if(authorizationWebsiteUrl == null) {
				throw new NullPointerException(OAuthFactoryException.OAuthExceptionMessages.OAUTH_NULL_AUTHORIZATION_URL);
			}
			break;

		default:
			throw new OAuthFactoryException(
					OAuthFactoryException.OAuthExceptionMessages.INCORRECT_OAUTH_TYPE);
		}
		
		httpConfig = new HttpConfig();
		
		oAuthVerifier = OAuthConfigDefaults.DEFAULT_OAUTH_VERIFIER;
		oAuthDenied = OAuthConfigDefaults.DEFAULT_OAUTH_DENIED;
		oAuthToken = OAuthConfigDefaults.DEFAULT_OAUTH_TOKEN;

	}
	
	private static class OAuthConfigDefaults {
		
		public static final String DEFAULT_OAUTH_VERIFIER = "oauth_verifier";
		public static final String DEFAULT_OAUTH_DENIED = "denied";
		public static final String DEFAULT_OAUTH_TOKEN = "oauth_token";
	}

}
