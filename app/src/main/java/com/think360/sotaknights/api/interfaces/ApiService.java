package com.think360.sotaknights.api.interfaces;

import com.think360.sotaknights.api.data.Responce;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;


/**
 * Created by think360 on 18/04/17.
 */

public interface ApiService {

    @FormUrlEncoded
    @POST("sota_login")
    Call<Responce.LoginResponce> sotaLogin(@Field("device_type") String device_type, @Field("device_id") String device_id, @Field("user_name") String user_name, @Field("password") String password);


    @Multipart
    @POST("sota_register")
    Call<Responce.StatusResponce> sotaRegistration(@Part("device_type") RequestBody device_type, @Part("device_id") RequestBody device_id,
                                                   @Part("sota_name") RequestBody sota_name, @Part("sota_email") RequestBody sota_email,
                                                   @Part("sota_mobile") RequestBody sota_mobile, @Part("sota_address") RequestBody sota_address,
                                                   @Part("sota_city") RequestBody sota_city, @Part("sota_state") RequestBody sota_state,
                                                   @Part("sota_ac_no") RequestBody sota_ac_no, @Part("sota_ifsc_code") RequestBody sota_ifsc_code,
                                                   @Part("sota_branch_name_add") RequestBody sota_branch_name_add, @Part MultipartBody.Part sota_aadhar_image1,
                                                   @Part MultipartBody.Part sota_aadhar_image2, @Part MultipartBody.Part sota_psara_image1,
                                                   @Part MultipartBody.Part sota_psara_image2, @Part MultipartBody.Part sota_dl_image1,
                                                   @Part MultipartBody.Part sota_dl_image2, @Part MultipartBody.Part sota_gl_image1, @Part MultipartBody.Part sota_gl_image2,
                                                   @Part("sota_pre_add") RequestBody sota_pre_add, @Part("sota_pre_city") RequestBody sota_pre_city,
                                                   @Part("sota_pre_state") RequestBody sota_pre_state,@Part("other_city") RequestBody other_city,
                                                   @Part("sota_other_pre_city") RequestBody sota_other_pre_city);
    @FormUrlEncoded
    @POST("sota_current_location")
    Call<Responce> sendSotaCurrentLocation(@Field("user_id") String user_id, @Field("lat") String lat, @Field("lang") String lang);

    @FormUrlEncoded
    @POST("sota_available_status")
    Call<Responce> sotaAvailableStatus(@Field("user_id") String user_id, @Field("lat") String lat, @Field("lang") String lang,@Field("status") String status);

    @POST("get_company_info")
    Call<Responce.CompanyInfoResponce> getCompanyInfo();

    @FormUrlEncoded
    @POST("send_question")
    Call<Responce.StatusResponce> askQuestion(@Field("user_id") String user_id, @Field("message") String message);

    @FormUrlEncoded
    @POST("push_thread_alarm")
    Call<Responce.StatusResponce> pushThreadAlarm(@Field("user_id") String user_id,@Field("lat") String lat,@Field("lang") String lang);

    @FormUrlEncoded
    @POST("get_sota_profile")
    Call<Responce.ProfileResponce> getProfile(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("get_sota_tasks")
    Call<Responce.SotaTaskResponce> getTask(@Field("user_id") String user_id);


    @POST("all_states")
    Call<Responce.StateResponce> all_States();

    @FormUrlEncoded
    @POST("all_cities")
    Call<Responce.CityResponce> all_Cities(@Field("state_id") String state_id);
/*    @Multipart
    @POST("/upload")
    Observable<ResponseBody> uploadVideo(@Body MultipartBody requestBody);*/
}
