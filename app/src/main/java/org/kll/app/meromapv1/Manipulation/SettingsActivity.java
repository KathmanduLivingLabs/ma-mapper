
package org.kll.app.meromapv1.Manipulation;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.kll.app.meromapv1.BaseActivity;
import org.kll.app.meromapv1.MainActivitys;
import org.kll.app.meromapv1.R;


public class SettingsActivity extends BaseActivity {

    private RadioGroup optiongroup;
    private RadioButton optionselect;
    private Button Selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settingactivity);

        addListenerOnButton();

    }

    public void addListenerOnButton()
    {
        optiongroup = (RadioGroup) findViewById(R.id.radiogroup);
        Selected = (Button) findViewById(R.id.select_option);

        Selected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = optiongroup.getCheckedRadioButtonId();

                optionselect = (RadioButton) findViewById(selectedId);
                Toast.makeText(SettingsActivity.this, optionselect.getText(), Toast.LENGTH_SHORT).show();

                Intent next = new Intent(SettingsActivity.this, MainActivitys.class);
                next.putExtra("send", optionselect.getText());
                startActivity(next);
            }
        });
    }

    @Override
    protected boolean useDrawerToggle() {
        return false;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if(item.getItemId() == android.R.id.home)
            onBackPressed();

        return super.onOptionsItemSelected(item);

    }
}
