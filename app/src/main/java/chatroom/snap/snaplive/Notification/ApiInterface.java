package chatroom.snap.snaplive.Notification;

import chatroom.snap.snaplive.global.Global;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiInterface {

    @Headers({"Authorization: key=" + Global.Server_Key, "Content-Type:application/json"})
    @POST("fcm/send")
    Call<ResponseBody> sendAndroidMessageNotification(@Body RootModel_Android<Message> root);
    @Headers({"Authorization: key=" + Global.Server_Key, "Content-Type:application/json"})
    @POST("fcm/send")
    Call<ResponseBody> sendAndroidMessageGroupNotification(@Body RootModel_Android<Message> root);
    @Headers({"Authorization: key=" + Global.Server_Key, "Content-Type:application/json"})
    @POST("fcm/send")
    Call<ResponseBody> sendAndroidAddGroupNotification(@Body RootModel_Android<AddGroup> root);
    @Headers({"Authorization: key=" + Global.Server_Key, "Content-Type:application/json"})
    @POST("fcm/send")
    Call<ResponseBody> sendAndroidDeleteMessageNotification(@Body RootModel_Android<DeleteMessage> root);
    @Headers({"Authorization: key=" + Global.Server_Key, "Content-Type:application/json"})
    @POST("fcm/send")
    Call<ResponseBody> sendAndroidDeleteGroupNotification(@Body RootModel_Android<DeleteMessage> root);
    @Headers({"Authorization: key=" + Global.Server_Key, "Content-Type:application/json"})
    @POST("fcm/send")
    Call<ResponseBody> sendAndroidVoiceCallNotification(@Body RootModel_Android<CallNotification> root);
    @Headers({"Authorization: key=" + Global.Server_Key, "Content-Type:application/json"})
    @POST("fcm/send")
    Call<ResponseBody> sendAndroidVideoCallNotification(@Body RootModel_Android<CallNotification> root);
    @Headers({"Authorization: key=" + Global.Server_Key, "Content-Type:application/json"})
    @POST("fcm/send")
    Call<ResponseBody> sendiOSMessageNotification(@Body RootModel_iOS<Message> root);
    @Headers({"Authorization: key=" + Global.Server_Key, "Content-Type:application/json"})
    @POST("fcm/send")
    Call<ResponseBody> sendiOSMessageGroupNotification(@Body RootModel_iOS<Message> root);
    @Headers({"Authorization: key=" + Global.Server_Key, "Content-Type:application/json"})
    @POST("fcm/send")
    Call<ResponseBody> sendiOSAddGroupNotification(@Body RootModel_iOS<AddGroup> root);
    @Headers({"Authorization: key=" + Global.Server_Key, "Content-Type:application/json"})
    @POST("fcm/send")
    Call<ResponseBody> sendiOSDeleteMessageNotification(@Body RootModel_iOS<DeleteMessage> root);
    @Headers({"Authorization: key=" + Global.Server_Key, "Content-Type:application/json"})
    @POST("fcm/send")
    Call<ResponseBody> sendiOSDeleteGroupNotification(@Body RootModel_iOS<DeleteMessage> root);
    @Headers({"Authorization: key=" + Global.Server_Key, "Content-Type:application/json"})
    @POST("fcm/send")
    Call<ResponseBody> sendiOSVoiceCallNotification(@Body RootModel_iOS<CallNotification> root);
    @Headers({"Authorization: key=" + Global.Server_Key, "Content-Type:application/json"})
    @POST("fcm/send")
    Call<ResponseBody> sendiOSVideoCallNotification(@Body RootModel_iOS<CallNotification> root);


}

