/*Copyright 2012 Vinay S Shenoy

Licensed under the Apache License, Version 2.0 (the "License"); 
you may not use this file except in compliance with the License. 
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, 
software distributed under the License is distributed on 
an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 
either express or implied. See the License for the specific language 
governing permissions and limitations under the License.
 */

package com.vinaysshenoy.easyoauth;

import com.vinaysshenoy.easyoauth.R;
import com.vinaysshenoy.easyoauth.factory.FactoryConstants;
import com.vinaysshenoy.easyoauth.factory.FactoryConstants.OAuthType;
import com.vinaysshenoy.easyoauth.http.HttpParamParser;
import com.vinaysshenoy.easyoauth.utils.Logger;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;

public class WebActivity extends Activity {

	private WebView webView;

	@SuppressWarnings("unused")
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

	private OAuthType oAuthType;

	private FrameLayout webLayout;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web);

		webLayout = (FrameLayout) findViewById(R.id.webView);

		webView = new WebView(WebActivity.this);

		FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		webView.setLayoutParams(layoutParams);
		webView.getSettings().setJavaScriptEnabled(true);

		webLayout.addView(webView);

		oAuthVerifier = getIntent().getStringExtra(KEY_VERIFIER);
		oAuthDenied = getIntent().getStringExtra(KEY_DENIED);
		callback = getIntent().getStringExtra(KEY_CALLBACK);
		oAuthToken = getIntent().getStringExtra(KEY_OAUTH_TOKEN);
		oAuthType = (OAuthType) getIntent()
				.getSerializableExtra(KEY_OAUTH_TYPE);

		webView.setWebViewClient(new WebViewClient() {

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {

				Logger.d(TAG, "Url:" + url);
				if (url.startsWith(callback)) {

					switch (oAuthType) {

					case OAUTH_1_0_A:

						if (url.contains(oAuthVerifier)) {

							Intent intent = new Intent();
							Bundle params = HttpParamParser.parseParams(
									oAuthType, url);

							if (params != null) {
								intent.putExtras(params);
								setResult(FactoryConstants.SUCCESS, intent);
							} else {
								setResult(FactoryConstants.FAILURE);
								intent = null;
							}
							finish();
						}

						else if (url.contains(oAuthDenied)) {

							setResult(FactoryConstants.FAILURE);
							finish();
						}
						break;

					case OAUTH_2_0:

						if (url.contains(oAuthToken)) {

							Intent intent = new Intent();
							Bundle params = HttpParamParser.parseParams(
									oAuthType, url);

							if (params != null) {
								intent.putExtras(params);
								setResult(FactoryConstants.SUCCESS, intent);
							} else {
								setResult(FactoryConstants.FAILURE);
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
	protected void onDestroy() {

		webView.removeAllViews();
		webView = null;
		super.onDestroy();
	}
}
