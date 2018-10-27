package com.adibu.aplk;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.adibu.aplk.informasi.InformasiDiterimaActivity;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class MyNotificationManager {

    private static final String TAG = "MyNotificationMgr";

    public static final String INFORMASI_CHANNEL = "INFORMASI";
    public static final String LAPORAN_CHANNEL = "LAPORAN";
    public static final String GENERAL_CHANNEL = "GENERAL";

    private Context mContext;
    private NotificationManager mNotificationManager;
    private SessionManager mSessionManager;

    public MyNotificationManager(Context mContext) {
        this.mContext = mContext;
        mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        mSessionManager = new SessionManager(mContext);
        createNotificationChannel();
    }

    public void showBigNotification(String title, String message, String url) {
        Intent intent = new Intent(mContext, MainActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                mContext,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(mContext, INFORMASI_CHANNEL)
                .setSmallIcon(android.R.drawable.ic_menu_add)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(getBitmapFromURL(url)))
                .setAutoCancel(true)
                .build();

        int id = mSessionManager.getNotificationId();
        mNotificationManager.notify(id, notification);
    }

    public void showGeneralNotification(String title, String message) {
        Intent intent = new Intent(mContext, MainActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                mContext,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(mContext, GENERAL_CHANNEL)
                .setSmallIcon(android.R.drawable.ic_menu_add)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setChannelId(GENERAL_CHANNEL)
                .build();

        int id = mSessionManager.getNotificationId();
        mNotificationManager.notify(id, notification);
    }

    public void showInformasiNotification(String title, String message) {

        Intent intent = new Intent(mContext, InformasiDiterimaActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                mContext,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(mContext, INFORMASI_CHANNEL)
                .setSmallIcon(android.R.drawable.ic_menu_add)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setChannelId(INFORMASI_CHANNEL)
                .setGroup(INFORMASI_CHANNEL)
                .build();

        int id = mSessionManager.getNotificationId();
        mNotificationManager.notify(id, notification);
        informasiSummaryNotificatiton();
    }

    private void informasiSummaryNotificatiton() {
        //use constant ID for notification used as group summary
        int SUMMARY_ID = 0;

        Notification summaryNotification =
                new NotificationCompat.Builder(mContext, INFORMASI_CHANNEL)
                        .setContentTitle("Informasi Baru")
                        //set content text to support devices running API level < 24
                        .setContentText("Informasi Baru")
                        .setSmallIcon(android.R.drawable.ic_menu_add)
                        //build summary info into InboxStyle template
                        .setStyle(new NotificationCompat.InboxStyle()
                                .setSummaryText("Informasi Baru"))
                        //specify which group this notification belongs to
                        .setGroup(INFORMASI_CHANNEL)
                        //set this notification as the summary for the group
                        .setGroupSummary(true)
                        .build();

        mNotificationManager.notify(SUMMARY_ID, summaryNotification);
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //Create channel
            NotificationChannel channel1 = new NotificationChannel(INFORMASI_CHANNEL, mContext.getString(R.string.informasi), NotificationManager.IMPORTANCE_DEFAULT);
            channel1.enableLights(true);
            channel1.setVibrationPattern(new long[]{600, 250, 250});

            NotificationChannel channel2 = new NotificationChannel(LAPORAN_CHANNEL, mContext.getString(R.string.laporan), NotificationManager.IMPORTANCE_DEFAULT);
            channel2.enableLights(true);
            channel2.setVibrationPattern(new long[]{600, 250, 250});

            NotificationChannel channel3 = new NotificationChannel(GENERAL_CHANNEL, mContext.getString(R.string.general), NotificationManager.IMPORTANCE_DEFAULT);
            channel3.enableLights(true);
            channel3.setVibrationPattern(new long[]{600, 600, 600});

            //Assign channel
            mNotificationManager.createNotificationChannel(channel1);
            mNotificationManager.createNotificationChannel(channel2);
            mNotificationManager.createNotificationChannel(channel3);
        }
    }

    private Bitmap getBitmapFromURL(String strURL) {
        try {
            return Picasso.with(mContext).load(strURL).get();
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
        }
        return null;
    }
}
