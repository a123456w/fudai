package com.ruirong.chefang.http;

import com.qlzx.mylibrary.bean.BaseBean;
import com.ruirong.chefang.adapter.CartListBean;
import com.ruirong.chefang.bean.*;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by guo on 2016/7/8.
 * Class Note:
 * {@link retrofit2.Retrofit} 使用的RemoteApi，
 */
public interface RemoteApi {

    //    /**
//     * 作品列表
//     * @return
//     */
//    @GET("works")
//    Observable<WorksResultBean> getWorks();
//
//    /**
//     * 作品详情
//     * @param worksId
//     * @return
//     */
//    @GET("works/{id}")
//    Observable<WorksDetailResultBean> getWorksDetail(@Path("id") int worksId);
//
//    /**
//     * 注册
//     * @return
//     */
//    @POST("auth/signup")
//    @FormUrlEncoded
//    Observable<LoginBean> register(@Field("phone") String phone, @Field("password") String password);


//    /**
//     * 注册
//     * @param code 验证码
//     * @return
//     */
//    @POST("moblie/user/register/{code}")
//    Observable<BaseBean<String>> register(@Path("code") String code,@Body RegisterParameter registerParameter);

    /**
     * 酒店预订套餐
     *
     * @param title
     * @param shop_id
     * @return
     */
    @POST("Hotel/searchroom")
    @FormUrlEncoded
    Observable<BaseBean<List<HotelReserveTaoCan>>> getReserveTaocan(@Field("shop_id") String shop_id, @Field("title") String title);

    /**
     * 查询夜审时间
     *
     * @param shop_id
     * @return
     */
    @POST("Hotel/searchyeshen")
    @FormUrlEncoded
    Observable<BaseBean<StatusBean>> getNightCheck(@Field("shop_id") String shop_id);

    /**
     * 店铺套餐列表
     *
     * @param shop_id
     * @param title
     * @return
     */
    @POST("hotel/packlist")
    @FormUrlEncoded
    Observable<BaseBean<List<ComboBean>>> comBoList(@Field("shop_id") String shop_id, @Field("title") String title);

    /**
     * 商品详情订单详情套餐列表
     *
     * @param token
     * @param id
     * @param xinban
     * @return
     */
    @POST("Hotel/reservepackage")
    @FormUrlEncoded()
    Observable<BaseBean<ComBinBean>> CombinData(@Field("token") String token, @Field("id") String id, @Field("xinban") String xinban);

    /**
     * 获取用餐人数
     *
     * @param shop_id
     * @return
     */
    @POST("Hotel/get_people")
    @FormUrlEncoded()
    Observable<BaseBean<List<PeopleBean>>> getPeopleList(@Field("shop_id") String shop_id);


    /**
     * 分享的信息
     *
     * @return
     */
    @POST("Config/regconf")
    @FormUrlEncoded
    Observable<BaseBean<ShareBean>> shareMessage(@Field("token") String token);

    /**
     * 登陆
     *
     * @param mobile   手机号
     * @param password 密码
     * @return
     */
    @POST("Login/login")
    @FormUrlEncoded
    Observable<BaseBean<LoginBean>> login(@Field("mobile") String mobile, @Field("password") String password, @Field("push_id") String push_id);

    /**
     * 注册时候获得验证码
     *
     * @param mobile 手机号
     * @param type   短信验证码类型｜１注册短信　２找回密码３推荐奖励用发送短信（客户端把不考虑该类型）
     * @return
     */
    @POST("SendMessage/send")
    @FormUrlEncoded
    Observable<BaseBean<String>> getcode(@Field("mobile") String mobile, @Field("type") String type);

    /**
     * 退出登录，清除pushId
     *
     * @param token
     * @return
     */
    @POST("Login/logout")
    @FormUrlEncoded
    Observable<BaseBean<String>> logOut(@Field("token") String token);


    /**
     * 注册
     *
     * @param mobile       手机号
     * @param code         验证码
     * @param password     密码
     * @param refer_mobile 推荐人手机号
     * @return
     */
    @POST("Login/signup")
    @FormUrlEncoded
    Observable<BaseBean<String>> register(@Field("mobile") String mobile, @Field("code") String code, @Field("password") String password, @Field("refer_mobile") String refer_mobile, @Field("province") String province, @Field("city") String city, @Field("county") String county);

    /*

     */

    /**
     * 店铺列表
     *
     * @return
     */
    @POST("Shop/index")
    @FormUrlEncoded
    Observable<BaseBean<List<ShopItemBean>>> getShopList(@Field("classify_id") String classify_id,
                                                         @Field("lat") String lat,
                                                         @Field("lng") String lng,
                                                         @Field("page") int page,
                                                         @Field("shiqu") String shiqu);

    /**
     * 全部行业分类
     *
     * @return
     */
    @POST("Stores/allcate")
    Observable<BaseBean<List<AllIndustriesBean>>> allcate();

    /*    *//**
     * 获取推荐商家
     *
     * @param page 分页
     * @return
     *//*
    @POST("Shop/recom")
    @FormUrlEncoded
    Observable<BaseBean<List<ShopItemBean>>> getRecommendShopList(@Field("page") int page);*/

    /*    *//**
     * 获取商家详情
     *
     * @param shopId 商家id
     * @return
     *//*
    @POST("Shop/show")
    @FormUrlEncoded
    Observable<BaseBean<ShopDetailBean>> getShopDetail(@Field("id") String shopId);*/

    /*
     */
/**
 * 获取楼盘列表
 *
 * @param area_id    楼盘区域选择[地区列表见 -配置-分类 ] 全部 传 -1/ 不传 具体的地址传 地区ID
 * @param sale_order 销量排序 asc 正序 desc 倒序
 * @param page       分页
 * @return
 *//*

    @POST("Premises/index")
    @FormUrlEncoded
    Observable<BaseBean<List<CarHouseItemBean>>> getHousesList(@Field("area_id") String area_id, @Field("sale_order") String sale_order, @Field("page") int page);
*/

    /*
     */
/**
 * 二手房
 *
 * @param page
 * @return
 *//*

    @POST("HouseCarList/houseList")
    @FormUrlEncoded
    Observable<BaseBean<List<CarHouseItemBean>>> secondHouseList(@Field("area_id") String area_id, @Field("page") int page);
*/

    /*
     */
/**
 * 合伙人中心
 *
 * @param token
 * @return
 *//*

    @POST("Partner/partnerCenter")
    @FormUrlEncoded
    Observable<BaseBean<PartenerCenterBean>> getPartenerCenter(@Field("token") String token);
*/


    /*
     */
/**
 * 合伙人收益曲线
 * @param type
 * @param token
 * @return
 *//*

    @POST("Partner/getDetail")
    @FormUrlEncoded
    Observable<BaseBean<List<ProfiteItem>>> getProfitItemList(@Field("type") String type, @Field("token") String token);
*/

    /**
     * 掉起打印机
     *
     * @return
     */
    @POST("Print/getOrderListPrint")
    Observable<BaseBean<String>> sendPrint();


    /*   *//**
     * 二手车
     *
     * @param page
     * @return
     *//*
    @POST("HouseCarList/carList")
    @FormUrlEncoded
    Observable<BaseBean<List<CarHouseItemBean>>> secondCarList(@Field("area_id") String area_id, @Field("page") int page);
*/

    /**
     * 申请二手房车
     *
     * @param type
     * @param token
     * @return
     */
    @POST("SecondHandApply/apply")
    @FormUrlEncoded
    Observable<BaseBean<String>> applyGoods(@Field("type") String type, @Field("token") String token, @Field("phone") String mobile, @Field("name") String name);


    /**
     * 获取地区列表
     *
     * @param pid 父id,一级传0
     * @return
     */

    @POST("AddressApply/getAddress")
    @FormUrlEncoded
    Observable<BaseBean<List<AreaBean>>> getAreaList(@Field("parent_id") String pid);


/**
 * 获取推荐楼盘
 *
 * @param page
 * @return
 *//*

    @POST("Premises/recom")
    @FormUrlEncoded
    Observable<BaseBean<List<HousesItemBean>>> getRecommendHousesList(@Field("page") int page);
*/

    /**
     * 发送邮箱验证码
     *
     * @param email
     * @param type  1，注册短信    2，找回密码   4，支付密码设置
     * @return
     */
    @POST("sendMail/send")
    @FormUrlEncoded
    Observable<BaseBean<String>> getEmailCode(@Field("email") String email, @Field("type") String type);


    /*
     */
/**
 * 获取楼盘详情
 *
 * @param housesId 楼盘id
 * @param token    用户token
 * @return
 *//*

    @POST("Premises/show")
    @FormUrlEncoded
    Observable<BaseBean<HousesDetailBean>> getHousesDetail(@Field("id") String housesId, @Field("token") String token);
*/

    /**
     * 获取地区列表
     *
     * @return
     */
    @POST("Config/area")
    Observable<BaseBean<List<AreaBean>>> getAreaList();

    /*  */

