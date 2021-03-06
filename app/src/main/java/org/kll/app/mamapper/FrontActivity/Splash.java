package org.kll.app.mamapper.FrontActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import org.kll.app.mamapper.R;

/**
 * Created by Rahul Singh Maharjan on 10/18/16.
 * Project for Kathmandu Living Labs
 */

public class Splash extends Activity {



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splash);
        startSplashScreen();
    }


    private void startSplashScreen() {

        // create a thread that counts up to the timeout
        Thread t = new Thread() {
            int count = 0;


            @Override
            public void run() {
                try {
                    super.run();
                    while (count < 2000) {
                        sleep(100);
                        count += 100;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    endSplashScreen();
                }
            }
        };
        t.start();
    }

    private void endSplashScreen() {

        Intent next = new Intent(Splash.this, MainActivity.class);
        next.putExtra("send", "school");
        startActivity(next);
        finish();
    }

}