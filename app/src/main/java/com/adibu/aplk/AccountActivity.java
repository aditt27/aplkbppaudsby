package com.adibu.aplk;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
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

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SessionManager sm = new SessionManager(getApplicationContext());
        HashMap session = sm.getSession();

        namaTV.setText((String) session.get(SessionManager.KEY_NAMA));
        nipTV.setText((String) session.get(SessionManager.KEY_NIP));

        String role = "";
        role = role + ((Boolean)session.get(SessionManager.KEY_ADMIN)? "Admin, ":"");
        role = role + ((Boolean)session.get(SessionManager.KEY_PENGAWAS)? "Pengawas, ":"");
        role = role + ((Boolean)session.get(SessionManager.KEY_KARYAWAN)? "Karyawan, ":"");
        role = role + ((Boolean)session.get(SessionManager.KEY_FUNGSIONAL)? "Fungsional, ":"");
        role = role + ((Boolean)session.get(SessionManager.KEY_PAMONG)? "Pamong, ":"");
        role = role + ((Boolean)session.get(SessionManager.KEY_PROGRAM)? "Program, ":"");
        role = role + ((Boolean)session.get(SessionManager.KEY_SIK)? "SIK, ":"");
        role = role + ((Boolean)session.get(SessionManager.KEY_PSD)? "PSD, ":"");
        role = role + ((Boolean)session.get(SessionManager.KEY_SUBBAG)? "SUBBAG, ":"");
        role = role + ((Boolean)session.get(SessionManager.KEY_WIYATA)? "Wiyata, ":"");

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
