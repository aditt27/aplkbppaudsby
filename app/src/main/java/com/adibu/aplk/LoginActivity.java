package com.adibu.aplk;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.adibu.aplk.firebase.MyFirebaseInstanceIdService;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    EditText mInputNip;
    EditText mInputPassword;
    Button mButtonSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mInputNip = findViewById(R.id.input_nip);
        mInputPassword = findViewById(R.id.input_password);
        mButtonSignIn = findViewById(R.id.btn_login);

        mButtonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nip = mInputNip.getText().toString().trim();
                String password = mInputPassword.getText().toString().trim();

                if(nip.isEmpty()) {
                    mInputNip.setError("Wajib diisi");
                }
                if(password.isEmpty()) {
                    mInputPassword.setError("Wajib diisi");
                }
                if(!nip.isEmpty() && !password.isEmpty()) {
                    verifySignIn(nip, password);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    private void verifySignIn(final String nip, final String password) {

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Login...");
        pDialog.show();

        final String tag_sign_in = "tag_sign_in";
        String url = ApiUrl.URL_READ_USER+nip;

        final JsonObjectRequest jsonGetUserData = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonUser = response.getJSONArray("users");
                    if(jsonUser.length()>0) {
                        String pass = jsonUser.getJSONObject(0).getString("password");
                        if(password.equals(pass)) {
                            String nama = jsonUser.getJSONObject(0).getString("nama");

                            Boolean karyawan = jsonUser.getJSONObject(0).getInt("karyawan") == 1;
                            Boolean pengawas = jsonUser.getJSONObject(0).getInt("pengawas") == 1;
                            Boolean admin = jsonUser.getJSONObject(0).getInt("admin") == 1;
                            Boolean fungsional = jsonUser.getJSONObject(0).getInt("fungsional") == 1;
                            Boolean pamong = jsonUser.getJSONObject(0).getInt("pamong") == 1;
                            Boolean program = jsonUser.getJSONObject(0).getInt("program") == 1;
                            Boolean sik = jsonUser.getJSONObject(0).getInt("sik") == 1;
                            Boolean psd = jsonUser.getJSONObject(0).getInt("psd") == 1;
                            Boolean subbag = jsonUser.getJSONObject(0).getInt("subbag") == 1;
                            Boolean wiyata = jsonUser.getJSONObject(0).getInt("wiyata") == 1;

                            SessionManager sessionManager = new SessionManager(getApplicationContext());
                            sessionManager.createSession(nip, nama, karyawan, pengawas, admin, fungsional, pamong, program, sik, psd, subbag, wiyata);

                            MyFirebaseInstanceIdService firebaseInstanceIdService = new MyFirebaseInstanceIdService(getApplicationContext());
                            firebaseInstanceIdService.onTokenRefresh();
                            if (pDialog.isShowing()) {
                                pDialog.dismiss();
                            }
                            finish();
                        } else {
                            if (pDialog.isShowing()) {
                                pDialog.dismiss();
                            }
                            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                            builder.setMessage(R.string.salahpassword);
                            builder.setPositiveButton(R.string.ok, null);
                            builder.show();
                        }
                    }
                    else {
                        if (pDialog.isShowing()) {
                            pDialog.dismiss();
                        }
                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                        builder.setMessage(R.string.salahnip);
                        builder.setPositiveButton(R.string.ok, null);
                        builder.show();
                    }

                } catch (JSONException e) {
                   e.fillInStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.fillInStackTrace();
            }
        });

        //Jalanin request yang udah dibuat
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonGetUserData, tag_sign_in);
    }
}