    /**
     * 获取商家类型列表
     *
     * @return
     */
    @POST("Config/trade")
    Observable<BaseBean<List<ShopCategoryBean>>> getShopCategoryList();

    /**
     * 新的接口获取商家类型
     *
     * @param token
     * @param type
     * @return
     */

    @POST("Personal/getclassify")
    @FormUrlEncoded
    Observable<BaseBean<List<ShopCategoryBean>>> getShopCategoryData(@Field("token") String token, @Field("type") int type);


    /**
     * 金豆增值收益列表
     *
     * @param token
     * @return
     */

    @POST("ManageMoney/getFundOrder")
    @FormUrlEncoded
    Observable<BaseBean<List<GoldProfitBean>>> goldProfitList(@Field("token") String token);


    /**
     * 合伙人申请
     *
     * @param token
     * @return
     */
    @POST("Partner/applyPartner")
    @FormUrlEncoded
    Observable<BaseBean<String>> applayPartener(@Field("mobile") String mobile, @Field("name") String name, @Field("token") String token);

    /**
     * 酒店预订计算订单价格
     *
     * @param token
     * @param id      房间id
     * @param shop_id 商家id
     * @param ru_time 入店时间
     * @param li_time 离店时间
     * @return
     */
    @POST("Hotel/calculation")
    @FormUrlEncoded
    Observable<BaseBean<ReservePriceBean>> getReservePrice(@Field("token") String token,
                                                           @Field("id") String id,
                                                           @Field("shop_id") String shop_id,
                                                           @Field("ru_time") String ru_time,
                                                           @Field("li_time") String li_time,
                                                           @Field("num") String num);


    /**
     * 金豆增值详情
     *
     * @param order_id
     * @param token
     * @return
     */

    @POST("ManageMoney/getOrderDetail")
    @FormUrlEncoded
    Observable<BaseBean<GoldAddDetailBean>> goldAddDetail(@Field("order_id") String order_id, @Field("token") String token);


    /**
     * 商家买单
     *
     * @param shopId
     * @return
     */
    @POST("Store/get_shop_mark")
    @FormUrlEncoded
    Observable<BaseBean<String>> buyGoods(@Field("shop_id") String shopId, @Field("token") String token);

    /**
     * 金豆增值的列表
     *
     * @return
     */
    @POST("FundList/fundList")
    Observable<BaseBean<ProdeceBean>> prodectList();

    /**
     * 收藏楼盘
     *
     * @param housesId
     * @param token
     * @return
     */
    @POST("premises/collet")
    @FormUrlEncoded
    Observable<BaseBean<String>> collectHouses(@Field("id") String housesId, @Field("token") String token);

    /**************************个人中心**********************************/
    /*****地址管理*******/
    /**
     * 设为默认地址
     *
     * @param addressId 选中的地址ID
     * @return
     */
    @POST("Address/defa")
    @FormUrlEncoded
    Observable<BaseBean<String>> setdefaultAddress(@Field("id") String addressId, @Field("token") String token);

    /**
     * 地址列表
     *
     * @param pageNumber 页编号   页码
     * @return
     */
    @POST("Address/index")
    @FormUrlEncoded
    Observable<BaseBean<List<AddressItemBean>>> addressList(@Field("page") int pageNumber, @Field("token") String token);

    /**
     * 查看地址
     *
     * @param addressId 地址ID
     * @return
     */
    @POST("Address/show")
    @FormUrlEncoded
    Observable<BaseBean<AddressItemBean>> addressDetail(@Field("id") String addressId, @Field("token") String token);

    /**
     * 删除地址
     *
     * @param addressId 地址ID
     * @return
     */
    @POST("Address/delete")
    @FormUrlEncoded
    Observable<BaseBean<String>> deleteAddress(@Field("id") String addressId, @Field("token") String token);

    /**
     * 编辑地址    修改地址
     *
     * @param id       地址ID
     * @param username 用户名
     * @param mobile   手机号
     * @param address  地址
     * @param pos_code 邮编
     * @param ssx      省市县
     * @param is_defa  是否是默认
     * @return
     */
    @POST("Address/edit")
    @FormUrlEncoded
    Observable<BaseBean<String>> updateAddress(@Field("token") String token,
                                               @Field("id") String id,
                                               @Field("rec_name") String username,
                                               @Field("mobile") String mobile,
                                               @Field("address") String address,
                                               @Field("pos_code") String pos_code,
                                               @Field("ssx") String ssx,
                                               @Field("is_defa") String is_defa);

    /**
     * 新增地址
     *
     * @param token
     * @param username 用户名
     * @param mobile   手机号
     * @param address  地址
     * @param pos_code 邮编
     * @param ssx      省市县
     * @param is_defa  是否是默认
     * @return
     */
    @POST("Address/edit")
    @FormUrlEncoded
    Observable<BaseBean<String>> addAddress(@Field("token") String token,
                                            @Field("rec_name") String username,
                                            @Field("mobile") String mobile,
                                            @Field("address") String address,
                                            @Field("pos_code") String pos_code,
                                            @Field("ssx") String ssx,
                                            @Field("is_defa") String is_defa);
    /*
     */

    /**
     * 推荐列表
     *
     * @param page
     * @param token
     * @return
     */

    @POST("Refer/index")
    @FormUrlEncoded
    Observable<BaseBean<List<RecomendListBean>>> recomendList(@Field("page") String page, @Field("token") String token);


    /**
     * 验证码验证
     *
     * @param mobile 手机号
     * @param code   验证码
     * @return
     */
    @POST("Login/test")
    @FormUrlEncoded
    Observable<BaseBean<String>> codeIsTrue(@Field("mobile") String mobile, @Field("code") String code);

    /**
     * 修改密码
     *
     * @param mobile   手机号
     * @param code     验证码
     * @param password 密码
     * @param
     * @return
     */
    @POST("Login/savepwd")
    @FormUrlEncoded
    Observable<BaseBean<String>> updatePwd(@Field("mobile") String mobile, @Field("code") String code, @Field("password") String password);

    /**
     * 验证修改密码验证码
     *
     * @param mobile
     * @param code
     * @param token
     * @return
     */
    @POST("UserInfo/verify")
    @FormUrlEncoded
    Observable<BaseBean<String>> updateCodeIsTrue(@Field("mobile") String mobile, @Field("code") String code, @Field("token") String token);


    /**
     * 是否首次消费
     * @param token
     * @return
     */
    @POST("Personal/firstorderinfo")
    @FormUrlEncoded
    Observable<BaseBean<StatusBean>> isFirstOrder(@Field("token") String token);

    /**
     * 获取体验金数量
     * @param token
     * @return
     */
    @POST("Personal/gettiyan")
    @FormUrlEncoded
    Observable<BaseBean<TiYanBean>> getTiYanNum(@Field("token") String token);

    /**
     * 获取体验金
     * @param token
     * @return
     */
    @POST("Personal/geteach")
    @FormUrlEncoded
    Observable<BaseBean<String>> holdTiyan(@Field("token") String token);


/**
 * 我的收藏
 *
 * @param token
 * @return
 *//*

    @POST("Personal/collection")
    @FormUrlEncoded
    Observable<BaseBean<List<MyStorageBean>>> myStorage(@Field("page") int page, @Field("token") String token);
*/

    /*   *//**
     * 商户收款码
     *
     * @param token
     * @return
     *//*
    @POST("Store/payee")
    @FormUrlEncoded
    Observable<BaseBean<CodeBean>> codeBean(@Field("token") String token);*/

    /**
     * 金豆提现
     *
     * @param money   提现金额
     * @param cardid  银行卡ID
     * @param paypass 支付密码
     * @param edition app 版本号
     * @param token
     * @return
     */
    @POST("Gold/cash")
    @FormUrlEncoded
    Observable<BaseBean<Object>> goldCash(@Field("money") String money, @Field("card_id") String cardid, @Field("pay_pass") String paypass,@Field("edition") String edition, @Field("token") String token);

    /**
     * 实际到账金额
     * @param money   体现金额
     * @param token   用户token
     * @return
     */
    @POST("Gold/actual")
    @FormUrlEncoded
    Observable<BaseBean<String>> getWithDrawMoney(@Field("money")String money,@Field("token")String token);


    /**
     * 我的订单删除
     *
     * @param id    删除ID 多个ID用’,’隔开
     * @param token
     * @return
     */
    @POST("Personal/odel")
    @FormUrlEncoded
    Observable<BaseBean<String>> myorderDelete(@Field("id") String id, @Field("token") String token);

    /**
     * 添加银行卡
     *
     * @param name             开户人姓名
     * @param bank_card_number 银行卡号
     * @param bank             所属银行
     * @param token
     * @return
     */
    @POST("Bank/add")
    @FormUrlEncoded
    Observable<BaseBean<String>> addBankCard(@Field("name") String name, @Field("bank_card_number") String bank_card_number, @Field("card_no") String bankNum, @Field("token") String token, @Field("bank") String bank);

