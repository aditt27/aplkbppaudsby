package com.adibu.aplk;

import android.app.AlertDialog;
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
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ChangePasswordActivity extends AppCompatActivity {

    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pDialog = new ProgressDialog(this);

        final EditText passLama = findViewById(R.id.input_passLama);
        final EditText passBaru = findViewById(R.id.input_passbaru);
        final EditText passBaruUlang = findViewById(R.id.input_passbaruulang);
        Button buttonGantiPassword = findViewById(R.id.btn_gantipassword);

        pDialog.setMessage("Proses...");

        buttonGantiPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String passwordlama = passLama.getText().toString();
                String passwordbaru = passBaru.getText().toString();
                String passwordbaruulang = passBaruUlang.getText().toString();

                if(passwordlama.isEmpty()) {
                    passLama.setError("Wajib Diisi");
                }
                if(passwordbaru.isEmpty()) {
                    passBaru.setError("Wajib Diisi");
                }
                if(passwordbaruulang.isEmpty()) {
                    passBaruUlang.setError("Wajib Diisi");
                }

                if(!passwordlama.isEmpty() && !passwordbaru.isEmpty() && !passwordbaruulang.isEmpty() && Helper.isInternetConnected(getApplicationContext())) {
                    pDialog.show();
                    verifyPassword(passwordlama, passwordbaru, passwordbaruulang);
                }
                else if(!passwordlama.isEmpty() && !passwordbaru.isEmpty() && !passwordbaruulang.isEmpty()) {
                    Toast.makeText(ChangePasswordActivity.this, getString(R.string.nointernet), Toast.LENGTH_SHORT).show();
                }


            }
        });

    }
    private void verifyPassword(final String passLama, final String passBaru, final String passBaruUlang) {
        SessionManager sm = new SessionManager(getApplicationContext());
        sm.checkLogin();
        final String TAG = "VERIFY_USER";
        String URL = ApiUrl.URL_READ_USER + sm.getSessionNIP();

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    JSONArray jsonUser = response.getJSONArray("users");
                    if (jsonUser.length() > 0) {
                        String pass = jsonUser.getJSONObject(0).getString("password");
                        if (Helper.stringToSHA256(passLama).toLowerCase().equals(pass.toLowerCase())) {
                            if(passBaru.equals(passBaruUlang)) {
                                changePassword(passBaru);
                            } else {
                                pDialog.dismiss();
                                AlertDialog.Builder builder = new AlertDialog.Builder(ChangePasswordActivity.this);
                                builder.setMessage("Password baru tidak sesuai");
                                builder.setPositiveButton(R.string.ok, null);
                                builder.show();
                            }
                        } else {
                            pDialog.dismiss();
                            AlertDialog.Builder builder = new AlertDialog.Builder(ChangePasswordActivity.this);
                            builder.setMessage("Password lama salah");
                            builder.setPositiveButton(R.string.ok, null);
                            builder.show();
                        }
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
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(objectRequest, TAG);

    }

    private void changePassword(final String passBaru) {

        SessionManager sm = new SessionManager(getApplicationContext());
        sm.checkLogin();
        final String TAG = "GANTI_PASSWORD";
        String URL = ApiUrl.URL_UPDATE_USER_PASS+sm.getSessionNIP();
        Log.d(TAG,URL);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);
                pDialog.dismiss();
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.fillInStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("pass", passBaru);
                return params;
            }
        };

        //Jalanin request yang udah dibuat
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest, TAG);

    }
}
