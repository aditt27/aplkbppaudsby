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
import android.widget.TextView;
import android.widget.Toast;

import com.adibu.aplk.ApiUrl;
import com.adibu.aplk.AppSingleton;
import com.adibu.aplk.Helper;
import com.adibu.aplk.R;
import com.adibu.aplk.SessionManager;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LaporanSayaActivity extends AppCompatActivity {

    private ArrayList<LaporanSuratModel> mListLaporan = new ArrayList<>();
    private LaporanSayaRVAdapter mLaporanSayaRVAdapter;
    private SwipeRefreshLayout mSwipeRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_swiperecycleview);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecyclerView recyclerView = findViewById(R.id.list_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        mLaporanSayaRVAdapter = new LaporanSayaRVAdapter(mListLaporan);
        recyclerView.setAdapter(mLaporanSayaRVAdapter);

        mSwipeRefresh = findViewById(R.id.list_swipe_refresh);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(Helper.isInternetConnected(getApplicationContext())) {
                    getListLaporanSaya();
                } else {
                    Toast.makeText(LaporanSayaActivity.this, getString(R.string.nointernet), Toast.LENGTH_SHORT).show();
                    mSwipeRefresh.setRefreshing(false);
                }
            }
        });

        //RefreshAnimation
        mSwipeRefresh.setRefreshing(true);
        //Ambil data dari DB
        getListLaporanSaya();
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

    private void getListLaporanSaya() {

        SessionManager sm = new SessionManager(getApplicationContext());
        sm.checkLogin();
        final String TAG = "READ_LAPORAN_TERKIRIM";
        String URL = ApiUrl.URL_READ_LAPORAN_TERKIRIM + sm.getSessionNIP();

        if(Helper.isInternetConnected(getApplicationContext())) {
            AppSingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().invalidate(URL, true);
        }

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    //hapus isi informasi (untuk refresh)
                    mListLaporan.clear();

                    //Ambil array dari json yg judul arraynya "info"
                    JSONArray jsonListSurat = response.getJSONArray("laporan");

                    //masukin yg ada di jsonarray ke arraylist surat
                    for(int i=0;i<jsonListSurat.length();i++) {
                        mListLaporan.add(new LaporanSuratModel(
                                jsonListSurat.getJSONObject(i).getString("noSurat"),
                                jsonListSurat.getJSONObject(i).getString("waktu"),
                                jsonListSurat.getJSONObject(i).getString("perihal"),
                                jsonListSurat.getJSONObject(i).getString("tempat"),
                                jsonListSurat.getJSONObject(i).getString("durasi"),
                                jsonListSurat.getJSONObject(i).getString("kategori"),
                                jsonListSurat.getJSONObject(i).getInt("noLaporan"),
                                jsonListSurat.getJSONObject(i).getString("isi"),
                                jsonListSurat.getJSONObject(i).getString("pic1"),
                                jsonListSurat.getJSONObject(i).getString("pic2"),
                                jsonListSurat.getJSONObject(i).getString("pic3")
                        ));
                    }

                    //update adapter setelah masukin ke arraylist informasi
                    mLaporanSayaRVAdapter.notifyDataSetChanged();

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

    private class LaporanSayaRVAdapter extends RecyclerView.Adapter<LaporanSayaRVAdapter.ViewHolder> {

        private ArrayList<LaporanSuratModel> listLaporan;

        public LaporanSayaRVAdapter(ArrayList<LaporanSuratModel> listLaporan) {
            this.listLaporan = listLaporan;
        }

        @NonNull
        @Override
        public LaporanSayaRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_laporan, parent, false);
            LaporanSayaRVAdapter.ViewHolder viewHolder = new LaporanSayaRVAdapter.ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull final LaporanSayaRVAdapter.ViewHolder holder, int position) {
            holder.judul.setText(listLaporan.get(position).getNoSurat());
            holder.kiri.setText(listLaporan.get(position).getKategori());
            holder.kanan.setText(listLaporan.get(position).getWaktu());
            holder.itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(LaporanSayaActivity.this, LaporanDetailActivity.class);
                    i.putExtra("kategori", listLaporan.get(holder.getAdapterPosition()).getKategori());
                    i.putExtra("noSurat", listLaporan.get(holder.getAdapterPosition()).getNoSurat());
                    i.putExtra("waktu", listLaporan.get(holder.getAdapterPosition()).getWaktu());
                    i.putExtra("status", listLaporan.get(holder.getAdapterPosition()).getStatus());
                    i.putExtra("perihal", listLaporan.get(holder.getAdapterPosition()).getPerihal());
                    i.putExtra("tempat", listLaporan.get(holder.getAdapterPosition()).getTempat());
                    i.putExtra("durasi", listLaporan.get(holder.getAdapterPosition()).getDurasi());
                    i.putExtra("isi", listLaporan.get(holder.getAdapterPosition()).getIsi());
                    i.putExtra("pic1", listLaporan.get(holder.getAdapterPosition()).getPic1());
                    i.putExtra("pic2", listLaporan.get(holder.getAdapterPosition()).getPic2());
                    i.putExtra("pic3", listLaporan.get(holder.getAdapterPosition()).getPic3());
                    startActivity(i);
                }
            });
        }

        @Override
        public int getItemCount() {
            return listLaporan.size();
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
