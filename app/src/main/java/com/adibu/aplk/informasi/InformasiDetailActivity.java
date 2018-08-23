package com.adibu.aplk.informasi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.adibu.aplk.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class InformasiDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informasi_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CardView namaCV = findViewById(R.id.detail_informasi_card_nama);
        TextView namaTV = findViewById(R.id.detail_informasi_nama);
        TextView isiTV = findViewById(R.id.detail_informasi_isi);
        ImageView fotoIV = findViewById(R.id.detail_informasi_foto);
        final ProgressBar fotoPB = findViewById(R.id.detail_informasi_foto_progress);
        TextView tanggalTV = findViewById(R.id.detail_informasi_tanggal);
        final TextView nullfoto = findViewById(R.id.detail_informasi_foto_null);

        Intent i = getIntent();

        String nama = i.getStringExtra("nama");
        if(nama==null) {
            namaCV.setVisibility(View.GONE);
        } else {
            namaTV.setText(nama);
        }

        isiTV.setText(i.getStringExtra("isi"));
        tanggalTV.setText(i.getStringExtra("waktu"));

        String gambar = i.getStringExtra("gambar");
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
}
