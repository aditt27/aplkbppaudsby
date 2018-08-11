package com.adibu.aplk;

import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TambahInformasiActivity extends AppCompatActivity {

    private EditText mInputInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_informasi);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mInputInfo = findViewById(R.id.input_info);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_tambah_informasi, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_tambah_informasi_kirim:
                String info = mInputInfo.getText().toString().trim();
                if(!info.isEmpty()){
                    addInfo(info);
                }else {
                    mInputInfo.setError(getString(R.string.harus_diisi));
                }
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addInfo(final String info) {
        String tag_add_info = "tag_add_info";
        String url = ApiUrl.URL_CREATE_MSG;

        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.fillInStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                SessionManager sm = new SessionManager(TambahInformasiActivity.this);
                Map<String, String> params = new HashMap<>();
                params.put("nip", sm.getSessionNIP());
                params.put("isi", info);
                return params;
            }
        };

        //Jalanin request yang udah dibuat
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(sr, tag_add_info);
    }
}
