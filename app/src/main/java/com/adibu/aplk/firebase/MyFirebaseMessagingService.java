package com.adibu.aplk.firebase;

import android.util.Log;

import com.adibu.aplk.MyNotificationManager;
import com.adibu.aplk.SessionManager;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Data Payload: " + remoteMessage.getData().toString());
            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());

                JSONObject data = json.getJSONObject("data");

                String title = data.getString("title");
                String message = data.getString("message");
                String imageUrl = data.getString("image");

                if(message.equals("NIP Anda login di device lain, mohon logout")) {
                    SessionManager sm = new SessionManager(getApplicationContext());
                    sm.clearSession();
                    sendPushNotification(title, message, imageUrl);

                    //TODO: check islogin onresume app, check islogin, app foreground saat ada notif logout
                } else {
                    sendPushNotification(title, message, imageUrl);
                }

            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }

    private void sendPushNotification(String title, String message, String imageUrl) {
        MyNotificationManager mNotificationManager = new MyNotificationManager(getApplicationContext());
        if(title.equals("Informasi Baru!")) {
            mNotificationManager.showInformasiNotification(title, message);
        } else {
            mNotificationManager.showGeneralNotification(title, message);
        }

        //if there is no image
        /*if(imageUrl.equals("null")){
            mNotificationManager.showInformasiNotification(title, message);
        }else{
            mNotificationManager.showBigNotification(title, message, imageUrl);
        }*/
    }

}

