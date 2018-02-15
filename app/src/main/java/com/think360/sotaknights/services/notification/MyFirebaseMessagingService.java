package com.think360.sotaknights.services.notification;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.think360.sotaknights.R;
import com.think360.sotaknights.activities.SotaKnightActivity;
import com.think360.sotaknights.api.AppController;
import com.think360.sotaknights.api.EventToRefresh;
import com.think360.sotaknights.util.NotificationUtils;
import org.json.JSONException;
import org.json.JSONObject;



/**
 * Created by Ravi Tamada on 08/08/16.
 * www.androidhive.info
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    private NotificationUtils notificationUtils;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage == null)
            return;


        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());

            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                handleDataMessage(json);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }



    private void handleDataMessage(JSONObject json) {
        Log.e(TAG, "push json: " + json.toString());

        try {
            String id = json.getString("doctor_id");
            String message = json.getString("alert");

if(AppController.getSharedPref().getBoolean("isLogin", false)){
    Intent resultIntent = new Intent(getApplicationContext(), SotaKnightActivity.class);
    resultIntent.putExtra("getTaskList",2);
    String timestamp = "";
    showNotificationMessage(getApplicationContext(), "", message, timestamp, resultIntent);

}/*else{
    Toast.makeText(getApplicationContext(),"Please Login to see Task",Toast.LENGTH_SHORT).show();
}
*/
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }

}
