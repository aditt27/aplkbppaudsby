package com.adibu.aplk;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class InformasiActivity extends AppCompatActivity {

    private ArrayList<InformasiModel> mListInformasi = new ArrayList<>();
    private InformasiRecyclerViewAdapter mInformasiRVAdapter;
    private SwipeRefreshLayout mSwipeRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informasi);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecyclerView recyclerView = findViewById(R.id.informasi_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        mInformasiRVAdapter = new InformasiRecyclerViewAdapter(mListInformasi);
        recyclerView.setAdapter(mInformasiRVAdapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(InformasiActivity.this, TambahInformasiActivity.class));
            }
        });

        mSwipeRefresh = findViewById(R.id.informasi_swipe_refresh);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getListInformasi();
            }
        });

        //RefreshAnimation
        mSwipeRefresh.setRefreshing(true);
        //Ambil Data informasi dari DB
        getListInformasi();
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

    private void getListInformasi() {

        String tag_get_listInformasi = "tag_get_listInformasi";
        String url = ApiUrl.URL_READ_MSGS;

        JsonObjectRequest jsonListInformasi = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    //hapus isi informasi (untuk refresh)
                    mListInformasi.clear();

                    //Ambil array dari json yg judul arraynya "msgs"
                    JSONArray jsonListInformasi = response.getJSONArray("infos");

                    //masukin yg ada di jsonarray ke arraylist informasi
                    for(int i=0;i<jsonListInformasi.length();i++) {
                        String nama = jsonListInformasi.getJSONObject(i).getString("nama");
                        String waktu = jsonListInformasi.getJSONObject(i).getString("waktu");
                        String isi = jsonListInformasi.getJSONObject(i).getString("isi");
                        //masukin ke arraylistnya descending order, masukin ke index 0 terus tiap item
                        mListInformasi.add(0, new InformasiModel(nama, waktu, isi));
                    }

                    //update adapter setelah masukin ke arraylist informasi
                    mInformasiRVAdapter.notifyDataSetChanged();

                    //selesai swipe refresh
                    mSwipeRefresh.setRefreshing(false);

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
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonListInformasi, tag_get_listInformasi);
    }

    private class InformasiModel {
        /*
        Data model untuk bagian informasi
        */

        private String nama;
        private String waktu;
        private String isi;

        private InformasiModel(String nama, String waktu, String isi) {
            this.nama = nama;
            this.waktu = waktu;
            this.isi = isi;
        }

        public String getNama() {
            return nama;
        }

        public String getWaktu() {
            return waktu;
        }

        public String getIsi() {
            return isi;
        }

    }

    public class InformasiRecyclerViewAdapter extends RecyclerView.Adapter<InformasiRecyclerViewAdapter.ViewHolder>{

        private ArrayList<InformasiModel> mListInformasi;

        public InformasiRecyclerViewAdapter(ArrayList<InformasiModel> mListInformasi) {
            this.mListInformasi = mListInformasi;
        }

        @NonNull
        @Override
        public InformasiRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.informasi_item, parent, false);
            InformasiRecyclerViewAdapter.ViewHolder viewHolder = new InformasiRecyclerViewAdapter.ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull InformasiRecyclerViewAdapter.ViewHolder holder, int position) {
            holder.nama.setText(mListInformasi.get(position).getNama());
            holder.tanggal.setText(mListInformasi.get(position).getWaktu());
            holder.isi.setText(mListInformasi.get(position).getIsi());
        }

        @Override
        public int getItemCount() {
            return mListInformasi.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView nama;
            TextView tanggal;
            TextView isi;

            RelativeLayout itemLayout;

            public ViewHolder(View itemView) {
                super(itemView);
                nama = itemView.findViewById(R.id.informasi_item_nama);
                tanggal = itemView.findViewById(R.id.informasi_item_tanggal);
                isi = itemView.findViewById(R.id.informasi_item_isi);
                itemLayout = itemView.findViewById(R.id.informasi_item);
            }
        }
    }
}
