package com.example.picha_clear.Cart;

import android.app.AlertDialog;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.picha_clear.ProductPreview.ProductDetails;
import com.example.picha_clear.R;
import com.example.picha_clear.Utility.AppUtilits;
import com.example.picha_clear.Utility.Constant;
import com.example.picha_clear.Utility.NetworkUtility;
import com.example.picha_clear.Utility.SharedPreferenceActivity;
import com.example.picha_clear.WebServices.ServiceWrapper;
import com.example.picha_clear.beanResponse.AddtoCart;
import com.example.picha_clear.beanResponse.EditCartItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Cart_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    private List<Cartitem_Model> cartitem_models;
    SharedPreferenceActivity sharedPreferenceActivity;
    private Context mContext;
    private String TAG ="cartAdapter";
    public Cart_Adapter (Context context, List<Cartitem_Model> cartitemModels){
        this.cartitem_models = cartitemModels;
        this.mContext = context;

    }

    private class cartItemView extends RecyclerView.ViewHolder {
        ImageView prod_img, prod_delete;
        TextView prod_name, prod_oldPrice, prod_price,confirm;
        CardView cardView;
       TextView prod_qty;

        public cartItemView(View itemView) {
            super(itemView);
            prod_img = (ImageView) itemView.findViewById(R.id.prod_img);
            prod_name = (TextView) itemView.findViewById(R.id.prod_name);
           // prod_oldPrice = (TextView) itemView.findViewById(R.id.price_old);
            prod_price = (TextView) itemView.findViewById(R.id.prod_price);
            cardView = (CardView) itemView.findViewById(R.id.card_view);

            prod_delete = (ImageView) itemView.findViewById(R.id.cart_delete);
            prod_qty = itemView.findViewById(R.id.prod_qty);
            confirm= itemView.findViewById(R.id.confirm);



        }
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_cartdetails_item, parent,false);
        Log.e(TAG, "  view created ");


        this.sharedPreferenceActivity = new SharedPreferenceActivity(mContext);
        return new cartItemView(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        final Cartitem_Model model =  cartitem_models.get(position);

        ((cartItemView) holder).prod_name.setText(model.getProd_name());
        ((cartItemView) holder).prod_price.setText("Days :"+ " " +model.getOld_price());
        ((cartItemView) holder).prod_qty.setText("Item(s) :"+ " " +model.getQty());

        Glide.with(mContext)
                .load(model.getImg_ulr())
                .into(((cartItemView) holder).prod_img);
/*

        ((cartItemView) holder).add_to_wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addtowishlist( model.getProd_id());
            }
        });
*/
        ((cartItemView) holder).prod_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ProductDetails.class);
                intent.putExtra("prod_id", model.getProd_id());
                mContext.startActivity(intent);
            }
        });

        ((cartItemView) holder).prod_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext,ProductDetails.class);
                intent.putExtra("prod_id", model.getProd_id());
                mContext.startActivity(intent);
            }
        });

        // delete product from cart
        ((cartItemView) holder).prod_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteProduct(model.getProd_id());
            }
        });

        ((cartItemView) holder).confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog;

                view = LayoutInflater.from(mContext).inflate(R.layout.cart_popup, null, false);
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

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

                qty.setText( model.getQty());
                days.setText( model.getOld_price());

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!(days.getText().toString().equals("") || days.getText().toString().equals("0")) ){

                            if(!(qty.getText().toString().equals("") || qty.getText().toString().equals("0")) ){



                                dialog.cancel();
                                updateCartQty( model.getProd_id(), qty.getText().toString());

                            }else{
                                Toast.makeText(mContext,"Please input number of items to hire" ,Toast.LENGTH_LONG).show();

                            }

                        }else{
                            Toast.makeText(mContext,"Please input number of days" ,Toast.LENGTH_LONG).show();
                        }
                    }
                });
         /*       if (!((cartItemView) holder).prod_qty.getText().toString().trim().equals("") || !((cartItemView) holder).prod_qty.getText().toString().trim().equals("0")){

                        updateCartQty( model.getProd_id(), ((cartItemView) holder).prod_qty.getText().toString().trim());

                    }*/

            }
        });


    }

    @Override
    public int getItemCount() {
        return cartitem_models.size();
    }





    public void deleteProduct(String prod_id){

        final AlertDialog progressbar = AppUtilits.createProgressBar(mContext);
        if (!NetworkUtility.isNetworkConnected(mContext)){
            AppUtilits.displayMessage(mContext,  mContext.getString(R.string.network_not_connected));

        }else {
            //  Log.e(TAG, "  user value "+ SharePreferenceUtils.getInstance().getString(Constant.USER_DATA));
            ServiceWrapper service = new ServiceWrapper(null);
            Call<AddtoCart> call = service.deletecartprod("12345", sharedPreferenceActivity.getItem(Constant.USER_DATA), prod_id );
            call.enqueue(new Callback<AddtoCart>() {
                @Override
                public void onResponse(Call<AddtoCart> call, Response<AddtoCart> response) {

                    Log.e(TAG, "reposne is " + response.body().getInformation());
                    if (response.body() != null && response.isSuccessful()) {
                        if (response.body().getStatus() == 1) {

                            AppUtilits.destroyDialog(progressbar);
                            AppUtilits.displayMessage(mContext, response.body().getMsg());

                            ((CartDetails) mContext).getUserCartDetails();
                            // update cart count
                            //    SharePreferenceUtils.getInstance().saveInt( Constant.CART_ITEM_COUNT,   SharePreferenceUtils.getInstance().getInteger(Constant.CART_ITEM_COUNT) -1);
                            //    AppUtilits.UpdateCartCount(mContext, CartDetails.mainmenu);

                        }else {
                            AppUtilits.displayMessage(mContext,  response.body().getMsg());
                        }
                    }else {
                        AppUtilits.displayMessage(mContext, mContext.getString(R.string.network_error));
                    }
                }

                @Override
                public void onFailure(Call<AddtoCart> call, Throwable t) {
                    Log.e(TAG, "  fail delete cart "+ t.toString());
                    AppUtilits.displayMessage(mContext, mContext.getString(R.string.fail_todeletecart));

                }
            });


        }

    }

    public void updateCartQty(String prod_id, String prod_qty){

        final AlertDialog progressbar =AppUtilits.createProgressBar(mContext);
        if (!NetworkUtility.isNetworkConnected(mContext)){
            AppUtilits.displayMessage(mContext,  mContext.getString(R.string.network_not_connected));

        }else {
            //  Log.e(TAG, "  user value "+ SharePreferenceUtils.getInstance().getString(Constant.USER_DATA));
            ServiceWrapper service = new ServiceWrapper(null);
            Call<EditCartItem>  call = service.editcartcartprodqty( "12345", sharedPreferenceActivity.getItem(Constant.USER_DATA), prod_id , prod_qty);
            call.enqueue(new Callback<EditCartItem>() {
                @Override
                public void onResponse(Call<EditCartItem> call, Response<EditCartItem> response) {

                    Log.e(TAG, " edit response "+ response.toString());
                    if (response.body() != null && response.isSuccessful()) {
                        if (response.body().getStatus() == 1) {

                            AppUtilits.destroyDialog(progressbar);

                            AppUtilits.displayMessage(mContext, response.body().getInformation().getStatus());

                            if (response.body().getInformation().getStatus().equalsIgnoreCase("successful update cart")){
                                ((CartDetails) mContext).getUserCartDetails();
                                ((CartDetails) mContext).cart_totalamt.setText(  "Ksh " + response.body().getInformation().getTotalprice());
                            }


                        }else {
                            AppUtilits.displayMessage(mContext,  response.body().getMsg());
                        }
                    }else {
                        AppUtilits.displayMessage(mContext, mContext.getString(R.string.network_error));
                    }
                }


                @Override
                public void onFailure(Call<EditCartItem> call, Throwable t) {
                    Log.e(TAG, " edit fail "+ t.toString());
                    AppUtilits.displayMessage(mContext,  mContext.getString(R.string.fail_toeditcart));
                }
            });





        }

    }



}
