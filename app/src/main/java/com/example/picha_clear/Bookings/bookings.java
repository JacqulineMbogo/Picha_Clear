package com.example.picha_clear.Bookings;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.picha_clear.R;
import com.example.picha_clear.Utility.AppUtilits;
import com.example.picha_clear.Utility.Constant;
import com.example.picha_clear.Utility.NetworkUtility;
import com.example.picha_clear.Utility.SharedPreferenceActivity;
import com.example.picha_clear.WebServices.ServiceWrapper;
import com.example.picha_clear.beanResponse.BookingsRes;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class bookings extends AppCompatActivity {


    private String TAG = "bookings";

    SharedPreferenceActivity sharedPreferenceActivity;
    Context context;

    private RecyclerView bookings_recyclerview;
    private ArrayList<bookings_m0del> bookingsModels = new ArrayList<>();
    private bookings_adapter bookingsAdapter;


    FloatingActionButton newbooking;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookings);

        context = this;
        sharedPreferenceActivity = new SharedPreferenceActivity(context);
        this.setTitle("My Bookings");

        newbooking = findViewById(R.id.newbookings);

       bookings_recyclerview = (RecyclerView) findViewById(R.id.recycler_bookings);

        LinearLayoutManager mLayoutManger3 = new LinearLayoutManager( this, RecyclerView.VERTICAL, false);
        bookings_recyclerview.setLayoutManager(mLayoutManger3);
        bookings_recyclerview.setItemAnimator(new DefaultItemAnimator());

       bookingsAdapter= new bookings_adapter(bookingsModels, this);
        bookings_recyclerview.setAdapter(bookingsAdapter);

        getUserBookings();
    }

    public void getUserBookings(){

        if (!NetworkUtility.isNetworkConnected(this)){
            AppUtilits.displayMessage(this,  getString(R.string.network_not_connected));

        }else {
            //  Log.e(TAG, "  user value "+ SharePreferenceUtils.getInstance().getString(Constant.USER_DATA));
            ServiceWrapper service = new ServiceWrapper(null);
            Call<BookingsRes> call = service.BookingsRescall("1234" , sharedPreferenceActivity.getItem(Constant.USER_DATA)  );

            call.enqueue(new Callback<BookingsRes>() {
                @Override
                public void onResponse(Call<BookingsRes> call, Response<BookingsRes> response) {
                    Log.e(TAG, "response is "+ response.body() + "  ---- "+ new Gson().toJson(response.body()));
                    //  Log.e(TAG, "  ss sixe 1 ");
                    if (response.body() != null && response.isSuccessful()) {
                        //    Log.e(TAG, "  ss sixe 2 ");
                        if (response.body().getStatus() == 1) {



                           bookingsModels.clear();
                            for (int i=0; i<response.body().getInformation().getBookingDetails().size(); i++){


                                bookingsModels.add( new bookings_m0del(response.body().getInformation().getBookingDetails().get(i).getId(),response.body().getInformation().getBookingDetails().get(i).getType(),response.body().getInformation().getBookingDetails().get(i).getDateCreated(),response.body().getInformation().getBookingDetails().get(i).getDateSet(),response.body().getInformation().getBookingDetails().get(i).getDuration(),response.body().getInformation().getBookingDetails().get(i).getStatus() ));

                            }

                            bookingsAdapter.notifyDataSetChanged();


                        } else {
                            AppUtilits.displayMessage(bookings.this, response.body().getMsg() );
                        }
                    }else {
                        AppUtilits.displayMessage(bookings.this, getString(R.string.network_error));
                    }

                }

                @Override
                public void onFailure(Call<BookingsRes> call, Throwable t) {

                    Log.e(TAG, "  fail- order symmer item "+ t.toString());
                    AppUtilits.displayMessage(bookings.this, getString(R.string.fail_toordersummery));


                }
            });



        }


    }
}
