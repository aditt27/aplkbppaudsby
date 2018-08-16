package com.adibu.aplk;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TambahInformasiActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;

    private EditText mInputInfo;
    private ImageView mGambarInfo;
    private Button mButtonGambar;
    private Bitmap mBitmapGambar;
    private CheckBox[] mKirimKeCheckBoxes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_informasi);

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
                findViewById(R.id.tambah_info_checkbox_wiyata)
        };

        mButtonGambar.setOnClickListener(new View.OnClickListener() {
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_tambah_informasi, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_tambah_informasi_kirim:
                String info = mInputInfo.getText().toString().trim();
                if(!info.isEmpty()){
                    if(mBitmapGambar==null){
                        addInfo(info);
                        finish();
                    } else {
                        addInfo(info, mBitmapGambar);
                        finish();
                    }
                }else {
                    mInputInfo.setError(getString(R.string.harus_diisi));
                }
                return true;
            case android.R.id.home:
                onBackPressed();
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
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void addInfo(final String info) {
        String tag_add_info = "tag_add_info";
        String url = ApiUrl.URL_CREATE_MSG;

        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.fillInStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                SessionManager sm = new SessionManager(getApplicationContext());
                Map<String, String> params = new HashMap<>();
                params.put("nip", sm.getSessionNIP());
                params.put("isi", info);
                params.put("a", String.valueOf(mKirimKeCheckBoxes[0].isChecked()? 1:0));
                params.put("b", String.valueOf(mKirimKeCheckBoxes[1].isChecked()? 1:0));
                params.put("c", String.valueOf(mKirimKeCheckBoxes[2].isChecked()? 1:0));
                params.put("d", String.valueOf(mKirimKeCheckBoxes[3].isChecked()? 1:0));
                params.put("e", String.valueOf(mKirimKeCheckBoxes[4].isChecked()? 1:0));
                params.put("f", String.valueOf(mKirimKeCheckBoxes[5].isChecked()? 1:0));
                params.put("g", String.valueOf(mKirimKeCheckBoxes[6].isChecked()? 1:0));
                return params;
            }
        };

        //Jalanin request yang udah dibuat
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(sr, tag_add_info);
    }

    private void addInfo(final String info, final Bitmap image) {
        String tag_add_info = "tag_add_info";
        String url = ApiUrl.URL_CREATE_MSG;

        VolleyMultiPartRequest volleyMultipartRequest = new VolleyMultiPartRequest(Request.Method.POST, url,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.fillInStackTrace();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                SessionManager sm = new SessionManager(getApplicationContext());
                Map<String, String> params = new HashMap<>();
                params.put("nip", sm.getSessionNIP());
                params.put("isi", info);
                params.put("a", String.valueOf(mKirimKeCheckBoxes[0].isChecked()? 1:0));
                params.put("b", String.valueOf(mKirimKeCheckBoxes[1].isChecked()? 1:0));
                params.put("c", String.valueOf(mKirimKeCheckBoxes[2].isChecked()? 1:0));
                params.put("d", String.valueOf(mKirimKeCheckBoxes[3].isChecked()? 1:0));
                params.put("e", String.valueOf(mKirimKeCheckBoxes[4].isChecked()? 1:0));
                params.put("f", String.valueOf(mKirimKeCheckBoxes[5].isChecked()? 1:0));
                params.put("g", String.valueOf(mKirimKeCheckBoxes[6].isChecked()? 1:0));
                return params;
            }

            /*
             * Here we are passing image by renaming it with a unique name
             * */
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("pic", new DataPart(imagename + ".png", getFileDataFromDrawable(image)));
                return params;
            }
        };

        //Jalanin request yang udah dibuat
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(volleyMultipartRequest, tag_add_info);

    }

    /*
     * The method is taking Bitmap as an argument
     * then it will return the byte[] array for the given bitmap
     * and we will send this array to the server
     * here we are using PNG Compression with 80% quality
     * you can give quality between 0 to 100
     * 0 means worse quality
     * 100 means best quality
     * */
    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
}
