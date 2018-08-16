package com.adibu.aplk;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private SessionManager mSessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Check Login, kalo belum login di redirect ke activity login
        mSessionManager = new SessionManager(this);
        mSessionManager.checkLogin();

        GridView gridView = findViewById(R.id.main_grid);
        GridAdapter gridAdapter = new GridAdapter(this, 0);
        gridView.setAdapter(gridAdapter);

        gridAdapter.add(new MainModel(android.R.drawable.ic_menu_share, R.string.laporan));
        gridAdapter.add(new MainModel(android.R.drawable.ic_dialog_info, R.string.informasi));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Intent i = new Intent(MainActivity.this, LaporanActivity.class);
                        startActivity(i);
                        break;
                    case 1:
                        Intent j = new Intent(MainActivity.this, InformasiActivity.class);
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

    private class GridAdapter extends ArrayAdapter<MainModel> {

        public GridAdapter(@NonNull Context context, int resource) {
            super(context, resource);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View gridItemView = convertView;
            if(gridItemView == null) {
                gridItemView = LayoutInflater.from(getContext()).inflate(
                        R.layout.main_item, parent, false);
            }

            MainModel currentItem = getItem(position);

            TextView title = gridItemView.findViewById(R.id.main_item_text);
            title.setText(getString(currentItem.getNameId()));

            ImageView image = (ImageView)gridItemView.findViewById(R.id.main_item_image);
            image.setImageResource(currentItem.getImageId());


            return gridItemView;
        }

    }

    private class MainModel {
        private int imageId;
        private int nameId;

        public MainModel(int imageId, int nameId) {
            this.imageId = imageId;
            this.nameId = nameId;
        }

        public int getImageId() {
            return imageId;
        }

        public void setImageId(int imageId) {
            this.imageId = imageId;
        }

        public int getNameId() {
            return nameId;
        }

        public void setNameId(int nameId) {
            this.nameId = nameId;
        }
    }

}
