package com.cottonsoil.sehatcentral.sehatcentral.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.cottonsoil.sehatcentral.R;
import com.cottonsoil.sehatcentral.sehatcentral.Constants;

public class PatientActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);
        getIntent().getExtras().getString(Constants.KEY_PATIENT_UUID);
        PatientFragment fragment = PatientFragment.newInstance(null);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.patient_detail_container, fragment)
                .commit();
    }
}
