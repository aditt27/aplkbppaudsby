package com.adibu.aplk.informasi;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InformasiDetailActivity extends AppCompatActivity {

    Intent mIntent;
    TextView mDibacaTV;
    CardView mDibacaCV;
    ListView mDibacaLV;
    ReadByAdapter mReadByAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informasi_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mIntent = getIntent();

        CardView namaCV = findViewById(R.id.detail_informasi_card_nama);
        TextView namaTV = findViewById(R.id.detail_informasi_nama);
        TextView isiTV = findViewById(R.id.detail_informasi_isi);
        ImageView fotoIV = findViewById(R.id.detail_informasi_foto);
        final ProgressBar fotoPB = findViewById(R.id.detail_informasi_foto_progress);
        TextView tanggalTV = findViewById(R.id.detail_informasi_tanggal);
        final TextView nullfoto = findViewById(R.id.detail_informasi_foto_null);

        mDibacaCV = findViewById(R.id.detail_informasi_card_dibaca);

        mDibacaLV = findViewById(R.id.detail_informasi_listview_dibaca);
        mReadByAdapter = new ReadByAdapter(this);
        mDibacaLV.setAdapter(mReadByAdapter);
        mDibacaLV.setEmptyView(findViewById(R.id.readby_emptyview));

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
            nullfoto.setTextColor(Color.BLACK);
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
                try {
                    JSONArray status = response.getJSONArray("status");
                    for(int i=0;i<status.length();i++) {
                        int no = status.getJSONObject(i).getInt("no_transaksi");
                        String nama = status.getJSONObject(i).getString("nama");
                        int stat = status.getJSONObject(i).getInt("status");
                        String waktu = status.getJSONObject(i).getString("waktu");
                        ReadBy readBy = new ReadBy(no, nama, stat, waktu);
                        if(stat==1) {
                            mReadByAdapter.add(new ReadBy(no, nama, stat, waktu));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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

    private void updateInfoTerbaca() {
        final String TAG = "UPDATE_INFO_TERBACA";
        String URL = ApiUrl.URL_UPDATE_INFO_TERBACA + mIntent.getStringExtra("no");

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

    private class ReadBy {
        private int noTransaksi;
        private String nama;
        private int status;
        private String waktu;

        public ReadBy(int noTransaksi, String nama, int status, String waktu) {
            this.noTransaksi = noTransaksi;
            this.nama = nama;
            this.status = status;
            this.waktu = waktu;
        }

        public int getNoTransaksi() {
            return noTransaksi;
        }

        public String getNama() {
            return nama;
        }

        public int getStatus() {
            return status;
        }

        public String getWaktu() {
            return waktu;
        }

        @Override
        public String toString() {
            return "ReadBy{" +
                    "noTransaksi=" + noTransaksi +
                    ", nama='" + nama + '\'' +
                    ", status=" + status +
                    ", waktu='" + waktu + '\'' +
                    '}';
        }
    }

    private class ReadByAdapter extends ArrayAdapter<ReadBy> {

        public ReadByAdapter(@NonNull Context context) {
            super(context, 0);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View listItemView = convertView;
            if(listItemView == null) {
                listItemView = LayoutInflater.from(getContext()).inflate(
                        R.layout.list_item, parent, false);
            }

            ReadBy currentItem = getItem(position);

            TextView nama = listItemView.findViewById(R.id.list_item_nama);
            TextView waktu = listItemView.findViewById(R.id.list_item_tanggal);
            View bar = listItemView.findViewById(R.id.detail_informasi_bar);
            bar.setVisibility(View.GONE);
            nama.setText(currentItem.getNama());
            waktu.setText(currentItem.getWaktu());
            return listItemView;
        }
    }
}
