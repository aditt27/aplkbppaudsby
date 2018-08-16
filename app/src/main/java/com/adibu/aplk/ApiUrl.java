package com.adibu.aplk;

public class ApiUrl {
    private static final String ROOT_API_URL = "https://cilukbaa.000webhostapp.com/v1/api.php?apicall=";

    //Informasi
    public static final String URL_CREATE_MSG = ROOT_API_URL + "c_info";  //POST , params: nip, isi, pic(optional), a(fungsional),b(pamong),c(program),d(sik),e(psd),f(subbag),g(wiyata)
    public static final String URL_READ_MSGS = ROOT_API_URL + "r_info_nip&nip="; //GET , params: nip
	//"r_sent_all&nip=" params: nip (ambil semua pesan yang dikirim oleh nip)
	//"r_status&no=" params: no (ambil status transaksi pada nomor informasi)
    public static final String URL_UPDATE_MSG = ROOT_API_URL + "updatemsg"; //POST , params: no, isi
    public static final String URL_DELETE_MSG = ROOT_API_URL + "deletemsg&no="; //GET , params: no

    //User
    public static final String URL_CREATE_USER = ROOT_API_URL + "c_user"; //POST, params: nip, password, nama, karyawan, pengawas, admin, fungsional, pamong, program, sik, psd, subbag, wiyata
    public static final String URL_READ_USERS = ROOT_API_URL + "r_user"; //GET , no params
    public static final String URL_READ_USER = ROOT_API_URL + "r_user_nip&nip="; //GET , params: nip
    public static final String URL_UPDATE_USER = ROOT_API_URL + "updateuser"; //POST , params: nip, nama
    public static final String URL_DELETE_USER = ROOT_API_URL + "deleteuser&no="; //GET , params: nip

    //Image
    public static final String URL_CREATE_INFO_UP = ROOT_API_URL + "up_img_info_update&no="; //POST , params: pic, no
}
