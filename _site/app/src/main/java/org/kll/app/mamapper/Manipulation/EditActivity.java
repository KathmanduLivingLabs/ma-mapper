package org.kll.app.mamapper.Manipulation;




import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import org.kll.app.mamapper.Database.DBManager;
import org.kll.app.mamapper.R;



public class EditActivity extends Activity implements View.OnClickListener {

    private Button addTodoBtn;
    private EditText subjectEditText;
    private EditText descEditText;
    private EditText contactEditText;

    private DBManager dbManager;

    private String getName;
    private String getDisc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Add Record");

        setContentView(R.layout.activity_edit);

        getName = getIntent().getStringExtra("Name");
        getDisc = getIntent().getStringExtra("Desc");




        subjectEditText = (EditText) findViewById(R.id.subject_edittext);
        descEditText = (EditText) findViewById(R.id.description_edittext);
        contactEditText = (EditText) findViewById(R.id.contact_edittext);

        subjectEditText.setText(getName);
        descEditText.setText(getDisc);

        addTodoBtn = (Button) findViewById(R.id.add_record);

        dbManager = new DBManager(this);
        dbManager.open();
        addTodoBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_record:

                final String name = subjectEditText.getText().toString();
                final String desc = descEditText.getText().toString();
                final String cont = contactEditText.getText().toString();

                dbManager.insert(name, desc, cont);

               Intent main = new Intent(EditActivity.this, ListActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                startActivity(main);
                break;
        }
    }

}