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
                    mInputNip.setError("Required");
                }
                if(password.isEmpty()) {
                    mInputPassword.setError("Required");
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
        pDialog.setMessage("Loading...");
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

                            Boolean karyawan = false;
                            Boolean pengawas = false;
                            Boolean admin = false;
                            if(jsonUser.getJSONObject(0).getInt("karyawan") == 1) {
                                karyawan = true;
                            }
                            if(jsonUser.getJSONObject(0).getInt("pengawas") == 1) {
                                pengawas = true;
                            }
                            if(jsonUser.getJSONObject(0).getInt("admin") ==1) {
                                admin = true;
                            }

                            SessionManager sessionManager = new SessionManager(LoginActivity.this);
                            sessionManager.createSession(nip, nama, karyawan, pengawas, admin);
                            finish();
                        } else {
                            if (pDialog.isShowing()) {
                                pDialog.dismiss();
                            }
                            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                            builder.setMessage(R.string.salahpassword);
                            builder.setPositiveButton(R.string.ok, null);
                            builder.setTitle(R.string.error);
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
                        builder.setTitle(R.string.error);
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
        AppSingleton.getInstance(this).addToRequestQueue(jsonGetUserData, tag_sign_in);
    }
}
