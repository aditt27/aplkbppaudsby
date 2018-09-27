package com.adibu.aplk.laporan;

public class SuratModel {
    private int noPerintah;
    private String noSurat;
    private String waktu;
    private int status;
    private String perihal;
    private String tempat;
    private String durasi;
    private String kategori;

    public SuratModel(int noPerintah, String noSurat, String waktu, int status, String perihal, String tempat, String durasi, String kategori) {
        this.noPerintah = noPerintah;
        this.noSurat = noSurat;
        this.waktu = waktu;
        this.status = status;
        this.perihal = perihal;
        this.tempat = tempat;
        this.durasi = durasi;
        this.kategori = kategori;
    }

    public int getNoPerintah() {
        return noPerintah;
    }

    public String getNoSurat() {
        return noSurat;
    }

    public String getWaktu() {
        return waktu;
    }

    public int getStatus() {
        return status;
    }

    public String getPerihal() {
        return perihal;
    }

    public String getTempat() {
        return tempat;
    }

    public String getDurasi() {
        return durasi;
    }

    public String getKategori() {
        return kategori;
    }
}
