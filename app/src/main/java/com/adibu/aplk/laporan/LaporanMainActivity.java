package com.adibu.aplk.laporan;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.adibu.aplk.R;
import com.adibu.aplk.SessionManager;
import com.adibu.aplk.grid.GridAdapter;
import com.adibu.aplk.grid.GridModel;

import java.util.HashMap;

public class LaporanMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_gridview);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        GridView gridView = findViewById(R.id.main_grid);
        GridAdapter gridAdapter = new GridAdapter(this, 0);
        gridView.setAdapter(gridAdapter);

        gridAdapter.add(new GridModel(R.drawable.ic_receive_mail, R.string.suratditerima));
        gridAdapter.add(new GridModel(R.drawable.ic_my_report, R.string.laporansaya));

        SessionManager sm = new SessionManager(getApplicationContext());
        HashMap session = sm.getSession();

        Boolean isPengawas = (Boolean) session.get(SessionManager.KEY_PENGAWAS);
        if(isPengawas) {
            gridAdapter.add(new GridModel(R.drawable.ic_all_reports, R.string.semualaporan));
        }
        
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch ((int)id) {
                    case 0:
                        Intent i = new Intent(LaporanMainActivity.this, SuratDiterimaActivity.class);
                        startActivity(i);
                        break;
                    case 1:
                        Intent j = new Intent(LaporanMainActivity.this, LaporanSayaActivity.class);
                        startActivity(j);
                        break;
                    case 2:
                        Intent k = new Intent(LaporanMainActivity.this, LaporanSemuaActivity.class);
                        startActivity(k);
                        break;
                }
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
