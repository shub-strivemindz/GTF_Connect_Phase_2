package com.gtfconnect.controller;



import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.gtfconnect.models.CountryResponse;

import java.net.URL;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;


public interface RestService {


    // Login Validation RestService --
    @FormUrlEncoded
    @POST(ApiUrls.URL_LOGIN)
    Observable<JsonElement> login(@Header("DeviceType") String device_type,@Header("DeviceToken") String device_token,@FieldMap Map<String, Object> params);


    @FormUrlEncoded
    @POST(ApiUrls.URL_REGISTRATION)
    Observable<JsonElement> registration(@Header ("DeviceToken") String deviceToken, @FieldMap Map<String, Object> params);


    @FormUrlEncoded
    @POST(ApiUrls.URL_VERIFY_OTP)
    Observable<JsonElement> verifyOTP(@FieldMap Map<String, Object> params);


    @FormUrlEncoded
    @POST(ApiUrls.URL_RESEND_OTP)
    Observable<JsonElement> resendOTP(@Field("email") String email);


    @FormUrlEncoded
    @POST(ApiUrls.URL_FORGET_PASSWORD)
    Observable<JsonElement> forgetPassword(@Field("Email") String email);


    // Get Country List RestService --
    @GET(ApiUrls.URL_GET_COUNTRY)
    Observable<JsonElement> getCountryData();



    // Get State corresponding to Country Code --
    @FormUrlEncoded
    @POST(ApiUrls.URL_GET_STATE)
    Observable<JsonElement> getStateList(@Field("CountryID") int countryCode);


    // Get State corresponding to State Code --
    @FormUrlEncoded
    @POST(ApiUrls.URL_GET_CITY)
    Observable<JsonElement> getCityList(@Field("StateID") int stateCode);



    @GET(ApiUrls.URL_GET_PROFILE_DETAIL)
    Observable<JsonElement> getUserProfile(@Header ("Authorization") String api_token);


    @FormUrlEncoded
    @POST(ApiUrls.UPDATE_PROFILE_DATA)
    Observable<JsonElement> updateUserProfile(@Header ("Authorization") String api_token,
                                              @Header("DeviceType") String device_type,
                                              @Header("DeviceToken") String device_token,
                                              @Header("FromGTFConnect") String fromGtfConnect,
                                              @FieldMap Map<String, Object> params);


    @GET(ApiUrls.URL_PIN_MESSAGE)
    Observable<JsonElement> pinMessage(@QueryMap Map<String,Object> params);


    @GET(ApiUrls.URL_GET_PINNED_MESSAGE)
    Observable<JsonElement> getPinnedMessages(@QueryMap Map<String,Object> params);


    @GET(ApiUrls.URL_REMOVE_PINNED_MESSAGE)
    Observable<JsonElement> removePinnedMessage(@QueryMap Map<String,Object> params);


    @GET(ApiUrls.URL_REMOVE_ALL_PINNED_MESSAGE)
    Observable<JsonElement> removeAllPinnedMessage(@QueryMap Map<String,Object> params);



    @GET(ApiUrls.URL_GET_EMOJI)
    Observable<JsonElement> getEmojiList();


    @GET(ApiUrls.URL_GROUP_CHANNEL+"/"+"{id}"+"/"+"{endpoint}")
    Observable<JsonElement> get_groupChannel_subscription(@Path("id") int id,
                                                          @Path("endpoint") String endPoint,
                                                          @Header ("Authorization") String api_token,
                                                          @Header("DeviceType") String device_type,
                                                          @Header("DeviceToken") String device_token);


    @GET(ApiUrls.URL_GROUP_CHANNEL+"/"+"{id}"+"/"+"details")
    Observable<JsonElement> get_admin_group_channel_settings(@Path("id") int id,
                                                             @Header ("Authorization") String api_token,
                                                             @Header("DeviceType") String device_type,
                                                             @Header("DeviceToken") String device_token);

    @FormUrlEncoded
    @PATCH(ApiUrls.URL_GROUP_CHANNEL+"/"+"{id}"+"/"+ ApiUrls.URL_GROUP_CHANNEL_UPDATE_PROFILE)
    Observable<JsonElement> update_group_channel_profile(@Path("id") int id,
                                                         @Header ("Authorization") String api_token,
                                                         @Header("DeviceType") String device_type,
                                                         @Header("DeviceToken") String device_token,
                                                         @FieldMap Map<String, Object> params);




    @FormUrlEncoded
    @PATCH(ApiUrls.URL_GROUP_CHANNEL+"/"+"{id}"+"/"+ApiUrls.URL_GROUP_CHANNEL_UPDATE_PERMISSION)
    Observable<JsonElement> update_group_channel_permission_settings(@Path("id") int id,
                                                         @Header ("Authorization") String api_token,
                                                         @Header("DeviceType") String device_type,
                                                         @Header("DeviceToken") String device_token,
                                                         @FieldMap Map<String, Object> params);



