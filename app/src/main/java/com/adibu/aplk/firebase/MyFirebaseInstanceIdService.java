package com.adibu.aplk.firebase;

import android.content.Context;
import android.util.Log;

import com.adibu.aplk.ApiUrl;
import com.adibu.aplk.AppSingleton;
import com.adibu.aplk.SessionManager;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.util.HashMap;
import java.util.Map;

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";
    private Context mContext;
    private SessionManager mSessionManager;

    public MyFirebaseInstanceIdService() {
    }

    public MyFirebaseInstanceIdService(Context context) {
        this.mContext = context;
        mSessionManager = new SessionManager(this.mContext);
    }

    @Override
    public void onTokenRefresh() {

        if( mSessionManager!=null && mSessionManager.isLoggedIn()) {
            //Getting registration token
            String refreshedToken = FirebaseInstanceId.getInstance().getToken();

            //Displaying token on logcat
            Log.d(TAG, "Refreshed token: " + refreshedToken);

            //calling the method store token and passing token
            storeToken(refreshedToken);
        }

    }

    private void storeToken(final String token) {

        //Store token on the app
        mSessionManager.setFirebaseDeviceToken(token);

        //Store token on the database
        final String TAG = "REG_DEVICE";
        String URL = ApiUrl.URL_REG_DEVICE;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.fillInStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nip", mSessionManager.getSessionNIP());
                params.put("token", token);
                return params;
            }
        };

        //Jalanin request yang udah dibuat
        AppSingleton.getInstance(mContext).addToRequestQueue(stringRequest, TAG);
    }


}
