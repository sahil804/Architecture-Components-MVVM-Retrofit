package com.cottonsoil.sehatcentral.sehatcentral.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cottonsoil.sehatcentral.R;
import com.cottonsoil.sehatcentral.sehatcentral.Constants;
import com.cottonsoil.sehatcentral.sehatcentral.data.models.Encounter;
import com.cottonsoil.sehatcentral.sehatcentral.data.models.PrescriptionEncounter;
import com.cottonsoil.sehatcentral.sehatcentral.data.models.VitalsEncounter;
import com.cottonsoil.sehatcentral.sehatcentral.viewmodel.AppointmentViewModel;
import com.cottonsoil.sehatcentral.sehatcentral.viewmodel.PatientEncountersViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PatientFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PatientFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PatientFragment extends Fragment {
    public static final String TAG = PatientFragment.class.getSimpleName();

    // TODO: Rename and change types of parameters
    private PatientEncountersViewModel mViewModel;

    private PatientEncounterAdapter mPatientEncounterAdapter;

    private RecyclerView mRecyclerView;

    private OnFragmentInteractionListener mListener;

    String mPatientUuid;

    public PatientFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment PatientFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PatientFragment newInstance(String uuid) {
        PatientFragment fragment = new PatientFragment();
        Bundle args = new Bundle();
        args.putString(Constants.KEY_PATIENT_UUID, uuid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPatientUuid = getArguments().getString(Constants.KEY_PATIENT_UUID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_patient, container, false);
        initialiseRecyclerView(view);
        initialiseViewModel();
        return view;
    }

    private void initialiseRecyclerView(View view) {
        mRecyclerView = view.findViewById(R.id.recyclerview_patient_encounters);
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mPatientEncounterAdapter = new PatientEncounterAdapter(getContext());
        mRecyclerView.setAdapter(mPatientEncounterAdapter);
        List<Encounter> encounterList = new ArrayList<>();
        mPatientEncounterAdapter.swapEncounters(encounterList);
    }

    private void initialiseViewModel() {
        mViewModel = ViewModelProviders.of(this).get(PatientEncountersViewModel.class);
        mViewModel.getmEncountersLiveData(mPatientUuid).observe(this, encounters -> {
            Log.d(TAG,"OnChange encounters Observed: "+encounters);
            for (Encounter encounter: encounters) {
                if(encounter instanceof PrescriptionEncounter) {
                    PrescriptionEncounter prescriptionEncounter = (PrescriptionEncounter) encounter;
                    Log.d(TAG,"OnChange encounters prescriptionEncounter matched: "+prescriptionEncounter);
                } else if(encounter instanceof VitalsEncounter) {
                    VitalsEncounter vitalsEncounter = (VitalsEncounter) encounter;
                    Log.d(TAG,"OnChange encounters VitalsEncounter matched: "+vitalsEncounter);
                } else {
                    Log.d(TAG,"OnChange encounters Nothing matched: ");
                }
            }
            mPatientEncounterAdapter.swapEncounters(encounters);
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}