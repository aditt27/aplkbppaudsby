package com.adibu.aplk;

/*
Data model untuk bagian informasi
*/

public class InformasiModel {

    private String nama;
    private String waktu;
    private String isi;

    public InformasiModel(String nama, String waktu, String isi) {
        this.nama = nama;
        this.waktu = waktu;
        this.isi = isi;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }

    public String getIsi() {
        return isi;
    }

    public void setIsi(String isi) {
        this.isi = isi;
    }
}
