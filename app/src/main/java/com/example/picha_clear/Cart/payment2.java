package com.example.picha_clear.Cart;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.picha_clear.Home;
import com.example.picha_clear.MyAccount.OrderHistory;
import com.example.picha_clear.R;
import com.example.picha_clear.Utility.AppUtilits;
import com.example.picha_clear.Utility.Constant;
import com.example.picha_clear.Utility.DataValidation;
import com.example.picha_clear.Utility.NetworkUtility;
import com.example.picha_clear.Utility.SharedPreferenceActivity;
import com.example.picha_clear.WebServices.ServiceWrapper;
import com.example.picha_clear.beanResponse.codeAPI;
import com.example.picha_clear.beanResponse.payAPI;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class payment2 extends AppCompatActivity {
    private String TAG = "payment";

    EditText payment_amount,payment_amount1;
    TextView thanks, request, continuebtn,continuebtn1 ;
    Button home, order,home2;
    RelativeLayout relative_lay2, relative_lay3,extrarelative_lay2;
    Context context;
    SharedPreferenceActivity sharedPreferenceActivity;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);


        context = this;
        sharedPreferenceActivity = new SharedPreferenceActivity(context);

        relative_lay3 = (RelativeLayout) findViewById(R.id.relative_lay3);
        relative_lay2 = (RelativeLayout) findViewById(R.id.relative_lay2);
        extrarelative_lay2 = (RelativeLayout) findViewById(R.id.extralayout);

        thanks = (TextView) findViewById(R.id.thanks);
        request = (TextView) findViewById(R.id.your_request);
        payment_amount = (EditText) findViewById(R.id.amount);
        continuebtn = (TextView) findViewById(R.id.continuebtn);
        payment_amount1 = (EditText) findViewById(R.id.amount1);
        continuebtn1 = (TextView) findViewById(R.id.continuebtn1);
        home=findViewById(R.id.homebutton);
        home2=findViewById(R.id.homebutton2);
        order=findViewById(R.id.shopimage);


        payment_amount.setText(String.valueOf(sharedPreferenceActivity.getItem(Constant.TOTAL_TOTAL)));


        continuebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                makepayment();
            }


        });
        continuebtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if ( DataValidation.isNotValidcode(payment_amount1.getText().toString())){
                    Toast.makeText(getApplicationContext(),"Invalid code length. Should be 10 characters.",Toast.LENGTH_LONG).show();

                }else {
                    getcode();

                }
            }


        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent intent = new Intent(payment2.this, OrderHistory.class);
                startActivity(intent);
                finish();

            }
        });
        home2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(payment2.this, OrderHistory.class);
                startActivity(intent);
                finish();

            }
        });
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(payment2.this, Home.class);
                startActivity(intent);
                finish();

            }
        });
    }
    public void makepayment(){

        final AlertDialog progressbar = AppUtilits.createProgressBar(this);
        if (!NetworkUtility.isNetworkConnected(payment2.this)){
            Toast.makeText(getApplicationContext(),"Network error",Toast.LENGTH_LONG).show();
            AppUtilits.destroyDialog(progressbar);

        }else {

            ServiceWrapper serviceWrapper = new ServiceWrapper(null);
            Call<payAPI> makepaymentAPICall=serviceWrapper.makepaymentcall("1234", sharedPreferenceActivity.getItem(Constant.USER_DATA),  sharedPreferenceActivity.getItem(Constant.USER_order_id),
                   String.valueOf(sharedPreferenceActivity.getItem(Constant.TOTAL_TOTAL)), String.valueOf(payment_amount.getText().toString()));
            makepaymentAPICall.enqueue(new Callback<payAPI>() {
                @Override
                public void onResponse(Call<payAPI> call, Response<payAPI> response) {

                        Log.e(TAG, "response is " + response.body() + "  ---- " + new Gson().toJson(response.body()));
                        //  Log.e(TAG, "  ss sixe 1 ");
                        if (response.body() != null && response.isSuccessful()) {
                            //    Log.e(TAG, "  ss sixe 2 ");
                            if (response.body().getStatus() == 1) {

                                AppUtilits.destroyDialog(progressbar);
                               // AppUtilits.displayMessage(payment2.this, response.body().getMsg() );
                                extrarelative_lay2.setVisibility(View.VISIBLE);

                                relative_lay2.setVisibility(View.GONE);


                            } else {
                                AppUtilits.destroyDialog(progressbar);

                                AppUtilits.displayMessage(payment2.this, response.body().getMsg());

                            }

                        } else {
                            AppUtilits.displayMessage(payment2.this, getString(R.string.network_error));

                            AppUtilits.destroyDialog(progressbar);
                        }



                }

                @Override
                public void onFailure(Call<payAPI> call, Throwable t) {

                    Log.e(TAG, "  fail- to make payment"+ t.toString());
                    Toast.makeText(getApplicationContext(),"Failed to make payment",Toast.LENGTH_LONG).show();

                    AppUtilits.destroyDialog(progressbar);

                }
            });

        }


    }

    public void getcode(){
        final AlertDialog progressbar =AppUtilits.createProgressBar(this);
        if (!NetworkUtility.isNetworkConnected(payment2.this)){
            Toast.makeText(getApplicationContext(),"Network error",Toast.LENGTH_LONG).show();
            AppUtilits.destroyDialog(progressbar);

        }else {

            ServiceWrapper serviceWrapper = new ServiceWrapper(null);
            Call<codeAPI> codeAPICall=serviceWrapper.codecall("1234", sharedPreferenceActivity.getItem(Constant.USER_order_id),
                    String.valueOf(payment_amount1.getText().toString()));
            codeAPICall.enqueue(new Callback<codeAPI>() {
                @Override
                public void onResponse(Call<codeAPI> call, Response<codeAPI> response) {


                    if (response.body() != null && response.isSuccessful()) {
                        //    Log.e(TAG, "  ss sixe 2 ");
                        if (response.body().getStatus() == 1) {


                            AppUtilits.destroyDialog(progressbar);
                            extrarelative_lay2.setVisibility(View.GONE);

                            relative_lay3.setVisibility(View.VISIBLE);

                        } else {
                            AppUtilits.destroyDialog(progressbar);

                            AppUtilits.displayMessage(payment2.this, response.body().getMsg());

                        }

                    } else {
                        AppUtilits.displayMessage(payment2.this, getString(R.string.network_error));

                        AppUtilits.destroyDialog(progressbar);
                    }



                }

                @Override
                public void onFailure(Call<codeAPI> call, Throwable throwable) {


                    Toast.makeText(getApplicationContext(),"Failed to make payment",Toast.LENGTH_LONG).show();

                    AppUtilits.destroyDialog(progressbar);
                }


            });

        }


    }


}

