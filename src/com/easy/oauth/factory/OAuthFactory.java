package com.easy.oauth.factory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.message.BasicNameValuePair;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.easy.oauth.WebActivity;
import com.easy.oauth.factory.FactoryConstants.HttpRequestTypes;
import com.easy.oauth.factory.FactoryConstants.OAuthType;
import com.easy.oauth.factory.FactoryConstants.Provider;
import com.easy.oauth.http.HttpManager;

/**
 * Class for generating OAuthFactory of a particular type
 * 
 * @author Vinay S Shenoy
 * 
 */
public class OAuthFactory {

	@SuppressWarnings("unused")
	private static final String TAG = OAuthFactory.class.getCanonicalName();

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
	 * One of the {@link Provider} constants. Use {@link Provider#CUSTOM} to define your own,
	 * One of the others to automatically configure it for you
	 */
	private Provider provider;

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
	
	/**
	 * @param activity
	 * @param consumerKey Your application consumer key for OAuth 1.0a, Your Client ID for OAuth 2.0
	 * @param consumerSecret Your application consumer secret for OAuth 2.0, null for OAuth 2.0
	 * @param oauthProviderType One of {@link Provider} constants
	 * @param oAuthConfig An {@link OAuthConfig} object, needed if you're using {@link Provider#CUSTOM}
	 * @throws OAuthFactoryException
	 * @throws NullPointerException
	 */
	public OAuthFactory(Activity activity, String consumerKey,
			String consumerSecret, Provider provider,
			OAuthConfig oAuthConfig) throws OAuthFactoryException,
			NullPointerException {

		this.activity = activity;
		this.consumerKey = consumerKey;
		this.consumerSecret = consumerSecret;
		this.provider = provider;
		this.oAuthConfig = oAuthConfig;

		// Init objects
		initOAuthConsumer();
		initOAuthProvider();
		initHttpManager();
	}

	private void initOAuthConsumer() {
		
		if(oAuthConfig.oAuthType == OAuthType.OAUTH_2_0)
			return;

		if (consumerKey == null || consumerSecret == null) {

			throw new NullPointerException(
					OAuthFactoryException.OAuthExceptionMessages.OAUTH_NULL_CONSUMER_KEY_OR_SECRET);
		}

		oAuthConsumer = new CommonsHttpOAuthConsumer(consumerKey,
				consumerSecret);

	}

	private void initOAuthProvider() throws OAuthFactoryException {

		if(oAuthConfig.oAuthType == OAuthType.OAUTH_2_0)
			return;
		
		switch (provider) {

		case CUSTOM:

			if (oAuthConfig == null) {
				throw new OAuthFactoryException(
						OAuthFactoryException.OAuthExceptionMessages.OAUTH_MISSING_CONFIG);
			}

			break;

		case FACEBOOK:

			if (oAuthConfig == null) {
				oAuthConfig = OAuthConfig.getConfigFor(Provider.FACEBOOK);
			}

			break;

		case TWITTER:

			if (oAuthConfig == null) {
				oAuthConfig = OAuthConfig.getConfigFor(provider);
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

		String url = null;
		
		switch(oAuthConfig.oAuthType) {
		
		case OAUTH_1_0_A:
			url = oAuthProvider.retrieveRequestToken(oAuthConsumer,
					oAuthConfig.callbackUrl, (String) null);
			break;
			
		case OAUTH_2_0:
			url = oAuthConfig.authorizationWebsiteUrl;
			
			StringBuilder urlBuilder = new StringBuilder(url);
			
			urlBuilder
				.append('?')
				.append(OAuth2AuthParams.PARAM_KEY_CLIENT_ID).append('=').append(consumerKey)
				.append('&')
				.append(OAuth2AuthParams.PARAM_KEY_REDIRECT_URI).append('=').append(URLEncoder.encode(oAuthConfig.callbackUrl))
				.append('&')
				.append(OAuth2AuthParams.PARAM_KEY_RESPONSE_TYPE).append('=').append(OAuth2AuthParams.PARAM_VALUE_RESPONSE_TYPE);
			
			appendExtraParamsToAuthUrl(urlBuilder, oAuthConfig.customOAuthParams);
				
			url = urlBuilder.toString();
			break;
		}

		Intent intent = new Intent(activity, WebActivity.class);
		intent.putExtra(WebActivity.KEY_ACCESS_TOKEN_URL, url);
		intent.putExtra(WebActivity.KEY_CALLBACK, oAuthConfig.callbackUrl);
		intent.putExtra(WebActivity.KEY_DENIED, oAuthConfig.oAuthDenied);
		intent.putExtra(WebActivity.KEY_VERIFIER, oAuthConfig.oAuthVerifier);
		intent.putExtra(WebActivity.KEY_OAUTH_TOKEN, oAuthConfig.oAuthToken);
		intent.putExtra(WebActivity.KEY_OAUTH_TYPE, oAuthConfig.oAuthType);
		activity.startActivityForResult(intent, requestCode);
	}

	/**
	 * 
	 * Method to append extra OAuthParams to the Authorization request, OAuth 2.0 only
	 * @param urlBuilder The Url Builder, bust contain a built authorize url
	 * @param customOAuthParams An array of non-url encoded strings containing the OAuth Params, 
	 * each string must be of the form "key=value"
	 */
	private void appendExtraParamsToAuthUrl(StringBuilder urlBuilder,
			String[] customOAuthParams) {
		
		if(customOAuthParams == null || customOAuthParams.length == 0)
			return;
		
		for(String param : customOAuthParams) {
			
			urlBuilder
				.append('&')
				.append(URLEncoder.encode(param));
		}
		
	}

	/**
	 * 
	 * @param args The extras in the intent you receive in OnActivityResult()
	 *            
	 * @throws OAuthMessageSignerException
	 * @throws OAuthNotAuthorizedException
	 * @throws OAuthExpectationFailedException
	 * @throws OAuthCommunicationException
	 */
	public AccessToken authorizeCallback(Bundle args)
			throws OAuthMessageSignerException, OAuthNotAuthorizedException,
			OAuthExpectationFailedException, OAuthCommunicationException {

		switch (oAuthConfig.oAuthType) {

		case OAUTH_1_0_A:
			oAuthProvider.retrieveAccessToken(oAuthConsumer, args.getString(oAuthConfig.oAuthVerifier),
					oAuthConfig.customOAuthParams);
			accessToken = new AccessToken(oAuthConsumer.getToken(),
					oAuthConsumer.getTokenSecret());
			break;

		case OAUTH_2_0:
			accessToken = new AccessToken(args.getString(oAuthConfig.oAuthToken), null);
			break;

		}
		
		return accessToken;
		
	}

	public AccessToken getAccessToken() {

		return accessToken;
	}

	/**
	 * Use this to sign an HttpRequest
	 * 
	 * @param request
	 * @return accessToken {@link AccessToken}
	 * @throws OAuthFactoryException
	 * @throws OAuthCommunicationException
	 * @throws OAuthExpectationFailedException
	 * @throws OAuthMessageSignerException
	 */
	public void signHttpRequest(HttpRequestBase request)
			throws OAuthFactoryException, OAuthMessageSignerException,
			OAuthExpectationFailedException, OAuthCommunicationException {

		if (accessToken == null || oAuthConsumer == null) {

			throw new OAuthFactoryException(
					OAuthFactoryException.OAuthExceptionMessages.OAUTH_NOT_AUTHORIZED);
		}

		oAuthConsumer.sign(request);
	}

	public InputStream executeRequestForInputStream(HttpRequestTypes requestType,
			String requestUrl, Bundle params) throws OAuthFactoryException,
			OAuthMessageSignerException, OAuthExpectationFailedException,
			OAuthCommunicationException, IllegalStateException, IOException {

		if (accessToken == null) {

			throw new OAuthFactoryException(
					OAuthFactoryException.OAuthExceptionMessages.OAUTH_NOT_AUTHORIZED);
		}

		StringBuilder requestParamsBuilder;

		switch (requestType) {

		case GET:

			HttpGet get = null;
			requestParamsBuilder = new StringBuilder('?');
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

			case OAUTH_1_0_A:
				if (requestParamsBuilder.lastIndexOf("&") != -1)
					requestParamsBuilder.deleteCharAt(requestParamsBuilder
							.length() - 1);
				get = new HttpGet(requestUrl
						+ requestParamsBuilder.toString());
				signHttpRequest(get);
				break;

			case OAUTH_2_0:

				requestParamsBuilder.append(oAuthConfig.oAuthToken).append('=')
						.append(accessToken.getToken());

				get = new HttpGet(requestUrl
						+ requestParamsBuilder.toString());
				break;
			}
			return httpManager.executeHttpRequestForStreamResponse(get);

			
		case POST:
			
			HttpPost post = null;
			
			List<NameValuePair> postParams = null;
			
			if (params != null && params.size() > 0) {

				Set<String> keySet = params.keySet();
				Iterator<String> keyIterator = keySet.iterator();
				String curKey;
				postParams = new ArrayList<NameValuePair>(params.size());

				while (keyIterator.hasNext()) {

					curKey = keyIterator.next();
					postParams.add(new BasicNameValuePair(curKey, params.getString(curKey)));
				}
			}

			switch (oAuthConfig.oAuthType) {

			case OAUTH_1_0_A:
				
				post = new HttpPost(requestUrl);
				
				signHttpRequest(post);
				break;

			case OAUTH_2_0:

				requestParamsBuilder = new StringBuilder('?');
				requestParamsBuilder.append(oAuthConfig.oAuthToken).append('=')
						.append(accessToken.getToken());

				post = new HttpPost(requestUrl
						+ requestParamsBuilder.toString());
				
				break;
			}
			
			post.setEntity(new UrlEncodedFormEntity(postParams));
			return httpManager.executeHttpRequestForStreamResponse(post);

		default:
			throw new OAuthFactoryException(
					OAuthFactoryException.OAuthExceptionMessages.UNSUPPORTED_METHOD);
		}

		
	}
	
	public String executeRequestForString(HttpRequestTypes requestType,
			String requestUrl, Bundle params) throws OAuthFactoryException,
			OAuthMessageSignerException, OAuthExpectationFailedException,
			OAuthCommunicationException, IllegalStateException, IOException {

		if (accessToken == null) {

			throw new OAuthFactoryException(
					OAuthFactoryException.OAuthExceptionMessages.OAUTH_NOT_AUTHORIZED);
		}

		StringBuilder requestParamsBuilder;

		switch (requestType) {

		case GET:

			HttpGet get = null;
			requestParamsBuilder = new StringBuilder('?');
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

			case OAUTH_1_0_A:
				if (requestParamsBuilder.lastIndexOf("&") != -1)
					requestParamsBuilder.deleteCharAt(requestParamsBuilder
							.length() - 1);
				get = new HttpGet(requestUrl
						+ requestParamsBuilder.toString());
				signHttpRequest(get);
				break;

			case OAUTH_2_0:

				requestParamsBuilder.append(oAuthConfig.oAuthToken).append('=')
						.append(accessToken.getToken());

				get = new HttpGet(requestUrl
						+ requestParamsBuilder.toString());
				break;
			}
			return httpManager.executeHttpRequestForStringResponse(get);

			
		case POST:
			
			HttpPost post = null;
			
			List<NameValuePair> postParams = null;
			
			if (params != null && params.size() > 0) {

				Set<String> keySet = params.keySet();
				Iterator<String> keyIterator = keySet.iterator();
				String curKey;
				postParams = new ArrayList<NameValuePair>(params.size());

				while (keyIterator.hasNext()) {

					curKey = keyIterator.next();
					postParams.add(new BasicNameValuePair(curKey, params.getString(curKey)));
				}
			}

			switch (oAuthConfig.oAuthType) {

			case OAUTH_1_0_A:
				
				post = new HttpPost(requestUrl);
				
				signHttpRequest(post);
				break;

			case OAUTH_2_0:

				requestParamsBuilder = new StringBuilder('?');
				requestParamsBuilder.append(oAuthConfig.oAuthToken).append('=')
						.append(accessToken.getToken());

				post = new HttpPost(requestUrl
						+ requestParamsBuilder.toString());
				
				break;
			}
			
			post.setEntity(new UrlEncodedFormEntity(postParams));
			return httpManager.executeHttpRequestForStringResponse(post);

		default:
			throw new OAuthFactoryException(
					OAuthFactoryException.OAuthExceptionMessages.UNSUPPORTED_METHOD);
		}
		
	}
	
	/**
	 * Destroy the OAuth Factory object if you no longer need it.
	 */
	public void destroy() {
		
		oAuthConfig = null;
		oAuthConsumer = null;
		oAuthProvider = null;
		accessToken = null;
		httpManager.destroy();
		
	}
	
	private static class OAuth2AuthParams {
		
		static final String PARAM_KEY_CLIENT_ID = "client_id";
		
		static final String PARAM_KEY_REDIRECT_URI = "redirect_uri";
		
		static final String PARAM_KEY_RESPONSE_TYPE = "response_type";
		
		static final String PARAM_VALUE_RESPONSE_TYPE = "token";
	}
}
