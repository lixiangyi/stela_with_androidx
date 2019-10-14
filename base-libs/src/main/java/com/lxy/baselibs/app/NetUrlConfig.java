package com.lxy.baselibs.app;

import com.common.baselibs.BuildConfig;

/**
 * @author xuhao
 * @date 2018/6/11 17:34
 * @desc APP 的配置参数
 */
public class NetUrlConfig {
    // TODO: 2019/7/15 修改正式接口url 
    private static final int WUBIN = 0;
    private static final int DEBUG = 1;
    private static final int RELEASE = 2;
    public static final String URL_WUBIN = "http://10.0.8.21:8083/v3/";
    public static final String URL_IN = "http://10.0.100.110:8083/v1/";
    public static final String URL_OUT = "http://50.112.202.174:8098/v2/";
    public static final String URL_ONLINE = "https://service.stela.com/v2.1.0/";
//    public static final String BASE_URL = BuildConfig.DEBUG ? URL_WUBIN : URL_WUBIN;//修改第一个参数
//    public static final String BASE_URL = BuildConfig.DEBUG ? URL_IN : URL_IN;//修改第一个参数
//    public static final String BASE_URL = BuildConfig.DEBUG ? URL_OUT : URL_OUT;//修改第一个参数
    public static final String BASE_URL = BuildConfig.DEBUG ? URL_ONLINE : URL_ONLINE;//修改第一个参数
}
