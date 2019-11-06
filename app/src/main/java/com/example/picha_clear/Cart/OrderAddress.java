package com.example.picha_clear.Cart;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.example.picha_clear.beanResponse.GetAddress;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class OrderAddress extends AppCompatActivity {

    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private String TAG = "orderaddress";
    private TextView continuebtn;

    private OrderAddress_Adapter adapter;
    private ArrayList<OrderAddress_Model> modellist = new ArrayList<>();
    private String totalamount="0";
    public String addressid ="0";
    public String pin="0";
    SharedPreferenceActivity sharedPreferenceActivity;
    Context context;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderaddress);


        getSupportActionBar().setHomeButtonEnabled(true);

        final Intent intent = getIntent();
        // totalamount =  intent.getExtras().getString("amount");


        fab =  findViewById(R.id.fab);
        recyclerView = findViewById(R.id.order_recyclerview);
        continuebtn = findViewById(R.id.continuebtn);
        context= this;
        sharedPreferenceActivity = new SharedPreferenceActivity(context);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(OrderAddress.this, OrderAddress_AddNew.class);
                startActivity(intent);

            }
        });

     /*   Log.e(TAG, " payAPI detaisl -"+SharedPreferenceActivity.getInstance().getString(Constant.USER_email)+"--"+
                SharedPreferenceActivity.getInstance().getString(Constant.USER_phone)+"--"+
                totalamount+ " buy from app "+"--" +SharedPreferenceActivity.getInstance().getString(Constant.USER_name) );
*/
        // totalamount ="12";
        continuebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!addressid.equalsIgnoreCase("0")){

                    Intent intent1 = new Intent(OrderAddress.this, PlaceOrderActivity.class);
                    intent1.putExtra("addressid", addressid);
                    intent1.putExtra("pin", pin);
                    startActivity(intent1);

                    Log.d("address", addressid);

                    Log.d("pin", pin);



                }else {
                    AppUtilits.displayMessage(OrderAddress.this, getResources().getString(R.string.select_address) );
                }

            }
        });

        LinearLayoutManager mLayoutManger3 = new LinearLayoutManager( this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManger3);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter = new OrderAddress_Adapter(OrderAddress.this, modellist);
        recyclerView.setAdapter(adapter);





    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserAddress();

    }


    public void getUserAddress(){

        if (!NetworkUtility.isNetworkConnected(OrderAddress.this)){
            AppUtilits.displayMessage(OrderAddress.this,  getString(R.string.network_not_connected));

        }else {

            ServiceWrapper serviceWrapper = new ServiceWrapper(null);
            Call<GetAddress> call = serviceWrapper.getUserAddresscall("1234", sharedPreferenceActivity.getItem(Constant.USER_DATA));
            call.enqueue(new Callback<GetAddress>() {
                @Override
                public void onResponse(Call<GetAddress> call, Response<GetAddress> response) {
                    Log.e(TAG, "response is "+ response.body() + "  ---- "+ new Gson().toJson(response.body()));
                    //  Log.e(TAG, "  ss sixe 1 ");
                    if (response.body() != null && response.isSuccessful()) {
                        //    Log.e(TAG, "  ss sixe 2 ");
                        if (response.body().getStatus() == 1) {

                            if (response.body().getInformation().getAddressDetails().size()>0){

                                modellist.clear();
                                for (int i=0; i<response.body().getInformation().getAddressDetails().size() ; i++){

                                    modellist.add(new OrderAddress_Model(response.body().getInformation().getAddressDetails().get(i).getAddressId(),
                                            response.body().getInformation().getAddressDetails().get(i).getFullname(),
                                            response.body().getInformation().getAddressDetails().get(i).getAddress1() +"\n"+
                                                    response.body().getInformation().getAddressDetails().get(i).getAddress2()+"\n"+
                                                    response.body().getInformation().getAddressDetails().get(i).getCity()+" "+
                                                    response.body().getInformation().getAddressDetails().get(i).getState()+"\n"+
                                                    response.body().getInformation().getAddressDetails().get(i).getPincode(),
                                            response.body().getInformation().getAddressDetails().get(i).getPhone()));
                                }

                                adapter.notifyDataSetChanged();

                            }



                        }else {
                            AppUtilits.displayMessage(OrderAddress.this, response.body().getMsg() );
                        }
                    }else {
                        AppUtilits.displayMessage(OrderAddress.this, getString(R.string.network_error));
                    }

                }

                @Override
                public void onFailure(Call<GetAddress> call, Throwable t) {
                    Log.e(TAG, "  fail- get user address "+ t.toString());
                    AppUtilits.displayMessage(OrderAddress.this, getString(R.string.fail_togetaddress));


                }
            });


        }
    }



}
