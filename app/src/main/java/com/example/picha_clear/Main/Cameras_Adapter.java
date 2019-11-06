package com.example.picha_clear.Main;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.picha_clear.ProductPreview.ProductDetails;
import com.example.picha_clear.R;
import java.util.List;



public class Cameras_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<Cameras_Model> mCamerasModelList;
    private String TAG ="Cameras_adapter";
    private int mScrenwith;

    public Cameras_Adapter(Context context, List<Cameras_Model> list, int screenwidth ){
        mContext = context;
        mCamerasModelList = list;
        mScrenwith =screenwidth;

    }

    private class NewProductHolder extends RecyclerView.ViewHolder {
        ImageView prod_img;
        TextView prod_name, prod_price, prod_stock;
        CardView cardView;

        public NewProductHolder(View itemView) {
            super(itemView);
            prod_img = (ImageView) itemView.findViewById(R.id.prod_img);
            prod_name = (TextView) itemView.findViewById(R.id.prod_name);
            prod_price = (TextView) itemView.findViewById(R.id.prod_price);
            prod_stock = (TextView) itemView.findViewById(R.id.prod_stock);

            cardView = (CardView) itemView.findViewById(R.id.card_view);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams( mScrenwith - (mScrenwith/100*70), LinearLayout.LayoutParams.MATCH_PARENT);
            params.setMargins(10,10,10,10);
            cardView.setLayoutParams(params);
            cardView.setPadding(5,5,5,5);

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_cameras, parent,false);
        Log.e(TAG, "  view created ");
        return new NewProductHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Cameras_Model model = mCamerasModelList.get(position);
        Log.e(TAG, " assign value ");
        ((NewProductHolder) holder).prod_name.setText(model.getProd_name());
        ((NewProductHolder) holder).prod_price.setText("Ksh: "+model.getPrice());
        ((NewProductHolder) holder).prod_stock.setText("Stock" + " " + model.getStock());



        Glide.with(mContext)
                .load(model.getImg_ulr())
                .into(((NewProductHolder) holder).prod_img);
        // imageview glider lib to get imagge from url


        ((NewProductHolder) holder).cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mContext, ProductDetails.class);
                intent.putExtra("prod_id", model.getProd_id());
                intent.putExtra("qty", model.getStock());

                mContext.startActivity(intent);

                //  Log.e(TAG, "  prod_id "+String.valueOf(prod_id));

                //  Log.e(TAG, "  prod_id "+String.valueOf(prod_id));



            }
        });

    }

    @Override
    public int getItemCount() {
        return mCamerasModelList.size();
    }
}
