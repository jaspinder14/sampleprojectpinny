package com.mogo.api;

import android.util.Log;

import com.mogo.model.EventDetailProp.EventDetailProp;
import com.mogo.model.SearchEventProp.SearchEventProp;
import com.mogo.model.attendEventsProp.AttendEventsProp;
import com.mogo.model.cancelEventProp.CancelEventProp;
import com.mogo.model.contactUsProp.ContactUsProp;
import com.mogo.model.createEvent.CreateEventPrp;
import com.mogo.model.deletenotificationprop.NotificationDeleteProp;
import com.mogo.model.forgot.ForgotPrp;
import com.mogo.model.login.LoginPrp;
import com.mogo.model.notificationprop.Notification;
import com.mogo.model.notificationprop.NotificationProp;
import com.mogo.model.radiusprop.RadiusProp;
import com.mogo.model.registerprop.RegisterProp;
import com.mogo.model.returnevent.ReturnEventPrp;
import com.mogo.model.returnsettingsprop.ReturnSettingsProp;
import com.mogo.model.todayeventsprop.TodayEventsProp;
import com.mogo.view.activities.EventDetail;
import com.mogo.view.activities.EventsListActivity;
import com.mogo.view.activities.ForgotPasswordActivity;
import com.mogo.view.activities.LoginActivity;
import com.mogo.view.activities.SignUpActivity;
import com.mogo.view.fragments.Contact_Fragment;
import com.mogo.view.fragments.CreateNewEvent;
import com.mogo.view.fragments.HomeFragment;
import com.mogo.view.fragments.NotificationFragment;
import com.mogo.view.fragments.Settings;
import com.mogo.view.fragments.TodayEventsFragment;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Mobile on 7/31/2017.
 */

public class WebServiceResult {

    public static void loginService(String email, String password, String name, String imageUrl, String socialType, String socialId, String deviceToken) {

        WebServiceConnection webServiceConnection = WebServiceConnection.getInstance();
        WebServiceConnection.holder.login(email, password, name, imageUrl, socialType, socialId, deviceToken).enqueue(new Callback<LoginPrp>() {
            @Override
            public void onResponse(Call<LoginPrp> call, Response<LoginPrp> response) {
                LoginActivity.getInstances().loginResponse(response.body());
            }

            @Override
            public void onFailure(Call<LoginPrp> call, Throwable t) {
                LoginActivity.getInstances().loginResponse(null);
            }
        });

    }

    public static void returnEvents(String date, String userId, String latitude, String longitude, String offset) {
        WebServiceConnection webServiceConnection = WebServiceConnection.getInstance();
        WebServiceConnection.holder.returnMapEvents(date, userId, latitude, longitude, offset).enqueue(new Callback<ReturnEventPrp>() {
            @Override
            public void onResponse(Call<ReturnEventPrp> call, Response<ReturnEventPrp> response) {
                HomeFragment.getInstance().returnEvents(response.body());
            }

            @Override
            public void onFailure(Call<ReturnEventPrp> call, Throwable t) {
                HomeFragment.getInstance().returnEvents(null);
            }
        });
    }

    public static void returnListEvents(String date, String userId, String latitude, String longitude, String offset) {
        WebServiceConnection webServiceConnection = WebServiceConnection.getInstance();
        WebServiceConnection.holder.returnEvent(date, userId, latitude, longitude, offset).enqueue(new Callback<ReturnEventPrp>() {
            @Override
            public void onResponse(Call<ReturnEventPrp> call, Response<ReturnEventPrp> response) {
                EventsListActivity.getInstance().returnEvents(response.body());
            }

            @Override
            public void onFailure(Call<ReturnEventPrp> call, Throwable t) {
                EventsListActivity.getInstance().returnEvents(null);
            }
        });

    }


    public static void forgotPassword(String email) {
        WebServiceConnection webServiceConnection = WebServiceConnection.getInstance();
        WebServiceConnection.holder.forgotPrp(email).enqueue(new Callback<ForgotPrp>() {
            @Override
            public void onResponse(Call<ForgotPrp> call, Response<ForgotPrp> response) {

                ForgotPasswordActivity.getinstance().forgotResponse(response.body());

            }

            @Override
            public void onFailure(Call<ForgotPrp> call, Throwable t) {

                ForgotPasswordActivity.getinstance().forgotResponse(null);
            }
        });


    }


