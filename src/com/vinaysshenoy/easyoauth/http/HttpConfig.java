package com.vinaysshenoy.easyoauth.http;

import org.apache.http.HttpVersion;

/**
 * Class to manage HttpClient configuration. Will populate with defaults
 * and user can change them afterwards
 * @author Vinay S Shenoy, Nov 11 2012
 *
 */
public class HttpConfig {
	
	/**
	 * Key for http protocol
	 */
	public static final String KEY_HTTP = "http";
	
	/**
	 * Key for https protocol
	 */
	public static final String KEY_HTTPS = "https";

	/**
	 * Http Version, default is 1.1
	 */
	public HttpVersion httpVersion;
	
	/**
	 * Http charset, default is utf-8
	 */
	public String contentCharset;
	
	/**
	 * Defines whether stale connection check is to be used. 
	 * Disabling stale connection check may result in slight performance 
	 * improvement at the risk of getting an I/O error 
	 * when executing a request over a connection 
	 * that has been closed at the server side.
	 * 
	 * default - true
	 */
	public boolean enableStaleChecking;
	
	/**
	 * Connnection timeout in milliseconds, default 20000
	 */
	public int connectionTimeout;
	
	/**
	 * Socket timeout in milliseconds, default 60000
	 */
	public int socketTimeout;
	
	/**
	 * Socket buffer size in bytes, default 8192
	 */
	public int socketBufferSize;
	
	/**
	 * Enable redirection, default false
	 */
	public boolean isRedirecting;
	
	/**
	 * User agent
	 */
	public String userAgent;
	
	/**
	 * Port used for Http Protocol, default 80
	 */
	public int httpPort;
	
	/**
	 * Port used for Https Protocol, default 443
	 */
	public int httpsPort;
	
	public HttpConfig() {
		
		//Set defaults
		httpVersion = HttpConfigDefaults.DEFAULT_HTTP_VERSION;
		contentCharset = HttpConfigDefaults.DEFAULT_CONTENT_CHARSET;
		enableStaleChecking = HttpConfigDefaults.DEFAULT_ENABLE_STALE_CHECKING;
		connectionTimeout = HttpConfigDefaults.DEFAULT_CONNECTION_TIMEOUT;
		socketTimeout = HttpConfigDefaults.DEFAULT_SOCKET_TIMEOUT;
		socketBufferSize = HttpConfigDefaults.DEFAULT_SOCKET_BUFFER_SIZE;
		isRedirecting = HttpConfigDefaults.DEFAULT_IS_REDIRECTING;
		userAgent = HttpConfigDefaults.DEFAULT_USER_AGENT;
		httpPort = HttpConfigDefaults.DEFAULT_HTTP_PORT;
		httpsPort = HttpConfigDefaults.DEFAULT_HTTPS_PORT;
	}
	
	/**
	 * Static class that holds all the Http Config Defaults
	 * @author Vinay S Shenoy, Nov 11 2012
	 * 
	 */
	private static class HttpConfigDefaults {
		
		private static final HttpVersion DEFAULT_HTTP_VERSION = HttpVersion.HTTP_1_1;
		private static final String DEFAULT_CONTENT_CHARSET = "UTF-8";
		private static final boolean DEFAULT_ENABLE_STALE_CHECKING = true;
		private static final int DEFAULT_CONNECTION_TIMEOUT = 20000;
		private static final int DEFAULT_SOCKET_TIMEOUT = 60000;
		private static final int DEFAULT_SOCKET_BUFFER_SIZE = 8192;
		private static final boolean DEFAULT_IS_REDIRECTING = false;
		private static final String DEFAULT_USER_AGENT = "EasyOAuth";
		private static final int DEFAULT_HTTP_PORT = 80;
		private static final int DEFAULT_HTTPS_PORT = 443;
		
	}

}
