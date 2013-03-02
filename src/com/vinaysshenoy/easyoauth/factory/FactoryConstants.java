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
package com.vinaysshenoy.easyoauth.factory;

/**
 * Class that holds some OAuth Constants
 * @author Vinay S Shenoy
 *
 */
public class FactoryConstants {

	public static final int FAILURE = 602;
	public static final int SUCCESS = 601;
	
	public enum HttpRequestTypes {
		
		GET,
		POST
	}

	public enum OAuthType {
		
		OAUTH_1_0_A,
		OAUTH_2_0
	}

	/**
	 * Different API Providers. Use {@link Provider#CUSTOM} to implement your own,
	 * one of the others to to generate it for you
	 * @author Vinay S Shenoy
	 *
	 */
	public enum Provider {
		
		CUSTOM,
		FACEBOOK,
		TWITTER
	}
	
}


