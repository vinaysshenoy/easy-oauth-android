package com.easy.oauth.factory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Set;

import org.apache.http.HttpConnection;
import org.apache.http.HttpRequest;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;

import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.easy.oauth.WebActivity;
import com.easy.oauth.http.HttpManager;

/**
 * Class for generating OAuthFactory of a particular type
 * 
 * @author Vinay S Shenoy
 * 
 */
public class OAuthFactory {

	private static final String TAG = OAuthFactory.class.getCanonicalName();

	/**
	 * Result code for successful authorization
	 */
	public static final int RESULT_CODE_SUCCESS = 501;

	/**
	 * Result code for unsuccessful authorization
	 */
	public static final int RESULT_CODE_FAILURE = 502;

	public static final int HTTP_GET = 601;

	public static final int HTTP_POST = 602;

	/**
	 * Consumer key
	 */
	private String consumerKey;

	/**
	 * Consumer Secret
	 */
	private String consumerSecret;

	/**
	 * OAuth Config
	 */
	private OAuthConfig oAuthConfig;

	/**
	 * One of the PROVIDER constants. Use PROVIDER_CUSTOM to define your own,
	 * One of the others to automatically configure it for you
	 */
	private int oauthProviderType;

	/**
	 * OAuth Provider
	 */
	private CommonsHttpOAuthProvider oAuthProvider;

	/**
	 * OAuth Consumer
	 */
	private CommonsHttpOAuthConsumer oAuthConsumer;

	/**
	 * Activity
	 */
	private Activity activity;

	/**
	 * HttpManager for making Http Requests
	 */
	private HttpManager httpManager;

	/**
	 * Access Token
	 */
	private AccessToken accessToken;

	public OAuthFactory(Activity activity, String consumerKey,
			String consumerSecret, int oauthProviderType,
			OAuthConfig oAuthConfig) throws OAuthFactoryException,
			NullPointerException {

		this.activity = activity;
		this.consumerKey = consumerKey;
		this.consumerSecret = consumerSecret;
		this.oauthProviderType = oauthProviderType;
		this.oAuthConfig = oAuthConfig;

		// Init objects
		initOAuthConsumer();
		initOAuthProvider();
		initHttpManager();
	}

	private void initOAuthConsumer() {

		if (consumerKey == null || consumerSecret == null) {

			throw new NullPointerException(
					OAuthFactoryException.OAuthExceptionMessages.OAUTH_NULL_CONSUMER_KEY_OR_SECRET);
		}

		oAuthConsumer = new CommonsHttpOAuthConsumer(consumerKey,
				consumerSecret);

	}

	private void initOAuthProvider() throws OAuthFactoryException {

		switch (oauthProviderType) {

		case OAuthProviders.PROVIDER_CUSTOM:

			if (oAuthConfig == null) {
				throw new OAuthFactoryException(
						OAuthFactoryException.OAuthExceptionMessages.OAUTH_MISSING_CONFIG);
			}

			break;

		case OAuthProviders.PROVIDER_FACEBOOK:

			if (oAuthConfig == null) {
				// Initialize oAuthConfig with facebook settings
			}

			break;

		case OAuthProviders.PROVIDER_TWITTER:

			if (oAuthConfig == null) {
				// Initialize oAuthConfig with twitter settings
			}
			break;

		default:
			throw new OAuthFactoryException(
					OAuthFactoryException.OAuthExceptionMessages.OAUTH_UNRECOGNIZED_PROVIDER);
		}

		oAuthProvider = new CommonsHttpOAuthProvider(
				oAuthConfig.requestTokenEndpointUrl,
				oAuthConfig.accessTokenEndpointUrl,
				oAuthConfig.authorizationWebsiteUrl);
	}

	private void initHttpManager() {

		httpManager = new HttpManager(oAuthConfig.httpConfig);
	}