    @GET(ApiUrls.URL_GROUP_CHANNEL+"/"+"{id}"+"/"+ApiUrls.URL_GET_EMOJI_REACTION_LIST)
    Observable<JsonElement> get_group_channel_manage_reaction_list(@Path("id") int id,
                                                                     @Header ("Authorization") String api_token,
                                                                     @Header("DeviceType") String device_type,
                                                                     @Header("DeviceToken") String device_token,
                                                                     @Query("page") int page,
                                                                     @Query("per_page") int per_page,
                                                                     @Query("is_active") int isActive);




    @GET(ApiUrls.URL_GROUP_CHANNEL+"/"+"{id}"+"/"+ApiUrls.URL_GROUP_CHANNEL_GET_SUBSCRIPTION_MEMBERS)
    Observable<JsonElement> get_group_channel_manage_subscriber_list(@Path("id") int id,
                                                                   @Header ("Authorization") String api_token,
                                                                   @Header("DeviceType") String device_type,
                                                                   @Header("DeviceToken") String device_token,
                                                                   @Query("page") int page,
                                                                   @Query("per_page") int per_page);



    @FormUrlEncoded
    @PATCH(ApiUrls.URL_GROUP_CHANNEL+"/"+"{id}"+"/"+ApiUrls.URL_GROUP_CHANNEL_UPDATE_SETTINGS)
    Observable<JsonElement> update_group_channel_settings(@Path("id") int id,
                                                                     @Header ("Authorization") String api_token,
                                                                     @Header("DeviceType") String device_type,
                                                                     @Header("DeviceToken") String device_token,
                                                                     @FieldMap Map<String, Object> params);




    @FormUrlEncoded
    @PATCH(ApiUrls.URL_GROUP_CHANNEL+"/"+"{id}"+"/"+ApiUrls.URL_GROUP_CHANNEL_UPDATE_REACTION_LIST)
    Observable<JsonElement> update_group_channel_reaction_list(@Path("id") int id,
                                                          @Header ("Authorization") String api_token,
                                                          @Header("DeviceType") String device_type,
                                                          @Header("DeviceToken") String device_token,
                                                          @FieldMap Map<String, Object> params);





    @FormUrlEncoded
    @PATCH(ApiUrls.URL_GROUP_CHANNEL+"/"+"{id}"+"/"+"{endpoint}")
    Observable<JsonElement> update_group_channel_reactions_settings(@Path("id") int id,
                                                            @Path("endpoint") String endPoint,
                                                            @Header ("Authorization") String api_token,
                                                            @Header("DeviceType") String device_type,
                                                            @Header("DeviceToken") String device_token,
                                                            @FieldMap Map<String, Object> params);


    @Multipart
    @POST(ApiUrls.URL_UPLOAD_FILE)
    Observable<JsonElement> attachFile(
            @QueryMap Map<String, Object> params,
            @Part List<MultipartBody.Part> files
    );






    @GET(ApiUrls.URL_GROUP_CHANNEL+"/"+"{id}"+"/"+ApiUrls.URL_GROUP_GET_DUMMY_USER)
    Observable<JsonElement> get_group_dummy_user_list(@Path("id") int id,
                                                                     @Header ("Authorization") String api_token,
                                                                     @Header("DeviceType") String device_type,
                                                                     @Header("DeviceToken") String device_token);




    @FormUrlEncoded
    @PATCH(ApiUrls.URL_GROUP_CHANNEL+"/"+"{id}"+"/"+ApiUrls.URL_GROUP_UPDATE_DUMMY_USER)
    Observable<JsonElement> update_group_dummy_user_list(@Path("id") int id,
                                                      @Header ("Authorization") String api_token,
                                                      @Header("DeviceType") String device_type,
                                                      @Header("DeviceToken") String device_token,
                                                      @FieldMap Map<String,Object> params);



    @GET(ApiUrls.URL_GROUP_CHANNEL+"/"+ApiUrls.URL_GET_EXCLUSIVE_OFFER)
    Observable<JsonElement> get_exclusive_offers(@Header ("Authorization") String api_token,
                                                 @Header("DeviceType") String device_type,
                                                 @Header("DeviceToken") String device_token,
                                                 //@Query("search") String searchQuery,
                                                 @Query("page") int page,
                                                 @Query("per_page") int per_page);







    @Multipart
    @POST(ApiUrls.UPDATE_PROFILE_PIC)
    Observable<JsonElement> update_profile_pic(
            @Header ("Authorization") String api_token,
            @Query("UserID") int userId,
            @Part MultipartBody.Part image
    );






    @GET(ApiUrls.URL_GROUP_CHANNEL+"/"+"{id}"+"/"+ApiUrls.URL_GET_GROUP_CHANNEL_MEMBER_PROFILE)
    Observable<JsonElement> get_group_channel_member_media(@Path("id") int id,
                                                         @Header ("Authorization") String api_token,
                                                         @Header("DeviceType") String device_type,
                                                         @Header("DeviceToken") String device_token,
                                                         @Query("gc_member_id") String memberID);

}




