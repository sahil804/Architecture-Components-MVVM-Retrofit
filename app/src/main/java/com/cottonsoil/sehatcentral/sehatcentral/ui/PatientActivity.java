package com.cottonsoil.sehatcentral.sehatcentral.ui;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.cottonsoil.sehatcentral.R;
import com.cottonsoil.sehatcentral.sehatcentral.Constants;
import com.cottonsoil.sehatcentral.sehatcentral.data.models.Person;

import static com.cottonsoil.sehatcentral.sehatcentral.Constants.KEY_PERSON;

public class PatientActivity extends AppCompatActivity implements PatientFragment.OnFragmentInteractionListener{

    public static final String TAG = PatientActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);
        String uuid = getIntent().getExtras().getString(Constants.KEY_PATIENT_UUID);
        Person person = getIntent().getExtras().getParcelable(KEY_PERSON);
        PatientFragment fragment = PatientFragment.newInstance(uuid, person);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.patient_detail_container, fragment)
                .commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        Log.d(TAG, "onFragmentInteraction: ");
    }
}