package com.cottonsoil.sehatcentral.sehatcentral.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cottonsoil.sehatcentral.R;
import com.cottonsoil.sehatcentral.sehatcentral.Utility;
import com.cottonsoil.sehatcentral.sehatcentral.data.database.entities.AppointmentDetailsEntity;
import com.cottonsoil.sehatcentral.sehatcentral.data.models.Person;

import java.util.List;

import static com.cottonsoil.sehatcentral.sehatcentral.Constants.DEBUG;

public class AppointmentsAdapter extends RecyclerView.Adapter<AppointmentsAdapter.AppointmentsAdapterViewHolder> {

    public static final String TAG = AppointmentsAdapter.class.getSimpleName();
    private List<AppointmentDetailsEntity> mAppointmentDetailsEntities;
    private final Context mContext;

    final private AppointmentsAdapterOnClickHandler mClickHandler;

    public AppointmentsAdapter(Context mContext, AppointmentsAdapterOnClickHandler mClickHandler) {
        this.mContext = mContext;
        this.mClickHandler = mClickHandler;
    }

    public interface AppointmentsAdapterOnClickHandler {
        void onClick(String UUID, Person person);
    }

    @NonNull
    @Override
    public AppointmentsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(
                parent.getContext()).inflate(R.layout.layout_appointments_viewholder, parent, false);
        view.setFocusable(true);
        return new AppointmentsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentsAdapterViewHolder holder, int position) {
        if(DEBUG) Log.d(TAG,"onBindViewHolder: "+mAppointmentDetailsEntities.get(position).getUuid());
        holder.tvPatientName.setText(mAppointmentDetailsEntities.get(position).getPatientName());
        holder.tvPatientAgeGender.setText(mAppointmentDetailsEntities.get(position).getPatientAge() + "/"
                + mAppointmentDetailsEntities.get(position).getPatientGender());
        holder.tvTimeSlot.setText(mAppointmentDetailsEntities.get(position).getStartDate() + " - "+
                mAppointmentDetailsEntities.get(position).getEndDate());
        holder.tvDisplayStatus.setText(Utility.getActualValue(mAppointmentDetailsEntities.get(position).getDisplayStatus()));
    }

    public void swapAppointmentDetails(final List<AppointmentDetailsEntity> appointmentDetailsEntities) {
        mAppointmentDetailsEntities = appointmentDetailsEntities;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (null == mAppointmentDetailsEntities) return 0;
        return mAppointmentDetailsEntities.size();
    }

    public class AppointmentsAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tvPatientName;
        public TextView tvPatientAgeGender;
        public TextView tvTimeSlot;
        public TextView tvDisplayStatus;
        public AppointmentsAdapterViewHolder(View itemView) {
            super(itemView);
            tvPatientName = itemView.findViewById(R.id.tv_patient_name);
            tvPatientAgeGender = itemView.findViewById(R.id.tv_patient_age_gender);
            tvTimeSlot = itemView.findViewById(R.id.tv_time_slot);
            tvDisplayStatus = itemView.findViewById(R.id.display_status);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            AppointmentDetailsEntity appointmentDetailsEntity = mAppointmentDetailsEntities.get(getAdapterPosition());
            Person person = new Person();
            person.setAge(appointmentDetailsEntity.getPatientAge());
            person.setName(appointmentDetailsEntity.getPatientName());
            person.setGender(appointmentDetailsEntity.getPatientGender());
            mClickHandler.onClick(appointmentDetailsEntity.getPatientUuid(), person);
        }
    }
}