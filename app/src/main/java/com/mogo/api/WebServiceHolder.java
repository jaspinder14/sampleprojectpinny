package com.mogo.api;

import com.mogo.model.EventDetailProp.EventDetailProp;
import com.mogo.model.SearchEventProp.SearchEventProp;
import com.mogo.model.attendEventsProp.AttendEventsProp;
import com.mogo.model.cancelEventProp.CancelEventProp;
import com.mogo.model.contactUsProp.ContactUsProp;
import com.mogo.model.createEvent.CreateEventPrp;
import com.mogo.model.deletenotificationprop.NotificationDeleteProp;
import com.mogo.model.forgot.ForgotPrp;
import com.mogo.model.login.LoginPrp;
import com.mogo.model.notificationprop.NotificationProp;
import com.mogo.model.radiusprop.RadiusProp;
import com.mogo.model.registerprop.RegisterProp;
import com.mogo.model.returnevent.ReturnEventPrp;
import com.mogo.model.returnsettingsprop.ReturnSettingsProp;
import com.mogo.model.todayeventsprop.TodayEventsProp;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by Mobile on 7/31/2017.
 */

public interface WebServiceHolder {

    @Multipart
    @POST("register.php")
    Call<RegisterProp> register(@Part("userName") RequestBody userName, @Part("email") RequestBody email, @Part("password") RequestBody password, @Part("deviceToken") RequestBody deviceToken, @Part MultipartBody.Part image);

    @FormUrlEncoded
    @POST("login.php")
    Call<LoginPrp> login(@Field("email") String email, @Field("password") String password, @Field("name") String name, @Field("imageUrl") String imageUrl, @Field("socialType") String socialType, @Field("socialId") String socialId, @Field("deviceToken") String deviceToken);

    @FormUrlEncoded
    @POST("returnEvents.php")
    Call<ReturnEventPrp> returnEvent(@Field("date") String date, @Field("userId") String userId, @Field("latitude") String latitude, @Field("longitude") String longitude, @Field("offset") String offset);

    @FormUrlEncoded
    @POST("returnAllEvents.php")
    Call<ReturnEventPrp> returnMapEvents(@Field("date") String date, @Field("userId") String userId, @Field("latitude") String latitude, @Field("longitude") String longitude, @Field("offset") String offset);

    @FormUrlEncoded
    @POST("forgotPassword.php")
    Call<ForgotPrp> forgotPrp(@Field("email") String email);

    @Multipart
    @POST("createEvent.php")
    Call<CreateEventPrp> createEvent(@Part("userId") RequestBody userId, @Part("name") RequestBody name, @Part("date") RequestBody date, @Part("endDate") RequestBody endDate, @Part("startTime") RequestBody startTime, @Part("endTime") RequestBody endTime, @Part MultipartBody.Part image, @Part("frequency") RequestBody frequency, @Part("zipCode") RequestBody zipCode, @Part("charityUrl") RequestBody charityUrl, @Part("charityPurposeTag") RequestBody charityPurposeTag, @Part("eventType") RequestBody eventType, @Part("city") RequestBody city, @Part("location") RequestBody location, @Part("latitude") RequestBody latitude, @Part("longitude") RequestBody longitude, @Part("yelpUrl") RequestBody yelpUrl, @Part("fbUrl") RequestBody fbUrl, @Part("benifitCharity") RequestBody benifitCharity, @Part("description") RequestBody description);

    @FormUrlEncoded
    @POST("searchEvents.php")
    Call<SearchEventProp> searchEvent(@Field("date") String date, @Field("userId") String userId, @Field("name") String name, @Field("startTime") String startTime, @Field("endTime") String endTime, @Field("type") String type);

    @FormUrlEncoded
    @POST("attendEvents.php")
    Call<AttendEventsProp> attendEvents(@Field("userId") String userId, @Field("eventId") String eventId);

    @FormUrlEncoded
    @POST("returnTodayEvents.php")
    Call<TodayEventsProp> todayEvents(@Field("date") String date, @Field("userId") String userId, @Field("latitude") String latitude, @Field("longitude") String longitude, @Field("offset") String offset);

    @FormUrlEncoded
    @POST("returnNotifications.php")
    Call<NotificationProp> notifications(@Field("userId") String userId);

    @FormUrlEncoded
    @POST("deleteNotifications.php")
    Call<NotificationDeleteProp> deleteNotification(@Field("notificationId") String notificationId);


    @FormUrlEncoded
    @POST("searchTodayEvents.php")
    Call<TodayEventsProp> searchTodayEvents(@Field("date") String date, @Field("userId") String userId, @Field("latitude") String latitude, @Field("longitude") String longitude, @Field("title") String title, @Field("type") String type);

    @FormUrlEncoded
    @POST("updateRadius.php")
    Call<RadiusProp> updateRadius(@Field("userId") String userId, @Field("radius") String radius);

    @FormUrlEncoded
    @POST("returnSettings.php")
    Call<ReturnSettingsProp> returnSettings(@Field("userId") String userId);

    @FormUrlEncoded
    @POST("cancelEvent.php")
    Call<CancelEventProp> cancelEvent(@Field("eventId") String eventId);

    @FormUrlEncoded
    @POST("updateEventToken.php")
    Call<ResponseBody> updateEventToken(@Field("userId") String userId, @Field("eventToken") String eventToken, @Field("eventId") String eventId);

    @FormUrlEncoded
    @POST("returnEventDetail.php")
    Call<EventDetailProp> returnEventDetail(@Field("eventId") String eventId, @Field("userId") String userId);

    @FormUrlEncoded
    @POST("addMessageForContactUs.php")
    Call<ContactUsProp> contactUsService(@Field("userId") String userId, @Field("message") String message, @Field("email") String email, @Field("name") String name);

}