    /**
     * 删除银行卡
     *
     * @param id    银行卡ID
     * @param token
     * @return
     */
    @POST("Bank/delete")
    @FormUrlEncoded
    Observable<BaseBean<String>> deleteBankCard(@Field("id") String id, @Field("token") String token);

    /**
     * 银行卡列表
     *
     * @param page
     * @param token
     * @return
     */
    @POST("Bank/index")
    @FormUrlEncoded
    Observable<BaseBean<BankCardBean>> bankCardList(@Field("page") int page, @Field("token") String token);

    /**
     * 银豆转账申请
     *
     * @param mobile 收人人手机号
     * @param money  转账金额
     * @param token
     * @return
     */
    @POST("Silver/transfer")
    @FormUrlEncoded
    Observable<BaseBean<String>> silverTransfer(@Field("mobile") String mobile, @Field("money") String money, @Field("token") String token);
    /*

     */
/**
 * 二手车房
 *
 * @param s_id
 * @param type
 * @return
 *//*

    @POST("HouseCarList/getDetail")
    @FormUrlEncoded
    Observable<BaseBean<OldCarAndHouseBean>> oldCarAnd(@Field("s_id") String s_id, @Field("type") int type);
*/


    /**
     * 闪屏页
     *
     * @return
     */
    @POST("Carousel/welcome")
    Observable<BaseBean<String>> getWelcomePage();


    /**
     * 转账时候需要确认的信息
     *
     * @param mobile 接受方的手机号
     * @param token
     * @return
     */
    @POST("Silver/confirmUser")
    @FormUrlEncoded
    Observable<BaseBean<CustomerBean>> customerMessage(@Field("mobile") String mobile, @Field("token") String token);

    /**
     * 银豆转账提交
     *
     * @param orderId  订单编号 (从银豆转账申请的接口结果中获取)
     * @param pay_pass 支付密码
     * @param token
     * @return
     */
    @POST("Silver/transpay")
    @FormUrlEncoded
    Observable<BaseBean<Object>> silverTransferCommit(@Field("order_id") String orderId, @Field("pay_pass") String pay_pass, @Field("token") String token);

    /**
     * 修改昵称
     *
     * @param name  昵称
     * @param token
     * @return
     */
    @POST("UserInfo/nickname")
    @FormUrlEncoded
    Observable<BaseBean<String>> updateNickname(@Field("name") String name, @Field("token") String token);

    /**
     * 修改性别    传值为1时，男 传值为 2 时 为 女
     *
     * @param sex
     * @param token
     * @return
     */
    @POST("UserInfo/sex")
    @FormUrlEncoded
    Observable<BaseBean<String>> updateSex(@Field("sex") String sex, @Field("token") String token);


    /**
     * 修改头像
     *
     * @param body
     * @return
     */
    @POST("UserInfo/pic")
    Observable<BaseBean<Object>> updateHeadPic(@Body RequestBody body);

    /**
     * 银豆转账列表
     *
     * @param type  转账类型 1转出 2转入
     * @param token
     * @return
     */
    @POST("Silver/index")
    @FormUrlEncoded
    Observable<BaseBean<List<SilverTransferBean>>> silverTransfer(@Field("type") int type, @Field("token") String token);

    /**
     * 银豆转账详情
     *
     * @param type  转账类型(转账列表带过来的) 1转出 2转入
     * @param id    转账ID
     * @param token
     * @return
     */
    @POST("Silver/show")
    @FormUrlEncoded
    Observable<BaseBean<SilverTransferDetailBean>> silverTransferDetail(@Field("type") int type, @Field("id") String id, @Field("token") String token);

    /**
     * 身份认证
     *
     * @param token
     * @return
     */
    @POST("UserInfo/identity")
    @FormUrlEncoded
    Observable<BaseBean<IdentityStatusBean>> identityStatus(@Field("token") String token);

    /**
     * 用户信息
     *
     * @param token
     * @return
     */
    @POST("Auth/info")
    @FormUrlEncoded
    Observable<BaseBean<UserInforBean>> userInfor(@Field("token") String token);

    /*    *//**
     * 盈利金记录
     *
     * @param type 类型 传1为收入 传2为提现记录
     * @param page
     * @return
     *//*
    @POST("Earning/index")
    @FormUrlEncoded
    Observable<BaseBean<List<KimProRecordBean>>> kimproRecord(@Field("type") int type, @Field("page") int page, @Field("token") String token);*/
    /*

     */
/**
 * 盈利金记录详情
 *
 * @param type
 * @param id
 * @return
 *//*

    @POST("Earning/detail")
    @FormUrlEncoded
    Observable<BaseBean<KimProRecordDetailBean>> kimRecordDetail(@Field("type") int type, @Field("id") String id, @Field("token") String token);
*/

    /*  *//**
     * 根据电话号码搜索盈利金记录
     *
     * @param mobile 付款方手机号
     * @param token
     * @return
     *//*
    @POST("Earning/serachTrans")
    @FormUrlEncoded
    Observable<BaseBean<List<KimProRecordBean>>> searchKim(@Field("mobile") String mobile, @Field("token") String token);
*/

    /**
     * 盈利金退款
     *
     * @param orderId  订单ID
     * @param pay_pass 支付密码
     * @param token
     * @return
     */
    @POST("Earning/drawback")
    @FormUrlEncoded
    Observable<BaseBean<String>> reFund(@Field("id") String orderId, @Field("pay_pass") String pay_pass, @Field("token") String token);


    /**
     * 我的返现
     *
     * @param page  页面
     * @param token token
     * @return
     */
    @POST("User/backMoney")
    @FormUrlEncoded
    Observable<BaseBean<CashBackBean>> cashBackList(@Field("page") int page,
                                                    @Field("token") String token);


    @POST("FundBuy/order")
    @FormUrlEncoded
    Observable<BaseBean<BuyProfitBean>> buyProfit(@Field("pass_word") String pass_word, @Field("f_id") String f_id, @Field("pay_gold") String pay_gold, @Field("token") String token);


    /*  */

    /**
     * 银行卡展示
     *
     * @param token
     * @return
     */
    @POST("Earning/show")
    @FormUrlEncoded
    Observable<BaseBean<BankCard>> bankCard(@Field("token") String token);

    /**
     * 获取提现额度
     *
     * @param token
     * @return
     */
    @POST("Gold/getedu")
    @FormUrlEncoded
    Observable<BaseBean<String>> getedu(@Field("token") String token);


    /**
     * 删除消费记录
     *
     * @param token
     * @param id
     * @return
     */
    @POST("Gold/del_record")
    @FormUrlEncoded
    Observable<BaseBean<Object>> del_record(@Field("token") String token,
                                            @Field("id") String id);


    /**
     * 上传认证接口
     *
     * @return
     */
    @POST("UserInfo/identitySubmit")
    Observable<BaseBean<Object>> uploadIdentity(@Body RequestBody requestBody);

    /**
     * 设置支付密码
     *
     * @param pay_pass 支付密码
     * @param token
     * @return
     */
    @POST("UserInfo/editpwd")
    @FormUrlEncoded
    Observable<BaseBean<String>> setPayPwd(@Field("pay_pass") String pay_pass, @Field("token") String token);

    /**
     * 修改支付密码
     *
     * @param mobile   手机号
     * @param code     验证码
     * @param pay_pass 支付密码
     * @param token
     * @return
     */
    @POST("UserInfo/savePassPwd")
    @FormUrlEncoded
    Observable<BaseBean<String>> updatePayPwd(@Field("mobile") String mobile, @Field("code") String code, @Field("pay_pass") String pay_pass, @Field("token") String token);

    /**
     * 盈利金提现申请
     *
     * @param id         银行ID
     * @param cash_money 提现金额
     * @param pay_pass   支付密码
     * @param token
     * @return
     */
    @POST("Earning/cash")
    @FormUrlEncoded
    Observable<BaseBean<Object>> withDraw(@Field("id") String id, @Field("cash_money") String cash_money, @Field("pay_pass") String pay_pass, @Field("token") String token);

    /* *//**
     * 预约记录
     *
     * @param token
     * @return
     *//*
    @POST("Personal/bespeak")
    @FormUrlEncoded
    Observable<BaseBean<List<AppoinmentRecordBean>>> appointmentRecordList(@Field("page") int page, @Field("token") String token);
*/

    /**
     * 我的钱包
     *
     * @param token
     * @return
     */
    @POST("Wallet/purse")
    @FormUrlEncoded
    Observable<BaseBean<WalletBean>> wallet(@Field("token") String token);


    /*  */

    /**
     * 我的推荐码
     *
     * @param token
     * @return
     */
    @POST("User/invite")
    @FormUrlEncoded
    Observable<BaseBean<MyTowCodeBean>> userPurchase(@Field("token") String token);

