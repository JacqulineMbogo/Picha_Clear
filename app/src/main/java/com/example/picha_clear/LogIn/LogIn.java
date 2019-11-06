package com.example.picha_clear.LogIn;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.example.picha_clear.Home;
import com.example.picha_clear.R;
import com.example.picha_clear.Utility.AppUtilits;
import com.example.picha_clear.Utility.Constant;
import com.example.picha_clear.Utility.DataValidation;
import com.example.picha_clear.Utility.NetworkUtility;
import com.example.picha_clear.Utility.SharedPreferenceActivity;
import com.example.picha_clear.WebServices.ServiceWrapper;
import com.example.picha_clear.beanResponse.UserSignInRes;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogIn extends AppCompatActivity {

    Context context;

    TextView  signup_btn;
    LinearLayout login_btn;
    EditText username, password;

    SharedPreferenceActivity sharedPreferenceActivity;

    private String TAG = "LoginActivity";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);


        context = this;

        this.setTitle("LogIn");

        sharedPreferenceActivity= new SharedPreferenceActivity(context);
        login_btn = findViewById(R.id.login_btn);
        signup_btn = findViewById(R.id.signup_btn);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);


        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LogIn.this,SignUp.class);
                startActivity(intent);
            }
        });

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( DataValidation.isNotValidFullName(username.getText().toString())){
                    //Toast.makeText(getApplicationContext(),"Invalid id number",Toast.LENGTH_LONG).show();
                     ((EditText)findViewById(R.id.username)).setError("Invalid username");
                     (findViewById(R.id.username)).requestFocus();

                }else if (DataValidation.isNotValidPassword(password.getText().toString())){
               //     Toast.makeText(getApplicationContext(),"Invalid password",Toast.LENGTH_LONG).show();

                    ((EditText)findViewById(R.id.password)).setError("Invalid password");
                    (findViewById(R.id.password)).requestFocus();


                }else {

                    LogIn_method();

                }
            }
        });


    }

    public  void  LogIn_method(){

        final AlertDialog progressbar = AppUtilits.createProgressBar(this);


        if (!NetworkUtility.isNetworkConnected(LogIn.this)){
            Toast.makeText(getApplicationContext(),"Network error",Toast.LENGTH_LONG).show();
            AppUtilits.destroyDialog(progressbar);

        }else {

            ServiceWrapper serviceWrapper = new ServiceWrapper(null);
            Call<UserSignInRes> userSigninCall = serviceWrapper.UserSigninCall(username.getText().toString(), password.getText().toString());
            userSigninCall.enqueue(new Callback<UserSignInRes>() {
                @Override
                public void onResponse(Call<UserSignInRes> call, Response<UserSignInRes> response) {

                    Log.d(TAG, "reponse : "+ response.toString());
                    if (response.body()!= null && response.isSuccessful()){
                        if (response.body().getStatus() ==1){
                            Log.e(TAG, "  user data "+  response.body().getInformation());
                            sharedPreferenceActivity.putItem(Constant.USER_DATA, response.body().getInformation().getUserId());
                            sharedPreferenceActivity.putItem(Constant.ID_NUMBER, response.body().getInformation().getIdNumber());
                            sharedPreferenceActivity.putItem(Constant.FIRST_NAME, response.body().getInformation().getFirstName());
                            sharedPreferenceActivity.putItem(Constant.LAST_NAME, response.body().getInformation().getLastName());
                            sharedPreferenceActivity.putItem(Constant.USER_name, response.body().getInformation().getUsername());
                            sharedPreferenceActivity.putItem(Constant.USER_email, response.body().getInformation().getEmail());
                            sharedPreferenceActivity.putItem(Constant.USER_phone, response.body().getInformation().getPhoneNumber());


                            AppUtilits.destroyDialog(progressbar);
                            // start home activity
                            Intent intent = new Intent(LogIn.this, Home.class);
                            startActivity(intent);
                            finish();





                        }else  if (response.body().getStatus() ==0){
                            AppUtilits.displayMessage(LogIn.this,  response.body().getMsg());
                            AppUtilits.destroyDialog(progressbar);
                        }
                    }else {
                        AppUtilits.displayMessage(LogIn.this,  response.body().getMsg());
                        Toast.makeText(getApplicationContext(),"Request failed",Toast.LENGTH_LONG).show();
                        AppUtilits.destroyDialog(progressbar);

                    }
                }

                @Override
                public void onFailure(Call<UserSignInRes> call, Throwable t) {
                    Log.e(TAG, " failure "+ t.toString());
                    Toast.makeText(getApplicationContext(),"Request failed",Toast.LENGTH_LONG).show();
                    AppUtilits.destroyDialog(progressbar);

                }
            });




        }





    }
}
