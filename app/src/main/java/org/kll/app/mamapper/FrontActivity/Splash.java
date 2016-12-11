package org.kll.app.mamapper.FrontActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import org.kll.app.mamapper.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Rahul Singh Maharjan on 10/18/16.
 * Project for Kathmandu Living Labs
 */

public class Splash extends Activity {



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);




        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Splash.this.runOnUiThread(new Runnable() {
                    public void run() {

                        Intent next = new Intent(Splash.this, MainActivitys.class);
                        next.putExtra("send", "school");
                        startActivity(next);
                        finish();
                    }
                });
            }
        },500);
    }



   /* public void addListenerOnButton()
    {

        final Context context = this;

        text = (TextView) findViewById(R.id.skip);

        text.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Splash.this, MapActivity.class);
                startActivity(intent);

            }
        });
    }*/
}