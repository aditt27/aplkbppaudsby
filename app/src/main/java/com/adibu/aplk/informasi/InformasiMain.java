package com.adibu.aplk.informasi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.adibu.aplk.R;
import com.adibu.aplk.grid.GridAdapter;
import com.adibu.aplk.grid.GridModel;

public class InformasiMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        GridView gridView = findViewById(R.id.main_grid);
        GridAdapter gridAdapter = new GridAdapter(this, 0);
        gridView.setAdapter(gridAdapter);

        gridAdapter.add(new GridModel(android.R.drawable.ic_popup_reminder, R.string.infoditerima));
        gridAdapter.add(new GridModel(android.R.drawable.ic_dialog_email, R.string.infoterkirim));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch ((int)id) {
                    case 0:
                        Intent i = new Intent(InformasiMain.this, InformasiDiterimaActivity.class);
                        startActivity(i);
                        break;
                    case 1:
                        Intent j = new Intent(InformasiMain.this, InformasiTerkirimActivity.class);
                        startActivity(j);
                        break;
                }
            }
        });
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