    public static void createNewEvent(String userId, String name, String date, String endDate, String startTime, String endTime, File image, String frequency, String zipCode, String charityUrl, String charityPurposeTag, String eventType, String city, String location, String latitude, String longitude, String yelpUrl, String fbUrl, String benifitCharity, String description) {
        MultipartBody.Part partimage = null;

        if (image != null) {
            RequestBody requestBodieimage = RequestBody.create(MediaType.parse("multipart/form-data"), image);
            partimage = MultipartBody.Part.createFormData("image", image.getName(), requestBodieimage);
        }


        RequestBody idbody = RequestBody.create(MediaType.parse("multipart/form-data"), userId);
        RequestBody namebody = RequestBody.create(MediaType.parse("multipart/form-data"), name);
        RequestBody datebody = RequestBody.create(MediaType.parse("multipart/form-data"), date);
        RequestBody endDatebody = RequestBody.create(MediaType.parse("multipart/form-data"), endDate);
        RequestBody startTimebody = RequestBody.create(MediaType.parse("multipart/form-data"), startTime);
        RequestBody endTimeBody = RequestBody.create(MediaType.parse("multipart/form-data"), endTime);
        RequestBody frequencyBody = RequestBody.create(MediaType.parse("multipart/form-data"), frequency);
        RequestBody zipCodeBody = RequestBody.create(MediaType.parse("multipart/form-data"), zipCode);
        RequestBody charityBody = RequestBody.create(MediaType.parse("multipart/form-data"), charityUrl);
        RequestBody charitypurposeBody = RequestBody.create(MediaType.parse("multipart/form-data"), charityPurposeTag);
        RequestBody eventTypeBody = RequestBody.create(MediaType.parse("multipart/form-data"), eventType);
        RequestBody cityTypeBody = RequestBody.create(MediaType.parse("multipart/form-data"), city);
        RequestBody locationBody = RequestBody.create(MediaType.parse("multipart/form-data"), location);
        RequestBody latitudeBody = RequestBody.create(MediaType.parse("multipart/form-data"), latitude);
        RequestBody longitudeBody = RequestBody.create(MediaType.parse("multipart/form-data"), longitude);
        RequestBody yelpBody = RequestBody.create(MediaType.parse("multipart/form-data"), yelpUrl);
        RequestBody fbBody = RequestBody.create(MediaType.parse("multipart/form-data"), fbUrl);
        RequestBody benifitBody = RequestBody.create(MediaType.parse("multipart/form-data"), benifitCharity);
        RequestBody descriptionBody = RequestBody.create(MediaType.parse("multipart/form-data"), description);

        WebServiceConnection webServiceConnection = WebServiceConnection.getInstance();
        WebServiceConnection.holder.createEvent(idbody, namebody, datebody, endDatebody, startTimebody, endTimeBody, partimage, frequencyBody, zipCodeBody, charityBody, charitypurposeBody, eventTypeBody, cityTypeBody, locationBody, latitudeBody, longitudeBody, yelpBody, fbBody, benifitBody, descriptionBody).enqueue(new Callback<CreateEventPrp>() {
            @Override
            public void onResponse(Call<CreateEventPrp> call, Response<CreateEventPrp> response) {
                CreateNewEvent.getInstance().createEventInfo(response.body());
                Log.e("response", response.body().toString());
            }

            @Override
            public void onFailure(Call<CreateEventPrp> call, Throwable t) {
                CreateNewEvent.getInstance().createEventInfo(null);
                Log.e("response", t.getLocalizedMessage());
            }
        });
    }

