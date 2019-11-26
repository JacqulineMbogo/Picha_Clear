package com.example.picha_clear.Bookings;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.picha_clear.R;

import java.util.List;

public class location_adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<location_model> location_models;
    private Context mContext;
    private String TAG ="locationAdapter";

    public location_adapter(List<location_model> location_models, Context mContext) {
        this.location_models = location_models;
        this.mContext = mContext;
    }

    private class locationsView extends RecyclerView.ViewHolder {

        TextView location_name,location_id;


        public locationsView(View itemView) {
            super(itemView);

            location_name =  itemView.findViewById(R.id.location_id);
            location_id=  itemView.findViewById(R.id.location_name);

        }
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_locations, parent,false);
        Log.e(TAG, "  view created ");
        return new locationsView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {


        final location_model model =  location_models.get(position);

        ((locationsView) holder).location_id.setText(model.getLocation_id());
        ((locationsView)holder).location_name.setText(model.getLocation_name());


    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
