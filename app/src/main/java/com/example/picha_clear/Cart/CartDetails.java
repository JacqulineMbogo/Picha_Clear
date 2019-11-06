package com.example.picha_clear.Cart;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.picha_clear.Home;
import com.example.picha_clear.R;
import com.example.picha_clear.Utility.AppUtilits;
import com.example.picha_clear.Utility.Constant;
import com.example.picha_clear.Utility.NetworkUtility;
import com.example.picha_clear.Utility.SharedPreferenceActivity;
import com.example.picha_clear.WebServices.ServiceWrapper;
import com.example.picha_clear.beanResponse.getCartDetails;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartDetails extends AppCompatActivity {

    Context context;
    SharedPreferenceActivity sharedPreferenceActivity;
    String TAG = "cartdetails";
    private RecyclerView recycler_cartitem;
    private TextView cart_count, continuebtn, addbtn;
    public  TextView cart_totalamt;
    private Cart_Adapter cartAdapter;
    private ArrayList<Cartitem_Model> cartitemModels = new ArrayList<>();



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cartdetails);

        context= this;
        sharedPreferenceActivity = new SharedPreferenceActivity(context);

        cart_count = (TextView) findViewById(R.id.cart_count);
        cart_totalamt = (TextView) findViewById(R.id.cart_totalamt);
        continuebtn = (TextView) findViewById(R.id.continuebtn);
        addbtn = (TextView) findViewById(R.id.addbtn);
        recycler_cartitem = (RecyclerView) findViewById(R.id.recycler_cartitem);

        LinearLayoutManager mLayoutManger3 = new LinearLayoutManager( this, RecyclerView.VERTICAL, false);
        recycler_cartitem.setLayoutManager(mLayoutManger3);
        recycler_cartitem.setItemAnimator(new DefaultItemAnimator());

        cartAdapter = new Cart_Adapter(this, cartitemModels);
        recycler_cartitem.setAdapter(cartAdapter);


        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CartDetails.this , Home. class);
                startActivity(intent);
            }
        });
        continuebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String temp = cart_totalamt.getText().toString().replace("Ksh ", "");
                if (!temp.equalsIgnoreCase("") && Integer.valueOf(temp)>0 ){

                    // then go to order summer page
                    Intent intent = new Intent(CartDetails.this , Order_Summary. class);
                    startActivity(intent);

                }else {

                    Toast.makeText(getApplicationContext(),"Please refresh by clicking on the cart icon on the top right corner ",Toast.LENGTH_LONG).show();
                }

            }
        });

        getUserCartDetails();
    }


    public void getUserCartDetails(){

        if (!NetworkUtility.isNetworkConnected(CartDetails.this)){
            Toast.makeText(getApplicationContext(),"Network error",Toast.LENGTH_LONG).show();

        }else {
            //  Log.e(TAG, "  user value "+ SharePreferenceUtils.getInstance().getString(Constant.USER_DATA));
            ServiceWrapper service = new ServiceWrapper(null);
            Call<getCartDetails> call = service.getCartDetailsCall( "1234" , sharedPreferenceActivity.getItem(Constant.QUOTE_ID),
                    sharedPreferenceActivity.getItem(Constant.USER_DATA));

            call.enqueue(new Callback<getCartDetails>() {
                @Override
                public void onResponse(Call<getCartDetails> call, Response<getCartDetails> response) {
                    Log.e(TAG, "response is "+ response.body() + "  ---- "+ new Gson().toJson(response.body()));
                    Log.e(TAG, "  ss sixe 1 ");
                    if (response.body() != null && response.isSuccessful()) {
                        Log.e(TAG, "  ss sixe 2 ");
                        if (response.body().getStatus() == 1) {
                            Log.e(TAG, "  ss sixe 3 ");


                            cart_totalamt .setText(  "Ksh " + response.body().getInformation().getTotalprice());
                            cart_count.setText(getString(R.string.you_have)+" "+ String.valueOf(response.body().getInformation().getProdDetails().size()) +" "+
                                    getString(R.string.product_in_cart));

                            Log.e(TAG, " size is  "+ String.valueOf(response.body().getInformation().getProdDetails().size()));
                            sharedPreferenceActivity.putItem( Constant.CART_ITEM_COUNT, String.valueOf(response.body().getInformation().getProdDetails().size()));

                            sharedPreferenceActivity.putItem(Constant.USER_Totalprice, response.body().getInformation().getTotalprice());


                            cartitemModels.clear();

                            for (int i=0; i<response.body().getInformation().getProdDetails().size(); i++){


                                cartitemModels.add( new Cartitem_Model(response.body().getInformation().getProdDetails().get(i).getId(),
                                        response.body().getInformation().getProdDetails().get(i).getName(),
                                        response.body().getInformation().getProdDetails().get(i).getImgUrl(), response.body().getInformation().getProdDetails().get(i).getMrp(),
                                        response.body().getInformation().getProdDetails().get(i).getPrice(), response.body().getInformation().getProdDetails().get(i).getQty()));

                            }

                            cartAdapter.notifyDataSetChanged();



                        }else {
                            AppUtilits.displayMessage(CartDetails.this, response.body().getMsg() );
                        }
                    }else {
                        Toast.makeText(getApplicationContext(),"Please try again",Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onFailure(Call<getCartDetails> call, Throwable t) {
                    Log.e(TAG, "  fail- add to cart item "+ t.toString());
                    Toast.makeText(getApplicationContext(),"please try again. Failed to get user cart details ",Toast.LENGTH_LONG).show();

                }
            });
        }
    }


}
