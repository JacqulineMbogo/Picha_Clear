package com.example.picha_clear.ProductPreview;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.picha_clear.Cart.CartDetails;
import com.example.picha_clear.R;
import com.example.picha_clear.Utility.AppUtilits;
import com.example.picha_clear.Utility.Constant;
import com.example.picha_clear.Utility.NetworkUtility;
import com.example.picha_clear.Utility.SharedPreferenceActivity;
import com.example.picha_clear.WebServices.ServiceWrapper;
import com.example.picha_clear.beanResponse.AddtoCart;
import com.example.picha_clear.beanResponse.ProductDetail_Res;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetails extends AppCompatActivity {


    Context context;
    private String TAG ="productDetails";
    private String prod_id="";
    private  String stock = "";
    private TextView prod_name, prod_price, prod_stock, prod_qty, details;
    private ImageView imageurl;
    private Button viewcart, add_to_cart;
    SharedPreferenceActivity sharedPreferenceActivity;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.productdetails);

        context = this;

        final Intent intent = getIntent();
        sharedPreferenceActivity= new SharedPreferenceActivity(context);
        prod_id = intent.getExtras().getString("prod_id");
        stock = intent.getExtras().getString("qty");

        details = findViewById(R.id.detail);
        prod_name = findViewById(R.id.prod_name);
        prod_price = findViewById(R.id.prod_price);
        prod_stock = findViewById(R.id.stock_avail);
        add_to_cart = findViewById(R.id.add_to_cart);
        viewcart = findViewById(R.id.view_cart);
        imageurl = findViewById(R.id.imgurl);

        getProductDetails();



        viewcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(ProductDetails.this, CartDetails.class);
                startActivity(intent1);
            }
        });


        add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Integer.valueOf(stock) <= 0) {

                    AppUtilits.displayMessage(ProductDetails.this, "Product is out of stock. Current stock is" +" " + stock);

                } else {

                final Dialog dialog;

                view = LayoutInflater.from(context).inflate(R.layout.cart_popup, null, false);
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

                alertDialog.setView(view);

                alertDialog.setCancelable(true);


                dialog = alertDialog.create();

                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

                dialog.show();


                final Window dialogWindow = dialog.getWindow();
                dialogWindow.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                dialogWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);


                final EditText qty = view.findViewById(R.id.qty);
                final EditText days = view.findViewById(R.id.days);
                final Button ok = view.findViewById(R.id.ok);

                qty.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {



                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                        Log.d("editable", String.valueOf(editable));
                        Log.d("editablee", stock);

                        if(qty.getText().toString().equals("")){

                            qty.setText("0");

                        }

                        if (Integer.valueOf(stock) < Integer.valueOf(qty.getText().toString())) {

                            Toast.makeText(context,"Quantity exceeds stock. Current stock is" +" " +  stock,Toast.LENGTH_LONG).show();
                            qty.setText("0");
                        }
                    }
                });

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!(days.getText().toString().equals("") || days.getText().toString().equals("0")) ){

                            if(!(qty.getText().toString().equals("") || qty.getText().toString().equals("0")) ){



                                dialog.cancel();
                                addtocartapi(days.getText().toString(), qty.getText().toString() );

                            }else{
                                Toast.makeText(context,"Please input number of items to hire" ,Toast.LENGTH_LONG).show();

                            }

                        }else{
                            Toast.makeText(context,"Please input number of days" ,Toast.LENGTH_LONG).show();
                        }






                    }
                });

            }

            }
        });



    }

    public void getProductDetails(){

        final AlertDialog progressbar =AppUtilits.createProgressBar(this);


        if (!NetworkUtility.isNetworkConnected(ProductDetails.this)){
            AppUtilits.displayMessage(ProductDetails.this,  getString(R.string.network_not_connected));

        }else {
            ServiceWrapper service = new ServiceWrapper(null);
            Call<ProductDetail_Res> call = service.getProductDetails("1234", prod_id );
            call.enqueue(new Callback<ProductDetail_Res>() {
                @Override
                public void onResponse(Call<ProductDetail_Res> call, Response<ProductDetail_Res> response) {
                    Log.e(TAG, "reposne is " + response.body().getInformation());
                    if (response.body() != null && response.isSuccessful()) {
                        if (response.body().getStatus() == 1) {

                            if (response.body().getInformation().getName()!=null){
                                prod_name.setText(response.body().getInformation().getName());

                                if (response.body().getInformation().getStock() >0){
                                    prod_stock.setText( getString(R.string.instock));
                                }else {
                                    prod_stock.setText( getString(R.string.outofstock));
                                }



                                AppUtilits.destroyDialog(progressbar);


                                prod_price.setText("Ksh "+""+response.body().getInformation().getPrice());
                                details.setText(response.body().getInformation().getDescription());
//                                prod_qty.setText("1");
                                // prod image

                                Glide.with(ProductDetails.this)
                                        .load(response.body().getInformation().getImgUrl())
                                        .into(imageurl);

                                // Log.e(TAG, "rating count "+
                                Log.e(TAG, "prod_price "+ prod_price);



                            }


                        } else {
                            Log.e(TAG, "failed to get prod details " + response.body().getMsg());
                            AppUtilits.destroyDialog(progressbar);
                             AppUtilits.displayMessage(ProductDetails.this,  response.body().getMsg());
                        }
                    } else {
                        Log.e(TAG, "failed to get prod details " + response.body().getMsg());
                        AppUtilits.destroyDialog(progressbar);
                         AppUtilits.displayMessage(ProductDetails.this,  response.body().getMsg());
                    }
                }

                @Override
                public void onFailure(Call<ProductDetail_Res> call, Throwable t) {
                    AppUtilits.destroyDialog(progressbar);
                    AppUtilits.displayMessage(ProductDetails.this,  "failed to get product details");
                    Log.e(TAG, " fail product details "+ t.toString());
                }
            });

        }


    }


    public void addtocartapi(String days, String qty){

        final AlertDialog progressbar =AppUtilits.createProgressBar(this);

        if (!NetworkUtility.isNetworkConnected(ProductDetails.this)){
            AppUtilits.displayMessage(ProductDetails.this,  getString(R.string.network_not_connected));

        }else {

            //  Log.e(TAG, "  user value "+ SharePreferenceUtils.getInstance().getString(Constant.USER_DATA));
            ServiceWrapper service = new ServiceWrapper(null);
            Call<AddtoCart> call = service.addtoCartCall("12345", prod_id, sharedPreferenceActivity.getItem(Constant.USER_DATA) ,qty,days );
            call.enqueue(new Callback<AddtoCart>() {
                @Override
                public void onResponse(Call<AddtoCart> call, Response<AddtoCart> response) {
                    Log.e(TAG, "prod_id "+ prod_id);
                    Log.e(TAG, "prod_price "+ prod_price);
                    if (response.body() != null && response.isSuccessful()) {
                        if (response.body().getStatus() == 1) {

                            AppUtilits.destroyDialog(progressbar);
                          AppUtilits.displayMessage(ProductDetails.this, getString(R.string.add_to_cart));
                            sharedPreferenceActivity.putItem(Constant.QUOTE_ID, response.body().getInformation().getQouteId());

                            sharedPreferenceActivity.putItem( Constant.CART_ITEM_COUNT,  String.valueOf( response.body().getInformation().getCartCount()));
                          //  AppUtilits.UpdateCartCount(ProductDetails.this,mainmenu);
                            Intent intent = new Intent(ProductDetails.this , CartDetails. class);
                            startActivity(intent);

                        }else {
                            AppUtilits.destroyDialog(progressbar);
                           AppUtilits.displayMessage(ProductDetails.this, getString(R.string.fail_add_to_cart));
                        }
                    }else {
                        AppUtilits.destroyDialog(progressbar);
                       AppUtilits.displayMessage(ProductDetails.this, getString(R.string.network_error));
                    }


                }

                @Override
                public void onFailure(Call<AddtoCart> call, Throwable t) {
                    // Log.e(TAG, "  fail- add to cart item "+ t.toString());
                    AppUtilits.destroyDialog(progressbar);
                    AppUtilits.displayMessage(ProductDetails.this, getString(R.string.fail_add_to_cart));
                }
            });
        }
    }
}
