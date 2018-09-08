package com.adibu.aplk;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;

public class AccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        TextView namaTV = findViewById(R.id.akun_nama);
        TextView nipTV = findViewById(R.id.akun_nip);
        TextView roleTV = findViewById(R.id.akun_role);
        Button testNotif = findViewById(R.id.test_notifikasi);

        testNotif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyNotificationManager notifMgr = new MyNotificationManager(getApplicationContext());
                notifMgr.showSmallNotification("Test Title", "Test Message");;
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SessionManager sm = new SessionManager(getApplicationContext());
        HashMap session = sm.getSession();

        namaTV.setText((String) session.get(SessionManager.KEY_NAMA));
        nipTV.setText((String) session.get(SessionManager.KEY_NIP));

        String role = "";
        role = role + ((Boolean)session.get(SessionManager.KEY_ADMIN)? getString(R.string.admin) + ", ":"");
        role = role + ((Boolean)session.get(SessionManager.KEY_PENGAWAS)? getString(R.string.pengawas) + ", ":"");
        role = role + ((Boolean)session.get(SessionManager.KEY_KARYAWAN)? getString(R.string.karyawan) + ", ":"");
        role = role + ((Boolean)session.get(SessionManager.KEY_FUNGSIONAL)? getString(R.string.fungsional) + ", ":"");
        role = role + ((Boolean)session.get(SessionManager.KEY_PAMONG)? getString(R.string.pamong) + ", ":"");
        role = role + ((Boolean)session.get(SessionManager.KEY_PROGRAM)? getString(R.string.program) + ", ":"");
        role = role + ((Boolean)session.get(SessionManager.KEY_SIK)? getString(R.string.sik) + ", ":"");
        role = role + ((Boolean)session.get(SessionManager.KEY_PSD)? getString(R.string.psd) + ", ":"");
        role = role + ((Boolean)session.get(SessionManager.KEY_SUBBAG)? getString(R.string.subbag) + ", ":"");
        role = role + ((Boolean)session.get(SessionManager.KEY_WIYATA)? getString(R.string.wiyata) + ", ":"");
        role = role.substring(0, role.length()-2);

        roleTV.setText(role);


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
