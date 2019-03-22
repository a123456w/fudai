package com.qlzx.mylibrary.http;


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

}