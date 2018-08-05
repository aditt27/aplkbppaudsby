package com.adibu.aplk;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class InformasiFragment extends Fragment {


    private ArrayList<InformasiModel> mListInformasi = new ArrayList<>();
    private InformasiRecyclerViewAdapter mInformasiRecyclerViewAdapter;

    public InformasiFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_informasi, container, false);

        RecyclerView recyclerView = rootView.findViewById(R.id.informasi_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        mInformasiRecyclerViewAdapter = new InformasiRecyclerViewAdapter(mListInformasi, getContext());
        recyclerView.setAdapter(mInformasiRecyclerViewAdapter);

        //Ambil Data informasi dari DB
        getListInformasi();

        // Inflate the layout for this fragment
        return rootView;

    }

    private void getListInformasi() {
        String tag_get_listInformasi = "tag_get_listInformasi";
        String url = ApiUrl.URL_READ_MSGS;

        JsonObjectRequest jsonListInformasi = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    //Ambil array dari json yg judul arraynya "msgs"
                    JSONArray jsonListInformasi = response.getJSONArray("msgs");
                    //masukin yg ada di jsonarray ke arraylist informasi
                    for(int i=0;i<jsonListInformasi.length();i++) {
                        String nama = jsonListInformasi.getJSONObject(i).getString("nama");
                        String waktu = jsonListInformasi.getJSONObject(i).getString("waktu");
                        String isi = jsonListInformasi.getJSONObject(i).getString("isi");
                        mListInformasi.add(new InformasiModel(nama, waktu, isi));
                    }
                    //update adapter setelah masukin ke arraylist informasi
                    mInformasiRecyclerViewAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("LISTINFROMASI", "ERROR GET MSG");
            }
        });
        //Jalanin request yang udah dibuat
        AppSingleton.getInstance(getContext()).addToRequestQueue(jsonListInformasi, tag_get_listInformasi);
    }


}
