package com.example.picha_clear.WebServices;



import com.example.picha_clear.beanResponse.AddNewAddress;
import com.example.picha_clear.beanResponse.AddtoCart;
import com.example.picha_clear.beanResponse.BookingPaymentsMadeRes;
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
import com.example.picha_clear.beanResponse.UpdatePriceRes;
import com.example.picha_clear.beanResponse.UserSignInRes;
import com.example.picha_clear.beanResponse.clearbalanceAPI;
import com.example.picha_clear.beanResponse.codeAPI;
import com.example.picha_clear.beanResponse.getCartDetails;
import com.example.picha_clear.beanResponse.payAPI;
import com.example.picha_clear.beanResponse.receiveAPI;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ServiceInterface {
    // method,, return type ,, secondary url
    @Multipart
    @POST("picha_clear/new_user_registration.php")
    Call<NewUserRegistration> NewUserRegistrationCall(

            @Part("id_number") RequestBody id_number,
            @Part("first_name") RequestBody first_name,
            @Part("last_name") RequestBody last_name,
            @Part("email") RequestBody email,
            @Part("phone_number") RequestBody phone_number,
            @Part("username") RequestBody username,
            @Part("password") RequestBody password
    );

    ///  user signin request
    @Multipart
    @POST("picha_clear/user_signin.php")
    Call<UserSignInRes> UserSigninCall(
            @Part("username") RequestBody username,
            @Part("password") RequestBody password
    );

    // get camera products
    @Multipart
    @POST("picha_clear/cameras.php")
    Call<CamerasProductRes> getCameraProduct(
            @Part("securecode") RequestBody securecode
    );
    // get drones products
    @Multipart
    @POST("picha_clear/drones.php")
    Call<DronesProductRes> getDroneProduct(
            @Part("securecode") RequestBody securecode
    );
    // get lenses products
    @Multipart
    @POST("picha_clear/lenses.php")
    Call<LensesProductRes> getLensesProduct(
            @Part("securecode") RequestBody securecode
    );

    // get product details
    @Multipart
    @POST("picha_clear/getproductdetails.php")
    Call<ProductDetail_Res> getProductDetials(
            @Part("securecode") RequestBody securecode,
            @Part("prod_id") RequestBody prod_id
    );

    // add to cart
    @Multipart
    @POST("picha_clear/add_prod_into_cart.php")
    Call<AddtoCart> addtocartcall(
            @Part("securecode") RequestBody securecode,
            @Part("prod_id") RequestBody prod_id,
            @Part("user_id") RequestBody user_id,
            @Part("qty") RequestBody qty,
            @Part("days") RequestBody days

    );

    // get user cart
    @Multipart
    @POST("picha_clear/getusercartdetails.php")
    Call<getCartDetails> getusercartcall(
            @Part("securecode") RequestBody securecode,
            @Part("qoute_id") RequestBody qoute_id,
            @Part("user_id") RequestBody user_id
    );

    // delete cart item
    @Multipart
    @POST("picha_clear/deletecartitem.php")
    Call<AddtoCart> deleteCartProd(
            @Part("securecode") RequestBody securecode,
            @Part("user_id") RequestBody user_id,
            @Part("prod_id") RequestBody prod_id
    );

    // edit cart qty
    @Multipart
    @POST("picha_clear/editcartitem.php")
    Call<EditCartItem> editCartQty(
            @Part("securecode") RequestBody securecode,
            @Part("user_id") RequestBody user_id,
            @Part("prod_id") RequestBody prod_id,
            @Part("prod_qty") RequestBody prod_qty
    );


    // get order summery
    @Multipart
    @POST("picha_clear/getordersummary.php")
    Call<OrderSummary> getOrderSummarycall(
            @Part("securecode") RequestBody securecode,
            @Part("user_id") RequestBody user_id,
            @Part("qoute_id") RequestBody qoute_id
    );

    // add new address
    @Multipart
    @POST("picha_clear/add_address.php")
    Call<AddNewAddress> addnewAddresscall(
            @Part("securecode") RequestBody securecode,
            @Part("user_id") RequestBody user_id,
            @Part("fullname") RequestBody fullname,
            @Part("phone") RequestBody phone,
            @Part("address1") RequestBody address1,
            @Part("address2") RequestBody address2,
            @Part("city") RequestBody city,
            @Part("state") RequestBody state,
            @Part("pincode") RequestBody pincode
    );

    // get user address
    @Multipart
    @POST("picha_clear/getUserAddress.php")
    Call<GetAddress> getUserAddress(
            @Part("securecode") RequestBody securecode,
            @Part("user_id") RequestBody user_id
    );

    // place order api
    @Multipart
    @POST("picha_clear/placeorderapi.php")
    Call<PlaceOrder> PlaceOrderCall(
            @Part("securecode") RequestBody securecode,
            @Part("user_id") RequestBody user_id,
            @Part("address_id") RequestBody address_id,
            @Part("total_price") RequestBody total_price,
            @Part("qoute_id") RequestBody qoute_id,
            @Part("deliverymode") RequestBody deliverymode
    );

    // make payment
    @Multipart
    @POST("picha_clear/makepayment.php")
    Call<payAPI> makepaymentcall(
            @Part("securecode") RequestBody securecode,
            @Part("user_id") RequestBody user_id,
            @Part("order_id") RequestBody order_id,
            @Part("total_price") RequestBody total_price,
            @Part("payment_amount") RequestBody payment_amount

    );
    // code
    @Multipart
    @POST("picha_clear/code.php")
    Call<codeAPI> codecall(
            @Part("securecode") RequestBody securecode,
            @Part("order_id") RequestBody order_id,
            @Part("code") RequestBody code

    );

    // get order history
    @Multipart
    @POST("picha_clear/getorderhistory.php")
    Call<OrderHistoryAPI> getorderHistorycall(
            @Part("securecode") RequestBody securecode,
            @Part("user_id") RequestBody user_id
    );

    // get order prodct details history
    @Multipart
    @POST("picha_clear/getorderhistoryproddetails.php")
    Call<GetOrderProductDetails> getorderProductdetailscall(
            @Part("securecode") RequestBody securecode,
            @Part("user_id") RequestBody user_id,
            @Part("order_id") RequestBody order_id
    );

    // CLEAR BALANCE
    @Multipart
    @POST("picha_clear/clearbalance.php")
    Call<clearbalanceAPI> clearbalancecall(
            @Part("securecode") RequestBody securecode,
            @Part("order_id") RequestBody order_id,
            @Part("total_price") RequestBody total_price,
            @Part("code") RequestBody code,
            @Part("mode") RequestBody mode,
            @Part("amount") RequestBody amount

    );

    // RECEIVE
    @Multipart
    @POST("picha_clear/receive.php")
    Call<receiveAPI> receivecall(
            @Part("securecode") RequestBody securecode,
            @Part("order_id") RequestBody order_id,
            @Part("status") RequestBody status

    );
    // get booking summery
    @Multipart
    @POST("picha_clear/getbookingsummary.php")
    Call<BookingsRes> BookingsRescall(
            @Part("securecode") RequestBody securecode,
            @Part("user_id") RequestBody user_id

    );

    // make new booking
    @Multipart
    @POST("picha_clear/make_booking.php")
    Call<NewBookingsRes> NewBookingsRescall(
            @Part("securecode") RequestBody securecode,
            @Part("user_id") RequestBody user_id,
            @Part("booking_type") RequestBody booking_type,
            @Part("date_set") RequestBody date_set,
            @Part("duration") RequestBody duration,
            @Part("total_price") RequestBody total_price,
            @Part("location_id") RequestBody location_id

    );

    // make new booking payment
    @Multipart
    @POST("picha_clear/make_booking_payment.php")
    Call<BookingsPaymentRes> NewBookingPaymentRescall(
            @Part("securecode") RequestBody securecode,
            @Part("user_id") RequestBody user_id,
            @Part("booking_id") RequestBody booking_id,
            @Part("code") RequestBody code,
             @Part("payment_amount") RequestBody payment_amount

    );

    // get locations
    @Multipart
    @POST("picha_clear/getlocation.php")
    Call<LocationRes> LocationResCall(
            @Part("securecode") RequestBody securecode
    );

    // get TYPES
    @Multipart
    @POST("picha_clear/getTypes.php")
    Call<TypesRes> TypesResCall(
            @Part("securecode") RequestBody securecode
    );

    // get booking payments
    @Multipart
    @POST("picha_clear/getbooking_payment.php")
    Call<BookingPaymentsMadeRes>BookingsPaymentMadeRescall(
            @Part("securecode") RequestBody securecode,
            @Part("booking_id") RequestBody booking_id

    );

    // make update booking payment
    @Multipart
    @POST("picha_clear/update_booking_pay.php")
    Call<UpdatePriceRes> UpdatePriceRescall(
            @Part("securecode") RequestBody securecode,
            @Part("user_id") RequestBody user_id,
            @Part("booking_id") RequestBody booking_id,
            @Part("code") RequestBody code,
            @Part("payment_amount") RequestBody payment_amount

    );


}




