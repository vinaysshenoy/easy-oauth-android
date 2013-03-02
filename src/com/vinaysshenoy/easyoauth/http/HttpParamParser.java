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

package com.vinaysshenoy.easyoauth.http;

import com.vinaysshenoy.easyoauth.factory.FactoryConstants.OAuthType;

import android.os.Bundle;

/**
 * Class that contains methods to parse the params from a response string
 * 
 * @author Vinay S Shenoy, Nov 12 2012
 * 
 */
public class HttpParamParser {

	private static final String TOKEN_AMPERSAND = "&";

	private static final String TOKEN_QUESTION = "\\?";

	private static final String TOKEN_EQUALS = "=";
	
	private static final String TOKEN_HASH = "#";

	public static Bundle parseParams(OAuthType oAuthType, String url) {

		String[] split = null;
		
		switch(oAuthType) {
		
		case OAUTH_1_0_A:
			split = url.split(TOKEN_QUESTION);
			
			break;
			
		case OAUTH_2_0:
			split = url.split(TOKEN_HASH);
			break;
		}
		

		if (split.length == 2) {

			Bundle params = null;
			String[] paramsArray = split[1].split(TOKEN_AMPERSAND);

			if (paramsArray.length >= 1) {

				params = new Bundle();
				String[] aParamParts;
				String aParam;

				for (int i = 0; i < paramsArray.length; i++) {

					aParam = paramsArray[i];
					aParamParts = aParam.split(TOKEN_EQUALS);
					params.putString(aParamParts[0], aParamParts[1]);
				}

				return params;
			}
		}
		
		return null;

	}
}
