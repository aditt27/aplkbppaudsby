package com.adibu.aplk;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class AccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        TextView namaTV = findViewById(R.id.akun_nama);
        TextView nipTV = findViewById(R.id.akun_nip);
        TextView roleTV = findViewById(R.id.akun_role);
        //Button testNotif = findViewById(R.id.test_notifikasi);
        LinearLayout gantiPassword = findViewById(R.id.akun_ganti_password);

        /*testNotif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyNotificationManager notifMgr = new MyNotificationManager(getApplicationContext());
                notifMgr.showInformasiNotification("title", "message");
            }
        });*/

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SessionManager sm = new SessionManager(getApplicationContext());
        HashMap session = sm.getSession();

        namaTV.setText((String) session.get(SessionManager.KEY_NAMA));
        nipTV.setText((String) session.get(SessionManager.KEY_NIP));

        String role = "";
        role = role + ((Boolean)session.get(SessionManager.KEY_ADMIN)? getString(R.string.admin) + ", ":"");
        role = role + ((Boolean)session.get(SessionManager.KEY_PENGAWAS)? getString(R.string.pengawas) + ", ":"");
        role = role + ((Boolean)session.get(SessionManager.KEY_KARYAWAN)? getString(R.string.karyawan) + ", ":"");
        role = role + ((Boolean)session.get(SessionManager.KEY_FUNGSIONAL)? getString(R.string.fungsional) + ", ":"");
        role = role + ((Boolean)session.get(SessionManager.KEY_PAMONG)? getString(R.string.pamong) + ", ":"");
        role = role + ((Boolean)session.get(SessionManager.KEY_PROGRAM)? getString(R.string.program) + ", ":"");
        role = role + ((Boolean)session.get(SessionManager.KEY_SIK)? getString(R.string.sik) + ", ":"");
        role = role + ((Boolean)session.get(SessionManager.KEY_PSD)? getString(R.string.psd) + ", ":"");
        role = role + ((Boolean)session.get(SessionManager.KEY_SUBBAG)? getString(R.string.subbag) + ", ":"");
        role = role + ((Boolean)session.get(SessionManager.KEY_WIYATA)? getString(R.string.wiyata) + ", ":"");
        role = role.substring(0, role.length()-2);

        roleTV.setText(role);

        gantiPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AccountActivity.this);
                dialogBuilder.setTitle(R.string.gantipassword);
                dialogBuilder.setView(R.layout.activity_account_ganti_password_dialog);
                dialogBuilder.setNegativeButton(R.string.cancel, null);
                dialogBuilder.setPositiveButton(R.string.ganti, null);

                final AlertDialog dialog = dialogBuilder.create();

                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(final DialogInterface dialogInterface) {

                        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                        positiveButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Dialog d = (Dialog) dialogInterface;

                                EditText passLamaET = d.findViewById(R.id.akun_ganti_password_current_pass);
                                EditText passBaruET = d.findViewById(R.id.akun_ganti_password_pass_baru);
                                EditText retypePassBaruET = d.findViewById(R.id.akun_ganti_password_retype_pass_baru);
                                TextView errorTV = d.findViewById(R.id.akun_ganti_password_tv);

                                String passLama = passLamaET.getText().toString();
                                String passBaru = passBaruET.getText().toString();
                                String retypePassBaru = retypePassBaruET.getText().toString();

                                if(passLama.isEmpty()) {
                                    passLamaET.setError("Harus Diisi");
                                }
                                if(passBaru.isEmpty()) {
                                    passBaruET.setError("Harus Diisi");
                                }
                                if(retypePassBaru.isEmpty()) {
                                    retypePassBaruET.setError("Harus Diisi");
                                }

                                if(!passBaru.isEmpty() && !passLama.isEmpty() && !retypePassBaru.isEmpty()) {
                                    gantiPass(passLama, passBaru, retypePassBaru);
                                    d.dismiss();
                                }
                            }
                        });
                    }
                });
                dialog.show();

            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void gantiPass (final String passLama, String passBaru, String retypePassBaru) {

        SessionManager sm = new SessionManager(getApplicationContext());
        final String TAG = "READ_USER";
        String URL = ApiUrl.URL_READ_USER + sm.getSessionNIP();

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    JSONArray jsonUser = response.getJSONArray("users");
                    if(jsonUser.length()>0) {
                        String pass = jsonUser.getJSONObject(0).getString("password");
                        if(Helper.stringToSHA256(passLama).equals(pass)) {

                        } else {

                        }
                    }
                } catch (JSONException e) {
                    e.fillInStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        //Jalanin request yang udah dibuat
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(objectRequest, TAG);
    }

}
