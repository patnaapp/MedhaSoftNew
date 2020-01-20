package bih.nic.medhasoft;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MismatchedBeneficiaryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mismatched_beneficiary);
    }


    public void onCLick_ViewMismatchedList(View v) {

        Intent i=new Intent(MismatchedBeneficiaryActivity.this,MismatchedBeneficiaryListActivity.class);
        startActivity(i);



    }

    public void onCLick_ViewUpdatedMismatchedList(View v) {



    }

    public void onCLick_UploadUpdatedMismatchedList(View v) {



    }
}
