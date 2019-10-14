package com.stela.comics_unlimited.ui.login;


import com.lxy.baselibs.mvp.IModel;
import com.lxy.baselibs.mvp.IView;
import com.lxy.baselibs.net.BaseHttpResult;
import com.stela.comics_unlimited.data.entity.PersonEntity;

import io.reactivex.Observable;

/**
 * @author xuhao
 * @date 2018/6/12 16:18
 * @desc 契约类
 */
public interface LoginContract {

    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {
        void login(PersonEntity result);
        void google_login(PersonEntity result);
        void google_exist(String msg,String email);
    }

    interface Model extends IModel {
        Observable<BaseHttpResult<PersonEntity>> login(String email, String password, String model,
                                                       String os, String version, String appVersion,
                                                       String buildnumber, String notificationToken, String pushEnabled);

        Observable<BaseHttpResult<PersonEntity>> google_login(String email, int source, String nickname, String model,
                                                              String os, String version, String appVersion,
                                                              String buildnumber, String notificationToken, String pushEnabled);

    }
}
