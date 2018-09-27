package com.adibu.aplk.informasi;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
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

public class InformasiTerkirimActivity extends AppCompatActivity implements InternetConnectivityListener{

    private ArrayList<InformasiModel> mListInformasi = new ArrayList<>();
    private InformasiTerkirimRVAdapter mInformasiTerkirimRVAdapter;
    private SwipeRefreshLayout mSwipeRefresh;
    private InternetAvailabilityChecker mInternetAvailabilityChecker;
    private Boolean internetConnected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_swiperecycleview);

        mInternetAvailabilityChecker = InternetAvailabilityChecker.getInstance();
        mInternetAvailabilityChecker.addInternetConnectivityListener(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecyclerView recyclerView = findViewById(R.id.list_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        mInformasiTerkirimRVAdapter = new InformasiTerkirimRVAdapter(mListInformasi);
        recyclerView.setAdapter(mInformasiTerkirimRVAdapter);

        //Reverse Item (Newest one in the top)
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setReverseLayout(true);
        llm.setStackFromEnd(true);
        recyclerView.setLayoutManager(llm);

        mSwipeRefresh = findViewById(R.id.list_swipe_refresh);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(internetConnected) {
                    getListInformasiTerkirim();
                } else {
                    Toast.makeText(InformasiTerkirimActivity.this, getString(R.string.nointernet), Toast.LENGTH_SHORT).show();
                    mSwipeRefresh.setRefreshing(false);
                }
            }
        });

        //RefreshAnimation
        mSwipeRefresh.setRefreshing(true);
        //Ambil Data informasi dari DB
        getListInformasiTerkirim();
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

    private void getListInformasiTerkirim() {
        SessionManager sm = new SessionManager(getApplicationContext());
        String TAG = "READ_INFOS_TERKIRIM";
        String URL = ApiUrl.URL_READ_INFOS_TERKIRIM + sm.getSessionNIP();

        if(internetConnected) {
            AppSingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().invalidate(URL, false);
        }

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    //hapus isi informasi (untuk refresh)
                    mListInformasi.clear();

                    //Ambil array dari json yg judul arraynya "info"
                    JSONArray jsonListInformasi = response.getJSONArray("info");

                    //masukin yg ada di jsonarray ke arraylist informasi
                    for(int i=0;i<jsonListInformasi.length();i++) {
                        //status informasi -1 karena informasi terkirim(tidak butuh status)
                        mListInformasi.add(new InformasiModel(
                                jsonListInformasi.getJSONObject(i).getInt("no_info"),
                                "",
                                jsonListInformasi.getJSONObject(i).getString("waktu"),
                                jsonListInformasi.getJSONObject(i).getString("isi"),
                                jsonListInformasi.getJSONObject(i).getString("gambar"),
                                -1
                        ));
                    }

                    //update adapter setelah masukin ke arraylist informasi
                    mInformasiTerkirimRVAdapter.notifyDataSetChanged();

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

    private class InformasiTerkirimRVAdapter extends RecyclerView.Adapter<InformasiTerkirimRVAdapter.ViewHolder>{

        private ArrayList<InformasiModel> listInformasi;

        private InformasiTerkirimRVAdapter(ArrayList<InformasiModel> mListInformasi) {
            this.listInformasi = mListInformasi;
        }

        @NonNull
        @Override
        public InformasiTerkirimRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_informasi, parent, false);
            InformasiTerkirimRVAdapter.ViewHolder viewHolder = new InformasiTerkirimActivity.InformasiTerkirimRVAdapter.ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull final InformasiTerkirimRVAdapter.ViewHolder holder, int position) {
            holder.isi.setText("\""+listInformasi.get(position).getIsi()+"\"");
            holder.tanggal.setText(listInformasi.get(position).getWaktu());
            holder.itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(InformasiTerkirimActivity.this, InformasiDetailActivity.class);
                    i.putExtra("no", String.valueOf(listInformasi.get(holder.getAdapterPosition()).getNo()));
                    i.putExtra("waktu", listInformasi.get(holder.getAdapterPosition()).getWaktu());
                    i.putExtra("isi", listInformasi.get(holder.getAdapterPosition()).getIsi());
                    i.putExtra("gambar", listInformasi.get(holder.getAdapterPosition()).getGambar());
                    i.putExtra("dari", "terkirim");
                    startActivity(i);
                }
            });
        }

        @Override
        public int getItemCount() {
            return listInformasi.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView isi;
            TextView tanggal;

            RelativeLayout itemLayout;

            public ViewHolder(View itemView) {
                super(itemView);
                isi = itemView.findViewById(R.id.informasi_item_nama);
                //ganti font family karena informasi terkirim
                isi.setTextAppearance(getBaseContext(), R.style.sansserif);
                tanggal = itemView.findViewById(R.id.informasi_item_tanggal);
                itemLayout = itemView.findViewById(R.id.informasi_item);
            }
        }
    }

}
