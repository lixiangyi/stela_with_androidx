package com.stela.comics_unlimited.ui.person;


import com.lxy.baselibs.mvp.IModel;
import com.lxy.baselibs.mvp.IView;
import com.lxy.baselibs.net.BaseHttpResult;
import com.stela.comics_unlimited.data.entity.ImgEntity;

import java.util.List;

import io.reactivex.Observable;

/**
 * @author xuhao
 * @date 2018/6/12 16:18
 * @desc 契约类
 */
public interface SelectTitleContract {

    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {
        void selecSuccess(String s);
        void findBySeriesGenresImg(List<ImgEntity> assetList);
    }

    interface Model extends IModel {
        Observable<BaseHttpResult<List<ImgEntity>>> findBySeriesGenresImg(String userid);
        Observable<BaseHttpResult<String>> addUserLikeCartoonSeries(String userid,String ids);
    }
}
