package com.example.picha_clear;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.picha_clear.Main.Cameras_Adapter;
import com.example.picha_clear.Main.Cameras_Model;
import com.example.picha_clear.Main.Drones_Adapter;
import com.example.picha_clear.Main.Drones_Model;
import com.example.picha_clear.Main.Lenses_Adapter;
import com.example.picha_clear.Main.Lenses_Model;
import com.example.picha_clear.Utility.AppUtilits;
import com.example.picha_clear.Utility.NetworkUtility;
import com.example.picha_clear.WebServices.ServiceWrapper;
import com.example.picha_clear.beanResponse.CamerasProductRes;
import com.example.picha_clear.beanResponse.DronesProductRes;
import com.example.picha_clear.beanResponse.LensesProductRes;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Home extends AppCompatActivity {

    private String TAG = "MainActivity";

    private RecyclerView recycler_cameras, recycler_drones, recycler_lenses;

    private ArrayList<Cameras_Model> NewCamerasModelList = new ArrayList<>();
    private ArrayList<Drones_Model> NewDronesModelList = new ArrayList<>();
    private ArrayList<Lenses_Model> NewLensesModelList = new ArrayList<>();

    private Cameras_Model newCameras_model;
    private Drones_Model newDrones_model;
    private Lenses_Model newLenses_model;

    private Cameras_Adapter newCameras_adapter;
    private Drones_Adapter newDrones_adapter;
    private Lenses_Adapter newLenses_adapter;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        recycler_cameras =(RecyclerView)findViewById(R.id.recycler_cameras);
        recycler_drones = findViewById(R.id.recycler_drones);
        recycler_lenses = findViewById(R.id.recycler_lenses);


        //Cameras
        LinearLayoutManager mLayoutManger2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recycler_cameras.setLayoutManager(mLayoutManger2);
        recycler_cameras.setItemAnimator(new DefaultItemAnimator());
        newCameras_adapter =new Cameras_Adapter(this,NewCamerasModelList, GetScreenWidth());
        recycler_cameras.setAdapter(newCameras_adapter);

        //drones
        LinearLayoutManager mLayoutManger3 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recycler_drones.setLayoutManager(mLayoutManger3);
        recycler_drones.setItemAnimator(new DefaultItemAnimator());
        newDrones_adapter =new Drones_Adapter(this,NewDronesModelList, GetScreenWidth());
        recycler_drones.setAdapter(newDrones_adapter);

        //drones
        LinearLayoutManager mLayoutManger4 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recycler_lenses.setLayoutManager(mLayoutManger4);
        recycler_lenses.setItemAnimator(new DefaultItemAnimator());
        newLenses_adapter =new Lenses_Adapter(this,NewLensesModelList, GetScreenWidth());
        recycler_lenses.setAdapter(newLenses_adapter);

        getCameraProdRes();
        getDronesProdRes();
        getLensesProdRes();


    }

    public void getCameraProdRes() {

        if (!NetworkUtility.isNetworkConnected(Home.this)) {
            AppUtilits.displayMessage(Home.this, getString(R.string.network_not_connected));

        } else {
            ServiceWrapper service = new ServiceWrapper(null);
            Call<CamerasProductRes> call = service.getCamerasProductRes("1234");
            call.enqueue(new Callback<CamerasProductRes>() {
                @Override
                public void onResponse(Call<CamerasProductRes> call, Response<CamerasProductRes> response) {
                    Log.e(TAG, " response is " + response.body().getInformation().toString());
                    if (response.body() != null && response.isSuccessful()) {
                        if (response.body().getStatus() == 1) {
                            if (response.body().getInformation().size() > 0) {

                                NewCamerasModelList.clear();
                                for (int i = 0; i < response.body().getInformation().size(); i++) {

                                    NewCamerasModelList.add(new Cameras_Model(response.body().getInformation().get(i).getId(), response.body().getInformation().get(i).getName(),
                                            response.body().getInformation().get(i).getImgUrl(),response.body().getInformation().get(i).getPrice(),response.body().getInformation().get(i).getStock()));

                                }

                                newCameras_adapter.notifyDataSetChanged();
                            }

                        } else {
                            Log.e(TAG, "failed to get camera prod " + response.body().getMsg());
                            // AppUtilits.displayMessage(HomeActivity.this,  response.body().getMsg());
                        }
                    } else {
                        // AppUtilits.displayMessage(HomeActivity.this,  getString(R.string.failed_request));

                        //  getNewProdRes();
                    }
                }

                @Override
                public void onFailure(Call<CamerasProductRes> call, Throwable t) {
                    Log.e(TAG, "fail camera prod " + t.toString());

                }
            });

        }

    }

    public void getDronesProdRes() {

        if (!NetworkUtility.isNetworkConnected(Home.this)) {
            AppUtilits.displayMessage(Home.this, getString(R.string.network_not_connected));

        } else {
            ServiceWrapper service = new ServiceWrapper(null);
            Call<DronesProductRes> call = service.getDronesProductRes("1234");
            call.enqueue(new Callback<DronesProductRes>() {
                @Override
                public void onResponse(Call<DronesProductRes> call, Response<DronesProductRes> response) {
                    Log.e(TAG, " response is " + response.body().getInformation().toString());
                    if (response.body() != null && response.isSuccessful()) {
                        if (response.body().getStatus() == 1) {
                            if (response.body().getInformation().size() > 0) {

                                NewDronesModelList.clear();
                                for (int i = 0; i < response.body().getInformation().size(); i++) {

                                    NewDronesModelList.add(new Drones_Model(response.body().getInformation().get(i).getId(), response.body().getInformation().get(i).getName(),
                                            response.body().getInformation().get(i).getImgUrl(),response.body().getInformation().get(i).getPrice(),response.body().getInformation().get(i).getStock()));

                                }

                                newDrones_adapter.notifyDataSetChanged();
                            }

                        } else {
                            Log.e(TAG, "failed to get drones prod " + response.body().getMsg());
                            // AppUtilits.displayMessage(HomeActivity.this,  response.body().getMsg());
                        }
                    } else {
                        // AppUtilits.displayMessage(HomeActivity.this,  getString(R.string.failed_request));

                        //  getNewProdRes();
                    }
                }

                @Override
                public void onFailure(Call<DronesProductRes> call, Throwable t) {
                    Log.e(TAG, "fail drones prod " + t.toString());

                }
            });

        }

    }

    public void getLensesProdRes() {

        if (!NetworkUtility.isNetworkConnected(Home.this)) {
            AppUtilits.displayMessage(Home.this, getString(R.string.network_not_connected));

        } else {
            ServiceWrapper service = new ServiceWrapper(null);
            Call<LensesProductRes> call = service.getLensesProductRes("1234");
            call.enqueue(new Callback<LensesProductRes>() {
                @Override
                public void onResponse(Call<LensesProductRes> call, Response<LensesProductRes> response) {
                    Log.e(TAG, " response is " + response.body().getInformation().toString());
                    if (response.body() != null && response.isSuccessful()) {
                        if (response.body().getStatus() == 1) {
                            if (response.body().getInformation().size() > 0) {

                                NewLensesModelList.clear();
                                for (int i = 0; i < response.body().getInformation().size(); i++) {

                                    NewLensesModelList.add(new Lenses_Model(response.body().getInformation().get(i).getId(), response.body().getInformation().get(i).getName(),
                                            response.body().getInformation().get(i).getImgUrl(),response.body().getInformation().get(i).getPrice(),response.body().getInformation().get(i).getStock()));

                                }

                                newLenses_adapter.notifyDataSetChanged();
                            }

                        } else {
                            Log.e(TAG, "failed to get lenses prod " + response.body().getMsg());
                            // AppUtilits.displayMessage(HomeActivity.this,  response.body().getMsg());
                        }
                    } else {
                        // AppUtilits.displayMessage(HomeActivity.this,  getString(R.string.failed_request));

                        //  getNewProdRes();
                    }
                }

                @Override
                public void onFailure(Call<LensesProductRes> call, Throwable t) {
                    Log.e(TAG, "fail lenses prod " + t.toString());

                }
            });

        }

    }

    public int GetScreenWidth(){
        int  width =100;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager =  (WindowManager) getApplicationContext().getSystemService(WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        width = displayMetrics.widthPixels;

        return width;

    }

}