    /*  *//**
     * 普通用户介绍
     *
     * @param token
     * @return
     *//*
    @POST("About/user")
    @FormUrlEncoded
    Observable<BaseBean<UserLevelBean>> userNomalLevel(@Field("token") String token);
*/
    /*  *//**
     * VIP会员介绍
     *
     * @param token
     * @return
     *//*
    @POST("About/vip")
    @FormUrlEncoded
    Observable<BaseBean<UserLevelBean>> userVipLevel(@Field("token") String token);
*/
    /* *//**
     * 看房记录
     *
     * @param token
     * @return
     *//*
    @POST("Personal/showings")
    @FormUrlEncoded
    Observable<BaseBean<List<ShowingRecordBean>>> showingRecordList(@Field("page") int page, @Field("token") String token);
*/

    /**
     * 收藏/预约/看房 删除
     *
     * @param id    删除ID 多个ID用’,’隔开
     * @param type  删除的类型 1:收藏删除 2:预约和看房删除
     * @param token
     * @return
     */
    @POST("Personal/coll_del")
    @FormUrlEncoded
    Observable<BaseBean<String>> deleteList(@Field("id") String id, @Field("type") int type, @Field("token") String token);

    /*  */

    /**
     * 我的订单
     *
     * @param type  消费类型 1，商家消费，2，商品消费，3，充值，4，提现
     * @param page
     * @param token
     * @return
     */
    @POST("Personal/myorder")
    @FormUrlEncoded
    Observable<BaseBean<List<GoldBuyRecordBean>>> orderList(@Field("type") int type, @Field("page") int page, @Field("token") String token);

    /* */

    /**
     * 订单详情
     *
     * @param type    查看类型 1:商家消费 2:商品消费 3:充值 4:提现
     * @param type_id 详细ID
     * @param token
     * @return
     */
    @POST("Personal/oinfo")
    @FormUrlEncoded
    Observable<BaseBean<OrderDetailBean>> orderDetail(@Field("type") String type, @Field("type_id") String type_id, @Field("token") String token);

    /**
     * 提现详情
     *
     * @param
     * @param type_id 详细ID
     * @param token
     * @return
     */
    @POST("Personal/tixianxq")
    @FormUrlEncoded
    Observable<BaseBean<WithDrawBean>> withDrawDetail(@Field("type_id") String type_id, @Field("token") String token);



    /*  *//**
     * 轮播图
     *
     * @param type 1:首页 2:楼盘 3:商城
     * @return
     *//*
    @POST("Carousel/banner")
    @FormUrlEncoded
    Observable<BaseBean<List<BannerItemBean>>> getBannerList(@Field("type") int type);
*/
    /* *//**
     * 限时特卖列表
     *
     * @param page
     * @return
     *//*
    @POST("Goods/index")
    @FormUrlEncoded
    Observable<BaseBean<List<FlashSaleItemBean>>> getFlashSaleList(@Field("page") int page);
*/
    /*  *//**
     * 获取商品详情
     *
     * @param goodsId 商品id
     * @return
     *//*
    @POST("Goods/show")
    @FormUrlEncoded
    Observable<BaseBean<GoodsDetailBean>> getGoodsDetail(@Field("id") String goodsId);
*/

    /**
     * 预约看房
     *
     * @param housesId  楼盘id
     * @param token
     * @param name
     * @param mobile
     * @param look_time 预约看房时间，时间戳格式
     * @return
     */
    @POST("premises/futurepre")
    @FormUrlEncoded
    Observable<BaseBean<Object>> appointmentSeeHouse(@Field("pid") String housesId, @Field("token") String token, @Field("name") String name, @Field("mobile") String mobile, @Field("look_time") String look_time);

    /*  */

    /**
     * 获取商家入驻状态
     *
     * @param token
     * @return
     */
    @POST("Shop/enter")
    @FormUrlEncoded
    Observable<BaseBean<EnteringStatusBean>> getEnteringStatus(@Field("token") String token);


    /**
     * 商家入驻提交资料
     *
     * @param
     * @return
     */
    @POST("Shop/enterSubmit")
    @FormUrlEncoded
    Observable<BaseBean<String>> postEnteringInfo(@Field("token") String token, @Field("sp_name") String sp_name,
                                                  @Field("sp_address") String sp_address, @Field("classify_id") String classify_id,
                                                  @Field("enter_name") String enter_name, @Field("enter_mobile") String enter_mobile,
                                                  @Field("style") int type);

    /**
     * 商家入驻提交资料
     *
     * @param token
     * @param sp_name
     * @param province_id
     * @param xian_id
     * @param city_id
     * @param sp_address
     * @param classify_id
     * @param enter_name
     * @param enter_mobile
     * @param type
     * @return
     */
    @POST("Shop/enterSubmit")
    @FormUrlEncoded
    Observable<BaseBean<String>> postEnterSubmitInfo(@Field("token") String token, @Field("sp_name") String sp_name,
                                                     @Field("sp_address") String sp_address,
                                                     @Field("classify_id") String classify_id, @Field("enter_name") String enter_name,
                                                     @Field("enter_mobile") String enter_mobile,
                                                     @Field("style") int type,
                                                     @Field("province_id") String province_id, @Field("xian_id") String xian_id,
                                                     @Field("city_id") String city_id);

    /**
     * 获取入驻店铺详情
     *
     * @param token
     * @return
     */
    @POST("Shop/information")
    @FormUrlEncoded
    Observable<BaseBean<ShopDetailBean>> getEnteringDetail(@Field("token") String token);

    /**
     * 获取收货地址
     *
     * @param token
     * @return
     */
    @POST("GoodsOrder/address")
    @FormUrlEncoded
    Observable<BaseBean<AddressItemBean>> getShippingAddress(@Field("token") String token);

    /*  *//**
     * 商品下单
     *
     * @param token
     * @param address_id 收货地址id
     * @param goods_id   商品id
     * @param num        数量
     * @return
     *//*
    @POST("GoodsOrder/orderConfirm")
    @FormUrlEncoded
    Observable<BaseBean<GoodsOrderBean>> getGoodsOrder(@Field("token") String token, @Field("address_id") String address_id, @Field("goods_id") String goods_id, @Field("num") int num);
*/
    /*   *//**
     * 搜索
     *
     * @param title 要搜索的关键字
     * @param page
     * @param type  1 搜索楼盘 2 搜索商家 -1 楼盘商家都有
     * @return
     *//*
    @POST("Index/search")
    @FormUrlEncoded
    Observable<BaseBean<SearchResultBean>> search(@Field("title") String title, @Field("page") int page, @Field("type") int type);
*/
    /**
     * 商品下单
     *
     * @param order_id   订单号
     * @param pay_pass   支付密码
     * @param is_support 是否用金豆抵扣，使用银豆支付时传0，使用金豆支付时传1
     * @return
     *//*
    @POST("GoodsOrder/payOrder")
    @FormUrlEncoded
    Observable<BaseBean<PayStatusBean>> goodsPay(@Field("token") String token, @Field("order_id") String order_id, @Field("pay_pass") String pay_pass, @Field("is_support") int is_support);
*/

/**
 * 扫码下单
 *
 * @param token
 * @param shopMark 商家唯一标示
 * @return
 *//*

    @POST("Store/goodsBuyConfirm")
    @FormUrlEncoded
    Observable<BaseBean<SweepOrderBean>> sweepOrder(@Field("token") String token, @Field("mark") String shopMark);
*/

    /*  *//**
     * 扫码下单支付
     *
     * @param token
     * @param shop_id    商家id
     * @param money      金额
     * @param pay_pass   支付密码
     * @param is_support 是否用金豆抵扣，使用银豆支付时传0，使用金豆支付时传1
     * @return
     *//*
    @POST("Store/payOrder")
    @FormUrlEncoded
    Observable<BaseBean<PayStatusBean>> sweepOrderPay(@Field("token") String token, @Field("shop_id") String shop_id, @Field("money") String money, @Field("pay_pass") String pay_pass, @Field("is_support") int is_support);
*/

    /**
     * 消息列表
     *
     * @param token
     * @param page
     * @return
     */
    @POST("User/message")
    @FormUrlEncoded
    Observable<BaseBean<List<MessageItemBean>>> getMessageList(@Field("token") String token, @Field("page") int page);

    /**
     * 删除消息
     *
     * @param token
     * @param ids
     * @return
     */
    @POST("User/del")
    @FormUrlEncoded
    Observable<BaseBean<Object>> deleteMessage(@Field("token") String token, @Field("id") String ids);

    /**
     * 充值
     *
     * @param token
     * @param money    充值金额
     * @param pay_type 支付宝 1 微信 0
     * @return
     */
    @POST("SilverRecharge/Pay")
    @FormUrlEncoded
    Observable<BaseBean<String>> recharge(@Field("token") String token, @Field("money") String money, @Field("pay_type") int pay_type);

    /**
     * 微信支付
     *
     * @param token
     * @param money
     * @param pay_type
     * @return
     */
    @POST("SilverRecharge/Pay")
    @FormUrlEncoded
    Observable<BaseBean<WXOrderBean>> wxRecharge(@Field("token") String token, @Field("money") String money, @Field("pay_type") int pay_type);


    /**
     * 联系我们
     *
     * @return
     */
    @POST("Config/contact")
    Observable<BaseBean<ContactOusBean>> getContactOusInfo();

