package com.example.picha_clear.WebServices;


import com.example.picha_clear.BuildConfig;
import com.example.picha_clear.Utility.Constant;
import com.example.picha_clear.beanResponse.AddNewAddress;
import com.example.picha_clear.beanResponse.AddtoCart;
import com.example.picha_clear.beanResponse.BookingsPaymentRes;
import com.example.picha_clear.beanResponse.BookingsRes;
import com.example.picha_clear.beanResponse.CamerasProductRes;
import com.example.picha_clear.beanResponse.DronesProductRes;
import com.example.picha_clear.beanResponse.EditCartItem;
import com.example.picha_clear.beanResponse.GetAddress;
import com.example.picha_clear.beanResponse.GetOrderProductDetails;
import com.example.picha_clear.beanResponse.LocationRes;
import com.example.picha_clear.beanResponse.NewBookingsRes;
import com.example.picha_clear.beanResponse.NewUserRegistration;
import com.example.picha_clear.beanResponse.LensesProductRes;
import com.example.picha_clear.beanResponse.OrderHistoryAPI;
import com.example.picha_clear.beanResponse.OrderSummary;
import com.example.picha_clear.beanResponse.PlaceOrder;
import com.example.picha_clear.beanResponse.ProductDetail_Res;
import com.example.picha_clear.beanResponse.TypesRes;
import com.example.picha_clear.beanResponse.UserSignInRes;
import com.example.picha_clear.beanResponse.clearbalanceAPI;
import com.example.picha_clear.beanResponse.codeAPI;
import com.example.picha_clear.beanResponse.getCartDetails;
import com.example.picha_clear.beanResponse.payAPI;
import com.example.picha_clear.beanResponse.receiveAPI;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



public class ServiceWrapper  {

    private ServiceInterface mServiceInterface;

    public ServiceWrapper(Interceptor mInterceptorheader) {
        mServiceInterface = getRetrofit(mInterceptorheader).create(ServiceInterface.class);
    }

    public Retrofit getRetrofit(Interceptor mInterceptorheader) {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient mOkHttpClient = null;
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(Constant.API_CONNECTION_TIMEOUT, TimeUnit.SECONDS);
        builder.readTimeout(Constant.API_READ_TIMEOUT, TimeUnit.SECONDS);

//        if (BuildConfig.DEBUG)
//            builder.addInterceptor(loggingInterceptor);

        if (BuildConfig.DEBUG) {
//            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(loggingInterceptor);
        }


        mOkHttpClient = builder.build();
        Gson gson = new GsonBuilder().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(mOkHttpClient)
                .build();
        return retrofit;
    }

    public Call<NewUserRegistration> newUserRegistrationCall(String id_number,String first_name, String last_name, String email, String phone_number, String username, String password){
        return mServiceInterface.NewUserRegistrationCall( convertPlainString(id_number),convertPlainString(first_name),convertPlainString(last_name),convertPlainString(email), convertPlainString(phone_number), convertPlainString( username),
                convertPlainString(password));
    }
    ///  user signin
    public Call<UserSignInRes> UserSigninCall(String username, String password){
        return mServiceInterface.UserSigninCall(convertPlainString(username), convertPlainString(password));
    }


    // convert aa param into plain text
    public RequestBody convertPlainString(String data){
        RequestBody plainString = RequestBody.create(MediaType.parse("text/plain"), data);
        return  plainString;
    }

    ///  cameras details
    public Call<CamerasProductRes> getCamerasProductRes(String securcode){
        return mServiceInterface.getCameraProduct(convertPlainString(securcode));
    }
    ///  drones details
    public Call<DronesProductRes> getDronesProductRes(String securcode){
        return mServiceInterface.getDroneProduct(convertPlainString(securcode));
    }
    ///  lenses details
    public Call<LensesProductRes> getLensesProductRes(String securcode){
        return mServiceInterface.getLensesProduct(convertPlainString(securcode));
    }

    // get product detials
    public Call<ProductDetail_Res> getProductDetails(String securcode, String prod_id){
        return mServiceInterface.getProductDetials(convertPlainString(securcode), convertPlainString(prod_id));
    }

    // add to cart
    public Call<AddtoCart> addtoCartCall(String securcode, String prod_id, String user_id, String qty, String days){
        return mServiceInterface.addtocartcall(convertPlainString(securcode), convertPlainString(prod_id),convertPlainString(user_id),convertPlainString(qty),convertPlainString(days) );
    }

