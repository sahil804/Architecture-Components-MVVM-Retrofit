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
        void onClick(String UUID, String name);
    }

    @NonNull
    @Override
    public AppointmentsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(
                parent.getContext()).inflate(R.layout.item_appointment_detail, parent, false);
        view.setFocusable(true);
        return new AppointmentsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentsAdapterViewHolder holder, int position) {
        if(DEBUG) Log.d(TAG,"onBindViewHolder: "+mAppointmentDetailsEntities.get(position).getUuid());
        holder.tvUuid.setText(mAppointmentDetailsEntities.get(position).getUuid());
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
        public TextView tvUuid;
        public AppointmentsAdapterViewHolder(View itemView) {
            super(itemView);
            tvUuid = itemView.findViewById(R.id.tv_uuid);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mClickHandler.onClick("test", "test");
        }
    }
}
