package com.adibu.aplk.laporan;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
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
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class LaporanBuatActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;

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
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
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

        VolleyMultiPartRequest multiPartRequest = new VolleyMultiPartRequest(Request.Method.POST, URL, new Response.Listener<NetworkResponse>() {
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
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("nop", String.valueOf(mIntent.getStringExtra("noPerintah")));
                params.put("isi", keterangan.getText().toString().trim());
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                int foto = 0;
                for(int i=0;i<bitmapFoto.length;i++){
                    if(bitmapFoto[i]!=null) {
                        foto += 1;
                        params.put("pic" + foto, new DataPart(imagename + ".png", Helper.getFileDataFromDrawable(bitmapFoto[i])));
                    }
                }
                return params;
            }
        };

        //Jalanin request yang udah dibuat
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(multiPartRequest, TAG);
    }
}