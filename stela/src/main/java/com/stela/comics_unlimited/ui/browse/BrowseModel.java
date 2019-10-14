package com.stela.comics_unlimited.ui.browse;

import com.lxy.baselibs.mvp.BaseModel;
import com.lxy.baselibs.net.BaseHttpResult;
import com.stela.comics_unlimited.data.entity.ImgEntity;
import com.stela.comics_unlimited.data.repository.RetrofitUtils;

import java.util.List;

import io.reactivex.Observable;


/**
 * @author xuhao
 * @date 2018/6/13 15:35
 * @desc
 */
public class BrowseModel extends BaseModel implements BrowseContract.Model {
    @Override
    public Observable<BaseHttpResult<List<ImgEntity>>> getBrowseTabList(String userId) {
        return RetrofitUtils.getHttpService().getBrowseTabList(userId);
    }
}
