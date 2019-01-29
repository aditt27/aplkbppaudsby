package com.adibu.aplk.laporan;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adibu.aplk.ApiUrl;
import com.adibu.aplk.AppSingleton;
import com.adibu.aplk.Helper;
import com.adibu.aplk.R;
import com.adibu.aplk.VolleyMultiPartRequest;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class LaporanBuatActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 2;

    Intent mIntent;
    TextView textfoto;
    EditText keterangan;
    RelativeLayout[] fotoRL = new RelativeLayout[3];
    ImageView[] fotoIV = new ImageView[3];
    ImageView[] deleteFoto = new ImageView[3];
    Button browse;
    Bitmap[] bitmapFoto = new Bitmap[3];
    int totalFoto = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_buat);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mIntent = getIntent();

        TextView noSurat = findViewById(R.id.buat_laporan_nomor);
        textfoto = findViewById(R.id.buat_laporan_textview_foto);
        keterangan = findViewById(R.id.buat_laporan_edittext_keterangan);
        fotoRL[0] = findViewById(R.id.buat_laporan_RL_foto1);
        fotoRL[1] = findViewById(R.id.buat_laporan_RL_foto2);
        fotoRL[2] = findViewById(R.id.buat_laporan_RL_foto3);
        fotoIV[0] = findViewById(R.id.buat_laporan_imageview_foto1);
        fotoIV[1] = findViewById(R.id.buat_laporan_imageview_foto2);
        fotoIV[2] = findViewById(R.id.buat_laporan_imageview_foto3);

        deleteFoto[0] = findViewById(R.id.buat_laporan_delete_foto1);
        deleteFoto[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFoto(0);
            }
        });
        deleteFoto[1] = findViewById(R.id.buat_laporan_delete_foto2);
        deleteFoto[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFoto(1);
            }
        });

        deleteFoto[2] = findViewById(R.id.buat_laporan_delete_foto3);
        deleteFoto[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFoto(2);
            }
        });

        browse = findViewById(R.id.buat_laporan_button_foto);

        noSurat.setText(mIntent.getStringExtra("noSurat"));

        fotoRL[0].setVisibility(View.GONE);
        fotoRL[1].setVisibility(View.GONE);
        fotoRL[2].setVisibility(View.GONE);

        textfoto.setText(getString(R.string.foto) + " (" + totalFoto + "/" + bitmapFoto.length + ")");

        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(LaporanBuatActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(LaporanBuatActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                else {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
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

            case R.id.menu_kirim:
                String ket = keterangan.getText().toString().trim();
                if(ket.isEmpty()) {
                    keterangan.setError(getString(R.string.harus_diisi));
                }
                if(totalFoto==0) {
                    Toast.makeText(LaporanBuatActivity.this, "Tidak ada foto yang diunggah", Toast.LENGTH_LONG).show();
                }
                if(!ket.isEmpty() && totalFoto>=1) {
                    if(Helper.isInternetConnected(getApplicationContext())) {
                        buatLaporan();
                        finish();
                    } else {
                        Toast.makeText(LaporanBuatActivity.this, getString(R.string.nointernet), Toast.LENGTH_SHORT).show();
                    }
                }
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            Uri image = data.getData();
            try {
                for(int i=0; i<bitmapFoto.length; i++){
                    if(bitmapFoto[i]==null) {
                        bitmapFoto[i] = MediaStore.Images.Media.getBitmap(getContentResolver(), image);
                        fotoRL[i].setVisibility(View.VISIBLE);
                        fotoIV[i].setImageBitmap(bitmapFoto[i]);
                        totalFoto += 1;
                        textfoto.setText(getString(R.string.foto) + " (" + totalFoto + "/" + bitmapFoto.length + ")");
                        if(totalFoto==3) {
                            browse.setEnabled(false);
                        }
                        break;
                    }
                }
            } catch (IOException e) {
                e.fillInStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    browse.callOnClick();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_kirim, menu);
        return true;
    }



    private void deleteFoto (int noArray) {
        bitmapFoto[noArray] = null;
        fotoIV[noArray].setImageBitmap(null);
        fotoRL[noArray].setVisibility(View.GONE);
        totalFoto -= 1;
        textfoto.setText(getString(R.string.foto) + " (" + totalFoto + "/" + bitmapFoto.length + ")");
        browse.setEnabled(true);
    }

    private void buatLaporan() {
        final String TAG = "CREATE_LAPORAN";
        String URL = ApiUrl.URL_CREATE_LAPORAN;
        Log.d(TAG, "mulai");

        VolleyMultiPartRequest multiPartRequest = new VolleyMultiPartRequest(URL, null, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                String resp = "";
                try {
                    resp = new String(response.data, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "Multipart: " + resp);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.fillInStackTrace();
            }
        });

        multiPartRequest.addPart(new VolleyMultiPartRequest.FormPart("nop", String.valueOf(mIntent.getStringExtra("noPerintah"))));
        multiPartRequest.addPart(new VolleyMultiPartRequest.FormPart("isi", String.valueOf(keterangan.getText().toString().trim())));

        //Fix volley library for sending data twice
        multiPartRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        int foto = 0;
        long imagename = System.currentTimeMillis();
        for(int i=0;i<bitmapFoto.length;i++){
            if(bitmapFoto[i]!=null) {
                foto += 1;
                multiPartRequest.addPart(new VolleyMultiPartRequest.FilePart("pic" + foto, "image/png", imagename + ".png", Helper.getCompressedBitmapData(bitmapFoto[i])));
            }
        }

        //Fix volley library for sending data twice
        multiPartRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        //Jalanin request yang udah dibuat
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(multiPartRequest, TAG);
    }
}