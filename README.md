# Easy OAuth

A generic OAuth Provider for Android

## Credits
This project uses the excellent [Signpost](http://code.google.com/p/oauth-signpost/ "Signpost") library for OAuth 1.0a authentication.

##Notes
The project is in its very early stages and requires a lot of work.

## Info
This project aims to provide a generic, lightweight OAuth solution which you can use in your projects to authenticate yourself with multiple services using OAuth authentication. The library supports both OAuth 1.0a and OAuth 2.0.

The library has default settings for common services(Facebook and Twitter as of now, more coming later).

## Usage
It is assumed that the user is already familiar with the OAuth flow.

You can either add this as a library project or copy the source code directly into your own project. Remember to copy the Signpost libs from the /libs directory and add them to the build path in case of the latter.

Facebook Connectivity
=====================
```java
    OAuthFactory facebookFactory = new OAuthFactory(MainActivity.this, "MY_CLIENT_ID", null, Provider.FACEBOOK, null, "MY_CALLBACK_URL");

    facebookFactory.getOAuthConfig().customOAuthParams = new String[] { "scope=read_stream,publish_actions" };

    facebookFactory.authorize(REQUEST_CODE);
```

This will open up a WebActivity in which you will be redirected to the Facebook OAuth Dialog and you can sign in. In order to retrieve the access token, override onActivityResult() as follows
```java
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		switch(requestCode) {
		
		case REQUEST_CODE: {
			
			if(resultCode == FactoryConstants.SUCCESS) {
				try {
					AccessToken token  = facebookFactory.authorizeCallback(data.getExtras());
					Log.d("Tag", "Token:" + token.getToken() /*+ " Secret:" + token.getTokenSecret()*/);
				} catch (OAuthMessageSignerException e) {
					e.printStackTrace();
				} catch (OAuthNotAuthorizedException e) {
					e.printStackTrace();
				} catch (OAuthExpectationFailedException e) {
					e.printStackTrace();
				} catch (OAuthCommunicationException e) {
					e.printStackTrace();
				}
			}
			
			else if(resultCode == FactoryConstants.FAILURE) {
				Log.d("Tag", "Failure");
			}
			break;
		}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
```

Twitter Connectivity
====================

```java
    OAuthFactory twitterFactory = new OAuthFactory(MainActivity.this, "MY_CONSUMER_KEY", "MY_CONSUMER_SECRET", Provider.TWITTER, null, "MY_CALLBACK_URL");

    twitterFactory.authorize(REQUEST_CODE);
```

Retrieving the access token is done in exactly the same way as for Facebook. After connecting to Twiiter, you can use the same twitterFactory instance to sign any HttpRequests as follows:

```java
    HttpGet get = new HttpGet("SOME_URL_THAT_NEEDS_SIGNING");
    
    twitterFactory.signHttpRequest(get);
```

Using a custom OAuth Provider
=============================

Coming Soon!

## License

Copyright 2012 Vinay S Shenoy

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
