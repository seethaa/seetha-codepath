package com.codepath.simpletodo;

import android.os.Bundle;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.view.Menu;

/**
 * SplashActivity adds a simple screen before launching MainActivity.
 */
public class SplashActivity extends Activity {

    private final int SPLASH_DURATION = 2000;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                SplashActivity.this.startActivity(mainIntent);
                SplashActivity.this.finish();
            }
        }, SPLASH_DURATION);
    }
}
