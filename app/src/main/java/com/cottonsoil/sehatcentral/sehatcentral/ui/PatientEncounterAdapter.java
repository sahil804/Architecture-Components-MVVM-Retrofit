package com.cottonsoil.sehatcentral.sehatcentral.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cottonsoil.sehatcentral.R;
import com.cottonsoil.sehatcentral.sehatcentral.Utility;
import com.cottonsoil.sehatcentral.sehatcentral.data.models.Encounter;
import com.cottonsoil.sehatcentral.sehatcentral.data.models.PrescriptionEncounter;
import com.cottonsoil.sehatcentral.sehatcentral.data.models.VitalsEncounter;

import java.util.List;

import static com.cottonsoil.sehatcentral.sehatcentral.Constants.VITAL_BLOOD_SATURATION;
import static com.cottonsoil.sehatcentral.sehatcentral.Constants.VITAL_DIASTOLIC;
import static com.cottonsoil.sehatcentral.sehatcentral.Constants.VITAL_HEIGHT;
import static com.cottonsoil.sehatcentral.sehatcentral.Constants.VITAL_PULSE;
import static com.cottonsoil.sehatcentral.sehatcentral.Constants.VITAL_SYSTOLIC;
import static com.cottonsoil.sehatcentral.sehatcentral.Constants.VITAL_TEMPERATURE;
import static com.cottonsoil.sehatcentral.sehatcentral.Constants.VITAL_WEIGHT;

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
                View viewVital = inflater.inflate(R.layout.layout_vitals_viewholder, parent, false);
                viewHolder = new VitalsViewHolder(viewVital);
                break;

            case PRESCRIPTION:
                View viewPrescription = inflater.inflate(R.layout.layout_prescription_viewholder, parent, false);
                viewHolder = new PrescriptionViewHolder(viewPrescription);
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
                vitalsViewHolder.configureVitalViewHolder(position);
                break;
            case PRESCRIPTION:
                PrescriptionViewHolder prescriptionViewHolder = (PrescriptionViewHolder) holder;
                prescriptionViewHolder.configurePrescriptionViewHolder(position);
                //prescriptionViewHolder.tvPrescription.setText(mEncounters.get(position).toString());
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

        private TextView tvVitalsDate;

        private LinearLayout lyBloodPressure;
        private TextView tvBloodPressure;

        private LinearLayout lyOxygenSaturation;
        private TextView tvOxygenSaturation;

        private LinearLayout lyPulse;
        private TextView tvPulse;

        private LinearLayout lyTemperature;
        private TextView tvTemperature;

        private LinearLayout lyHeight;
        private TextView tvHeight;

        private LinearLayout lyWeight;
        private TextView tvWeight;

        public VitalsViewHolder(View itemView) {
            super(itemView);
            tvVitalsDate = itemView.findViewById(R.id.tv_vitals_date);

            lyBloodPressure = itemView.findViewById(R.id.ly_blood_pressure);
            tvBloodPressure = itemView.findViewById(R.id.tv_bp_value);

            lyOxygenSaturation = itemView.findViewById(R.id.ly_oxygen_saturation);
            tvOxygenSaturation = itemView.findViewById(R.id.tv_oxygen_sat_value);

            lyPulse = itemView.findViewById(R.id.ly_pulse);
            tvPulse = itemView.findViewById(R.id.tv_pulse_value);

            lyTemperature = itemView.findViewById(R.id.ly_temperature);
            tvTemperature = itemView.findViewById(R.id.tv_temperature_value);

            lyHeight = itemView.findViewById(R.id.ly_height);
            tvHeight = itemView.findViewById(R.id.tv_height_value);

            lyWeight = itemView.findViewById(R.id.ly_weight);
            tvWeight = itemView.findViewById(R.id.tv_weight_value);
        }

        private void configureVitalViewHolder(int position) {
            VitalsEncounter vitalsEncounter = (VitalsEncounter) mEncounters.get(position);
            if (vitalsEncounter != null) {
                tvVitalsDate.setText(Utility.parserTime(vitalsEncounter.getEncounterDatetime()));
                List<VitalsEncounter.Vital> vitalList = vitalsEncounter.getVital();
                Log.d(TAG, "configureVitalViewHolder: vital list "+vitalList);
                String systolic = null; String diastolic = null;
                for (VitalsEncounter.Vital vital : vitalList) {
                    if(vital.getDisplay().contains(VITAL_SYSTOLIC)) {
                        systolic = Utility.getActualValue(vital.getDisplay());
                    } else if(vital.getDisplay().contains(VITAL_DIASTOLIC)) {
                        diastolic = Utility.getActualValue(vital.getDisplay());
                    } else if(vital.getDisplay().contains(VITAL_BLOOD_SATURATION)) {
                        lyOxygenSaturation.setVisibility(View.VISIBLE);
                        tvOxygenSaturation.setText(Utility.getActualValue(vital.getDisplay()));
                    } else if(vital.getDisplay().contains(VITAL_PULSE)) {
                        lyPulse.setVisibility(View.VISIBLE);
                        tvPulse.setText(Utility.getActualValue(vital.getDisplay()));
                    } else if(vital.getDisplay().contains(VITAL_TEMPERATURE)) {
                        lyTemperature.setVisibility(View.VISIBLE);
                        tvTemperature.setText(Utility.getActualValue(vital.getDisplay()));
                    } else if(vital.getDisplay().contains(VITAL_HEIGHT)) {
                        lyHeight.setVisibility(View.VISIBLE);
                        tvHeight.setText(Utility.getActualValue(vital.getDisplay()));
                    } else if(vital.getDisplay().contains(VITAL_WEIGHT)) {
                        lyWeight.setVisibility(View.VISIBLE);
                        tvWeight.setText(Utility.getActualValue(vital.getDisplay()));
                    }
                }
                if(systolic != null && diastolic != null) {
                    lyBloodPressure.setVisibility(View.VISIBLE);
                    tvBloodPressure.setText(systolic + " / " + diastolic);
                }
            }
        }

    }



    public class PrescriptionViewHolder extends RecyclerView.ViewHolder{
        private LinearLayout lyDrugs;
        private TextView tvDrugs;
        private TextView tvPrescriptionDate;
        public PrescriptionViewHolder(View itemView) {
            super(itemView);
            lyDrugs = itemView.findViewById(R.id.ly_drugs);
            tvDrugs = itemView.findViewById(R.id.tv_drugs_value);
            tvPrescriptionDate = itemView.findViewById(R.id.tv_prescription_date);
        }

        private void configurePrescriptionViewHolder(int position) {
            PrescriptionEncounter prescriptionEncounter = (PrescriptionEncounter) mEncounters.get(position);
            if (prescriptionEncounter != null) {
                tvPrescriptionDate.setText(Utility.parserTime(prescriptionEncounter.getEncounterDatetime()));
                List<PrescriptionEncounter.Order> orders = prescriptionEncounter.getOrders();
                Log.d(TAG, "configurePrescriptionViewHolder: orders list "+orders);
                for (PrescriptionEncounter.Order order : orders) {
                    tvDrugs.append(order.getDisplay()+" \n");
                }
            }
        }
    }

}