package com.adibu.aplk.informasi;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.adibu.aplk.ApiUrl;
import com.adibu.aplk.AppSingleton;
import com.adibu.aplk.Helper;
import com.adibu.aplk.R;
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
import java.util.Map;

public class InformasiDetailActivity extends AppCompatActivity {

    Intent mIntent;
    CardView mDibacaCV;
    ListView mDibacaLV;
    ReadByAdapter mReadByAdapter;
    ProgressBar mReadByProgressBar;
    TextView mReadByEmpty;
    String dariActivity;

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

        mReadByProgressBar = findViewById(R.id.readby_progressbar);
        mReadByEmpty = findViewById(R.id.readby_empty);
        mReadByEmpty.setVisibility(View.GONE);

        mDibacaCV = findViewById(R.id.detail_informasi_card_dibaca);
        mDibacaLV = findViewById(R.id.detail_informasi_listview_dibaca);

        mReadByAdapter = new ReadByAdapter(this);
        mDibacaLV.setAdapter(mReadByAdapter);

        mDibacaLV.setVisibility(View.GONE);

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

        dariActivity = mIntent.getStringExtra("dari");
        if(dariActivity.equals("terkirim")) {
            readInfoTerbaca();
        } else  {
            mDibacaCV.setVisibility(View.GONE);
            updateInfoTerbaca();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_delete, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.menu_hapus:
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(InformasiDetailActivity.this);
                dialogBuilder.setMessage(R.string.deleteinfoconfirm);
                dialogBuilder.setTitle(R.string.hapusinfo);
                dialogBuilder.setNegativeButton(R.string.cancel, null);
                dialogBuilder.setPositiveButton(R.string.hapus, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(Helper.isInternetConnected(getApplicationContext())) {
                            if(dariActivity.equals("terkirim")) {
                                deleteInfoTerkirim();
                            } else {
                                deleteInfoDiterima();
                            }
                            finish();
                        } else {
                            Toast.makeText(InformasiDetailActivity.this, getString(R.string.nointernet), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                AlertDialog dialog = dialogBuilder.create();
                dialog.show();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteInfoDiterima() {
        final String TAG = "DELETE_INFO_DITERIMA";
        String URL = ApiUrl.URL_DELETE_INFO_DITERIMA +mIntent.getStringExtra("no");

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
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

    private void deleteInfoTerkirim() {
        final String TAG = "DELETE_INFO_TERKIRIM";
        String URL = ApiUrl.URL_DELETE_INFO_TERKIRIM +mIntent.getStringExtra("no");

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
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

    private void readInfoTerbaca() {
        final String TAG = "READ_INFO_TERBACA";
        String URL = ApiUrl.URL_READ_INFO_TERBACA + mIntent.getStringExtra("no");

        if(Helper.isInternetConnected(getApplicationContext())) {
            AppSingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().invalidate(URL, true);
        }

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

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
                    mReadByProgressBar.setVisibility(View.GONE);
                    if(mReadByAdapter.isEmpty()) {
                        mReadByEmpty.setVisibility(View.VISIBLE);
                    } else {
                        mDibacaLV.setVisibility(View.VISIBLE);
                    }
                    Helper.justifyListViewHeightBasedOnChildren(mDibacaLV);

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
                Log.d(TAG, response);
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
                        R.layout.item_informasi, parent, false);
            }

            ReadBy currentItem = getItem(position);

            TextView nama = listItemView.findViewById(R.id.informasi_item_nama);
            TextView waktu = listItemView.findViewById(R.id.informasi_item_tanggal);
            View bar = listItemView.findViewById(R.id.detail_informasi_bar);
            bar.setVisibility(View.GONE);
            nama.setText(currentItem.getNama());
            waktu.setText(currentItem.getWaktu());
            return listItemView;
        }
    }
}
