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

public class types_adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<types_model> types_models;
    private Context mContext;
    private String TAG ="locationAdapter";

    public types_adapter(List<types_model> types_models, Context mContext) {
        this.types_models = types_models;
        this.mContext = mContext;
    }

    private class  typesView extends RecyclerView.ViewHolder {

        TextView location_name,location_id;


        public typesView(View itemView) {
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
        return new typesView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {


        final types_model model =  types_models.get(position);

        ((typesView) holder).location_id.setText(model.getType_id());
        ((typesView)holder).location_name.setText(model.getType_name());


    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
