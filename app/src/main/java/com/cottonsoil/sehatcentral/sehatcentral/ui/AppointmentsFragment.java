package com.cottonsoil.sehatcentral.sehatcentral.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cottonsoil.sehatcentral.R;
import com.cottonsoil.sehatcentral.sehatcentral.Constants;
import com.cottonsoil.sehatcentral.sehatcentral.Utility;
import com.cottonsoil.sehatcentral.sehatcentral.data.models.Appointment;
import com.cottonsoil.sehatcentral.sehatcentral.data.models.AppointmentDetails;
import com.cottonsoil.sehatcentral.sehatcentral.data.models.Person;
import com.cottonsoil.sehatcentral.sehatcentral.data.network.ApiInterface;
import com.cottonsoil.sehatcentral.sehatcentral.data.network.ServiceBuilder;
import com.cottonsoil.sehatcentral.sehatcentral.scheduler.AppointmentListSyncUtils;
import com.cottonsoil.sehatcentral.sehatcentral.viewmodel.AppointmentViewModel;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cottonsoil.sehatcentral.sehatcentral.Constants.DEBUG;
import static java.security.AccessController.getContext;

public class AppointmentsFragment extends Fragment implements View.OnClickListener, AppointmentsAdapter.AppointmentsAdapterOnClickHandler {

    private static final String TAG = AppointmentsFragment.class.getSimpleName();

    private OnFragmentInteractionListener mListener;

    private AppointmentViewModel mViewModel;

    private AppointmentsAdapter mAppointmentsAdapter;

    private RecyclerView mRecyclerView;

    private ImageView leftArrow,rightArrow;

    private TextView tvDateSelected;

    public AppointmentsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(DEBUG) Log.i(TAG, "onCreate: ");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_appointment_list, container, false);
        leftArrow = view.findViewById(R.id.iv_left_arrow);
        rightArrow = view.findViewById(R.id.iv_right_arrow);
        tvDateSelected = view.findViewById(R.id.tv_date_selected);
        leftArrow.setOnClickListener(this);
        rightArrow.setOnClickListener(this);
        initialiseRecyclerView(view);
        initialiseViewModel();
        AppointmentListSyncUtils.initialize(getContext());
        return view;
    }

    private void initialiseViewModel() {
        mViewModel = ViewModelProviders.of(this).get(AppointmentViewModel.class);
        mViewModel.getAppointmentDetails().observe(this, appointmentDetailsEntities -> {
            if(DEBUG) Log.d(TAG,"change detected !!! "+appointmentDetailsEntities);
            if(appointmentDetailsEntities != null) {
                if(DEBUG) Log.d(TAG,"change detected size !!! "+appointmentDetailsEntities.size());
            }
            if (appointmentDetailsEntities != null) mAppointmentsAdapter.swapAppointmentDetails(appointmentDetailsEntities);
        });
        mViewModel.setDate(Utility.getTodayDateInString());
        tvDateSelected.setText("Today, "+Utility.getTodayDateInString());
    }

    private void initialiseRecyclerView(View view) {
        mRecyclerView = view.findViewById(R.id.recyclerview_appointment_details);
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mAppointmentsAdapter = new AppointmentsAdapter(getContext(), this);
        mRecyclerView.setAdapter(mAppointmentsAdapter);
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

    @Override
    public void onClick(String UUID, Person person) {
        Intent intent = new Intent();
        intent.setClass(getContext(), PatientActivity.class);
        intent.putExtra(Constants.KEY_PATIENT_UUID, UUID);
        intent.putExtra(Constants.KEY_PERSON, person);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    public void onClick(View view) {
        int counter = mViewModel.getCurrentCounter();
        Log.d(TAG, "onClick: ");
        if (view == rightArrow) {
            ++counter;
            if(counter <= 10) {
                mViewModel.setCurrentCounter(counter);
                String date = Utility.getDateAfterXdaysInString(counter);
                mViewModel.setDate(date);
                handleViews(counter, date);
                Log.d(TAG, "Right onClick: "+date);
            }

        } else if(view == leftArrow) {
            --counter;
            if(counter >= -2) {
                mViewModel.setCurrentCounter(counter);
                String date = Utility.getDateAfterXdaysInString(counter);
                mViewModel.setDate(date);
                handleViews(counter, date);
                Log.d(TAG, "Left onClick: "+date);
            }
        }
    }

    private void handleViews(int counter, String date) {
        if(counter >= 10) {
            rightArrow.setVisibility(View.GONE);
            leftArrow.setVisibility(View.VISIBLE);
        } else if(counter <= -2) {
            rightArrow.setVisibility(View.VISIBLE);
            leftArrow.setVisibility(View.INVISIBLE);
        } else {
            rightArrow.setVisibility(View.VISIBLE);
            leftArrow.setVisibility(View.VISIBLE);
        }

        if(counter == 0) {
            tvDateSelected.setText("Today's, "+date);
        } else if(counter == 1) {
            tvDateSelected.setText("Tomorrow's, "+date);
        } else if (counter == -1) {
            tvDateSelected.setText("Yesterday's, "+date);
        } else {
            tvDateSelected.setText(date);
        }
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