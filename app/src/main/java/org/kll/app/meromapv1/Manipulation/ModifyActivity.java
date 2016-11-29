package org.kll.app.meromapv1.Manipulation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.kll.app.meromapv1.Database.DBManager;
import org.kll.app.meromapv1.R;

/**
 * Created by Rahul Singh Maharjan on 11/28/16.
 * Project for Kathmandu Living Labs
 */

public class ModifyActivity extends Activity implements View.OnClickListener{

    private EditText titleText;
    private Button updateBtn, deleteBtn;
    private EditText descText;
    private EditText contText;

    private long _id;

    private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Modify Record");



        setContentView(R.layout.activity_modify);

        dbManager = new DBManager(this);
        dbManager.open();

        titleText = (EditText) findViewById(R.id.subject_edittext);
        descText = (EditText) findViewById(R.id.description_edittext);
        contText = (EditText) findViewById(R.id.contact_edittext);

        updateBtn = (Button) findViewById(R.id.btn_update);
        deleteBtn = (Button) findViewById(R.id.btn_delete);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String name = intent.getStringExtra("title");
        String desc = intent.getStringExtra("desc");
        String cont = intent.getStringExtra("cont");

        _id = Long.parseLong(id);

        titleText.setText(name);
        descText.setText(desc);
        contText.setText(cont);


        updateBtn.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_update:
                String title = titleText.getText().toString();
                String desc = descText.getText().toString();
                String cont = contText.getText().toString();
                dbManager.update(_id, title, desc, cont);
                this.returnHome();
                break;

            case R.id.btn_delete:
                dbManager.delete(_id);
                this.returnHome();
                break;
        }
    }

    public void returnHome() {
        Intent home_intent = new Intent(getApplicationContext(), ListActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(home_intent);
    }
}

