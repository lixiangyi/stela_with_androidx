package com.lxy.baselibs.net.token;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.lxy.baselibs.app.BaseApplication;
import com.lxy.baselibs.net.BaseHttpResult;
import com.lxy.baselibs.net.BaseRetrofit;
import com.lxy.baselibs.utils.CommonApkShare;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import retrofit2.Call;

/**
 * Token失效的处理方案二，如果服务端没有遵循设计规范，可以尝试使用如下方法
 * 使用方法：addInterceptor(new TokenInterceptor());
 */
public class TokenInterceptor implements Interceptor {

    private static final Charset UTF8 = Charset.forName("UTF-8");

    @Override
    public Response intercept(final Chain chain) throws IOException {
        Request request = chain.request();
        String token = CommonApkShare.getString(BaseApplication.getContext(), CommonApkShare.KEY_TOKEN);
        Request.Builder requestBuilder = request.newBuilder().addHeader("Authorization", token);

        // try the request
        Response originalResponse = chain.proceed(requestBuilder.build());

        /**
         * 通过如下方法提前获取到请求完成的数据
         */
        ResponseBody responseBody = originalResponse.body();
        BufferedSource source = responseBody.source();
        source.request(Long.MAX_VALUE); // Buffer the entire body.
        Buffer buffer = source.buffer();
        Charset charset = UTF8;
        MediaType contentType = responseBody.contentType();
        if (contentType != null) {
            charset = contentType.charset(UTF8);
        }

        String bodyString = buffer.clone().readString(charset);
        int code = 0;
        if (!TextUtils.isEmpty(bodyString)) {
            code = new Gson().fromJson(bodyString, BaseHttpResult.class).getStatus();
        }
//        TextUtils.isEmpty(request.header("Authorization")) && code == 201
        //  头部为空（201未登录） 刷新token  ||重新登陆清除token
        if (!TextUtils.isEmpty(token) && request.header("Authorization") != null && !request.header("Authorization").equals(token)) {
            // 获取到新的Token
            // create a new request and modify it accordingly using the new token
            Request newRequest = request.newBuilder()
                    .header("Authorization", token)
                    .build();

            originalResponse.body().close();
            // retry the request
            return chain.proceed(newRequest);
        }
//        // 根据和服务端的约定判断Token是否过期
//        if (code == 10000) {
//            String userId = CommonApkShare.getString(BaseApplication.getContext(), CommonApkShare.KEY_USER_ID);
//            Call<BaseHttpResult<ApiToken>> call = BaseRetrofit.getRetrofit().create(ApiServiceToken.class).updateApiToken(userId);
//            BaseHttpResult<ApiToken> apiToken = call.execute().body();
//            CommonApkShare.putString(BaseApplication.getContext(), CommonApkShare.KEY_TOKEN, apiToken.getData().apiToken);
//            // create a new request and modify it accordingly using the new token
//            Request newRequest = request.newBuilder()
//                    .header("Authorization", apiToken.getData().apiToken)
//                    .build();
//
//            originalResponse.body().close();
//            // retry the request
//            return chain.proceed(newRequest);
//        }


        // otherwise just pass the original response on
        return originalResponse;
    }
}