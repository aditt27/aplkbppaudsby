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

    private static final String KEY_NIP = "nip";
    private static final String KEY_NAMA = "nama";
    private static final String KEY_KARYAWAN = "karyawan";
    private static final String KEY_PENGAWAS = "pengawas";
    private static final String KEY_ADMIN = "admin";

    //
    public SessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void createSession(String nip, String nama, Boolean karyawan, Boolean pengawas, Boolean admin) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_NIP, nip);
        editor.putString(KEY_NAMA, nama);
        editor.putBoolean(KEY_KARYAWAN, karyawan);
        editor.putBoolean(KEY_PENGAWAS, pengawas);
        editor.putBoolean(KEY_ADMIN, admin);
        editor.commit();
    }

    public HashMap getSession() {
        HashMap session = new HashMap();
        session.put(KEY_NIP, pref.getString(KEY_NIP, null));
        session.put(KEY_NAMA, pref.getString(KEY_NAMA, null));
        session.put(KEY_KARYAWAN, pref.getBoolean(KEY_KARYAWAN, false));
        session.put(KEY_PENGAWAS, pref.getBoolean(KEY_PENGAWAS, false));
        session.put(KEY_ADMIN, pref.getBoolean(KEY_ADMIN, false));
        return session;
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