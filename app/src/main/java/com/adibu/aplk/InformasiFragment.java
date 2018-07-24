package com.adibu.aplk;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class InformasiFragment extends Fragment {


    private ArrayList<Informasi> mListInformasi = new ArrayList<>();

    public InformasiFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_informasi, container, false);

        //DATA DUMMY
        mListInformasi.add(new Informasi("Abdul", "01/02/2018", "LOREM IPSUM"));
        mListInformasi.add(new Informasi("Adit", "02/02/2018", "LOREM IPSUM 2"));

        RecyclerView recyclerView = rootView.findViewById(R.id.informasi_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(mListInformasi, getContext());
        recyclerView.setAdapter(recyclerViewAdapter);

        // Inflate the layout for this fragment
        return rootView;
    }

}
