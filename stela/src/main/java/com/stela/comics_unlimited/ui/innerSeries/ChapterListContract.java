package com.stela.comics_unlimited.ui.innerSeries;


import com.lxy.baselibs.mvp.IModel;
import com.lxy.baselibs.mvp.IView;
import com.stela.comics_unlimited.data.entity.HomeBrowseEntity;

import java.util.List;

/**
 * @author xuhao
 * @date 2018/6/12 22:57
 * @desc 契约类
 */
public interface ChapterListContract {


    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {

    }

    interface Model extends IModel {

    }

}
