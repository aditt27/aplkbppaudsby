package com.adibu.aplk.laporan;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.adibu.aplk.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.treebo.internetavailabilitychecker.InternetAvailabilityChecker;
import com.treebo.internetavailabilitychecker.InternetConnectivityListener;

public class LaporanDetailActivity extends AppCompatActivity implements InternetConnectivityListener {

    private InternetAvailabilityChecker mInternetAvailabilityChecker;
    private Boolean internetConnected = false;
    Intent mIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mInternetAvailabilityChecker = InternetAvailabilityChecker.getInstance();
        mInternetAvailabilityChecker.addInternetConnectivityListener(this);

        mIntent = getIntent();

        TextView noSurat = findViewById(R.id.laporan_detail_nomor);
        TextView kategori = findViewById(R.id.laporan_detail_kategori);
        TextView perihal = findViewById(R.id.laporan_detail_perihal);
        TextView tempat = findViewById(R.id.laporan_detail_tempat);
        TextView durasi = findViewById(R.id.laporan_detail_durasi);
        TextView waktu = findViewById(R.id.laporan_detail_waktu);
        TextView keterangan = findViewById(R.id.laporan_detail_keterangan);
        final ProgressBar progressFoto = findViewById(R.id.laporan_detail_foto_progress);
        final ImageView foto1 = findViewById(R.id.laporan_detail_foto1);
        final ImageView foto2 = findViewById(R.id.laporan_detail_foto2);
        final ImageView foto3 = findViewById(R.id.laporan_detail_foto3);

        noSurat.setText(mIntent.getStringExtra("noSurat"));
        kategori.setText(mIntent.getStringExtra("kategori"));
        perihal.setText(mIntent.getStringExtra("perihal"));
        tempat.setText(mIntent.getStringExtra("tempat"));
        durasi.setText(mIntent.getStringExtra("durasi"));
        waktu.setText(mIntent.getStringExtra("waktu"));
        keterangan.setText(mIntent.getStringExtra("isi"));

        //foto1.setVisibility(View.GONE);
        //foto2.setVisibility(View.GONE);
        //foto3.setVisibility(View.GONE);

        String pic1 = mIntent.getStringExtra("pic1");
        String pic2 = mIntent.getStringExtra("pic2");
        String pic3 = mIntent.getStringExtra("pic3");

        if(!pic1.isEmpty() || !pic2.isEmpty() || !pic3.isEmpty()) {
            Picasso.with(this)
                    .load(pic1)
                    .into(foto1, new Callback() {
                        @Override
                        public void onSuccess() {
                            foto1.setVisibility(View.VISIBLE);
                            progressFoto.setVisibility(View.GONE);
                        }
                        @Override
                        public void onError() {

                        }
                    });
            Picasso.with(this)
                    .load(pic2)
                    .into(foto2, new Callback() {
                        @Override
                        public void onSuccess() {
                            foto2.setVisibility(View.VISIBLE);
                            progressFoto.setVisibility(View.GONE);
                        }
                        @Override
                        public void onError() {

                        }
                    });
            Picasso.with(this)
                    .load(pic3)
                    .into(foto3, new Callback() {
                        @Override
                        public void onSuccess() {
                            foto3.setVisibility(View.VISIBLE);
                            progressFoto.setVisibility(View.GONE);
                        }
                        @Override
                        public void onError() {

                        }
                    });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);
                onBackPressed();
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


}
