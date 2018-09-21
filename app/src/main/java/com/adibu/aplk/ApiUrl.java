package com.adibu.aplk;

public class ApiUrl {
    private static final String ROOT_API_URL = "https://cilukbaa.000webhostapp.com/v1/api.php?apicall=";

    //Informasi
    public static final String URL_CREATE_INFO = ROOT_API_URL + "c_info";  //POST , params: nip, isi, pic(optional), a(fungsional),b(pamong),c(program),d(sik),e(psd),f(subbag),g(wiyata)

    public static final String URL_READ_INFOS_DITERIMA = ROOT_API_URL + "r_info_nip&nip="; //GET , params: nip
	public static final String URL_READ_INFOS_TERKIRIM = ROOT_API_URL + "r_sent_all&nip="; //GET , params: nip (ambil semua pesan yang dikirim oleh nip)

	public static final String URL_READ_INFO_TERBACA = ROOT_API_URL + "r_sent&no="; //GET , params: no (ambil status transaksi dari nomor informasi)
	public static final String URL_UPDATE_INFO_TERBACA = ROOT_API_URL + "u_status&no="; //GET ,  params: no, status (untuk mengubah angka pada status)

    public static final String URL_UPDATE_MSG = ROOT_API_URL + "updatemsg"; //POST , params: no, isi
    public static final String URL_DELETE_MSG = ROOT_API_URL + "deletemsg&no="; //GET , params: no

	//Laporan
	//r_surat&nip= , params: nip //Untuk surat diterima
	
    //User
    public static final String URL_CREATE_USER = ROOT_API_URL + "c_user"; //POST, params: nip, password, nama, karyawan, pengawas, admin, fungsional, pamong, program, sik, psd, subbag, wiyata
    public static final String URL_READ_USERS = ROOT_API_URL + "r_user"; //GET , no params
    public static final String URL_READ_USER = ROOT_API_URL + "r_user_nip&nip="; //GET , params: nip
	public static final String URL_UPDATE_USER_PASS = "u_pass&nip="; //GET, params: nip, password
    public static final String URL_UPDATE_USER = ROOT_API_URL + "updateuser"; //POST , params: nip, nama
    public static final String URL_DELETE_USER = ROOT_API_URL + "deleteuser&no="; //GET , params: nip

    //Image
    public static final String URL_CREATE_INFO_UP = ROOT_API_URL + "up_img_info_update&no="; //POST , params: pic, no

	//Notifikasi firebase
	public static final String URL_REG_DEVICE = ROOT_API_URL + "c_device"; //POST, params: nip, token
}
