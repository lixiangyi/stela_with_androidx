package com.stela.comics_unlimited.ui.mylibrary.innerLibrary;


import com.lxy.baselibs.mvp.IModel;
import com.lxy.baselibs.mvp.IView;
import com.lxy.baselibs.net.BaseHttpResult;
import com.stela.comics_unlimited.data.entity.CollectionEntity;
import com.stela.comics_unlimited.data.entity.SeriesEntity;

import java.util.List;

import io.reactivex.Observable;

/**
 * @author xuhao
 * @date 2018/6/12 22:57
 * @desc 契约类
 */
public interface CollectionInnerContract {


    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {

        void showListData(List<SeriesEntity> collectionEntities);
        void cancelSuccess(String s,int position);

        void showListDataNew(List<CollectionEntity> data);
    }

    interface Model extends IModel {
        Observable<BaseHttpResult<List<CollectionEntity>>> findByUserCollectComicList(String userId);
        Observable<BaseHttpResult<String>> addUserCollect( String seriesId, String userId,int flag,String seriesType);
    }

}
