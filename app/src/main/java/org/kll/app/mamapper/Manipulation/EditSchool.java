package org.kll.app.mamapper.Manipulation;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.greenrobot.eventbus.EventBus;
import org.kll.app.mamapper.Model.OverpassQueryResult;
import org.kll.app.mamapper.R;
import org.kll.app.mamapper.databinding.EditSchoolBinding;

/**
 * Created by nirab on 1/3/17.
 */

public class EditSchool extends AppCompatActivity {

    EditSchoolBinding mBinding;
    OverpassQueryResult.Element mElement;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.edit_school);
        mElement = EventBus.getDefault().removeStickyEvent(OverpassQueryResult.Element.class);
        mBinding.setTags(mElement.tags);

        mBinding.btnSenddata.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("school");
                getDataFromViews();
                myRef.push().setValue(mElement);
                Toast.makeText(getApplicationContext(), "Thank you for your contribution, the data will be reviewed and uploaded within few days", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    private void getDataFromViews(){
        mElement.tags.name = mBinding.editTextName.getText().toString();
        mElement.tags.nepaliName = mBinding.editTextNepaliName.getText().toString();
        mElement.tags.operatorType = mBinding.editTextOperator.getText().toString();
        mElement.tags.personnelCount = mBinding.editTextPersonnelCount.getText().toString();
        mElement.tags.studentCount = mBinding.editTextStudentCount.getText().toString();
        mElement.tags.website = mBinding.editTextWebsite.getText().toString();
        mElement.tags.phone = mBinding.editTextPhone.getText().toString();
    }
}
