package com.stela.comics_unlimited.ui.innerBrowse;


import com.lxy.baselibs.mvp.IModel;
import com.lxy.baselibs.mvp.IView;
import com.lxy.baselibs.net.BaseHttpResult;
import com.stela.comics_unlimited.data.entity.HomeNewBrowseEntity;
import com.stela.comics_unlimited.data.entity.HomePageEntity;
import com.stela.comics_unlimited.data.entity.SeriesAsset;

import java.util.ArrayList;

import io.reactivex.Observable;

/**
 * @author xuhao
 * @date 2018/6/12 22:57
 * @desc 契约类
 */
public interface InnerBrowseContract {


    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {

        void showData(HomeNewBrowseEntity testNews);
        void showNewData(ArrayList<SeriesAsset> seriesEntities, HomePageEntity result);
    }

    interface Model extends IModel {
        Observable<BaseHttpResult<HomeNewBrowseEntity>> getBrowseList(int pageNo, String seriesId, String seriesName);
        Observable<BaseHttpResult<HomePageEntity>> getBrowseListNew(String seriesId,String userId);
    }

}
