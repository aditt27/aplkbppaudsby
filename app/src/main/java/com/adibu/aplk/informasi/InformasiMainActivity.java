package com.adibu.aplk.informasi;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.adibu.aplk.R;
import com.adibu.aplk.grid.GridAdapter;
import com.adibu.aplk.grid.GridModel;

public class InformasiMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_gridview);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        GridView gridView = findViewById(R.id.main_grid);
        GridAdapter gridAdapter = new GridAdapter(this, 0);
        gridView.setAdapter(gridAdapter);

        gridAdapter.add(new GridModel(R.drawable.ic_info_received, R.string.infoditerima));
        gridAdapter.add(new GridModel(R.drawable.ic_info_sent, R.string.infoterkirim));
        gridAdapter.add(new GridModel(R.drawable.ic_info_add, R.string.infokirim ));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch ((int)id) {
                    case 0:
                        Intent i = new Intent(InformasiMainActivity.this, InformasiDiterimaActivity.class);
                        startActivity(i);
                        break;
                    case 1:
                        Intent j = new Intent(InformasiMainActivity.this, InformasiTerkirimActivity.class);
                        startActivity(j);
                        break;
                    case 2:
                        Intent k = new Intent(InformasiMainActivity.this, InformasiTambahActivity.class);
                        startActivity(k);
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