    /**
     * 检查更新
     *
     * @param type    传1
     * @param version 当前系统版本号，传versionName
     * @return
     */
    @POST("Version/check")
    @FormUrlEncoded
    Observable<BaseBean<UpdateBean>> checkUpdate(@Field("type") int type, @Field("version") String version);


    /**
     * 银行卡列表
     *
     * @return
     */
    @POST("Config/bank")
    Observable<BaseBean<List<BankCardItem>>> bankCardList();

    /**
     * 金钱的配置接口
     *
     * @return
     */
    @POST("Config/parament")
    Observable<BaseBean<ConfigBean>> configCash();

    /**
     * 消息的数量
     *
     * @return
     */
    @POST("User/noReadNoticeCount")
    @FormUrlEncoded
    Observable<BaseBean<Integer>> messageNum(@Field("token") String token);

    /**
     * 消息的数量
     *
     * @return
     */
    @POST("User/messageDetail")
    @FormUrlEncoded
    Observable<BaseBean<MessageDetailBean>> messageDetail(@Field("id") String id, @Field("token") String token);


    /**
     * 商城首页
     *
     * @param lat
     * @param lng
     * @return
     */
    @POST("Stores/shopindex")
    @FormUrlEncoded
    Observable<BaseBean<ShopHomeBean>> shopindexHome(@Field("lat") String lat,
                                                     @Field("lng") String lng,
                                                     @Field("shiqu") String shiqu,
                                                     @Field("p") int p);

    /**
     * 福返酒店列表
     *
     * @param lat
     * @param lng
     * @param type
     * @param page
     * @return
     */
    @POST("Hotel/hotel_list")
    @FormUrlEncoded
    Observable<BaseBean<FuBackHotelBean>> hoteList(@Field("lat") String lat,
                                                   @Field("lng") String lng,
                                                   @Field("type") String type,
                                                   @Field("page") int page);

    /**
     * 福返房产
     *
     * @return
     */
    @POST("Property/lists")
    @FormUrlEncoded
    Observable<BaseBean<HousePropertyBean>> houseProperty(@Field("page") String page,
                                                          @Field("classify_id") String classify_id);


    /**
     * 商品分类
     *
     * @return
     */
    @POST("Special/lists")
    Observable<BaseBean<List<SpecialPrefectureListviewBean>>> Commodityclassification();

    /**
     * 商品列表和搜索
     *
     * @return
     */
    @POST("Special/commodity")
    @FormUrlEncoded
    Observable<BaseBean<SpecialListListviewBean>> commodityList(@Field("id") String id,
                                                                @Field("title") String title,
                                                                @Field("page") int page);

    /**
     * 商品详情
     *
     * @return
     */
    @POST("Special/details")
    @FormUrlEncoded
    Observable<BaseBean<SpecialtyDetailsBean>> Specialdetails(@Field("id") String id,
                                                              @Field("token") String token);

    /**
     * 收藏取消收藏
     *
     * @param id
     * @param token
     * @return
     */
    @POST("Special/collection")
    @FormUrlEncoded
    Observable<BaseBean<ObjectBean>> collection(@Field("token") String token,
                                                @Field("id") String id);


    /**
     * 房产详情页
     *
     * @param id
     * @return
     */
    @POST("Property/details")
    @FormUrlEncoded
    Observable<BaseBean<CarAndRoomsBean>> Propertydetails(@Field("token") String token,
                                                          @Field("id") String id,
                                                          @Field("lng") String longtitude,
                                                          @Field("lat") String latitude,
                                                          @Field("page") int page);

    /**
     * 产品详情页id
     *
     * @param shop_id
     * @return
     */
    @POST("Property/product")
    @FormUrlEncoded
    Observable<BaseBean<HousedetailsBean>> Propertyproduct(@Field("id") String id, @Field("shop_id") String shop_id);

    /**
     * 预约看房
     *
     * @param id 房产id
     * @return
     */
    @POST("Property/showings")
    @FormUrlEncoded
    Observable<BaseBean<ReservationsBean>> Propertyshowings(@Field("id") String id);

    /**
     * 预约
     *
     * @param token
     * @param id     房产id
     * @param name   姓名
     * @param mobile 手机号
     * @param time   时间
     * @return
     */
    @POST("Property/makean")
    @FormUrlEncoded
    Observable<BaseBean<ObjectBean>> Propertymakean(@Field("token") String token,
                                                    @Field("id") String id,
                                                    @Field("name") String name,
                                                    @Field("mobile") String mobile,
                                                    @Field("time") String time);

    /**
     * 收藏取消收藏
     *
     * @param id
     * @param token
     * @return
     */
    @POST("Property/shop_Collection")
    @FormUrlEncoded
    Observable<BaseBean<ObjectBean>> Propertyshop_Collection(@Field("id") String id,
                                                             @Field("token") String token);

    /**
     * 预定房间
     *
     * @param token
     * @param id
     * @param shop_id
     * @return banben   只为判断显示房间数量   不传的时候默认返回1 间，返回的话，可以返回多间。
     */
    @POST("Hotel/reserve")
    @FormUrlEncoded
    Observable<BaseBean<ReserveTitleBean>> Hotelreserve(@Field("token") String token,
                                                        @Field("id") String id,
                                                        @Field("shop_id") String shop_id,
                                                        @Field("banben") String banben);

    /**
     * 福返酒店详情
     *
     * @param id
     * @param lat
     * @param lng
     * @param limit
     * @return
     */
    @POST("Hotel/hotel_info")
    @FormUrlEncoded
    Observable<BaseBean<FuBackHotelDetailsBean>> hotelInfo(@Field("id") String id,
                                                           @Field("lat") String lat,
                                                           @Field("lng") String lng,
                                                           @Field("limit") int limit);

    /**
     * 特产商品评价
     *
     * @param id
     * @param page
     * @return
     */
    @POST("Special/pinglun")
    @FormUrlEncoded
    Observable<BaseBean<SpecialtyCommodityEvaluateBean>> pinglun(@Field("id") String id,
                                                                 @Field("page") String page);

    /**
     * 福袋专区评论
     *
     * @param id
     * @param page
     * @return
     */
    @POST("Special/pinglun_each")
    @FormUrlEncoded
    Observable<BaseBean<EachChildCommentBean>> pinglun_each(@Field("id") String id,
                                                            @Field("page") String page);

    /**
     * 预订酒店
     *
     * @param map
     * @return
     */
    @POST("Hotel/addorder")
    @FormUrlEncoded
    Observable<BaseBean<String>> addorder(@FieldMap() Map<String, Object> map);


    /**
     * 排行
     *
     * @param type
     * @param page
     * @return
     */
    @POST("Property/sales")
    @FormUrlEncoded
    Observable<BaseBean<RankingBean>> sales(@Field("type") String type,
                                            @Field("page") String page);


    /**
     * 全部商品分类
     *
     * @param id
     * @return
     */
    @POST("hotel/goods_cate")
    @FormUrlEncoded
    Observable<BaseBean<LargeCategoryBean>> goodsCate(@Field("token") String token, @Field("id") String id);

    /**
     * 全部商品
     *
     * @param type
     * @param cate
     * @param shop_id
     * @param lat
     * @param lng
     * @param page
     * @return
     */
    @POST("Hotel/goods_list")
    @FormUrlEncoded
    Observable<BaseBean<SmallCategoryBean>> goodsList(@Field("token") String token,
                                                      @Field("type") String type,
                                                      @Field("cate") String cate,
                                                      @Field("shop_id") String shop_id,
                                                      @Field("lat") String lat,
                                                      @Field("lng") String lng,
                                                      @Field("page") String page);

    /**
     * 单品详情
     *
     * @param id
     * @return
     */
    @POST("Hotel/goodinfo")
    @FormUrlEncoded
    Observable<BaseBean<DanpinDetail>> goodinfo(@Field("id") String id,
                                                @Field("token") String token,
                                                @Field("page") int page);

    /**
     * 单品购物车列表
     *
     * @param token
     * @param id
     * @return
     */
    @POST("Shopgoods/cartlist")
    @FormUrlEncoded
    Observable<BaseBean<CartListBean>> cartlist(@Field("token") String token,
                                                @Field("id") String id);

    /**
     * 单品加入购物车
     *
     * @param token
     * @param id
     * @param num
     * @return
     */
    @POST("Hotel/addcart")
    @FormUrlEncoded
    Observable<BaseBean<Object>> addcart(@Field("token") String token,
                                         @Field("shop_id") String shop_id,
                                         @Field("id") String id,
                                         @Field("num") String num);


    /**
     * 福袋专区列表接口
     *
     * @return
     */
    @POST("Special/eachchild")
    Observable<BaseBean<List<EachChildBean>>> eachchild();

    /**
     * 福袋专区详情页接口
     *
     * @param id
     * @return
     */
    @POST("Special/getFudaiDetails")
    @FormUrlEncoded
    Observable<BaseBean<EachChildDetailsBean>> bag_details(@Field("id") String id);


