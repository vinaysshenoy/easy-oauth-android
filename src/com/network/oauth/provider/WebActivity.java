package com.network.oauth.provider;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class WebActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_web, menu);
        return true;
    }
}
