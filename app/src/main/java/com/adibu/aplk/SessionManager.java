package com.adibu.aplk;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

/*
*
 */
public class SessionManager {

    private Context context;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private static final String PREF_NAME = "APLK_Pref";

    private static final String IS_LOGIN = "isLogin";

    public static final String KEY_NIP = "nip";
    public static final String KEY_NAMA = "nama";

    //Role
    public static final String KEY_KARYAWAN = "karyawan";
    public static final String KEY_PENGAWAS = "pengawas";
    public static final String KEY_ADMIN = "admin";

    public static final String KEY_FUNGSIONAL = "fungsional";
    public static final String KEY_PAMONG = "pamong";
    public static final String KEY_PROGRAM = "program";
    public static final String KEY_SIK = "sik";
    public static final String KEY_PSD = "psd";
    public static final String KEY_SUBBAG = "subbag";
    public static final String KEY_WIYATA = "wiyata";

    //FIREBASE
    public static final String KEY_FIREBASE_DEVICE_TOKEN = "firebase_device_token";


    public SessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void createSession(String nip, String nama, Boolean karyawan, Boolean pengawas, Boolean admin, Boolean fungsional, Boolean pamong, Boolean program, Boolean sik, Boolean psd, Boolean subbag, Boolean wiyata) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_NIP, nip);
        editor.putString(KEY_NAMA, nama);

        editor.putBoolean(KEY_KARYAWAN, karyawan);
        editor.putBoolean(KEY_PENGAWAS, pengawas);
        editor.putBoolean(KEY_ADMIN, admin);

        editor.putBoolean(KEY_FUNGSIONAL, fungsional);
        editor.putBoolean(KEY_PAMONG, pamong);
        editor.putBoolean(KEY_PROGRAM, program);
        editor.putBoolean(KEY_SIK, sik);
        editor.putBoolean(KEY_PSD, psd);
        editor.putBoolean(KEY_SUBBAG, subbag);
        editor.putBoolean(KEY_WIYATA, wiyata);
        editor.commit();
    }

    public HashMap getSession() {
        HashMap session = new HashMap();
        session.put(KEY_NIP, pref.getString(KEY_NIP, null));
        session.put(KEY_NAMA, pref.getString(KEY_NAMA, null));

        session.put(KEY_KARYAWAN, pref.getBoolean(KEY_KARYAWAN, false));
        session.put(KEY_PENGAWAS, pref.getBoolean(KEY_PENGAWAS, false));
        session.put(KEY_ADMIN, pref.getBoolean(KEY_ADMIN, false));

        session.put(KEY_FUNGSIONAL, pref.getBoolean(KEY_FUNGSIONAL, false));
        session.put(KEY_PAMONG, pref.getBoolean(KEY_PAMONG, false));
        session.put(KEY_PROGRAM, pref.getBoolean(KEY_PROGRAM, false));
        session.put(KEY_SIK, pref.getBoolean(KEY_SIK, false));
        session.put(KEY_PSD, pref.getBoolean(KEY_PSD, false));
        session.put(KEY_SUBBAG, pref.getBoolean(KEY_SUBBAG, false));
        session.put(KEY_WIYATA, pref.getBoolean(KEY_WIYATA, false));

        return session;
    }

    public String getSessionNIP() {
        return pref.getString(KEY_NIP, null);
    }

    public void setFirebaseDeviceToken(String token) {
        editor.putString(KEY_FIREBASE_DEVICE_TOKEN, token);
        editor.commit();
    }

    public String getFirebaseDeviceToken() {
        return pref.getString(KEY_FIREBASE_DEVICE_TOKEN, null);
    }

    public void checkLogin(){
        // Check login status
        if(!this.isLoggedIn()){
            startLoginActivity();
        }
    }

    public void clearSession() {
        //Clear All data from Shared Preferences
        editor.clear();
        editor.commit();

        //Starting Login Activity
        startLoginActivity();
    }

    public Boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }


    private void startLoginActivity() {
        //Redirect user to Login Activity
        Intent i = new Intent(context, LoginActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Starting Login Activity
        context.startActivity(i);
    }

}