    /**
     * 特产 加入购物车
     *
     * @param token
     * @param id
     * @param numbers
     * @return
     */
    @POST("Special/shopping")
    @FormUrlEncoded
    Observable<BaseBean<ObjectBean>> shopping(@Field("token") String token,
                                              @Field("id") String id,
                                              @Field("numbers") int numbers);

    /**
     * 立即购买和购物车结算
     *
     * @param token
     * @param id
     * @param types
     * @return
     */
    @POST("Special/purchase")
    @FormUrlEncoded
    Observable<BaseBean<PurchaseBean>> purchase(@Field("token") String token,
                                                @Field("id") String id,
                                                @Field("types") String types);

    /**
     * 福袋专区立即购买
     *
     * @param token
     * @param id
     * @return
     */
    @POST("Special/immediately")
    @FormUrlEncoded
    Observable<BaseBean<ImmediatelyBean>> immediately(@Field("token") String token,
                                                      @Field("id") String id);


    /**
     * 特产 确认订单
     *
     * @param token
     * @param id      商品ID （多个商品逗号分隔）
     * @param number  商品总数量(购物车中需要传)
     * @param rid
     * @param price
     * @param remarks
     * @return
     */
    @POST("Special/add_order")
    @FormUrlEncoded
    Observable<BaseBean<ObjectBean>> addOrder(@Field("token") String token,
                                              @Field("id") String id,
                                              @Field("number") String number,
                                              @Field("rid") String rid,
                                              @Field("price") String price,
                                              @Field("remarks") String remarks,
                                              @Field("is_cart") String is_cart,
                                              @Field("distri_price") String distri_price);

    /**
     * 福袋 确认订单
     *
     * @param token
     * @param id
     * @param number
     * @param rid
     * @param remarks
     * @return
     */
    @POST("Special/order_child")
    @FormUrlEncoded
    Observable<BaseBean<ObjectBean>> orderChild(@Field("token") String token,
                                                @Field("id") String id,
                                                @Field("number") String number,
                                                @Field("rid") String rid,
                                                @Field("remarks") String remarks);


    /**
     * 清空购物车
     *
     * @param token
     * @param id
     * @return
     */
    @POST("Shopgoods/delcart")
    @FormUrlEncoded
    Observable<BaseBean<Object>> delcart(@Field("token") String token,
                                         @Field("id") String id);

    /**
     * 首页的首页数据全部
     *
     * @return
     */
    @POST("Config/notice")
    @FormUrlEncoded
    Observable<BaseBean<HomePageBean>> getHomePageData(@Field("lng") String lng,
                                                       @Field("lat") String lat);

    /**
     * 二手车二手房首页
     *
     * @return
     */
    @POST("Config/carroom")
    @FormUrlEncoded
    Observable<BaseBean<CarHouseBean>> getCarAndHouseData(@Field("lat") String lat,
                                                          @Field("lng") String lng,
                                                          @Field("shiqu") String shiqu);
    /**
     * 二手车二手房首页
     *
     * @return
     */
    @POST("Config/carroom_pro")
    @FormUrlEncoded
    Observable<BaseBean<CarHouseBean>> getCarroomPro(@Field("lat") String lat,
                                                          @Field("lng") String lng,
                                                          @Field("shiqu") String shiqu);

    /**
     * 特产订单列表
     *
     * @param token
     * @param type
     * @param page
     * @return
     */
    @POST("Personal/orderList")
    @FormUrlEncoded
    Observable<BaseBean<List<SpecialtyOrderBean>>> orderList(@Field("token") String token,
                                                             @Field("type") String type,
                                                             @Field("page") int page);

    /**
     * 店铺购物车生成订单
     *
     * @param token
     * @param id
     * @return
     */
    @POST("Hotel/jiesuan")
    @FormUrlEncoded
    Observable<BaseBean<JiesuanBean>> jiesuan(@Field("token") String token,
                                              @Field("id") String id);


    /**
     * 获取个人中心金额
     *
     * @param token
     * @return
     */
    @POST("Auth/getMoney")
    @FormUrlEncoded
    Observable<BaseBean<PersionMoneyBean>> persionMoney(@Field("token") String token);

    /**
     * 店铺提交订单
     *
     * @param token
     * @param address_id 地址id
     * @param shop_id    店铺id
     * @param content    订单备注
     * @param is_since   1到店自取2，店内配送3，店外配送
     * @param canzhuo    餐桌编号
     * @param yudao_time 预计到店时间
     * @param poople     用餐人数
     * @param p_name     称呼
     * @param p_phone    联系方式
     * @return
     */
    @POST("Hotel/suborder")
    @FormUrlEncoded
    Observable<BaseBean<String>> suborder(@Field("token") String token,
                                          @Field("address_id") String address_id,
                                          @Field("shop_id") String shop_id,
                                          @Field("content") String content,
                                          @Field("is_since") String is_since,
                                          @Field("canzhuo") String canzhuo,
                                          @Field("yudao_time") String yudao_time,
                                          @Field("poople") String poople,
                                          @Field("p_name") String p_name,
                                          @Field("p_phone") String p_phone);

    /**
     * 获取储值百分比
     *
     * @param token
     * @param shop_id
     * @return
     */
    @POST("Shop/get_storge")
    @FormUrlEncoded
    Observable<BaseBean<StorgeBean>> getStorge(@Field("token") String token,
                                               @Field("shop_id") String shop_id);

    /**
     * 预定套餐
     *
     * @param token
     * @param id    套餐id
     * @return
     */
    @POST("Hotel/reservepackage")
    @FormUrlEncoded
    Observable<BaseBean<ReservepackageBean>> reservepackage(@Field("token") String token,
                                                            @Field("id") String id);

    /**
     * 提交预定套餐订单
     *
     * @param token
     * @param id
     * @param shop_id
     * @param content
     * @param time
     * @param time_duan
     * @return
     */
    @POST("Hotel/okorder")
    @FormUrlEncoded
    Observable<BaseBean<String>> okorder(@Field("token") String token,
                                         @Field("id") String id,
                                         @Field("shop_id") String shop_id,
                                         @Field("content") String content,
                                         @Field("time") String time,
                                         @Field("time_duan") String time_duan,
                                         @Field("p_name") String p_name, @Field("p_phone") String p_phone);


    /**
     * ktv提交预定套餐订单
     *
     * @param token
     * @param id
     * @param shop_id
     * @param content
     * @param time
     * @param time_duan
     * @return
     */
    @POST("Hotel/okorder")
    @FormUrlEncoded
    Observable<BaseBean<String>> ktvOkorder(@Field("token") String token,
                                            @Field("id") String id,
                                            @Field("shop_id") String shop_id,
                                            @Field("content") String content,
                                            @Field("time") String time,
                                            @Field("time_duan") String time_duan,
                                            @Field("p_name") String p_name, @Field("p_phone") String p_phone);


    /**
     * 获取兑换记录
     *
     * @param type
     * @param page
     * @param token
     * @return
     */
    @POST("Personal/myorder")
    @FormUrlEncoded
    Observable<BaseBean<List<ForRecordBean>>> getForRecord(@Field("type") String type,
                                                           @Field("page") int page,
                                                           @Field("token") String token);

    /**
     * 获取储值记录
     *
     * @param token
     * @param page
     * @return
     */
    @POST("Personal/userstored")
    @FormUrlEncoded
    Observable<BaseBean<List<StoredRecordsBean>>> getStoredRecords(@Field("token") String token,
                                                                   @Field("page") int page);

    /**
     * 获取我的房时中的房时
     *
     * @param token
     * @param page
     * @return
     */
    @POST("Personal/fangshi")
    @FormUrlEncoded
    Observable<BaseBean<List<RoomTimeBean>>> getRoomTime(@Field("token") String token,
                                                         @Field("page") int page);

    /**
     * 获取我的房时中的已兑换
     *
     * @param token
     * @return
     */
    @POST("Personal/yidui")
    @FormUrlEncoded
    Observable<BaseBean<List<AlreadyExchangeBean>>> getAlreadyExchange(@Field("token") String token);


    /**
     * 删除特产订单
     *
     * @param token
     * @param id
     * @param cancel
     * @return
     */
    @POST("Personal/delspecialty")
    @FormUrlEncoded
    Observable<BaseBean<Object>> delspecialty(@Field("token") String token,
                                              @Field("id") String id,
                                              @Field("cancel") String cancel);

    /**
     * 特产订单详情
     *
     * @param token
     * @param number_bh
     * @return
     */
    @POST("TeGoodsDetail/orderDetail")
    @FormUrlEncoded
    Observable<BaseBean<SpecialtyOrderDetailBean>> specialtyOrderDetail(@Field("token") String token,
                                                                        @Field("number_bh") String number_bh);

    /**
     * 快递详情
     *
     * @param token
     * @param number_bh
     * @return
     */
    @POST("TeGoodsDetail/showlogistics")
    @FormUrlEncoded
    Observable<BaseBean<Showlogistics>> showlogistics(@Field("token") String token,
                                                      @Field("number_bh") String number_bh);