	public void authorize(int requestCode) throws OAuthMessageSignerException,
			OAuthNotAuthorizedException, OAuthExpectationFailedException,
			OAuthCommunicationException {

		String url = oAuthProvider.retrieveRequestToken(oAuthConsumer,
				oAuthConfig.callbackUrl, (String) null);

		Intent intent = new Intent(activity, WebActivity.class);
		intent.putExtra("accesstokenurl", url);
		intent.putExtra(WebActivity.KEY_CALLBACK, oAuthConfig.callbackUrl);
		intent.putExtra(WebActivity.KEY_DENIED, "denied");
		intent.putExtra(WebActivity.KEY_VERIFIER, "oauth_verifier");
		activity.startActivityForResult(intent, requestCode);
	}

	/**
	 * 
	 * @param result
	 *            If you're using OAuth 1.0a, send the oauth_verifier code you
	 *            received, for OAuth 2.0. send the access token
	 * @throws OAuthMessageSignerException
	 * @throws OAuthNotAuthorizedException
	 * @throws OAuthExpectationFailedException
	 * @throws OAuthCommunicationException
	 */
	public void saveAccessToken(String result)
			throws OAuthMessageSignerException, OAuthNotAuthorizedException,
			OAuthExpectationFailedException, OAuthCommunicationException {

		switch (oAuthConfig.oAuthType) {

		case OAuthTypes.OAUTH_TYPE_1_0_A:
			oAuthProvider.retrieveAccessToken(oAuthConsumer, result,
					(String) null);
			accessToken = new AccessToken(oAuthConsumer.getToken(),
					oAuthConsumer.getTokenSecret());
			break;

		case OAuthTypes.OAUTH_TYPE_2_0:
			accessToken = new AccessToken(result, null);
			break;

		}

	}

	public AccessToken getAccessToken() {

		return accessToken;
	}

	/**
	 * Use this to sign an HttpRequest
	 * 
	 * @param request
	 * @throws OAuthFactoryException
	 * @throws OAuthCommunicationException
	 * @throws OAuthExpectationFailedException
	 * @throws OAuthMessageSignerException
	 */
	public void signHttpRequest(HttpRequestBase request)
			throws OAuthFactoryException, OAuthMessageSignerException,
			OAuthExpectationFailedException, OAuthCommunicationException {

		if (accessToken == null) {

			throw new OAuthFactoryException(
					OAuthFactoryException.OAuthExceptionMessages.OAUTH_NOT_AUTHORIZED);
		}

		oAuthConsumer.sign(request);
	}

	public InputStream executeRequestForInputStream(int requestType,
			String requestUrl, Bundle params) throws OAuthFactoryException,
			OAuthMessageSignerException, OAuthExpectationFailedException,
			OAuthCommunicationException, IllegalStateException, IOException {

		if (accessToken == null) {

			throw new OAuthFactoryException(
					OAuthFactoryException.OAuthExceptionMessages.OAUTH_NOT_AUTHORIZED);
		}

		HttpRequestBase request = null;

		switch (requestType) {

		case HTTP_GET:

			StringBuilder requestParamsBuilder = new StringBuilder('?');
			if (params != null && params.size() > 0) {

				Set<String> keySet = params.keySet();
				Iterator<String> keyIterator = keySet.iterator();
				String curKey;

				while (keyIterator.hasNext()) {

					curKey = keyIterator.next();

					requestParamsBuilder.append(curKey).append('=')
							.append(params.get(curKey));

					requestParamsBuilder.append('&');

				}
			}

			switch (oAuthConfig.oAuthType) {

			case OAuthTypes.OAUTH_TYPE_1_0_A:
				if (requestParamsBuilder.lastIndexOf("&") != -1)
					requestParamsBuilder.deleteCharAt(requestParamsBuilder
							.length() - 1);
				request = new HttpGet(requestUrl
						+ requestParamsBuilder.toString());
				signHttpRequest(request);
				break;

			case OAuthTypes.OAUTH_TYPE_2_0:

				requestParamsBuilder.append(oAuthConfig.oAuthToken).append('=')
						.append(accessToken.getToken());

				request = new HttpGet(requestUrl
						+ requestParamsBuilder.toString());
				break;
			}

			break;

		case HTTP_POST:
			break;

		default:
			throw new OAuthFactoryException(
					OAuthFactoryException.OAuthExceptionMessages.UNSUPPORTED_METHOD);
		}

		Log.d(TAG, "Request:" + request.getURI());
		return httpManager.executeHttpRequestForStreamResponse(request);
	}
}
