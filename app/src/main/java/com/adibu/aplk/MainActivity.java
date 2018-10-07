package com.adibu.aplk;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;


import com.adibu.aplk.grid.GridAdapter;
import com.adibu.aplk.grid.GridModel;
import com.adibu.aplk.informasi.InformasiMainActivity;
import com.adibu.aplk.laporan.LaporanMainActivity;

public class MainActivity extends AppCompatActivity {

    private SessionManager mSessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_gridview);

        //Check Login, kalo belum login di redirect ke activity login
        mSessionManager = new SessionManager(getApplicationContext());
        mSessionManager.checkLogin();

        GridView gridView = findViewById(R.id.main_grid);
        GridAdapter gridAdapter = new GridAdapter(this, 0);
        gridView.setAdapter(gridAdapter);

        gridAdapter.add(new GridModel(R.drawable.ic_reports, R.string.laporan));
        gridAdapter.add(new GridModel(R.drawable.ic_info, R.string.informasi));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch ((int)id) {
                    case 0:
                        Intent i = new Intent(MainActivity.this, LaporanMainActivity.class);
                        startActivity(i);
                        break;
                    case 1:
                        Intent j = new Intent(MainActivity.this, InformasiMainActivity.class);
                        startActivity(j);
                        break;
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_main_logout:
                mSessionManager.clearSession();
                return true;
            case R.id.menu_main_account:
                Intent i = new Intent(MainActivity.this, AccountActivity.class);
                startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

}
