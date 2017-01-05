package org.kll.app.mamapper.Manipulation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.kll.app.mamapper.R;

/**
 * Created by Rahul Singh Maharjan on 11/27/16.
 * Project for Kathmandu Living Labs
 */

public class DetailInfo extends Activity{

     String getName;
     String getDisc;
    private Button ok;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);





        setContentView(R.layout.activity_view_record);
        getName = getIntent().getStringExtra("DetailName");
        getDisc = getIntent().getStringExtra("DetailDisc");


        final TextView textNametoChange = (TextView) findViewById(R.id.text_infoname);
        final TextView textDisctoChange = (TextView) findViewById(R.id.text_infodisc);
        textNametoChange.setText(getName);
        textDisctoChange.setText(getDisc);
        ok = (Button) findViewById(R.id.edit_query);

        ok.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {



                Intent add_mem = new Intent(getApplicationContext(), EditActivity.class);
                add_mem.putExtra("Name", getName);
                add_mem.putExtra("Desc", getDisc);
                startActivity(add_mem);
            }
        });

    }



}
