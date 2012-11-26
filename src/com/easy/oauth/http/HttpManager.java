package com.easy.oauth.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

/**
 * Class that manages Http connections and requests
 * @author Vinay S Shenoy, Nov 11 2012
 *
 */
public class HttpManager {

	/**
	 * Debug tag
	 */
	@SuppressWarnings("unused")
	private static final String TAG = HttpManager.class.getCanonicalName();

	/**
	 * DefaultHttpClient Object.
	 */
	private DefaultHttpClient mHttpClient;

	/**
	 * Http configuration
	 */
	private HttpConfig mHttpConfig;

	public HttpManager(HttpConfig config) {

		this.mHttpConfig = config;
		initHttpClient();
	}

	private void initHttpClient() {

		HttpParams params = new BasicHttpParams();

		HttpProtocolParams.setUserAgent(params, mHttpConfig.userAgent);
		HttpProtocolParams.setVersion(params, mHttpConfig.httpVersion);
		HttpProtocolParams
				.setContentCharset(params, mHttpConfig.contentCharset);

		HttpConnectionParams.setStaleCheckingEnabled(params,
				mHttpConfig.enableStaleChecking);
		HttpConnectionParams.setConnectionTimeout(params,
				mHttpConfig.connectionTimeout);
		HttpConnectionParams.setSoTimeout(params, mHttpConfig.socketTimeout);
		HttpConnectionParams.setSocketBufferSize(params,
				mHttpConfig.socketBufferSize);

		HttpClientParams.setRedirecting(params, mHttpConfig.isRedirecting);

		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme(HttpConfig.KEY_HTTP,
				PlainSocketFactory.getSocketFactory(), mHttpConfig.httpPort));
		schemeRegistry.register(new Scheme(HttpConfig.KEY_HTTPS,
				SSLSocketFactory.getSocketFactory(), mHttpConfig.httpsPort));

		ClientConnectionManager manager = new ThreadSafeClientConnManager(
				params, schemeRegistry);
		mHttpClient = new DefaultHttpClient(manager, params);
	}

	/**
	 * Execute an HttpRequest and get the response as a stream
	 * @param request The HttpRequest to be executed
	 * @return the response as an inputstream, or null if the 
	 * response statusline was not set
	 * @throws IOException
	 * @throws IllegalStateException
	 */
	public InputStream executeHttpRequestForStreamResponse(HttpRequestBase request)
			throws IOException, IllegalStateException {

		HttpResponse httpRes = mHttpClient.execute(request);
		StatusLine statusLine = httpRes.getStatusLine();
		if (statusLine != null) {
			HttpEntity entity = httpRes.getEntity();
			return entity.getContent();
		}
		return null;

	}
	
	/**
	 * Execute an HttpRequest and get the response as a String
	 * @param request The HttpRequest to be executed
	 * @return the response as String, or null if the 
	 * response statusline was not set
	 * @throws IOException
	 * @throws IllegalStateException
	 */
	public String executeHttpRequestForStringResponse(HttpRequestBase request)
			throws IOException, IllegalStateException {

		HttpResponse httpRes = mHttpClient.execute(request);
		StatusLine statusLine = httpRes.getStatusLine();
		
		InputStream is = null;
		
		if (statusLine != null) {
			HttpEntity entity = httpRes.getEntity();
			is = entity.getContent();
			
			if(is != null) {
				
				BufferedReader reader = new BufferedReader(new InputStreamReader(is));
				StringBuilder response = new StringBuilder();
				String line;
				while ((line = reader.readLine()) != null) {
					response.append(line);
				}
				
				is.close();
				reader.close();
				
				return response.toString();
			}
		}
		return null;

	}

	public void destroy() {
		
		mHttpClient = null;
		
	}

}
