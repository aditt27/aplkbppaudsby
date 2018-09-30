package com.adibu.aplk.informasi;

public class InformasiModel {

    /*
    Data model untuk bagian informasi
    */

    private int no;
    private String nama;
    private String waktu;
    private String isi;
    private String gambar;
    private int status;

    public InformasiModel(int no, String nama, String waktu, String isi, String gambar, int status) {
        this.no = no;
        this.nama = nama;
        this.waktu = waktu;
        this.isi = isi;
        this.gambar = gambar;
        this.status = status;
    }

    public int getNo() {
        return no;
    }

    public String getNama() {
        return nama;
    }

    public String getWaktu() {
        return waktu;
    }

    public String getIsi() {
        return isi;
    }

    public String getGambar() {
        return gambar;
    }

    public int getStatus() {
        return status;
    }


}