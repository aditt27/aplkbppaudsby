package com.adibu.aplk.informasi;

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
import android.widget.RelativeLayout;
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

public class InformasiDiterimaActivity extends AppCompatActivity {

    private ArrayList<InformasiModel> mListInformasi = new ArrayList<>();
    private InformasiDiterimaRVAdapter mInformasiDiterimaRVAdapter;
    private SwipeRefreshLayout mSwipeRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_swiperecycleview);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecyclerView recyclerView = findViewById(R.id.list_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        mInformasiDiterimaRVAdapter = new InformasiDiterimaRVAdapter(mListInformasi);
        recyclerView.setAdapter(mInformasiDiterimaRVAdapter);

        //Reverse Item (Newest one in the top)
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setReverseLayout(true);
        llm.setStackFromEnd(true);
        recyclerView.setLayoutManager(llm);

        mSwipeRefresh = findViewById(R.id.list_swipe_refresh);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(Helper.isInternetConnected(getApplicationContext())) {
                    getListInformasiDiterima();
                } else {
                    Toast.makeText(InformasiDiterimaActivity.this, getString(R.string.nointernet), Toast.LENGTH_SHORT).show();
                    mSwipeRefresh.setRefreshing(false);
                }
            }
        });

        //RefreshAnimation
        mSwipeRefresh.setRefreshing(true);
        //Ambil Data informasi dari DB
        getListInformasiDiterima();
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

    private void getListInformasiDiterima() {

        SessionManager sm = new SessionManager(getApplicationContext());
        final String TAG = "READ_INFOS_DITERIMA";
        String URL = ApiUrl.URL_READ_INFOS_DITERIMA + sm.getSessionNIP();

        if(Helper.isInternetConnected(getApplicationContext())) {
            AppSingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().invalidate(URL, true);
        }

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    //hapus isi informasi (untuk refresh)
                    mListInformasi.clear();

                    //Ambil array dari json yg judul arraynya "msgs"
                    JSONArray jsonListInformasi = response.getJSONArray("info");

                    //masukin yg ada di jsonarray ke arraylist informasi
                    for(int i=0;i<jsonListInformasi.length();i++) {
                        mListInformasi.add(new InformasiModel(
                                jsonListInformasi.getJSONObject(i).getInt("no"),
                                jsonListInformasi.getJSONObject(i).getString("nama"),
                                jsonListInformasi.getJSONObject(i).getString("waktu"),
                                jsonListInformasi.getJSONObject(i).getString("isi"),
                                jsonListInformasi.getJSONObject(i).getString("gambar"),
                                jsonListInformasi.getJSONObject(i).getInt("status")
                        ));
                    }

                    //update adapter setelah masukin ke arraylist informasi
                    mInformasiDiterimaRVAdapter.notifyDataSetChanged();

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
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(objectRequest, TAG);
    }

    private class InformasiDiterimaRVAdapter extends RecyclerView.Adapter<InformasiDiterimaRVAdapter.ViewHolder>{

        private ArrayList<InformasiModel> listInformasi;

        private InformasiDiterimaRVAdapter(ArrayList<InformasiModel> mListInformasi) {
            this.listInformasi = mListInformasi;
        }

        @NonNull
        @Override
        public InformasiDiterimaRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_informasi, parent, false);
            InformasiDiterimaRVAdapter.ViewHolder viewHolder = new InformasiDiterimaRVAdapter.ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull final InformasiDiterimaRVAdapter.ViewHolder holder, int position) {
            String isiNama = listInformasi.get(position).getNama();
            if(listInformasi.get(position).getStatus()==0) {
                isiNama = isiNama + " " + "(Baru)";
            }
            holder.nama.setText(isiNama);
            String tanggalInfo = listInformasi.get(position).getWaktu();

            holder.tanggal.setText(tanggalInfo);
            holder.itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.nama.setText(listInformasi.get(holder.getAdapterPosition()).getNama());
                    Intent i = new Intent(InformasiDiterimaActivity.this, InformasiDetailActivity.class);
                    //Integer diubah menjadi string agar mudah
                    i.putExtra("no", String.valueOf(listInformasi.get(holder.getAdapterPosition()).getNo()));
                    i.putExtra("nama", listInformasi.get(holder.getAdapterPosition()).getNama());
                    i.putExtra("waktu", listInformasi.get(holder.getAdapterPosition()).getWaktu());
                    i.putExtra("isi", listInformasi.get(holder.getAdapterPosition()).getIsi());
                    i.putExtra("gambar", listInformasi.get(holder.getAdapterPosition()).getGambar());
                    i.putExtra("status", listInformasi.get(holder.getAdapterPosition()).getStatus());
                    i.putExtra("dari", "diterima");
                    startActivity(i);
                }
            });
        }

        @Override
        public int getItemCount() {
            return listInformasi.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView nama;
            TextView tanggal;

            RelativeLayout itemLayout;

            public ViewHolder(View itemView) {
                super(itemView);
                nama = itemView.findViewById(R.id.informasi_item_nama);
                tanggal = itemView.findViewById(R.id.informasi_item_tanggal);
                itemLayout = itemView.findViewById(R.id.informasi_item);
            }
        }
    }
}
