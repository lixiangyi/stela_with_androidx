package com.stela.comics_unlimited.ui.deeplink;

import com.lxy.baselibs.mvp.BaseModel;
import com.lxy.baselibs.net.BaseHttpResult;
import com.stela.comics_unlimited.data.entity.ChapterEntity;
import com.stela.comics_unlimited.data.entity.DeeplinkEntity;
import com.stela.comics_unlimited.data.repository.RetrofitUtils;

import io.reactivex.Observable;

public class DeepLinkModel  extends BaseModel implements DeepLinkContract.Model {




    @Override
    public Observable<BaseHttpResult<DeeplinkEntity>> getDeeplinkList(String deeplinkId) {
        return RetrofitUtils.getHttpService().getDeeplinkList(deeplinkId);
    }
}
