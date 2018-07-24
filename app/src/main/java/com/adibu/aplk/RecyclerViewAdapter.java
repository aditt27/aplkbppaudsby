package com.adibu.aplk;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private ArrayList<Informasi> mListInformasi;
    private Context mContext;

    public RecyclerViewAdapter(ArrayList<Informasi> mListInformasi, Context mContext) {
        this.mListInformasi = mListInformasi;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.informasi_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.nama.setText(mListInformasi.get(position).getNama());
        holder.tanggal.setText(mListInformasi.get(position).getTanggal());
        holder.isi.setText(mListInformasi.get(position).getIsi());
    }

    @Override
    public int getItemCount() {
        return mListInformasi.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nama;
        TextView tanggal;
        TextView isi;

        RelativeLayout itemLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            nama = itemView.findViewById(R.id.informasi_item_nama);
            tanggal = itemView.findViewById(R.id.informasi_item_tanggal);
            isi = itemView.findViewById(R.id.informasi_item_isi);
            itemLayout = itemView.findViewById(R.id.informasi_item);
        }
    }
}