    /**
     * 兑换房时
     *
     * @param token
     * @param shop_id
     * @param pay_pass
     * @return
     */
    @POST("Personal/exchange")
    @FormUrlEncoded
    Observable<BaseBean<String>> exchangeRoomTime(@Field("token") String token,
                                                  @Field("shop_id") String shop_id,
                                                  @Field("pay_pass") String pay_pass);

    /**
     * 获取我的收藏
     *
     * @param token
     * @param page
     * @return
     */
    @POST("Core/Core_list")
    @FormUrlEncoded
    Observable<BaseBean<MyCollectionBean>> getMyCollection(@Field("token") String token,
                                                           @Field("page") int page);

    /**
     * 获取我的收藏中的房车
     *
     * @param token
     * @param longitude
     * @param latitude
     * @param page
     * @return
     */
    @POST("Core/house_car_core_list")
    @FormUrlEncoded
    Observable<BaseBean<MyRoomCollectionBean>> getMyRoomCollection(@Field("token") String token,
                                                                   @Field("longitude") String longitude,
                                                                   @Field("latitude") String latitude,
                                                                   @Field("page") int page);

    /**
     * 删除我的收藏中的商品收藏
     *
     * @param token
     * @param id
     * @return
     */
    @POST("Core/core_delete")
    @FormUrlEncoded
    Observable<BaseBean<String>> deleteCollection(@Field("token") String token,
                                                  @Field("id") String id);

    /**
     * 删除我的收藏中的车房收藏
     *
     * @param token
     * @param id
     * @return
     */
    @POST("Core/house_car_core_delete")
    @FormUrlEncoded
    Observable<BaseBean<String>> deleteRoomCollection(@Field("token") String token,
                                                      @Field("id") String id);

    /**
     * 获取预定订单
     *
     * @param token
     * @param type
     * @param page
     * @param num
     * @return
     */
    @POST("Personal/reserveOrderList")
    @FormUrlEncoded
    Observable<BaseBean<ReserveOrderBean>> getReserveOrder(@Field("token") String token,
                                                           @Field("type") int type,
                                                           @Field("page") int page,
                                                           @Field("num") int num);


    /**
     * 看房记录
     *
     * @param type
     * @param page
     * @param token
     * @return
     */
    @POST("Personal/bespeak")
    @FormUrlEncoded
    Observable<BaseBean<LookRoomHistoryBean>> getLookRoomHistory(@Field("type") int type,
                                                                 @Field("page") int page,
                                                                 @Field("token") String token);

    /**
     * 获取预定订单详情
     *
     * @param token
     * @param number_bh
     * @return
     */
    @POST("Personal/reserveDetail")
    @FormUrlEncoded
    Observable<BaseBean<ReserveOrderDetailsBean>> getReserveDetails(@Field("token") String token,
                                                                    @Field("number_bh") String number_bh);

    /**
     * 预定订单评论页接口
     *
     * @param token
     * @param order_id
     * @return
     */
    @POST("Comment/reserve")
    @FormUrlEncoded
    Observable<BaseBean<ReserveOrderCommentBean>> getReserveComment(@Field("token") String token,
                                                                    @Field("order_id") String order_id);

    /**
     * 评论页面接口
     *
     * @param token
     * @param order_id
     * @return
     */
    @POST("Comment/te_comment")
    @FormUrlEncoded
    Observable<BaseBean<List<TeCommentBean>>> teComment(@Field("token") String token,
                                                        @Field("order_id") String order_id);

    /**
     * 删除看房记录
     *
     * @param id
     * @param token
     * @return
     */
    @POST("Personal/bespeak_delete")
    @FormUrlEncoded
    Observable<BaseBean<String>> deleteLookRoomHistory(@Field("id") int id, @Field("token") String token);

    /**
     * 消费记录
     *
     * @param time
     * @param page
     * @param token
     * @return
     */
    @POST("Gold/grecord")
    @FormUrlEncoded
    Observable<BaseBean<List<GRecordBean>>> getGRecordHistory(@Field("time") String time, @Field("page") int page, @Field("token") String token);


    /**
     * 搜索结果页搜索
     *
     * @param token
     * @param lat
     * @param lng
     * @param page
     * @return
     */
    @POST("Config/shousearch")
    @FormUrlEncoded
    Observable<BaseBean<HomeSearchBean>> search(@Field("title") String token,
                                                @Field("lat") String lat,
                                                @Field("lng") String lng,
                                                @Field("mac") String mac,
                                                @Field("page") int page);

    /**
     * 获取商城订单列表
     *
     * @param token
     * @param type
     * @param page
     * @return
     */
    @POST("personal/orders")
    @FormUrlEncoded
    Observable<BaseBean<List<ShopOrderBean>>> getShopOrderList(@Field("token") String token,
                                                               @Field("type") int type,
                                                               @Field("page") int page);

    /**
     * 删除商城订单数据
     *
     * @param token
     * @param id
     * @param cancel
     * @return
     */
    @POST("Personal/delshopping")
    @FormUrlEncoded
    Observable<BaseBean<Object>> delShopOrder(@Field("token") String token,
                                              @Field("id") String id,
                                              @Field("cancel") String cancel);

    /**
     * 获取购物车列表
     *
     * @param page
     * @param token
     * @return
     */
    @POST("Special/shoppinglist")
    @FormUrlEncoded
    Observable<BaseBean<ShoppingCartBean>> getShoppingCart(@Field("page") int page,
                                                           @Field("token") String token);

    /**
     * 增加删除商品个数
     *
     * @param id
     * @param number
     * @param token
     * @return
     */
    @POST("Special/change")
    @FormUrlEncoded
    Observable<BaseBean<String>> deleteShoppingGoods(@Field("id") int id,
                                                     @Field("num") int number,
                                                     @Field("token") String token);

    /**
     * 删除商品列表条目
     *
     * @param id
     * @param token
     * @return
     */
    @POST("Special/delete_shopping")
    @FormUrlEncoded
    Observable<BaseBean<String>> deleteShopping(@Field("id") int id,
                                                @Field("token") String token);


    /**
     * 消费记录详情
     *
     * @param id
     * @param token
     * @return
     */
    @POST("Gold/loginfo")
    @FormUrlEncoded
    Observable<BaseBean<List<PurchaseHistoryDetailsBean>>> getPurchaseHistoryDetails(@Field("id") int id,
                                                                                     @Field("token") String token);

    /**
     * 确认收货
     *
     * @param token
     * @param id
     * @param password
     * @return
     */
    @POST("Personal/goodsreceipt")
    @FormUrlEncoded
    Observable<BaseBean<Object>> goodsreceipt(@Field("token") String token,
                                              @Field("id") String id,
                                              @Field("password") String password);


    /***
     *    领取福袋
     */
  /*  @POST("Personal/geteach")
    @FormUrlEncoded
    Observable<BaseBean<String>> get*/




    /**
     * 发布评论
     *
     * @param body
     * @return
     */
    @POST("Comment/release")
    Observable<BaseBean<Object>> release(@Body RequestBody body);

    /**
     * 申请售后
     *
     * @param body
     * @return
     */
    @POST("Special/applyRefund")
    Observable<BaseBean<Object>> applyRefund(@Body RequestBody body);


    /**
     * 获取收款码
     *
     * @param token
     * @return
     */
    @POST("Personal/showQrcode")
    @FormUrlEncoded
    Observable<BaseBean<ReceiptBean>> getReceiptCode(@Field("token") String token);

    /**
     * 获取付款码
     *
     * @param token
     * @return
     */
    @POST("Personal/addfucode")
    @FormUrlEncoded
    Observable<BaseBean<PayCodeBean>> getPayCode(@Field("token") String token);


    /**
     * 最近搜索记录
     *
     * @return
     */
    @POST("Config/searchnear")
    @FormUrlEncoded
    Observable<BaseBean<List<String>>> getSearchNer(@Field("mac") String mac);

    /**
     * 删除搜索记录
     *
     * @return
     */
    @POST("Config/delnaar")
    @FormUrlEncoded
    Observable<BaseBean<String>> delSearchNer(@Field("mac") String mac);

    /**
     * 商城订单 详情
     *
     * @param token
     * @param number_bh
     * @return
     */
    @POST("GoodsOrderDetail/getGoodsOrderDetail")
    @FormUrlEncoded
    Observable<BaseBean<ShopOrderDetailBean>> getShopOrderDetail(@Field("token") String token,
                                                                 @Field("number_bh") String number_bh);

    /**
     * 删除预定订单
     *
     * @param token
     * @param id
     * @param cancel
     * @return
     */
    @POST("Personal/delreserve")
    @FormUrlEncoded
    Observable<BaseBean<Object>> delReserveOrder(@Field("token") String token,
                                                 @Field("id") String id,
                                                 @Field("cancel") String cancel);

