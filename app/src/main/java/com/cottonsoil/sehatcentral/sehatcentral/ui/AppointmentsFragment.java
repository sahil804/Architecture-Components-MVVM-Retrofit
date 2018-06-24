package com.cottonsoil.sehatcentral.sehatcentral.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
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
import android.widget.Toast;

import com.cottonsoil.sehatcentral.R;
import com.cottonsoil.sehatcentral.sehatcentral.data.models.Appointment;
import com.cottonsoil.sehatcentral.sehatcentral.data.models.AppointmentDetails;
import com.cottonsoil.sehatcentral.sehatcentral.data.models.AppointmentList;
import com.cottonsoil.sehatcentral.sehatcentral.data.network.ApiInterface;
import com.cottonsoil.sehatcentral.sehatcentral.data.network.ServiceBuilder;
import com.cottonsoil.sehatcentral.sehatcentral.viewmodel.AppointmentViewModel;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AppointmentsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AppointmentsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AppointmentsFragment extends Fragment implements AppointmentsAdapter.AppointmentsAdapterOnClickHandler {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String TAG = AppointmentsFragment.class.getSimpleName();

    private OnFragmentInteractionListener mListener;

    private AppointmentViewModel mViewModel;

    private AppointmentsAdapter mAppointmentsAdapter;

    private RecyclerView mRecyclerView;

    public AppointmentsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AppointmentsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AppointmentsFragment newInstance(String param1, String param2) {
        AppointmentsFragment fragment = new AppointmentsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate: ");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_appointment_list, container, false);
        SharedPreferences mSettings = PreferenceManager.getDefaultSharedPreferences(getActivity());
        final String accessToken = mSettings.getString("accessToken", null);
        String provider = mSettings.getString("provider", null);
        Log.d(TAG,"accessToken: "+accessToken + " provider: "+provider);

        mRecyclerView = view.findViewById(R.id.recyclerview_appointment_details);
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mAppointmentsAdapter = new AppointmentsAdapter(getContext(), this);
        mRecyclerView.setAdapter(mAppointmentsAdapter);

        mViewModel = ViewModelProviders.of(this).get(AppointmentViewModel.class);

        mViewModel.getAppointmentDetails().observe(this, appointmentDetailsEntities -> {
            Log.d(TAG,"change detected !!! "+appointmentDetailsEntities);
            if(appointmentDetailsEntities != null) {
                Log.d(TAG,"change detected size !!! "+appointmentDetailsEntities.size());
            }
            if (appointmentDetailsEntities != null) mAppointmentsAdapter.swapAppointmentDetails(appointmentDetailsEntities);
        });

        /*ApiInterface apiInterface = ServiceBuilder.buildService(ApiInterface.class);
        Call<AppointmentList> authorizationCall = null;
        try {
            authorizationCall = apiInterface.getAppointmentList(URLDecoder.decode(accessToken, "UTF-8"), provider);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        authorizationCall.enqueue(new Callback<AppointmentList>() {
            @Override
            public void onResponse(Call<AppointmentList> call, Response<AppointmentList> response) {
                AppointmentList list = response.body();
                Log.i(TAG, "onResponse: " + list);
                if(list != null) {
                    for (Appointment appointment :
                            list.getAppointments()) {
                        Log.i(TAG, "onResponse: " + appointment);
                    }
                    getAppointment(list, accessToken);
                }
            }

            @Override
            public void onFailure(Call<AppointmentList> call, Throwable t) {

            }
        });*/

        return view;
    }

    private void getAppointment(AppointmentList list, String accessToken) {
        ApiInterface apiInterface = ServiceBuilder.buildService(ApiInterface.class);
        Call<AppointmentDetails> authorizationCall = null;
        try {
            for (Appointment appoint: list.getAppointments()) {
            //Appointment appoint = list.getAppointments().get(0);
                authorizationCall = apiInterface.getAppointment(URLDecoder.decode(accessToken, "UTF-8"), appoint.getUuid());
                authorizationCall.enqueue(new Callback<AppointmentDetails>() {
                    @Override
                    public void onResponse(Call<AppointmentDetails> call, Response<AppointmentDetails> response) {
                        AppointmentDetails list = response.body();
                        Log.i(TAG, "onResponse: " + list);
                    }

                    @Override
                    public void onFailure(Call<AppointmentDetails> call, Throwable t) {

                    }
                });
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
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

    @Override
    public void onClick(String UUID, String name) {

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
