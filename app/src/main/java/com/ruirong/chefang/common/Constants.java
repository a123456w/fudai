package com.ruirong.chefang.common;

import static com.qlzx.mylibrary.common.Constants.HOST;
import static com.qlzx.mylibrary.common.Constants.IMG_HOST;

/**
 * Created by guo on 2017/3/31.
 */

public class Constants {

    public static final String WEIXIN_APP_ID = "wx5d1eaf86165c26d4";
    public static final String ABOUT_OUS_URL = HOST + "About/Team.html";
    public static final String AGREEMENT_URL = HOST + "About/agreement";
    public static final String YJFD_DOWNLOAD = "yjfdDownload";
    public static final String SEARVICE = "https://www.sobot.com/chat/h5/index.html?sysNum=3aafddbd178b4cebbd26110164b3a390&robotFlag=1";
    public static final String HOUSERULE = IMG_HOST + "/index.php?s=/Home/Index/articleinfo/id/147.html";// 房时规则
    public static final String EXCHANGERULE = IMG_HOST + "/index.php?s=/Home/Index/articleinfo/id/148.html"; // 金豆提现规则
    public static String WEIXIN_PAY_TYPE = "";


    public static final String TAKE_PHOTO = "BGAPhotoPickerTakePhoto";


    public static final int ADDBANKCARDREQUESTCODE = 1;     // 添加银行卡请求码
    public static final int BANKCARKDETAILREQUESTCODE = 2;  // 银行卡详情
    public static final int PWDSETORNOT = 4;  // 银行卡详情

    public static final int GOLDWITHDRAWREQUESTCODE = 3;   // 金豆提现
    public static final int UPDATENICKNAMEREQUESTCODE = 5; // 修改昵称
    public static final String ISBACK = "ISBACK";           //
    public static final String BANKCARDID = "BANKCARDID";      // 银行卡id
    public static final String BANKCARDNUM = "BANKCARDNUM";      // 银行卡卡号
    public static final String BANKCARDNAME = "BANKCARDNAME";      // 银行卡名字
    public static final String UPDATENICKNAME = "UPDATENICKNAME";  // 修改昵称
    public static final int DELETESTORAGE = 1;  // 收藏删除类型
    public static final int DELETEAPPOINT = 2; // 预约和看房删除类型
    public static final String UPDATEFLAGE = "UPDATEFLAGE"; // 修改密码的标记，
    public static final String UPDATESETPAY = "UPDATESETPAY"; // 修改支付密码
    public static final String ORDERID = "ORDERID"; // 修改支付密码
    public static final String SEARCHLISTKEY = "SEARCHLISTKEY"; // 存储list集合的key
}
