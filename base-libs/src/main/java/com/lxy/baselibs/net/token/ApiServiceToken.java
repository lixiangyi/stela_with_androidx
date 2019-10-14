package com.lxy.baselibs.net.token;


import com.lxy.baselibs.net.BaseHttpResult;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * @author xuhao
 * @date 2018/6/11 23:04
 * @desc
 */
public interface ApiServiceToken{


    /**
     * 更新Token接口
     *
     * @return
     */
    @POST("auth/refresh")
    @FormUrlEncoded
    Call<BaseHttpResult<ApiToken>> updateApiToken(@Field("userId") String userId);
}
