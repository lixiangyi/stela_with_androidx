package com.lxy.baselibs.net.interceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class NetInterceptor implements Interceptor {

    private OkHttpClient builder;

    public NetInterceptor() {
        super();
    }

    public NetInterceptor(OkHttpClient builder) {
        this.builder = builder;
    }

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request request = chain.request();
        //移除头部参数
        request = request.newBuilder()
                .removeHeader("User-Agent")
                .removeHeader("Accept-Encoding")
                .build();
        Response response = chain.proceed(request);
        if (response.body() != null && response.body().contentType() != null) {
            MediaType mediaType = response.body().contentType();
            String content = response.body().string();
            ResponseBody responseBody = ResponseBody.create(mediaType, content);
            return response.newBuilder().body(responseBody).build();
        } else {
            return response;
        }
    }

}
