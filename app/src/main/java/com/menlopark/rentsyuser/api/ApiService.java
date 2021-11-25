package com.menlopark.rentsyuser.api;

import com.menlopark.rentsyuser.model.Add_Booking.AddBookingRes;
import com.menlopark.rentsyuser.model.Booking.BokkingRes;
import com.menlopark.rentsyuser.model.Booking.BookingParams;
import com.menlopark.rentsyuser.model.Booking_Summary.BookingSummaryParams;
import com.menlopark.rentsyuser.model.Booking_Summary.BookingSummaryRes;
import com.menlopark.rentsyuser.model.Business_signup_model.BSignupParams;
import com.menlopark.rentsyuser.model.Business_signup_model.BusinessResp;
import com.menlopark.rentsyuser.model.Calendar_Data.CalendarDataRes;
import com.menlopark.rentsyuser.model.Calendar_Data.CalenderDataParams;
import com.menlopark.rentsyuser.model.Cart_Detail.CartDetailRes;
import com.menlopark.rentsyuser.model.Cart_Detail.Cart_Detail_Params;
import com.menlopark.rentsyuser.model.Catalogueitem.CatalogueItemRes;
import com.menlopark.rentsyuser.model.Catalogueitem.Catalogue_item_Params;
import com.menlopark.rentsyuser.model.Facebook_Model.FacebookRes;
import com.menlopark.rentsyuser.model.Forgot_model.ForgotParams;
import com.menlopark.rentsyuser.model.Forgot_model.ForgotResp;
import com.menlopark.rentsyuser.model.HalpCata.HelpCatRes;
import com.menlopark.rentsyuser.model.Help_QuestionAnswer.HelpQuestionAnswerParams;
import com.menlopark.rentsyuser.model.Help_QuestionAnswer.HelpQuestionAnswerRes;
import com.menlopark.rentsyuser.model.Notification.NotiParam;
import com.menlopark.rentsyuser.model.Notification.NotiRes;
import com.menlopark.rentsyuser.model.Pendding_Booking.PendingBookingParams;
import com.menlopark.rentsyuser.model.Pendding_Booking.PendingBookingRes;
import com.menlopark.rentsyuser.model.Pending_Bookig_summary.PendingSummaryParams;
import com.menlopark.rentsyuser.model.Pending_Bookig_summary.PendingSummaryRes;
import com.menlopark.rentsyuser.model.Post_Rating.PostRatingRes;
import com.menlopark.rentsyuser.model.Profile.ProfileRes;
import com.menlopark.rentsyuser.model.Promocode.PromocodeParam;
import com.menlopark.rentsyuser.model.Promocode.PromocodeRes;
import com.menlopark.rentsyuser.model.Rating.RatingParam;
import com.menlopark.rentsyuser.model.Rating.RatingRes;
import com.menlopark.rentsyuser.model.ResponseModel;
import com.menlopark.rentsyuser.model.Setting.Post_Setting.PostSettingParam;
import com.menlopark.rentsyuser.model.Setting.Post_Setting.PostSettingRes;
import com.menlopark.rentsyuser.model.Setting.SettingParam;
import com.menlopark.rentsyuser.model.Setting.SettingRes;
import com.menlopark.rentsyuser.model.Term_Condition.TermParams;
import com.menlopark.rentsyuser.model.Term_Condition.TermRes;
import com.menlopark.rentsyuser.model.add_to_cart.AddToCartParams;
import com.menlopark.rentsyuser.model.add_to_cart.AddToCartRes;
import com.menlopark.rentsyuser.model.favorite_store.AddFavoriteModel;
import com.menlopark.rentsyuser.model.location.LocationModel;
import com.menlopark.rentsyuser.model.login.LoginModel;
import com.menlopark.rentsyuser.model.login.LoginParams;
import com.menlopark.rentsyuser.model.ordercancel.ResponseCancel;
import com.menlopark.rentsyuser.model.ordercancel.requestCancel;
import com.menlopark.rentsyuser.model.signup.SignupParams;
import com.menlopark.rentsyuser.model.stores.Catalogs.CatalogList;
import com.menlopark.rentsyuser.model.stores.StoreDetails.StoreDetailsResp;
import com.menlopark.rentsyuser.model.stores.StoreModel;
import com.menlopark.rentsyuser.model.stores.StoresDetailsParams;
import com.menlopark.rentsyuser.model.stores.StoresParams;
import com.menlopark.rentsyuser.payment.NewCard;
import com.menlopark.rentsyuser.payment.NewCardResponse;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiService {

    ////////23Aug /rentsy/public/api/
    @POST("/rentsy/public/api/customer/register")
    public Call<ResponseModel> callSignup(@Body SignupParams body);

    @POST("/rentsy/public/api/customer/login")
    public Call<LoginModel> callLogin(@Body LoginParams body);


    @GET("rentsy/public/api/customer")
    public Call<ProfileRes> call_Profile(@Header("Authorization") String token);

    @GET("rentsy/public/api/all-locations")
    public Call<LocationModel> callLocation();

    @POST("rentsy/public/api/customer/forgot-password")
    public Call<ForgotResp> callForgot(@Body ForgotParams body);

    @POST("rentsy/public/api/all-stores")
    public Call<StoreModel> callStores(@Body StoresParams body);

    @POST("rentsy/public/api/subcategory-stores-list")
    public Call<StoreModel> callStores_subCat(@Body StoresParams body);

    @GET("rentsy/public/api/help-categories")
    public Call<HelpCatRes> call_Help_Categories();

    @POST("rentsy/public/api/customer/favourite-stores")
    public Call<StoreModel> callFavoriteList(@Header("Authorization") String token);

    @POST("rentsy/public/api/store-details")
    public Call<StoreDetailsResp> callStoresDetails(@Body StoresDetailsParams body);

    @POST("rentsy/public/api/stores-product-by-id")
    public Call<CatalogList> callCatalog(@Body StoresDetailsParams body);

    @POST("rentsy/public/api/product-details-by-id")
    public Call<CatalogueItemRes> call_get_catalogue_item(@Body Catalogue_item_Params body);


    @POST("rentsy/public/api/customer/add-store-to-favourite")
    public Call<ResponseModel> getFavoriteStatus(@Body AddFavoriteModel body,@Header("Authorization") String token);

    @POST("rentsy/public/api/price-wise-list")
    public Call<StoreModel> callgetPriceWiseList(@Body StoresParams body);

    @POST("rentsy/public/api/add-to-cart")
    public Call<AddToCartRes> callAddTocart(@Body AddToCartParams body);

    @POST("rentsy/public/api/get-static-content")
    public Call<TermRes> call_Term_Condition(@Body TermParams body);

    @POST("rentsy/public/api/customer/business-register")
    public Call<BusinessResp> callBusinessSignUp(@Body BSignupParams body);

    @POST("rentsy/public/api/setting-cust")
    public Call<PostSettingRes> call_post_setting(@Body PostSettingParam body);

    @POST("rentsy/public/api/get-setting-list")
    public Call<SettingRes> call_get_Setting(@Body SettingParam body);

    @POST("rentsy/public/api/notifications-list")
    public Call<NotiRes> call_Notification_List(@Body NotiParam body);

    //@POST("/rentsy/apis/helpQuestionAnswer.json")
    @POST("rentsy/public/api/help-question-answer")
    public Call<HelpQuestionAnswerRes> call_Help_Question_Answer(@Body HelpQuestionAnswerParams body);

    @POST("rentsy/public/api/get-cart-data.json")
    public Call<CartDetailRes> call_Get_Cart_Detail(@Body Cart_Detail_Params body);

    @POST("rentsy/public/api/review-details")
    public Call<RatingRes> call_get_rating(@Body RatingParam body);

    @POST("rentsy/public/api/pending-bookings")
    public Call<PendingBookingRes> call_Pending_Booking(@Body PendingBookingParams body);

    @POST("rentsy/public/api/view-bookings")
    public Call<PendingSummaryRes> call_Pending_Summaey(@Body PendingSummaryParams body);

    @POST("rentsy/public/api/calendar-data")
    public Call<CalendarDataRes> call_CalenderData(@Body CalenderDataParams body);

    //////end

    //  @Headers({"Content-Type: application/json"})



    @POST("/rentsy/apis/add-to-bookings.json")
    public Call<BokkingRes> callBOoking(@Body BookingParams body);

    @POST("/rentsy/apis/customer-facebook-login.json")
    public Call<FacebookRes> callFacebookLogin(@Body LoginParams body);



    @POST("/rentsy/apis/add-to-bookings.json")
    public Call<AddBookingRes> call_Add_Booking(@Body BookingParams body);

    @POST("/rentsy/apis/applyPromocode.json")
    public Call<PromocodeRes> call_ApplyPromocode(@Body PromocodeParam body);


    @POST("/rentsy/apis/booking-summary.json")
    public Call<BookingSummaryRes> call_Booking_Summary(@Body BookingSummaryParams body);







    @POST("/rentsy/public/api/cancel-booking")
    public Call<ResponseCancel> ordercancel(@Body requestCancel body);








   /* @POST("/rentsy/apis/rateCust.json")
    public Call<PostRatingRes> call_post_rating(@Body HashMap<String, String> body);*/

    @Headers("Content-Type:application/json")
    @POST("/rentsy/apis/rateCust.json")
    public Call<PostRatingRes> call_post_rating(@Body JSONObject body);


    @POST("/rentsy/apis/addPayment.json")
    public Call<NewCardResponse> addPayment(@Body NewCard body);
}