    public static void registerService(String userName, String email, String password, String deviceToken, File image) {


        MultipartBody.Part partimage = null;

        if (image != null) {
            RequestBody requestBodieimage = RequestBody.create(MediaType.parse("multipart/form-data"), image);
            partimage = MultipartBody.Part.createFormData("image", image.getName(), requestBodieimage);
        }


        RequestBody userNamebody = RequestBody.create(MediaType.parse("multipart/form-data"), userName);
        RequestBody emailbody = RequestBody.create(MediaType.parse("multipart/form-data"), email);
        RequestBody passwordbody = RequestBody.create(MediaType.parse("multipart/form-data"), password);
        RequestBody devicetokenbody = RequestBody.create(MediaType.parse("multipart/form-data"), deviceToken);

        WebServiceConnection webServiceConnection = WebServiceConnection.getInstance();
        WebServiceConnection.holder.register(userNamebody, emailbody, passwordbody, devicetokenbody, partimage).enqueue(new Callback<RegisterProp>() {
            @Override
            public void onResponse(Call<RegisterProp> call, Response<RegisterProp> response) {
                SignUpActivity.getinstance().register(response.body());
            }

            @Override
            public void onFailure(Call<RegisterProp> call, Throwable t) {
                SignUpActivity.getinstance().register(null);
            }
        });
    }

    public static void searchEventService(String date, String userId, String name, String startTime, String endTime, String type) {
        WebServiceConnection webServiceConnection = WebServiceConnection.getInstance();
        WebServiceConnection.holder.searchEvent(date, userId, name, startTime, endTime, type).enqueue(new Callback<SearchEventProp>() {
            @Override
            public void onResponse(Call<SearchEventProp> call, Response<SearchEventProp> response) {
                EventsListActivity.getInstance().searchEvents(response.body());
            }

            @Override
            public void onFailure(Call<SearchEventProp> call, Throwable t) {
                EventsListActivity.getInstance().searchEvents(null);
            }
        });
    }

    public static void searchEventsOnMap(String date, String userId, String name, String startTime, String endTime, String type) {
        WebServiceConnection webServiceConnection = WebServiceConnection.getInstance();
        WebServiceConnection.holder.searchEvent(date, userId, name, startTime, endTime, type).enqueue(new Callback<SearchEventProp>() {
            @Override
            public void onResponse(Call<SearchEventProp> call, Response<SearchEventProp> response) {
                HomeFragment.getInstance().searchEventsonMap(response.body());
            }

            @Override
            public void onFailure(Call<SearchEventProp> call, Throwable t) {
                HomeFragment.getInstance().searchEventsonMap(null);
            }
        });
    }

    public static void attendEventService(String userId, String eventId) {
        WebServiceConnection webServiceConnection = WebServiceConnection.getInstance();
        WebServiceConnection.holder.attendEvents(userId, eventId).enqueue(new Callback<AttendEventsProp>() {
            @Override
            public void onResponse(Call<AttendEventsProp> call, Response<AttendEventsProp> response) {
                EventDetail.getInstance().attendEvent(response.body());
            }

            @Override
            public void onFailure(Call<AttendEventsProp> call, Throwable t) {
                EventDetail.getInstance().attendEvent(null);
            }
        });
    }

    public static void returnTodayEventsService(String date, String userId, String latitude, String longitude, String offset) {
        WebServiceConnection webServiceConnection = WebServiceConnection.getInstance();
        WebServiceConnection.holder.todayEvents(date, userId, latitude, longitude, offset).enqueue(new Callback<TodayEventsProp>() {
            @Override
            public void onResponse(Call<TodayEventsProp> call, Response<TodayEventsProp> response) {
                TodayEventsFragment.getInstance().returnTodayEvents(response.body());
            }

            @Override
            public void onFailure(Call<TodayEventsProp> call, Throwable t) {
                TodayEventsFragment.getInstance().returnTodayEvents(null);
            }
        });
    }

    public static void returnNotifications(String userId) {
        WebServiceConnection webServiceConnection = WebServiceConnection.getInstance();
        WebServiceConnection.holder.notifications(userId).enqueue(new Callback<NotificationProp>() {
            @Override
            public void onResponse(Call<NotificationProp> call, Response<NotificationProp> response) {
                NotificationFragment.getInstance().returnNotifications(response.body());
            }

            @Override
            public void onFailure(Call<NotificationProp> call, Throwable t) {
                NotificationFragment.getInstance().returnNotifications(null);
            }
        });
    }

    public static void deleteNotificationService(String notificationId) {
        WebServiceConnection webServiceConnection = WebServiceConnection.getInstance();
        WebServiceConnection.holder.deleteNotification(notificationId).enqueue(new Callback<NotificationDeleteProp>() {
            @Override
            public void onResponse(Call<NotificationDeleteProp> call, Response<NotificationDeleteProp> response) {
                NotificationFragment.getInstance().deletNotification(response.body());

            }

            @Override
            public void onFailure(Call<NotificationDeleteProp> call, Throwable t) {
                NotificationFragment.getInstance().deletNotification(null);
            }
        });
    }

