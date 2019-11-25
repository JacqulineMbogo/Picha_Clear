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

public class bookings_adapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<bookings_m0del> bookings_models;
    private Context mContext;
    private String TAG = "bookings_modelr";

    public bookings_adapter(List<bookings_m0del> bookings_models, Context mContext) {
        this.bookings_models = bookings_models;
        this.mContext = mContext;
    }

    private class BookingHistoryItemView extends RecyclerView.ViewHolder {
        TextView booking_id, booking_set_date, booking_duration, booking_type, Booking_status ;


        public BookingHistoryItemView(View itemView) {
            super(itemView);
            booking_id = (TextView) itemView.findViewById(R.id.booking_id);
            booking_set_date = (TextView) itemView.findViewById(R.id.booking_set_date);
            booking_duration = (TextView) itemView.findViewById(R.id.booking_duration);
            booking_type= (TextView) itemView.findViewById(R.id.booking_type);
            Booking_status = itemView.findViewById(R.id.booking_status);

        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_booking_item, parent,false);
        Log.e(TAG, "  view created ");
        return new BookingHistoryItemView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        final bookings_m0del model = bookings_models.get(position);

        ((BookingHistoryItemView) holder).booking_id.setText(model.getBooking_id());
        ((BookingHistoryItemView) holder).booking_type.setText(model.getBooking_type());
        ((BookingHistoryItemView) holder).booking_set_date.setText(model.getBooking_date_set());
        ((BookingHistoryItemView) holder).booking_duration.setText(model.getBooking_duration());
        ((BookingHistoryItemView) holder).Booking_status.setText(model.getBooking_status());
    }

    @Override
    public int getItemCount() {
        return bookings_models.size();
    }
}