    // get user cart
    public Call<getCartDetails> getCartDetailsCall(String securcode, String qoute_id, String user_id){
        return mServiceInterface.getusercartcall(convertPlainString(securcode), convertPlainString(qoute_id),convertPlainString(user_id) );
    }
    // delete cart item
    public Call<AddtoCart> deletecartprod(String securcode, String user_id, String prod_id){
        return mServiceInterface.deleteCartProd(convertPlainString(securcode), convertPlainString(user_id), convertPlainString(prod_id) );
    }
    // edit cart item
    public Call<EditCartItem> editcartcartprodqty(String securcode, String user_id, String prod_id, String prod_qty){
        return mServiceInterface.editCartQty(convertPlainString(securcode), convertPlainString(user_id), convertPlainString(prod_id),  convertPlainString(prod_qty) );
    }
    // get order prodcut detais history
    public Call<payAPI> makepaymentcall(String securcode, String user_id, String order_id , String total_price, String payment_amount){
        return mServiceInterface. makepaymentcall(convertPlainString(securcode), convertPlainString(user_id), convertPlainString(order_id) , convertPlainString(total_price),  convertPlainString(payment_amount) );
    }
    ///receive code
    public Call<codeAPI> codecall(String securcode, String order_id , String code){
        return mServiceInterface. codecall(convertPlainString(securcode), convertPlainString(order_id) , convertPlainString(code) );
    }


    // get order history
    public Call<OrderHistoryAPI> getorderhistorycall(String securcode, String user_id){
        return mServiceInterface.getorderHistorycall(convertPlainString(securcode), convertPlainString(user_id) );
    }
    // get order prodcut detais history
    public Call<GetOrderProductDetails> getorderproductcall(String securcode, String user_id, String order_id){
        return mServiceInterface.getorderProductdetailscall(convertPlainString(securcode), convertPlainString(user_id), convertPlainString(order_id) );
    }


    // get order summery
    public Call<OrderSummary> getOrderSummarycall(String securcode, String user_id, String qoute_id){
        return mServiceInterface.getOrderSummarycall(convertPlainString(securcode), convertPlainString(user_id), convertPlainString(qoute_id) );
    }

    // add new address
    public Call<AddNewAddress> addNewAddressCall(String securcode, String user_id, String fullname, String phone, String address1, String adress2, String city, String state,
                                                 String pincode){
        return mServiceInterface.addnewAddresscall(convertPlainString(securcode), convertPlainString(user_id),convertPlainString(fullname),convertPlainString(phone), convertPlainString(address1)
                , convertPlainString(adress2), convertPlainString(city), convertPlainString(state), convertPlainString(pincode));
    }

    // get order summery
    public Call<GetAddress> getUserAddresscall(String securcode, String user_id){
        return mServiceInterface.getUserAddress(convertPlainString(securcode), convertPlainString(user_id) );
    }
    // get place order api
    public Call<PlaceOrder> placceOrdercall(String securcode, String user_id, String address_id,
                                            String total_price, String qoute_id, String delivermode){
        return mServiceInterface.PlaceOrderCall(convertPlainString(securcode), convertPlainString(user_id),
                convertPlainString(address_id), convertPlainString(total_price), convertPlainString(qoute_id), convertPlainString( delivermode));
    }

    public Call<clearbalanceAPI> clearbalancecall(String securcode, String order_id , String total_price, String code, String mode, String amount){
        return mServiceInterface. clearbalancecall(convertPlainString(securcode), convertPlainString(order_id) , convertPlainString(total_price),  convertPlainString(code),  convertPlainString(mode), convertPlainString(amount) );
    }

    public Call<receiveAPI> receivecall(String securcode, String order_id , String status){
        return mServiceInterface. receivecall(convertPlainString(securcode), convertPlainString(order_id) , convertPlainString(status) );
    }

    // get booking summery
    public Call<BookingsRes> BookingsRescall(String securcode, String user_id){
        return mServiceInterface.BookingsRescall(convertPlainString(securcode), convertPlainString(user_id) );
    }

    // make new booking
    public Call<NewBookingsRes> NewBookingsRescall(String securcode, String user_id , String booking_type,  String date_set, String duration , String total_price ){
        return mServiceInterface.NewBookingsRescall(convertPlainString(securcode), convertPlainString(user_id), convertPlainString(booking_type),  convertPlainString(date_set),  convertPlainString(duration),  convertPlainString(total_price) );
    }

    // make new booking payment
    public Call<BookingsPaymentRes> NewBookingPaymentRescall(String securcode, String user_id , String booking_id, String code,String payment_amount ){
        return mServiceInterface.NewBookingPaymentRescall(convertPlainString(securcode), convertPlainString(user_id), convertPlainString(booking_id),  convertPlainString(code),  convertPlainString(payment_amount) );
    }

    ///  get location
    public Call<LocationRes> LocationResCall(String securcode){
        return mServiceInterface.LocationResCall(convertPlainString(securcode));
    }


    ///  get types
    public Call<TypesRes> TypesResCall(String securcode){
        return mServiceInterface.TypesResCall(convertPlainString(securcode));
    }


}


