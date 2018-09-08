package com.adibu.aplk;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.text.Html;
import android.util.Log;

import com.adibu.aplk.informasi.InformasiDiterimaActivity;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MyNotificationManager {

    private static final String TAG = "MyNotificationMgr";

    public static final String INFORMASI_CHANNEL = "INFORMASI";
    public static final String LAPORAN_CHANNEL = "LAPORAN";

    private Context mContext;
    private NotificationManager mNotificationManager;

    public MyNotificationManager(Context mContext) {
        this.mContext = mContext;
        mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        createNotificationChannel();
    }

    public void showBigNotification(String title, String message, String url) {
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
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(getBitmapFromURL(url)))
                .setAutoCancel(true)
                .build();

        mNotificationManager.notify(0, notification);
    }

    public void showSmallNotification(String title, String message) {

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
                .build();

        mNotificationManager.notify(0, notification);

    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //Create channel
            NotificationChannel channel1 = new NotificationChannel(INFORMASI_CHANNEL, mContext.getString(R.string.informasi), NotificationManager.IMPORTANCE_DEFAULT);
            channel1.enableLights(true);
            channel1.setVibrationPattern(new long[]{0, 1000, 500, 500});

            NotificationChannel channel2 = new NotificationChannel(LAPORAN_CHANNEL, mContext.getString(R.string.laporan), NotificationManager.IMPORTANCE_DEFAULT);
            channel2.enableLights(true);
            channel2.setVibrationPattern(new long[]{0, 1000, 500, 500});

            //Assign channel
            mNotificationManager.createNotificationChannel(channel1);
            mNotificationManager.createNotificationChannel(channel2);
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
