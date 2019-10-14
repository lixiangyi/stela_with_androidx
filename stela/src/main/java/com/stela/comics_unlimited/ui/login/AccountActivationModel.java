package com.stela.comics_unlimited.ui.login;


import com.lxy.baselibs.mvp.BaseModel;
import com.lxy.baselibs.net.BaseHttpResult;
import com.stela.comics_unlimited.data.repository.RetrofitUtils;

import io.reactivex.Observable;

/**
 * @author xuhao
 * @date 2018/6/13 13:58
 * @desc 对数据层进行操作（比如，是否需要缓存，调用不同 apiservce，外界无需知道内部怎么实现）
 */
public class AccountActivationModel extends BaseModel implements AccountActivationContract.Model {

    @Override
    public Observable<BaseHttpResult<String>> retrieveEmail(String email) {
        return RetrofitUtils.getHttpService().retrieveEmail(email);
    }
}
