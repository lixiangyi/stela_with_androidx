package com.stela.comics_unlimited.ui.innerSeries;


import com.lxy.baselibs.mvp.IModel;
import com.lxy.baselibs.mvp.IView;
import com.lxy.baselibs.net.BaseHttpResult;
import com.stela.comics_unlimited.data.entity.HomeBrowseEntity;

import java.util.List;

import io.reactivex.Observable;

/**
 * @author xuhao
 * @date 2018/6/12 22:57
 * @desc 契约类
 */
public interface SeriesInfoContract {


    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {
        void likeSuccess();
        void collectSuccess();
    }

    interface Model extends IModel {
        Observable<BaseHttpResult<String>> userLikes(String seriesId, String userId, int flag);
        Observable<BaseHttpResult<String>> addUserCollect( String seriesId, String userId,int flag,String seriesType);
    }

}
