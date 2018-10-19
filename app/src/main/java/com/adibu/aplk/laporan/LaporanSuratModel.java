package com.adibu.aplk.laporan;

public class LaporanSuratModel {
    private int noPerintah;
    private String noSurat;
    private String waktu;
    private int status;
    private String perihal;
    private String tempat;
    private String durasi;
    private String kategori;

    private int noLaporan;
    private String isi;
    private String pic1;
    private String pic2;
    private String pic3;

    private String nama;

    //SURAT
    public LaporanSuratModel(int noPerintah, String noSurat, String waktu, int status, String perihal, String tempat, String durasi, String kategori) {
        this.noPerintah = noPerintah;
        this.noSurat = noSurat;
        this.waktu = waktu;
        this.status = status;
        this.perihal = perihal;
        this.tempat = tempat;
        this.durasi = durasi;
        this.kategori = kategori;
    }

    //LAPORAN_SAYA
    public LaporanSuratModel(String noSurat, String waktu, String perihal, String tempat, String durasi, String kategori, int noLaporan, String isi, String pic1, String pic2, String pic3) {
        this.noSurat = noSurat;
        this.waktu = waktu;
        this.perihal = perihal;
        this.tempat = tempat;
        this.durasi = durasi;
        this.kategori = kategori;
        this.noLaporan = noLaporan;
        this.isi = isi;
        this.pic1 = pic1;
        this.pic2 = pic2;
        this.pic3 = pic3;
    }

    //LAPORAN_SEMUA
    public LaporanSuratModel(String noSurat, String waktu, String perihal, String tempat, String durasi, String kategori, int noLaporan, String isi, String pic1, String pic2, String pic3, String nama) {
        this.noSurat = noSurat;
        this.waktu = waktu;
        this.perihal = perihal;
        this.tempat = tempat;
        this.durasi = durasi;
        this.kategori = kategori;
        this.noLaporan = noLaporan;
        this.isi = isi;
        this.pic1 = pic1;
        this.pic2 = pic2;
        this.pic3 = pic3;
        this.nama = nama;
    }

    public String getNama() {
        return nama;
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

    public int getNoLaporan() {
        return noLaporan;
    }

    public String getIsi() {
        return isi;
    }

    public String getPic1() {
        return pic1;
    }

    public String getPic2() {
        return pic2;
    }

    public String getPic3() {
        return pic3;
    }
}
