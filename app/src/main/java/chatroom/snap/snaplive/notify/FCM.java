package chatroom.snap.snaplive.notify;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by CodeSlu on 9/4/2018.
 */

public interface FCM {
    @Headers({

            "Content-Type: application/json",
            "Authorization:key=AAAAz8Y1Lg0:APA91bGZskHRFsV_Mlb8eKQcn2VJ0tfYIisAe1YEg3LxJlO9r60_WVGVC9V0nB6cB3iQ_93Shij1dYD3ippjhjbWjbi_12_DwkC6Op5TtqowlV3pic9G-tUrgh7VA9Dc4wfc9RCpr07R"
    })
    @POST("fcm/send")
    Call<FCMresp> send(@Body Sender body);
}
