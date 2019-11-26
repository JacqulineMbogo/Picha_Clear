package com.example.picha_clear.Bookings;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.picha_clear.R;
import com.example.picha_clear.Spinner_Adapter;
import com.example.picha_clear.Utility.AppUtilits;
import com.example.picha_clear.Utility.NetworkUtility;
import com.example.picha_clear.Utility.SharedPreferenceActivity;
import com.example.picha_clear.WebServices.ServiceWrapper;
import com.example.picha_clear.beanResponse.LocationRes;
import com.example.picha_clear.beanResponse.TypesRes;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class newbooking extends AppCompatActivity {

    Context context;
    SharedPreferenceActivity sharedPreferenceActivity;

    private  location_adapter location_adapter;
    private  types_adapter types_adapter;
    private ArrayList<location_model> locationModels = new ArrayList<>();
    private ArrayList<types_model> typesModels = new ArrayList<>();

    Spinner spinner, type_spinner;
    EditText date, time;
    DatePickerDialog picker;
    TimePickerDialog picker1;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_newbooking);

        context= this;
        sharedPreferenceActivity = new SharedPreferenceActivity(context);

       location_adapter = new location_adapter(locationModels, context);
        types_adapter = new types_adapter(typesModels, context);


       spinner= findViewById(R.id.locationspinner);
       type_spinner = findViewById(R.id.booking_type);
       date = findViewById(R.id.date);
       time = findViewById(R.id.time);

        this.setTitle("Make a Booking");

   date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minutes = cldr.get(Calendar.MINUTE);
                // time picker dialog
                picker1 = new TimePickerDialog(context,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                time.setText(sHour + ":" + sMinute);
                            }
                        }, hour, minutes, true);
                picker1.show();
            }
        });



        Loadlocations();
        LoadTypes();

    }

    public  void  LoadTypes(){

        if (!NetworkUtility.isNetworkConnected(this)) {

            Toast.makeText(getApplicationContext(), "Network error", Toast.LENGTH_LONG).show();


        } else {

            //  Log.e(TAG, "  user value "+ SharePreferenceUtils.getInstance().getString(Constant.USER_DATA));
            ServiceWrapper service = new ServiceWrapper(null);
            Call<TypesRes> call = service.TypesResCall("1234");


            call.enqueue(new Callback<TypesRes>() {
                @Override
                public void onResponse(Call<TypesRes> call, Response<TypesRes> response) {

                    if (response.body() != null && response.isSuccessful()) {

                        if (response.body().getStatus() == 1) {


                            typesModels.clear();
                            if (response.body().getInformation().size()>0){

                                String[] code = new String[response.body().getInformation().size()];
                                String[] name = new String[response.body().getInformation().size()];

                                Log.d("codey", String.valueOf(response.body().getInformation().size()));
                                for (int i =0; i<response.body().getInformation().size(); i++){

                                   typesModels.add(  new types_model(response.body().getInformation().get(i).getTypeId(),response.body().getInformation().get(i).getTypeName(),response.body().getInformation().get(i).getTypeCost()));
                                    code[i] = response.body().getInformation().get(i).getTypeCost();
                                    name[i] = response.body().getInformation().get(i).getTypeName();
                                    Log.d("codee", code[i]);
                                    Log.d("codee", name[i]);
                                }

                                Spinner_Adapter spinner_adapter= new Spinner_Adapter(context, code, name);
                                spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                             type_spinner.setAdapter(spinner_adapter);

                            }

                        } else {

                            AppUtilits.displayMessage(context, response.body().getMsg());
                        }
                    } else {

                        Toast.makeText(getApplicationContext(), "Please try again", Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onFailure(Call<TypesRes> call, Throwable t) {

                    Toast.makeText(getApplicationContext(), "please try again. Failed to get locations ", Toast.LENGTH_LONG).show();

                }
            });


        }



    }

    public  void  Loadlocations(){

        if (!NetworkUtility.isNetworkConnected(this)) {

            Toast.makeText(getApplicationContext(), "Network error", Toast.LENGTH_LONG).show();


        } else {

            //  Log.e(TAG, "  user value "+ SharePreferenceUtils.getInstance().getString(Constant.USER_DATA));
            ServiceWrapper service = new ServiceWrapper(null);
            Call<LocationRes> call = service.LocationResCall("1234");


            call.enqueue(new Callback<LocationRes>() {
                @Override
                public void onResponse(Call<LocationRes> call, Response<LocationRes> response) {

                    if (response.body() != null && response.isSuccessful()) {

                        if (response.body().getStatus() == 1) {


                            locationModels.clear();
                            if (response.body().getInformation().size()>0){

                                String[] code = new String[response.body().getInformation().size()];
                                String[] name = new String[response.body().getInformation().size()];

                                Log.d("codey", String.valueOf(response.body().getInformation().size()));
                                for (int i =0; i<response.body().getInformation().size(); i++){

                                    locationModels.add(  new location_model(response.body().getInformation().get(i).getLocationId(),response.body().getInformation().get(i).getLocationName(),response.body().getInformation().get(i).getLocationPrice()));
                                    code[i] = response.body().getInformation().get(i).getLocationPrice();
                                    name[i] = response.body().getInformation().get(i).getLocationName();
                                    Log.d("codee", code[i]);
                                    Log.d("codee", name[i]);
                                }

                                Spinner_Adapter spinner_adapter= new Spinner_Adapter(context, code, name);
                                spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner.setAdapter(spinner_adapter);

                            }

                        } else {

                            AppUtilits.displayMessage(context, response.body().getMsg());
                        }
                    } else {

                        Toast.makeText(getApplicationContext(), "Please try again", Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onFailure(Call<LocationRes> call, Throwable t) {

                    Toast.makeText(getApplicationContext(), "please try again. Failed to get locations ", Toast.LENGTH_LONG).show();

                }
            });


        }



    }
}