    public static void searchTodayEventsService(String date, String userId, String latitude, String longitude, String title, String type) {

        WebServiceConnection webServiceConnection = WebServiceConnection.getInstance();
        WebServiceConnection.holder.searchTodayEvents(date, userId, latitude, longitude, title, type).enqueue(new Callback<TodayEventsProp>() {
            @Override
            public void onResponse(Call<TodayEventsProp> call, Response<TodayEventsProp> response) {
                TodayEventsFragment.getInstance().returnTodayEvents(response.body());
            }

            @Override
            public void onFailure(Call<TodayEventsProp> call, Throwable t) {
                TodayEventsFragment.getInstance().returnTodayEvents(null);
            }
        });

    }

    public static void updateRadiusService(String userId, String radius) {
        WebServiceConnection webServiceConnection = WebServiceConnection.getInstance();
        WebServiceConnection.holder.updateRadius(userId, radius).enqueue(new Callback<RadiusProp>() {
            @Override
            public void onResponse(Call<RadiusProp> call, Response<RadiusProp> response) {
                Settings.getInstance().updateRadius(response.body());
            }

            @Override
            public void onFailure(Call<RadiusProp> call, Throwable t) {
                Settings.getInstance().updateRadius(null);
            }
        });
    }

    public static void returnSettings(String userId) {
        WebServiceConnection webServiceConnection = WebServiceConnection.getInstance();
        WebServiceConnection.holder.returnSettings(userId).enqueue(new Callback<ReturnSettingsProp>() {
            @Override
            public void onResponse(Call<ReturnSettingsProp> call, Response<ReturnSettingsProp> response) {
                Settings.getInstance().returnSettings(response.body());
            }

            @Override
            public void onFailure(Call<ReturnSettingsProp> call, Throwable t) {
                Settings.getInstance().returnSettings(null);
            }
        });
    }

    public static void cancelEventService(String eventId) {
        WebServiceConnection webServiceConnection = WebServiceConnection.getInstance();
        WebServiceConnection.holder.cancelEvent(eventId).enqueue(new Callback<CancelEventProp>() {
            @Override
            public void onResponse(Call<CancelEventProp> call, Response<CancelEventProp> response) {
                EventDetail.getInstance().cancelEvent(response.body());
            }

            @Override
            public void onFailure(Call<CancelEventProp> call, Throwable t) {
                EventDetail.getInstance().cancelEvent(null);
            }
        });
    }

    public static void updateEventTokenService(String userId, String eventToken, String eventId) {
        WebServiceConnection webServiceConnection = WebServiceConnection.getInstance();
        WebServiceConnection.holder.updateEventToken(userId, eventToken, eventId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                EventDetail.getInstance().updateEventToken(response.body());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                EventDetail.getInstance().updateEventToken(null);
            }
        });
    }

    public static void retunEventDetail(String eventId, String userId) {

        WebServiceConnection webServiceConnection = WebServiceConnection.getInstance();
        WebServiceConnection.holder.returnEventDetail(eventId, userId).enqueue(new Callback<EventDetailProp>() {
            @Override
            public void onResponse(Call<EventDetailProp> call, Response<EventDetailProp> response) {
                EventDetail.getInstance().returnEventDetail(response.body());
            }

            @Override
            public void onFailure(Call<EventDetailProp> call, Throwable t) {
                EventDetail.getInstance().returnEventDetail(null);
            }
        });

    }

    public static void contactUsService(String userId, String message, String email, String name) {
        WebServiceConnection.holder.contactUsService(userId, message, email, name).enqueue(new Callback<ContactUsProp>() {
            @Override
            public void onResponse(Call<ContactUsProp> call, Response<ContactUsProp> response) {
                Contact_Fragment.getInstance().contactUs(response.body());
            }

            @Override
            public void onFailure(Call<ContactUsProp> call, Throwable t) {
                Contact_Fragment.getInstance().contactUs(null);
            }
        });
    }

}
