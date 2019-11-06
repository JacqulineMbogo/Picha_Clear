package com.example.picha_clear.LogIn;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.picha_clear.R;
import com.example.picha_clear.Utility.AppUtilits;
import com.example.picha_clear.Utility.Constant;
import com.example.picha_clear.Utility.DataValidation;
import com.example.picha_clear.Utility.NetworkUtility;
import com.example.picha_clear.Utility.SharedPreferenceActivity;
import com.example.picha_clear.WebServices.ServiceWrapper;
import com.example.picha_clear.beanResponse.NewUserRegistration;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUp extends AppCompatActivity {

    Context context;
    SharedPreferenceActivity sharedPreferenceActivity;

    EditText username, phone_no, email, password, retype_password, fname, lname, id_number;
    TextView create_acc, signin;


    private String TAG = "SignUpActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        context = this;
        sharedPreferenceActivity = new SharedPreferenceActivity(context);
        this.setTitle("Sign Up");

       username =  findViewById(R.id.username);
        fname =  findViewById(R.id.f_name);
        lname = findViewById(R.id.l_name);
        phone_no =  findViewById(R.id.phone_number);
        email =  findViewById(R.id.email);
        password =  findViewById(R.id.password);
        retype_password =  findViewById(R.id.retype_password);
        signin =  findViewById(R.id.sign_in);
        create_acc = findViewById(R.id.create_account);
        id_number = findViewById(R.id.id_number);


        create_acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (DataValidation.isNotValidID(id_number.getText().toString())) {
                    /// show error pupup
                 //   Toast.makeText(getApplicationContext(), "Invalid id number", Toast.LENGTH_LONG).show();
                    ((EditText)findViewById(R.id.id_number)).setError("Invalid id number");
                    (findViewById(R.id.id_number)).requestFocus();
                } else  if (DataValidation.isNotValidLName(fname.getText().toString())) {
                    /// show error pupup

                    ((EditText)findViewById(R.id.f_name)).setError("Invalid first name");
                    (findViewById(R.id.f_name)).requestFocus();
                 //   Toast.makeText(getApplicationContext(), "Invalid first name", Toast.LENGTH_LONG).show();
                } else if (DataValidation.isNotValidFullName(lname.getText().toString())) {
                    /// show error pupup
                    ((EditText)findViewById(R.id.l_name)).setError("Invalid last name");
                    (findViewById(R.id.l_name)).requestFocus();
                  //  Toast.makeText(getApplicationContext(), "Invalid last name", Toast.LENGTH_LONG).show();
                } else  if (DataValidation.isNotValidFName(username.getText().toString())) {
                    /// show error pupup

                    ((EditText)findViewById(R.id.username)).setError("Invalid username");
                    (findViewById(R.id.username)).requestFocus();
                 //   Toast.makeText(getApplicationContext(), "Invalid username", Toast.LENGTH_LONG).show();
                } else if (DataValidation.isValidPhoneNumber(phone_no.getText().toString())) {
                    //Toast.makeText(getApplicationContext(), "Invalid phone number.", Toast.LENGTH_LONG).show();

                    ((EditText)findViewById(R.id.phone_number)).setError("Invalid phone number");
                    (findViewById(R.id.phone_number)).requestFocus();
                } else if (DataValidation.isNotValidemail(email.getText().toString())) {
                   // Toast.makeText(getApplicationContext(), "Invalid email", Toast.LENGTH_LONG).show();

                    ((EditText)findViewById(R.id.email)).setError("Invalid email");
                    (findViewById(R.id.email)).requestFocus();
                } else if (DataValidation.isNotValidPassword(password.getText().toString())) {
                   // Toast.makeText(getApplicationContext(), "Password should be at least 6 characters ", Toast.LENGTH_LONG).show();

                    ((EditText)findViewById(R.id.password)).setError("Invalid password. Password should be at least 6 characters ");
                    (findViewById(R.id.username)).requestFocus();
                } else if (!password.getText().toString().equals(retype_password.getText().toString())) {
                  //  Toast.makeText(getApplicationContext(), "passwords do not match", Toast.LENGTH_LONG).show();
                    ((EditText)findViewById(R.id.retype_password)).setError("Password do not match ");
                    (findViewById(R.id.retype_password)).requestFocus();

                } else {
                    // network connection and progroess dialog
                    /// here call retrofit method

                    sendNewRegistrationReq();
                }

            }
        });
    }


    public void sendNewRegistrationReq() {

        final AlertDialog progressbar = AppUtilits.createProgressBar(this);

        if (!NetworkUtility.isNetworkConnected(SignUp.this)) {
            Toast.makeText(getApplicationContext(), "Network error", Toast.LENGTH_LONG).show();
            AppUtilits.destroyDialog(progressbar);


        } else {

            ServiceWrapper serviceWrapper = new ServiceWrapper(null);
            Call<NewUserRegistration> callNewREgistration = serviceWrapper.newUserRegistrationCall( id_number.getText().toString(), fname.getText().toString(), lname.getText().toString(),
                    email.getText().toString(), phone_no.getText().toString(),
                    username.getText().toString(), password.getText().toString());
            callNewREgistration.enqueue(new Callback<NewUserRegistration>() {
                @Override
                public void onResponse(Call<NewUserRegistration> call, Response<NewUserRegistration> response) {
                    Log.d(TAG, "reponse : " + response.toString());
                    if (response.body() != null && response.isSuccessful()) {
                        if (response.body().getStatus() == 1) {
                            AppUtilits.destroyDialog(progressbar);
                            // store userdata to share prerference
                            sharedPreferenceActivity.putItem(Constant.USER_DATA, response.body().getInformation().getUserId());
                            sharedPreferenceActivity.putItem(Constant.ID_NUMBER, response.body().getInformation().getIdNumber());
                            sharedPreferenceActivity.putItem(Constant.FIRST_NAME, response.body().getInformation().getFirstName());
                            sharedPreferenceActivity.putItem(Constant.LAST_NAME, response.body().getInformation().getLastName());
                            sharedPreferenceActivity.putItem(Constant.USER_name, response.body().getInformation().getUsername());
                            sharedPreferenceActivity.putItem(Constant.USER_email, response.body().getInformation().getEmail());
                            sharedPreferenceActivity.putItem(Constant.USER_phone, response.body().getInformation().getPhoneNumber());

                            // start home activity

                            AppUtilits.createToaster(SignUp.this, "Welcome, " + sharedPreferenceActivity.getItem(Constant.FIRST_NAME) +  " " + sharedPreferenceActivity.getItem(Constant.LAST_NAME) + "\n Please continue to sign in upon admin approval", Toast.LENGTH_LONG);
                            Intent intent = new Intent(SignUp.this, LogIn.class);

                            startActivity(intent);
                            finish();

                        } else {
                            AppUtilits.destroyDialog(progressbar);
                            AppUtilits.displayMessage(SignUp.this, response.body().getMsg());
                        }
                    } else {
                        AppUtilits.destroyDialog(progressbar);
                        Toast.makeText(getApplicationContext(), "Request failed", Toast.LENGTH_LONG).show();

                    }
                }

                @Override
                public void onFailure(Call<NewUserRegistration> call, Throwable t) {
                    AppUtilits.destroyDialog(progressbar);
                    Log.e(TAG, " failure " + t.toString());
                    Toast.makeText(getApplicationContext(), "Request failed", Toast.LENGTH_LONG).show();


                }
            });
        }
    }
}