    /**
     * 确认支付 微信
     *
     * @param token
     * @param paytype
     * @param order_sn
     * @param type
     * @param is_support
     * @param pay_pass
     * @return
     */
    @POST("Pay/info")
    @FormUrlEncoded
    Observable<BaseBean<InfoWBean>> infoPreci(@Field("token") String token,
                                              @Field("paytype") String paytype,
                                              @Field("order_sn") String order_sn,
                                              @Field("type") String type,
                                              @Field("is_support") String is_support,
                                              @Field("pay_pass") String pay_pass);

    /**
     * 确认支付 支付宝
     *
     * @param token
     * @param paytype
     * @param order_sn
     * @param type
     * @return
     */
    @POST("Pay/info")
    @FormUrlEncoded
    Observable<BaseBean<String>> infoPreciZ(@Field("token") String token,
                                            @Field("paytype") String paytype,
                                            @Field("order_sn") String order_sn,
                                            @Field("type") String type);

    /**
     * 用户扫码下单页面 微信和余额
     *
     * @param token
     * @param shop_id
     * @param money
     * @param pay_type
     * @param pay_pass
     * @param is_support
     * @return
     */
    @POST("Store/doBuyConfirm")
    @FormUrlEncoded
    Observable<BaseBean<DoBuyConfirmBean>> doBuyConfirm(@Field("token") String token,
                                                        @Field("shop_id") String shop_id,
                                                        @Field("money") String money,
                                                        @Field("pay_type") String pay_type,
                                                        @Field("pay_pass") String pay_pass,
                                                        @Field("is_support") String is_support);

    /**
     * 用户扫码下单页面 支付宝
     *
     * @param token
     * @param shop_id
     * @param money
     * @param pay_type
     * @return
     */
    @POST("Store/doBuyConfirm")
    @FormUrlEncoded
    Observable<BaseBean<String>> doBuyConfirmZ(@Field("token") String token,
                                               @Field("shop_id") String shop_id,
                                               @Field("money") String money,
                                               @Field("pay_type") String pay_type);

    /**
     * 福币支付接口
     *
     * @param token
     * @param order_number
     * @param pay_pass
     * @return
     */
    @POST("special/paychild")
    @FormUrlEncoded
    Observable<BaseBean<Object>> paychild(@Field("token") String token,
                                          @Field("order_number") String order_number,
                                          @Field("pay_pass") String pay_pass);

    /**
     * 商城订单评价
     *
     * @return
     */
    @POST("Comment/shopping")
    @FormUrlEncoded
    Observable<BaseBean<List<ShopOrderCommentBean>>> shopComment(@Field("token") String token,
                                                                 @Field("order_id") String order_id);

    /**
     * 获取热门城市
     *
     * @return
     */
    @POST("shop/getarea")
    Observable<BaseBean<List<GetareaBean>>> getarea();


    /**
     * 商城订单发布评论
     *
     * @param body
     * @return
     */
    @POST("Comment/order_shopping")
    Observable<BaseBean<Object>> shopOrderRelease(@Body RequestBody body);

    /**
     * 商城订单 确认收货
     *
     * @param token
     * @param id
     * @param password
     * @return
     */
    @POST("Personal/shopsreceipt")
    @FormUrlEncoded
    Observable<BaseBean<Object>> shopGoodsreceipt(@Field("token") String token,
                                                  @Field("id") String id,
                                                  @Field("password") String password);

    /**
     * 预定订单评论
     *
     * @param body
     * @return
     */
    @POST("Comment/reserve_rele")
    Observable<BaseBean<Object>> reserveOrderComment(@Body RequestBody body);

    /**
     * 商城订单退款
     *
     * @param token
     * @param tui_price
     * @param cancelorder
     * @param order_id
     * @param tui_content
     * @return
     */
    @POST("Store/applyRefund")
    @FormUrlEncoded
    Observable<BaseBean<Object>> shopOrderRefund(@Field("token") String token,
                                                 @Field("tui_price") String tui_price,
                                                 @Field("cancelorder") String cancelorder,
                                                 @Field("order_id") String order_id,
                                                 @Field("tui_content") String tui_content);


    /**
     * 获取福袋订单列表
     *
     * @param token
     * @param type
     * @param page
     * @return
     */
    @POST("Personal/fudaiList")
    @FormUrlEncoded
    Observable<BaseBean<List<LuckyBagBean>>> getLuckyBagList(@Field("token") String token,
                                                             @Field("type") String type,
                                                             @Field("page") int page);

    /**
     * 福袋订单详情
     *
     * @param token
     * @param number_bh
     * @return
     */
    @POST("Personal/fudaiDetail")
    @FormUrlEncoded
    Observable<BaseBean<LuckyBagOrderDetailBean>> getLuckyBagDetail(@Field("token") String token,
                                                                    @Field("number_bh") String number_bh);

    /**
     * 删除福袋订单
     *
     * @param token
     * @param order_id
     * @param cancel
     * @return
     */
    @POST("Personal/confirm_detele")
    @FormUrlEncoded
    Observable<BaseBean<Object>> delLuckyBagOrder(@Field("token") String token,
                                                  @Field("order_id") String order_id,
                                                  @Field("cancel") String cancel);

    /**
     * 福袋订单确认收货
     *
     * @param token
     * @param id
     * @param password
     * @return
     */
    @POST("Personal/confirm")
    @FormUrlEncoded
    Observable<BaseBean<Object>> LukyBagsreceipt(@Field("token") String token,
                                                 @Field("order_id") String id,
                                                 @Field("password") String password);

    /**
     * 福袋订单评论
     *
     * @param token
     * @param order_id
     * @return
     */
    @POST("Comment/child_comment")
    @FormUrlEncoded
    Observable<BaseBean<LuckyBagCommentBean>> getLuckyBagComment(@Field("token") String token,
                                                                 @Field("order_id") String order_id);

    /**
     * 店铺储值 微信
     *
     * @return
     */
    @POST("Shop/storage")
    @FormUrlEncoded
    Observable<BaseBean<StorageBean>> storage(@Field("token") String token,
                                              @Field("shop_id") String shop_id,
                                              @Field("money") String money,
                                              @Field("paytype") String paytype);

    /**
     * 店铺储值 支付宝
     *
     * @return
     */
    @POST("Shop/storage")
    @FormUrlEncoded
    Observable<BaseBean<String>> storageZ(@Field("token") String token,
                                          @Field("shop_id") String shop_id,
                                          @Field("money") String money,
                                          @Field("paytype") String paytype);


    /**
     * 用户扫码下单页面
     *
     * @return
     */
    @POST("Store/goodsBuyConfirm")
    @FormUrlEncoded
    Observable<BaseBean<GoodsBuyConfirmBean>> goodsBuyConfirm(@Field("token") String token,
                                                              @Field("mark") String mark);

    /**
     * 获取测试数据
     * @return
     */
//    @POST("Special/lists")
//    Observable<BaseBean<List<TestBean>>> getTestData();

    /**
     * 福袋订单发表评论
     *
     * @param body
     * @return
     */
    @POST("Comment/child_area")
    Observable<BaseBean<Object>> luckyBagOrderComment(@Body RequestBody body);

    /**
     * 发布评论接口
     *
     * @param body
     * @return
     */
    @POST("Comment/business_ment")
    Observable<BaseBean<Object>> businesMent(@Body RequestBody body);


    /**
     * 实名认证获取token
     *
     * @param token
     * @return
     */
    @POST("Personal/rpbasic")
    @FormUrlEncoded
    Observable<BaseBean<IdentyBean>> idention(@Field("token") String token);

    /**
     * 更新实名认证数据
     *
     * @param token
     * @return
     */
    @POST("Personal/saverpbasic")
    @FormUrlEncoded
    Observable<BaseBean<String>> updateIdention(@Field("token") String token);

    /**
     * 预定订单退款
     *
     * @param token
     * @param orderId
     * @param cancelorder
     * @param tui_price
     * @param tui_content
     * @return
     */
    @POST("Hotel/applyRefund")
    @FormUrlEncoded
    Observable<BaseBean<Object>> reserveRefund(@Field("token") String token,
                                               @Field("order_id") String orderId,
                                               @Field("cancelorder") String cancelorder,
                                               @Field("tui_price") String tui_price,
                                               @Field("tui_content") String tui_content);


    /**
     * 提醒发货
     *
     * @param token
     * @param order_id
     * @param type     1 特产  2福袋  3商城
     * @return
     */
    @POST("Personal/remind")
    @FormUrlEncoded
    Observable<BaseBean<Object>> remind(@Field("token") String token,
                                        @Field("order_id") String order_id,
                                        @Field("type") String type);

    /**
     * 评论页面接口
     *
     * @param token
     * @param shop_id
     * @return
     */
    @POST("Comment/business")
    @FormUrlEncoded
    Observable<BaseBean<BusinessBean>> business(@Field("token") String token,
                                                @Field("shop_id") String shop_id);

    /**
     * 首页推荐加点浏览量
     *
     * @param id
     * @return
     */
    @POST("Config/setinc")
    @FormUrlEncoded
    Observable<BaseBean<String>> addBrowse(@Field("id") String id);

}