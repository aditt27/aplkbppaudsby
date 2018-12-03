package com.adibu.aplk.informasi;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.adibu.aplk.ApiUrl;
import com.adibu.aplk.AppSingleton;
import com.adibu.aplk.Helper;
import com.adibu.aplk.R;
import com.adibu.aplk.SessionManager;
import com.adibu.aplk.VolleyMultiPartRequest;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class InformasiTambahActivity extends AppCompatActivity{

    private static final int PICK_IMAGE = 1;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 2;

    private EditText mInputInfo;
    private ImageView mGambarInfo;
    private Button mButtonGambar;
    private Bitmap mBitmapGambar;
    private CheckBox[] mKirimKeCheckBoxes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informasi_tambah);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mInputInfo = findViewById(R.id.tambah_info_edittext_informasi);
        mGambarInfo = findViewById(R.id.tambah_info_imageview_foto);
        mButtonGambar = findViewById(R.id.tambah_info_button_foto);

        mKirimKeCheckBoxes  = new CheckBox[]{
                findViewById(R.id.tambah_info_checkbox_fungsional),
                findViewById(R.id.tambah_info_checkbox_pamong),
                findViewById(R.id.tambah_info_checkbox_program),
                findViewById(R.id.tambah_info_checkbox_sik),
                findViewById(R.id.tambah_info_checkbox_psd),
                findViewById(R.id.tambah_info_checkbox_subbag),
                findViewById(R.id.tambah_info_checkbox_wiyata),
                findViewById(R.id.tambah_info_checkbox_semua)
        };

        mButtonGambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(InformasiTambahActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(InformasiTambahActivity.this,
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

        mKirimKeCheckBoxes[7].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    for(int i=0; i<mKirimKeCheckBoxes.length-1;i++) {
                        mKirimKeCheckBoxes[i].setEnabled(false);
                    }
                } else {
                    for(int i=0; i<mKirimKeCheckBoxes.length-1;i++) {
                        mKirimKeCheckBoxes[i].setEnabled(true);
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_kirim, menu);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    mButtonGambar.callOnClick();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_kirim:
                String info = mInputInfo.getText().toString().trim();
                if(!info.isEmpty()){
                    if(isCheckBoxesChecked()) {
                        if(Helper.isInternetConnected(getApplicationContext())) {
                            if(mBitmapGambar==null){
                                addInfo(info);
                                finish();
                            } else {
                                addInfo(info, mBitmapGambar);
                                finish();
                            }
                        } else {
                            Toast.makeText(InformasiTambahActivity.this, getString(R.string.nointernet), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(InformasiTambahActivity.this, getString(R.string.notujuaninfo), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    mInputInfo.setError(getString(R.string.harus_diisi));
                }
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            Uri image = data.getData();

            try {
                mBitmapGambar = MediaStore.Images.Media.getBitmap(getContentResolver(), image);
                mGambarInfo.setImageBitmap(mBitmapGambar);
            } catch (IOException e) {
                e.fillInStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void addInfo(final String info) {
        final String TAG = "CREATE_INFO";
        String URL = ApiUrl.URL_CREATE_INFO;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "StringRequest: " + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.fillInStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                SessionManager sm = new SessionManager(getApplicationContext());
                sm.checkLogin();
                Map<String, String> params = new HashMap<>();
                params.put("nip", sm.getSessionNIP());
                params.put("isi", info);

                if(mKirimKeCheckBoxes[7].isChecked()) {
                    params.put("a", String.valueOf(1));
                    params.put("b", String.valueOf(1));
                    params.put("c", String.valueOf(1));
                    params.put("d", String.valueOf(1));
                    params.put("e", String.valueOf(1));
                    params.put("f", String.valueOf(1));
                    params.put("g", String.valueOf(1));

                } else {
                    params.put("a", String.valueOf(mKirimKeCheckBoxes[0].isChecked()? 1:0));
                    params.put("b", String.valueOf(mKirimKeCheckBoxes[1].isChecked()? 1:0));
                    params.put("c", String.valueOf(mKirimKeCheckBoxes[2].isChecked()? 1:0));
                    params.put("d", String.valueOf(mKirimKeCheckBoxes[3].isChecked()? 1:0));
                    params.put("e", String.valueOf(mKirimKeCheckBoxes[4].isChecked()? 1:0));
                    params.put("f", String.valueOf(mKirimKeCheckBoxes[5].isChecked()? 1:0));
                    params.put("g", String.valueOf(mKirimKeCheckBoxes[6].isChecked()? 1:0));
                }
                return params;
            }
        };

        //Fix volley library for sending data twice
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        //Jalanin request yang udah dibuat
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest, TAG);
    }

    private void addInfo(final String info, final Bitmap image) {
        final String TAG = "CREATE_INFO 2";
        String URL = ApiUrl.URL_CREATE_INFO;
        SessionManager sm = new SessionManager(getApplicationContext());
        sm.checkLogin();

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

        multiPartRequest.addPart(new VolleyMultiPartRequest.FormPart("nip", sm.getSessionNIP() ));
        multiPartRequest.addPart(new VolleyMultiPartRequest.FormPart("isi", info ));

        long imagename = System.currentTimeMillis();
        multiPartRequest.addPart(new VolleyMultiPartRequest.FilePart("pic", "image/png", imagename + ".png", Helper.getFileDataFromDrawable(image)));

        if(mKirimKeCheckBoxes[7].isChecked()) {
            multiPartRequest.addPart(new VolleyMultiPartRequest.FormPart("a", "1"));
            multiPartRequest.addPart(new VolleyMultiPartRequest.FormPart("b", "1"));
            multiPartRequest.addPart(new VolleyMultiPartRequest.FormPart("c", "1"));
            multiPartRequest.addPart(new VolleyMultiPartRequest.FormPart("d", "1"));
            multiPartRequest.addPart(new VolleyMultiPartRequest.FormPart("e", "1"));
            multiPartRequest.addPart(new VolleyMultiPartRequest.FormPart("f", "1"));
            multiPartRequest.addPart(new VolleyMultiPartRequest.FormPart("g", "1"));

        } else {
            multiPartRequest.addPart(new VolleyMultiPartRequest.FormPart("a", String.valueOf(mKirimKeCheckBoxes[0].isChecked()? 1:0)));
            multiPartRequest.addPart(new VolleyMultiPartRequest.FormPart("b", String.valueOf(mKirimKeCheckBoxes[0].isChecked()? 1:0)));
            multiPartRequest.addPart(new VolleyMultiPartRequest.FormPart("c", String.valueOf(mKirimKeCheckBoxes[0].isChecked()? 1:0)));
            multiPartRequest.addPart(new VolleyMultiPartRequest.FormPart("d", String.valueOf(mKirimKeCheckBoxes[0].isChecked()? 1:0)));
            multiPartRequest.addPart(new VolleyMultiPartRequest.FormPart("e", String.valueOf(mKirimKeCheckBoxes[0].isChecked()? 1:0)));
            multiPartRequest.addPart(new VolleyMultiPartRequest.FormPart("f", String.valueOf(mKirimKeCheckBoxes[0].isChecked()? 1:0)));
            multiPartRequest.addPart(new VolleyMultiPartRequest.FormPart("g", String.valueOf(mKirimKeCheckBoxes[0].isChecked()? 1:0)));
        }

        //Jalanin request yang udah dibuat
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(multiPartRequest, TAG);
    }

    private Boolean isCheckBoxesChecked() {
        if(mKirimKeCheckBoxes[0].isChecked() ||
                mKirimKeCheckBoxes[1].isChecked() ||
                mKirimKeCheckBoxes[2].isChecked() ||
                mKirimKeCheckBoxes[3].isChecked() ||
                mKirimKeCheckBoxes[4].isChecked() ||
                mKirimKeCheckBoxes[5].isChecked() ||
                mKirimKeCheckBoxes[6].isChecked() ||
                mKirimKeCheckBoxes[7].isChecked()) {
            return true;
        }
        return false;
    }


}
