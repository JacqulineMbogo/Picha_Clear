package com.example.picha_clear.Bookings;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.picha_clear.R;
import com.example.picha_clear.Utility.AppUtilits;
import com.example.picha_clear.Utility.Constant;
import com.example.picha_clear.Utility.DataValidation;
import com.example.picha_clear.Utility.NetworkUtility;
import com.example.picha_clear.Utility.SharedPreferenceActivity;
import com.example.picha_clear.WebServices.ServiceWrapper;
import com.example.picha_clear.beanResponse.BookingPaymentsMadeRes;
import com.example.picha_clear.beanResponse.BookingsPaymentRes;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class bookings_adapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<bookings_m0del> bookings_models;
    private Context mContext;
    private String TAG = "bookings_modelr";
    private  SharedPreferenceActivity sharedPreferenceActivity;

    public bookings_adapter(List<bookings_m0del> bookings_models, Context mContext, SharedPreferenceActivity sharedPreferenceActivity) {
        this.bookings_models = bookings_models;
        this.mContext = mContext;
        this.sharedPreferenceActivity = new SharedPreferenceActivity(mContext);
    }

    private class BookingHistoryItemView extends RecyclerView.ViewHolder {
        TextView booking_id, booking_set_date, booking_duration, booking_type, Booking_status, view_details, location ;


        public BookingHistoryItemView(View itemView) {
            super(itemView);
            booking_id = (TextView) itemView.findViewById(R.id.booking_id);
            booking_set_date = (TextView) itemView.findViewById(R.id.booking_set_date);
            booking_duration = (TextView) itemView.findViewById(R.id.booking_duration);
            booking_type= (TextView) itemView.findViewById(R.id.booking_type);
            Booking_status = itemView.findViewById(R.id.booking_status);
            view_details = itemView.findViewById(R.id.viewdetails);
            location = itemView.findViewById(R.id.location);

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
        ((BookingHistoryItemView) holder).booking_type.setText("Type :" +" " + model.getBooking_type());
        ((BookingHistoryItemView) holder).booking_set_date.setText(model.getBooking_date_set());
        ((BookingHistoryItemView) holder).booking_duration.setText("Hours : " + " " + model.getBooking_duration());
        ((BookingHistoryItemView) holder).Booking_status.setText(model.getBooking_status());
        ((BookingHistoryItemView) holder).location.setText("Location : " + " " +model.getLocation());

        ((BookingHistoryItemView) holder).view_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                    if (!NetworkUtility.isNetworkConnected(mContext)){
                        AppUtilits.displayMessage(mContext,  "Network Error");

                    }else {
                        //  Log.e(TAG, "  user value "+ SharePreferenceUtils.getInstance().getString(Constant.USER_DATA));
                        ServiceWrapper service = new ServiceWrapper(null);
                        Call<BookingPaymentsMadeRes> call = service.BookingsPaymentMadeRescall("1234" ,model.getBooking_id() );

                        Log.d(TAG, model.getBooking_id());
                        call.enqueue(new Callback<BookingPaymentsMadeRes>() {
                            @Override
                            public void onResponse(Call<BookingPaymentsMadeRes> call, Response<BookingPaymentsMadeRes> response) {
                                Log.e(TAG, "response is "+ response.body() + "  ---- "+ new Gson().toJson(response.body()));
                                //  Log.e(TAG, "  ss sixe 1 ");
                                if (response.body() != null && response.isSuccessful()) {
                                    //    Log.e(TAG, "  ss sixe 2 ");
                                    if (response.body().getStatus() == 1) {


                                        final Dialog dialog;
                                        final  View view;

                                        view = LayoutInflater.from(mContext).inflate(R.layout.pay_popup, null, false);
                                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

                                        alertDialog.setView(view);

                                        alertDialog.setCancelable(true);


                                        dialog = alertDialog.create();

                                        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

                                        dialog.show();


                                        final Window dialogWindow = dialog.getWindow();
                                        dialogWindow.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                                        dialogWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);


                                        final TextView paymenttext = view.findViewById(R.id.paymenttext);
                                        final EditText mpesacode = view.findViewById(R.id.mpesacode);
                                        final EditText total_amount = view.findViewById(R.id.total_amount);
                                        final Button okay = view.findViewById(R.id.ok);
                                        final Button cancel = view.findViewById(R.id.cancel);
                                        final  TextView cleartext = view.findViewById(R.id.cleartext);
                                        final LinearLayout mpesalinear = view.findViewById(R.id.mpesalinear);
                                        final  LinearLayout amountlinear = view.findViewById(R.id.amountlinear);

                                        Integer balance;
                                        balance= Integer.valueOf(model.getBooking_totalprice().trim()) - Integer.valueOf(response.body().getMsg().trim() );


                                        if (balance == 0){

                                            paymenttext.setText("Total amount : " + " "   +  model.getBooking_totalprice() +" " +'\n' + "Amount paid: " + response.body().getMsg()
                                                    +'\n'  + " " + "Balance : " +String.valueOf(balance));

                                            amountlinear.setVisibility(View.GONE);
                                            mpesalinear.setVisibility(View.GONE);
                                            cleartext.setVisibility(View.GONE);
                                            okay.setVisibility(View.GONE);

                                        }else   if (balance < 0){

                                            paymenttext.setText("Total amount : " + " "   +  model.getBooking_totalprice() +" " +'\n' + "Amount paid: " + response.body().getMsg()
                                                    +'\n'  + " " + "Overpayment : " +String.valueOf(balance));
                                            amountlinear.setVisibility(View.GONE);
                                            mpesalinear.setVisibility(View.GONE);
                                            cleartext.setVisibility(View.GONE);
                                            okay.setVisibility(View.GONE);

                                        }else{

                                            paymenttext.setText("Total amount : " + " "   +  model.getBooking_totalprice() +" " +'\n' + "Amount paid: " + response.body().getMsg()
                                                    +'\n'  + " " + "Balance : " +String.valueOf(balance));

                                        }


                                        cancel.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                dialog.cancel();
                                            }
                                        });


                                        okay.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                if (!total_amount.getText().toString().isEmpty()){

                                                    if (!mpesacode.getText().toString().isEmpty()){

                                                        if (!DataValidation.isNotValidcode(mpesacode.getText().toString())) {

                                                            makebookingpay();

                                                        }else{

                                                            mpesacode.setError("Invalid code length. Should be 10 characters.");
                                                            mpesacode.requestFocus();

                                                        }
                                                    }else{

                                                        mpesacode.setError("please input transaction code");
                                                        mpesacode.requestFocus();

                                                    }

                                                }else{
                                                    total_amount.setError("please input amount");
                                                    total_amount.requestFocus();



                                                }

                                            }

                                            private void makebookingpay() {


                                                if (!NetworkUtility.isNetworkConnected(mContext)) {
                                                    Toast.makeText(mContext, "Network error", Toast.LENGTH_LONG).show();


                                                } else {

                                                    ServiceWrapper serviceWrapper = new ServiceWrapper(null);
                                                    Call<BookingsPaymentRes> NewBookingPaymentRescall = serviceWrapper.NewBookingPaymentRescall( "1234",sharedPreferenceActivity.getItem(Constant.USER_DATA),model.getBooking_id(),mpesacode.getText().toString(),total_amount.getText().toString().trim());
                                                    NewBookingPaymentRescall.enqueue(new Callback<BookingsPaymentRes>() {
                                                        @Override
                                                        public void onResponse(Call<BookingsPaymentRes> call, Response<BookingsPaymentRes> response) {
                                                            Log.d(TAG, "reponse : " + response.toString());
                                                            if (response.body() != null && response.isSuccessful()) {
                                                                if (response.body().getStatus() == 1) {

                                                                    AppUtilits.createToaster(mContext, "Payment made Successfully", Toast.LENGTH_LONG);
                                                                    dialog.cancel();


                                                                } else {

                                                                    AppUtilits.displayMessage(mContext, response.body().getMsg());
                                                                }
                                                            } else {

                                                                Toast.makeText(mContext, "Request failed", Toast.LENGTH_LONG).show();

                                                            }
                                                        }

                                                        @Override
                                                        public void onFailure(Call<BookingsPaymentRes> call, Throwable t) {

                                                            Log.e(TAG, " failure " + t.toString());
                                                            Toast.makeText(mContext, "Request failed", Toast.LENGTH_LONG).show();


                                                        }
                                                    });
                                                }

                                            }
                                        });


                                    } else {
                                        AppUtilits.displayMessage(mContext, "No booking payments exist." );
                                    }
                                }else {
                                    AppUtilits.displayMessage(mContext, "Network error");
                                }

                            }

                            @Override
                            public void onFailure(Call<BookingPaymentsMadeRes> call, Throwable t) {

                                Log.e(TAG, "  fail- order symmer item "+ t.toString());
                                AppUtilits.displayMessage(mContext, "Failed to get booking payments");


                            }
                        });



                    }





              /*  Log.e(TAG, "  user select the order id " + model.getBooking_id() );


                Intent intent = new Intent(mContext, booking_payment.class);
                intent.putExtra("booking_id", model.getBooking_id());
                intent.putExtra("total_price", model.getBooking_totalprice());
                mContext.startActivity(intent);*/



            }
        });
    }




    @Override
    public int getItemCount() {
        return bookings_models.size();
    }
}
