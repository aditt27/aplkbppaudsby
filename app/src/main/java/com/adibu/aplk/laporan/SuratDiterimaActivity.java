package com.adibu.aplk.laporan;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adibu.aplk.ApiUrl;
import com.adibu.aplk.AppSingleton;
import com.adibu.aplk.R;
import com.adibu.aplk.SessionManager;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.treebo.internetavailabilitychecker.InternetAvailabilityChecker;
import com.treebo.internetavailabilitychecker.InternetConnectivityListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SuratDiterimaActivity extends AppCompatActivity implements InternetConnectivityListener {

    private ArrayList<SuratModel> mListSurat = new ArrayList<>();
    private SuratDiterimaRVAdapter mSuratDiterimaRVAdapter;
    private SwipeRefreshLayout mSwipeRefresh;
    private InternetAvailabilityChecker mInternetAvailabilityChecker;
    private Boolean internetConnected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_swiperecycleview);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mInternetAvailabilityChecker = InternetAvailabilityChecker.getInstance();
        mInternetAvailabilityChecker.addInternetConnectivityListener(this);

        RecyclerView recyclerView = findViewById(R.id.list_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        mSuratDiterimaRVAdapter = new SuratDiterimaRVAdapter(mListSurat);
        recyclerView.setAdapter(mSuratDiterimaRVAdapter);

        mSwipeRefresh = findViewById(R.id.list_swipe_refresh);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(internetConnected) {
                    getListSuratDiterima();
                } else {
                    Toast.makeText(SuratDiterimaActivity.this, getString(R.string.nointernet), Toast.LENGTH_SHORT).show();
                    mSwipeRefresh.setRefreshing(false);
                }
            }
        });

        //RefreshAnimation
        mSwipeRefresh.setRefreshing(true);
        //Ambil Data informasi dari DB
        getListSuratDiterima();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onInternetConnectivityChanged(boolean isConnected) {
        internetConnected = isConnected;
    }

    @Override
    protected void onDestroy() {
        mInternetAvailabilityChecker.removeInternetConnectivityChangeListener(this);
        super.onDestroy();
    }

    private void getListSuratDiterima() {

        SessionManager sm = new SessionManager(getApplicationContext());
        final String TAG = "READ_SURAT_DITERIMA";
        String URL = ApiUrl.URL_READ_SURAT_DITERIMA + sm.getSessionNIP();

        if(internetConnected) {
            AppSingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().invalidate(URL, false);
        }

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.e(TAG, response.toString());
                    //hapus isi informasi (untuk refresh)
                    mListSurat.clear();

                    //Ambil array dari json yg judul arraynya "info"
                    JSONArray jsonListSurat = response.getJSONArray("surat");

                    //masukin yg ada di jsonarray ke arraylist surat
                    for(int i=0;i<jsonListSurat.length();i++) {
                        mListSurat.add(new SuratModel(
                                jsonListSurat.getJSONObject(i).getInt("noPerintah"),
                                jsonListSurat.getJSONObject(i).getString("noSurat"),
                                jsonListSurat.getJSONObject(i).getString("waktu"),
                                jsonListSurat.getJSONObject(i).getInt("status"),
                                jsonListSurat.getJSONObject(i).getString("perihal"),
                                jsonListSurat.getJSONObject(i).getString("tempat"),
                                jsonListSurat.getJSONObject(i).getString("durasi"),
                                jsonListSurat.getJSONObject(i).getString("kategori")
                        ));
                    }

                    //update adapter setelah masukin ke arraylist informasi
                    mSuratDiterimaRVAdapter.notifyDataSetChanged();

                    //selesai swipe refresh
                    mSwipeRefresh.setRefreshing(false);

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

    private class SuratDiterimaRVAdapter extends RecyclerView.Adapter<SuratDiterimaRVAdapter.ViewHolder> {

        private ArrayList<SuratModel> listSurat;

        public SuratDiterimaRVAdapter(ArrayList<SuratModel> listSurat) {
            this.listSurat = listSurat;
        }

        @NonNull
        @Override
        public SuratDiterimaRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_laporan, parent, false);
            SuratDiterimaRVAdapter.ViewHolder viewHolder = new SuratDiterimaRVAdapter.ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull final SuratDiterimaRVAdapter.ViewHolder holder, int position) {
            holder.judul.setText(listSurat.get(position).getNoSurat());
            holder.kiri.setText(listSurat.get(position).getWaktu());
            //holder.kanan.setText(String.valueOf(listSurat.get(position).getStatus()));
            holder.kanan.setText(" ");
            holder.itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(SuratDiterimaActivity.this, DetailSuratActivity.class);
                    i.putExtra("noPerintah", listSurat.get(holder.getAdapterPosition()).getNoPerintah());
                    i.putExtra("noSurat", listSurat.get(holder.getAdapterPosition()).getNoSurat());
                    i.putExtra("waktu", listSurat.get(holder.getAdapterPosition()).getWaktu());
                    i.putExtra("status", listSurat.get(holder.getAdapterPosition()).getStatus());
                    i.putExtra("perihal", listSurat.get(holder.getAdapterPosition()).getPerihal());
                    i.putExtra("tempat", listSurat.get(holder.getAdapterPosition()).getTempat());
                    i.putExtra("durasi", listSurat.get(holder.getAdapterPosition()).getDurasi());
                    i.putExtra("kategori", listSurat.get(holder.getAdapterPosition()).getKategori());
                    startActivity(i);
                }
            });
        }

        @Override
        public int getItemCount() {
            return listSurat.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView judul;
            TextView kiri;
            TextView kanan;

            LinearLayout itemLayout;

            public ViewHolder(View itemView) {
                super(itemView);
                judul = itemView.findViewById(R.id.laporan_item_judul);
                kiri = itemView.findViewById(R.id.laporan_item_kiri);
                kanan = itemView.findViewById(R.id.laporan_item_kanan);
                itemLayout = itemView.findViewById(R.id.laporan_item_layout);
            }
        }
    }

}