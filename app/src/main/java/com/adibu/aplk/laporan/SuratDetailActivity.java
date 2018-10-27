package com.adibu.aplk.laporan;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adibu.aplk.R;

public class SuratDetailActivity extends AppCompatActivity {

    Intent mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surat_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mIntent = getIntent();

        TextView noSurat = findViewById(R.id.detail_surat_nomor);
        TextView kategori = findViewById(R.id.detail_surat_kategori);
        TextView perihal = findViewById(R.id.detail_surat_perihal);
        TextView tempat = findViewById(R.id.detail_surat_tempat);
        TextView durasi = findViewById(R.id.detail_surat_durasi);
        TextView waktu = findViewById(R.id.detail_surat_waktu);
        LinearLayout buatLaporan = findViewById(R.id.detail_surat_buat_laporan);

        noSurat.setText(mIntent.getStringExtra("noSurat"));
        kategori.setText(mIntent.getStringExtra("kategori"));
        perihal.setText(mIntent.getStringExtra("perihal"));
        tempat.setText(mIntent.getStringExtra("tempat"));
        durasi.setText(mIntent.getStringExtra("durasi"));
        waktu.setText(mIntent.getStringExtra("waktu"));

        buatLaporan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SuratDetailActivity.this, LaporanBuatActivity.class);
                i.putExtras(mIntent);
                startActivity(i);
            }
        });

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
}
