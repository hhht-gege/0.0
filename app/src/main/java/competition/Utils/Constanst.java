package competition.Utils;

import competition.fragment.Anim.JavaBean.AnimAdd_javabean;
import competition.fragment.Anim.JavaBean.Anim_javabean;
import competition.fragment.Setting.Javabean.Pet_ip_javabean;
import okhttp3.MediaType;
import competition.Login.JavaBean.*;

public class Constanst {
    public static final MediaType Gson_media = MediaType.parse(Constanst.Gson_Media);
    public static final String Gson_Media = "Content-Type:application/json";
    public static final String registerurl = "http://api.petcollar.top:8083/register";
    public static final String login_url = "http://api.petcollar.top:8083/login";
    public static final String Login_nopass_url = "http://api.petcollar.top:8083/token_login";

    public static AnimAdd_javabean animal;

    public static UU myuser;

    public static String token;

    public static Anim_javabean anim;

    public static int tmp;

    public static String firstip;

    public static Pet_ip_javabean.DataBean.PetBean petBean;


    //   相册二维码
// request参数
    public static final int REQ_QR_CODE = 11002; // // 打开扫描界面请求码
    public static final int REQ_PERM_CAMERA = 11003; // 打开摄像头
    public static final int REQ_PERM_EXTERNAL_STORAGE = 11004; // 读写文件

    public static final String INTENT_EXTRA_KEY_QR_SCAN = "qr_scan_result";

}