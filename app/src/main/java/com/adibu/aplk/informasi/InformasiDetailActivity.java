package com.adibu.aplk.informasi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.adibu.aplk.ApiUrl;
import com.adibu.aplk.AppSingleton;
import com.adibu.aplk.R;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class InformasiDetailActivity extends AppCompatActivity {

    Intent mIntent;
    TextView mDibacaTV;
    CardView mDibacaCV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informasi_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mIntent = getIntent();
        Log.d("INFO_DETAIL", "NO_INFO: " + String.valueOf(mIntent.getStringExtra("no")));

        CardView namaCV = findViewById(R.id.detail_informasi_card_nama);
        TextView namaTV = findViewById(R.id.detail_informasi_nama);
        TextView isiTV = findViewById(R.id.detail_informasi_isi);
        ImageView fotoIV = findViewById(R.id.detail_informasi_foto);
        final ProgressBar fotoPB = findViewById(R.id.detail_informasi_foto_progress);
        TextView tanggalTV = findViewById(R.id.detail_informasi_tanggal);
        final TextView nullfoto = findViewById(R.id.detail_informasi_foto_null);
        mDibacaTV = findViewById(R.id.detail_informasi_dibaca);
        mDibacaCV = findViewById(R.id.detail_informasi_card_dibaca);

        String nama = mIntent.getStringExtra("nama");
        if(nama==null) {
            namaCV.setVisibility(View.GONE);
        } else {
            namaTV.setText(nama);
        }

        isiTV.setText(mIntent.getStringExtra("isi"));
        tanggalTV.setText(mIntent.getStringExtra("waktu"));

        String gambar = mIntent.getStringExtra("gambar");
        if(!gambar.isEmpty()) {
            nullfoto.setVisibility(View.GONE);
            //Load picture from url
            Picasso.with(this)
                    .load(gambar)
                    .into(fotoIV, new Callback() {
                        @Override
                        public void onSuccess() {
                            fotoPB.setVisibility(View.GONE);
                        }
                        @Override
                        public void onError() {
                            fotoPB.setVisibility(View.GONE);
                            nullfoto.setVisibility(View.VISIBLE);
                            nullfoto.setText(R.string.errorfoto);
                        }
                    });
        } else {
            nullfoto.setText(R.string.nullfoto);
            fotoPB.setVisibility(View.GONE);
        }


        String dari = mIntent.getStringExtra("dari");
        if(dari.equals("terkirim")) {
            readInfoTerbaca();
        } else  {
            mDibacaCV.setVisibility(View.GONE);
            updateInfoTerbaca();
        }
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

    private void readInfoTerbaca() {
        String TAG = "READ_INFO_TERBACA";
        String URL = ApiUrl.URL_READ_INFO_TERBACA + mIntent.getStringExtra("no");

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                mDibacaTV.setText(response.toString());
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

    private void updateInfoTerbaca() {
        final String TAG = "UPDATE_INFO_TERBACA";
        String URL = ApiUrl.URL_UPDATE_INFO_TERBACA + String.valueOf( mIntent.getStringExtra("no"));

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("status", "1");
                return params;
            }
        };

        //Jalanin request yang udah dibuat
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest, TAG);

    }
}
