package com.adibu.aplk;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private ViewPagerAdapter mViewPagerAdapter;
    private SessionManager mSessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Check Login, kalo belum login di redirect ke activity login
        mSessionManager = new SessionManager(this);
        mSessionManager.checkLogin();

        //FRAGMENT
        mViewPager = findViewById(R.id.view_pager);
        // Create an adapter that knows which fragment should be shown on each page
        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        // Set the adapter onto the view pager
        mViewPager.setAdapter(mViewPagerAdapter);

        //TAB LAYOUT UNTUK FRAGMENT
        final TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(mViewPager);
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
        }
        return super.onOptionsItemSelected(item);
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        //Judul tiap tab
        private String[] tabTitles = {getString(R.string.laporan), getString(R.string.informasi)};
        final int pageCount = tabTitles.length;

        //Default constructor
        private ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public Fragment getItem(int position) {
            switch (position) {
                //Urut class tiap fragment
                case 0:
                    return new LaporanFragment();
                default:
                    return new InformasiFragment();
            }
        }

        @Override
        public int getCount() {
            return pageCount;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }
    }
}
