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
import com.cottonsoil.sehatcentral.sehatcentral.data.database.entities.AppointmentDetailsEntity;
import com.cottonsoil.sehatcentral.sehatcentral.data.models.Encounter;
import com.cottonsoil.sehatcentral.sehatcentral.data.models.PrescriptionEncounter;
import com.cottonsoil.sehatcentral.sehatcentral.data.models.VitalsEncounter;

import java.util.List;

/**
 * Created by sahil on 6/27/2018.
 */
public class PatientEncounterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final String TAG = PatientEncounterAdapter.class.getSimpleName();
    private List<Encounter> mEncounters;
    private final Context mContext;

    private final int VITALS = 0, PRESCRIPTION = 1;

    public PatientEncounterAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case VITALS:
                View v1 = inflater.inflate(R.layout.layout_vitals_viewholder, parent, false);
                viewHolder = new VitalsViewHolder(v1);
                break;

            case PRESCRIPTION:
                View v2 = inflater.inflate(R.layout.layout_prescription_viewholder, parent, false);
                viewHolder = new PrescriptionViewHolder(v2);
                break;

            default:
                View v = inflater.inflate(R.layout.layout_vitals_viewholder, parent, false);
                viewHolder = new VitalsViewHolder(v);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case VITALS:
                VitalsViewHolder vitalsViewHolder = (VitalsViewHolder) holder;
                vitalsViewHolder.tvVitals.setText(mEncounters.get(position).toString());
                break;
            case PRESCRIPTION:
                PrescriptionViewHolder prescriptionViewHolder = (PrescriptionViewHolder) holder;
                prescriptionViewHolder.tvPrescription.setText(mEncounters.get(position).toString());
                break;
            default:
                Log.e(TAG, "onBindViewHolder: should not come here !!!");

        }

    }

    @Override
    public int getItemCount() {
        if(null == mEncounters) return 0;
        return mEncounters.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(mEncounters.get(position) instanceof VitalsEncounter) {
            return VITALS;
        } else if(mEncounters.get(position) instanceof PrescriptionEncounter) {
            return PRESCRIPTION;
        }
        return -1;
    }

    public void swapEncounters(final List<Encounter> encounterList) {
        mEncounters = encounterList;
        notifyDataSetChanged();
    }


    public class VitalsViewHolder extends RecyclerView.ViewHolder{
        public TextView tvVitals;
        public VitalsViewHolder(View itemView) {
            super(itemView);
            tvVitals = itemView.findViewById(R.id.tv_vitals);
        }
    }

    public class PrescriptionViewHolder extends RecyclerView.ViewHolder{
        public TextView tvPrescription;
        public PrescriptionViewHolder(View itemView) {
            super(itemView);
            tvPrescription = itemView.findViewById(R.id.tv_prescription);
        }
    }

}