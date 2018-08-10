package com.adibu.aplk;

public class ApiUrl {
    private static final String ROOT_API_URL = "https://cilukbaa.000webhostapp.com/v1/api.php?apicall=";

    //Informasi
    public static final String URL_CREATE_MSG = ROOT_API_URL + "createmsg";  //POST , params: nama, isi
    //New "c_info" POST , params: nip, isi, gambar_info(optional)
    public static final String URL_READ_MSGS = ROOT_API_URL + "getmsg"; //GET , no params
    //New "r_info" GET , no params
    public static final String URL_UPDATE_MSG = ROOT_API_URL + "updatemsg"; //POST , params: no, isi
    public static final String URL_DELETE_MSG = ROOT_API_URL + "deletemsg&no="; //GET , params: no

    //User
    public static final String URL_CREATE_USER = ROOT_API_URL + "createuser"; //POST , params: nip, password, nama, karyawan, pengawas, admin
    //New "c_user" POST, params: nip, password, nama, karyawan, pengawas, admin, fungsional, pamong, program, sik, psd, subbag, wiyata
    public static final String URL_READ_USERS = ROOT_API_URL + "getuser"; //GET , no params
    //New "r_user" GET , no params
    public static final String URL_READ_USER = ROOT_API_URL + "getusernip&nip="; //GET , params: nip
    //New "r_user_nip&nip=" GET , params: nip
    public static final String URL_UPDATE_USER = ROOT_API_URL + "updateuser"; //POST , params: nip, nama
    public static final String URL_DELETE_USER = ROOT_API_URL + "deleteuser&no="; //GET , params: nip
}
