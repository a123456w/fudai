package com.qlzx.mylibrary.util;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * Created by Anthony on 2016/5/23.
 * Class Note:
 * SharedPreference Helper class
 */
public class PreferencesHelper {
    private static final String SETTING = "setting";
    private static final String ID = "USER_ID";
    private static final String TOKEN = "token";
    private static final String PRIVACYPWD = "PrivacyPwd";
    private static final String OPEN_PRIVACY_PWD = "openPrivacyPwd";
    private static final String SMSCODE = "smsCode";
    private static final String TIMESTAMP = "timestamp";
    private static final String FIRST_TIME = "firstTime";
    private static final String ISLOGIN = "isLogin";
    private static final boolean FIRST_TIME_DEFAULT = true;
    private static final String PREF_KEY_SIGNED_IN_RIBOT = "PREF_KEY_SIGNED_IN_RIBOT";
    private final SharedPreferences mPref;
    public static final String SP_HOME_DATA = "M_CACHE_DATA";
    public static final String SP_LOCALTION = "LOCALTION";
    public static final String SP_VERSION = "VERSION";
    public static final String KEY_VERSION_INT = "version";
    public static final String USER_INFO = "userInfo";
    public static final String INIT_INFO = "initInfo";
    public static final String FENDA_CATEGORY = "fendaCategory";
    public static final String KEY_NOTIFY_LAST_TIME = "notify_last_time";
    private Context context;
    private static final String KEY_VERSON_TYPE = "type";
    private static final String PHONE_NUM = "phone_num";
    private static final String IGNORE_VERSION = "ignore_version";
    private static final String REGISTRATION_ID = "registrationID";


    private static final String CITYNAME = "cityname";
    private static final String LONGTITUDE = "LANGGINTUDITU";
    private static final String LATITUDE = "LATITUDE";

    private static final String USERNUMBER = "USERNUMBER";

    /**
     * 获取用户名
     * @return
     */
    public String getUserName(){
        return getString(USERNUMBER,USERNUMBER,"");
    }

    /**
     * 保存用户名
     * @param userName
     */
    public void saveUserName(String userName){
        saveString(USERNUMBER,USERNUMBER,userName);
    }

    /**
     * 获取版本号
     *
     * @return
     */
    public int getSaveVersion() {
        return context.getSharedPreferences(SP_VERSION, 0).getInt(
                KEY_VERSION_INT, 0);
    }

    /**
     * 保存当前版本号
     */
    public void saveCurrentVersions(int version) {
        context.getSharedPreferences(SP_VERSION, 0).edit()
                .putInt(KEY_VERSION_INT, version).commit();
    }

    /**
     * 保存极光id
     * @param registrationID
     */
    public void saveRegistrationID(String registrationID){
        saveString(REGISTRATION_ID,REGISTRATION_ID,registrationID);
    }

    /**
     * 获取极光id
     * @return
     */
    public String getRegistrationID(){
        return getString(REGISTRATION_ID,REGISTRATION_ID,"");
    }

    /**
     * 保存城市名字
     * @param cityName
     */
    public void saveCityName(String cityName){
        saveString(CITYNAME,CITYNAME,cityName);
    }

    /**
     * 获取城市名字
     * @return
     */
    public String getCityName(){
        return getString(CITYNAME,CITYNAME,"");
    }

    /**
     * 保存经度
     * @param longtitude
     */
    public void saveLongTitude(String longtitude){
        saveString(LONGTITUDE,LONGTITUDE,longtitude);
    }

    /**
     * 获取经度
     * @return
     */
    public String getLongTitude(){
        return getString(LONGTITUDE,LONGTITUDE,"");
    }

    /**
     * 保存维度
     * @param latitude
     */
    public void saveLatitude(String latitude){
        saveString(LATITUDE,LATITUDE,latitude);
    }

    /**
     * 获取维度
     * @return
     */
    public String getLatitude(){
        return getString(LATITUDE,LATITUDE,"");
    }

    /**
     * 保存更新版本号
     *
     * @param type
     */
    public void saveUpgradeType(int type) {
        context.getSharedPreferences(SP_VERSION, 0).edit()
                .putInt(KEY_VERSON_TYPE, type).commit();
    }

    public int getUpgradeType() {
        return context.getSharedPreferences(SP_VERSION, 0)
                .getInt(KEY_VERSON_TYPE, 0);
    }

    /**
     * 保存忽略版本号
     * @param version
     */
    public void saveIgnoreVersion(String version){
        mPref.edit().putString(IGNORE_VERSION,version).apply();
    }

    /**
     * 获取忽略版本号
     * @return
     */
    public String getIgnoreVersion(){
        return mPref.getString(IGNORE_VERSION,"");
    }

