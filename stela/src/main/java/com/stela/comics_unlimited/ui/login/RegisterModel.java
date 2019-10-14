package com.stela.comics_unlimited.ui.login;


import com.lxy.baselibs.mvp.BaseModel;
import com.lxy.baselibs.net.BaseHttpResult;
import com.stela.comics_unlimited.data.entity.PersonEntity;
import com.stela.comics_unlimited.data.repository.RetrofitUtils;

import io.reactivex.Observable;

/**
 * @author xuhao
 * @date 2018/6/13 13:58
 * @desc 对数据层进行操作（比如，是否需要缓存，调用不同 apiservce，外界无需知道内部怎么实现）
 */
public class RegisterModel extends BaseModel implements RegisterContract.Model {

    @Override
    public Observable<BaseHttpResult<String>> register(String email,String password,String versionCode) {
        return RetrofitUtils.getHttpService().register(email,password,versionCode);
    }
    @Override
    public Observable<BaseHttpResult<PersonEntity>> google_login(String email,int source, String nickname, String model, String os,
                                                                 String version, String appVersion, String buildnumber,String notificationToken, String pushEnabled) {
        return RetrofitUtils.getHttpService().google_login(email,source,nickname,model,os,version,appVersion,buildnumber,notificationToken,pushEnabled);
    }

}
