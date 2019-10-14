package com.stela.comics_unlimited.ui.series;


import com.lxy.baselibs.mvp.IModel;
import com.lxy.baselibs.mvp.IView;
import com.lxy.baselibs.net.BaseHttpResult;
import com.stela.comics_unlimited.data.entity.SeriesEntity;
import com.stela.comics_unlimited.data.entity.PersonEntity;

import io.reactivex.Observable;

/**
 * @author xuhao
 * @date 2018/6/12 16:18
 * @desc 契约类
 */
public interface SeriesContract {

    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {
        void showData(SeriesEntity homeNewBrowseEntity);
        void updateSuccess(PersonEntity result);
    }

    interface Model extends IModel {
        Observable<BaseHttpResult<SeriesEntity>> getSeriesInfo(String seriesId, String userid);
        Observable<BaseHttpResult<PersonEntity>> updataUser(String userId);
    }
}
