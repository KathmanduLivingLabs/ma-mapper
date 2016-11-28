package org.kll.app.meromapv1.Manipulation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.kll.app.meromapv1.Database.DBManager;
import org.kll.app.meromapv1.R;

/**
 * Created by Rahul Singh Maharjan on 11/27/16.
 * Project for Kathmandu Living Labs
 */

public class DetailInfo extends Activity{

     String getName;
     String getDisc;
    private Button ok;
    private Button edit;

    private DBManager dbManager;



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

       /* dbManager = new DBManager(this);
        dbManager.open();

        Log.d("Inserting: ", "Inserting ..");
        dbManager.insert(getName, getDisc);
        dbManager.insert("Koteshowr", "School");

        Log.d("reading:","reading ...");
*/


        ok.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                //newInfo.setInfoName(getName.toString());
               // newInfo.setInfoDescription(getDisc.toString());
               //dbHandler.addInformation(newInfo);

                //Toast.makeText(getApplicationContext(), "Name :" + newInfo.getInfoName(), Toast.LENGTH_SHORT).show();
                /*Intent i = new Intent(DetailInfo.this, MainActivitys.class);
                i.putExtra("send", "school");
                startActivity(i);*/

                /*dbManager.insert(getName, getDisc);*/
              //  Toast.makeText(getApplicationContext(), "ok",Toast.LENGTH_SHORT).show();


                Intent add_mem = new Intent(getApplicationContext(), EditActivity.class);
                add_mem.putExtra("Name", getName);
                add_mem.putExtra("Desc", getDisc);
                startActivity(add_mem);
            }
        });

    }



}