    /**
     * 保存文本数据
     *
     * @param spName 文件名
     * @param key    键
     * @param value  值
     */
    public void saveString(String spName, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences(spName, 0);
        boolean commit = sp.edit().putString(key, value).commit();
//		LogOut.e("saveString:", commit+",key:"+key);
    }

    /**
     * 保存int数据
     *
     * @param spName 文件名
     * @param key    键
     * @param value  值
     */
    public void saveInt(String spName, String key, int value) {
        SharedPreferences sp = context.getSharedPreferences(spName, 0);
        sp.edit().putInt(key, value).apply();
    }

    /**
     * 获取保存的Int数据
     *
     * @param spName
     * @param key
     * @param defValue
     * @return
     */
    public int getInt(String spName, String key, int defValue) {
        return context.getSharedPreferences(spName, 0).getInt(key, defValue);
    }

    public void saveBoolean(String spName, String key, boolean value){
        SharedPreferences sp = context.getSharedPreferences(spName, 0);
        sp.edit().putBoolean(key, value).apply();
    }

    public boolean getBoolean(String spName, String key, boolean defValue){
        return context.getSharedPreferences(spName, 0).getBoolean(key, defValue);
    }

    /**
     * 删除指定的key
     *
     * @param spName
     * @param key
     */
    public void remove(String spName, String key) {
        context.getSharedPreferences(spName, 0).edit().remove(key).apply();
    }

    /**
     * 获取指定路径文本数据
     *
     * @param spName
     * @param key      键
     * @param defValue 值
     * @return
     */
    public String getString(String spName, String key, String defValue) {
        String string = context.getSharedPreferences(spName, Context.MODE_PRIVATE).getString(key, defValue);
        return string;
    }


    public PreferencesHelper(Context context) {
        this.context = context;
        mPref = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE);
    }


    public void saveCurrentUserId(String id) {
        mPref.edit().putString(ID, id).apply();
    }

    public String getCurrentUserId() {
        return mPref.getString(ID, "");

    }

    public void saveSmsCode(String smsCode) {
        mPref.edit().putString(SMSCODE, smsCode).apply();
    }

    public String getSmsCode() {
        return mPref.getString(SMSCODE, "");
    }

    public void saveSmsTimestamp(long timestamp) {
        mPref.edit().putLong(TIMESTAMP, timestamp).apply();
    }

    public void savePhoneNum(String phoneNum){
        mPref.edit().putString(PHONE_NUM,phoneNum).apply();
    }

    public String getPhoneNum(){
        return mPref.getString(PHONE_NUM,"");
    }

    public long getSmsTimestamp() {
        return mPref.getLong(TIMESTAMP, 0);
    }

    public void saveToken(String token) {
        mPref.edit().putString(TOKEN, token).apply();
    }

    public String getToken() {
        return mPref.getString(TOKEN, "");
    }

    public void savePrivacyPwd(String privacyPwd) {
        mPref.edit().putString(PRIVACYPWD, privacyPwd).apply();
    }

    public String getPrivacyPwd() {
        return mPref.getString(PRIVACYPWD, "");
    }

    public void setPrivacyPwdIsOpened(boolean opened) {
        mPref.edit().putBoolean(OPEN_PRIVACY_PWD, opened).apply();
    }

    public boolean isPrivacyPwdOpened() {
        return mPref.getBoolean(OPEN_PRIVACY_PWD, false);
    }

    public boolean isLogined() {
        return mPref.getBoolean(ISLOGIN, false);
    }


    public void setIslogin(boolean islogin) {
        SharedPreferences.Editor editor = mPref.edit();
        editor.putBoolean(ISLOGIN, islogin);
        editor.apply();

    }


    public void saveUserInfo(String userInfo) {
        saveString(USER_INFO, USER_INFO, userInfo);
    }


    public void saveInitInfo(String initinfo) {
        saveString(INIT_INFO, INIT_INFO, initinfo);
    }

    public String getInitInfo() {
        return getString(INIT_INFO, INIT_INFO, "");
    }


    public String getUserInfo() {
        return getString(USER_INFO, USER_INFO, "");
    }

    public boolean isFirstTime() {
        return getBoolean(FIRST_TIME,FIRST_TIME,FIRST_TIME_DEFAULT);
    }

    public void saveFirstTime(boolean isFirst) {
        saveBoolean(FIRST_TIME,FIRST_TIME,isFirst);
    }


    public void clear() {
        mPref.edit().clear().apply();
        remove(USER_INFO, USER_INFO);
    }


//    public void putSignedInRibot(Friends friends) {
//        mPref.edit().putString(PREF_KEY_SIGNED_IN_RIBOT, mGson.toJson(friends)).apply();
//    }

//    @Nullable
//    public Friends getSignedInRibot() {
//        String ribotJson = mPref.getString(PREF_KEY_SIGNED_IN_RIBOT, null);
//        if (ribotJson == null) return null;
//        return mGson.fromJson(ribotJson, Friends.class);
//    }
}
