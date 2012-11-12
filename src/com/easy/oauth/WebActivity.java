package com.easy.oauth;

import com.easy.oauth.factory.OAuthFactory;
import com.easy.oauth.factory.OAuthTypes;
import com.easy.oauth.http.HttpParamParser;
import com.network.oauth.provider.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Menu;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebActivity extends Activity {

	private WebView webView;

	private static final String TAG = WebActivity.class.getCanonicalName();

	public static final String KEY_VERIFIER = "oauth_verifier_key";

	public static final String KEY_DENIED = "oauth_denied_key";

	public static final String KEY_CALLBACK = "callback_key";

	public static final String KEY_OAUTH_TOKEN = "oauth_token_key";

	public static final String KEY_OAUTH_TYPE = "oauth_type";

	public static final String KEY_ACCESS_TOKEN_URL = "access_token_url";

	private String oAuthVerifier;

	private String oAuthDenied;

	private String callback;

	private String oAuthToken;

	private int oAuthType;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web);

		oAuthVerifier = getIntent().getStringExtra(KEY_VERIFIER);
		oAuthDenied = getIntent().getStringExtra(KEY_DENIED);
		callback = getIntent().getStringExtra(KEY_CALLBACK);
		oAuthToken = getIntent().getStringExtra(KEY_OAUTH_TOKEN);
		oAuthType = getIntent().getIntExtra(KEY_OAUTH_TYPE, 0);

		webView = (WebView) findViewById(R.id.webView);
		webView.setWebViewClient(new WebViewClient() {

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {

				if (url.startsWith(callback)) {

					switch (oAuthType) {

					case OAuthTypes.OAUTH_TYPE_1_0_A:

						if (url.contains(oAuthVerifier)) {

							Intent intent = new Intent();
							Bundle params = HttpParamParser.parseParams(
									oAuthType, url);

							if (params != null) {
								intent.putExtras(params);
								setResult(OAuthFactory.RESULT_CODE_SUCCESS,
										intent);
							} else {
								setResult(OAuthFactory.RESULT_CODE_FAILURE);
							}
							finish();
						}

						else if (url.contains(oAuthDenied)) {

							setResult(OAuthFactory.RESULT_CODE_FAILURE);
							finish();
						}
						break;

					case OAuthTypes.OAUTH_TYPE_2_0:

						if (url.contains(oAuthToken)) {

							Intent intent = new Intent();
							Bundle params = HttpParamParser.parseParams(
									oAuthType, url);

							if (params != null) {
								intent.putExtras(params);
								setResult(OAuthFactory.RESULT_CODE_SUCCESS,
										intent);
							} else {
								setResult(OAuthFactory.RESULT_CODE_FAILURE);
							}
							finish();
						}
						break;
					}

				}

				else
					super.onPageStarted(view, url, favicon);
			}
		});

		webView.loadUrl(getIntent().getStringExtra(KEY_ACCESS_TOKEN_URL));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_web, menu);
		return true;
	}

	@Override
	protected void onDestroy() {

		webView.removeAllViews();
		super.onDestroy();
	}
}
