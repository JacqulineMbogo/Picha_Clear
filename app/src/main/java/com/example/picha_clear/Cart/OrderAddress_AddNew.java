package com.example.picha_clear.Cart;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.picha_clear.R;
import com.example.picha_clear.Utility.AppUtilits;
import com.example.picha_clear.Utility.Constant;
import com.example.picha_clear.Utility.NetworkUtility;
import com.example.picha_clear.Utility.SharedPreferenceActivity;
import com.example.picha_clear.WebServices.ServiceWrapper;
import com.example.picha_clear.beanResponse.AddNewAddress;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class OrderAddress_AddNew extends AppCompatActivity {

    private String TAG = "orderaddress_addnew";
    private CheckBox checkbill, checkship;
    private EditText address1, address2, city,state,pincode,fullname,phone;
    private TextView savecontinue;
    private Spinner spinner;
    private String pin,selectedItemText;
    SharedPreferenceActivity sharedPreferenceActivity;
    Context context;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_editaddress);

        fullname = findViewById(R.id.fullname);
        phone=findViewById(R.id.phone);
        address1 = (EditText) findViewById(R.id.address1);
        address2 = (EditText) findViewById(R.id.address2);
        Spinner spinner=(Spinner)findViewById(R.id.city);
        context = this;
        sharedPreferenceActivity = new SharedPreferenceActivity(context);

        String[] counties=getResources().getStringArray(R.array.county_arrays);

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,R.layout.spinner,R.id.text, counties);

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 selectedItemText = (String) parent.getItemAtPosition(position);
                // Notify the selected item text


        if(selectedItemText.equals("Mombasa")){

            pin="1000";
        }else  if(selectedItemText.equals("Nairobi")){

            pin="300";
        }else  if(selectedItemText.equals("Kisumu")){

            pin="800";
        }else  if(selectedItemText.equals("Nyeri")){

            pin="150";
        }else  if(selectedItemText.equals("Meru")){

            pin="500";
        }else  if(selectedItemText.equals("Kiambu")){

            pin="300";
        }else  if(selectedItemText.equals("Embu")){

            pin="100";
        }else  if(selectedItemText.equals("Migori")){

            pin="1500";
        }

                Toast.makeText
                        (getApplicationContext(), "transport cost for : " + selectedItemText +"is "  + pin, Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        String selectedItem = spinner.getSelectedItem().toString();
        state = (EditText) findViewById(R.id.state);
        pincode = (EditText) findViewById(R.id.pincode);

        fullname.setText(sharedPreferenceActivity.getItem(Constant.USER_name));
        phone.setText(sharedPreferenceActivity.getItem(Constant.USER_phone));


        savecontinue = (TextView) findViewById(R.id.savecontinue);


        savecontinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                    Log.e(TAG, " aass ");

                    addnewAddressAPI();




            }
        });


    }

    public void addnewAddressAPI(){
        if (!NetworkUtility.isNetworkConnected(OrderAddress_AddNew.this)){
            AppUtilits.displayMessage(OrderAddress_AddNew.this,  getString(R.string.network_not_connected));

        }else {
            ServiceWrapper serviceWrapper = new ServiceWrapper(null);
            Call<AddNewAddress> call = serviceWrapper.addNewAddressCall("1234", sharedPreferenceActivity.getItem(Constant.USER_DATA),
                    sharedPreferenceActivity.getItem(Constant.USER_name),pin, "", "", String.valueOf(selectedItemText),
                    "", sharedPreferenceActivity.getItem(Constant.USER_phone));

            call.enqueue(new Callback<AddNewAddress>() {
                @Override
                public void onResponse(Call<AddNewAddress> call, Response<AddNewAddress> response) {
                    Log.e(TAG, "response is "+ response.body() + "  ---- "+ new Gson().toJson(response.body()));
                    //  Log.e(TAG, "  ss sixe 1 ");
                    if (response.body() != null && response.isSuccessful()) {
                        //    Log.e(TAG, "  ss sixe 2 ");
                        if (response.body().getStatus() == 1) {
                            //      Log.e(TAG, "  ss sixe 3 ");

                            AlertDialog.Builder builder = new AlertDialog.Builder(OrderAddress_AddNew.this);
                            LayoutInflater inflater = LayoutInflater.from(OrderAddress_AddNew.this);
                            View g=    inflater.inflate(R.layout.display_message_popup, null);
                            TextView txtview = (TextView) g.findViewById(R.id.txt_msg);
                            TextView btn_ok = (TextView) g.findViewById(R.id.btn_ok);

                            txtview.setText(response.body().getMsg());
                            builder.setView(g);
                            final AlertDialog alert = builder.create();
                            alert.setCancelable(false);
                            alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                            btn_ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    alert.dismiss();
                                    finish();
                                }
                            });

                            alert.show();

                        }else {
                            AppUtilits.displayMessage(OrderAddress_AddNew.this, response.body().getMsg() );
                        }
                    }else {
                        AppUtilits.displayMessage(OrderAddress_AddNew.this, getString(R.string.network_error));
                    }
                }

                @Override
                public void onFailure(Call<AddNewAddress> call, Throwable t) {
                    Log.e(TAG, "  fail- add new address "+ t.toString());
                    AppUtilits.displayMessage(OrderAddress_AddNew.this, getString(R.string.fail_toaddaddress));

                }
            });




        }


    }



}