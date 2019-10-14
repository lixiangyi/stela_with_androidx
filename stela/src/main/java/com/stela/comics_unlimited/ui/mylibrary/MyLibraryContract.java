package com.stela.comics_unlimited.ui.mylibrary;


import com.lxy.baselibs.mvp.IModel;
import com.lxy.baselibs.mvp.IView;

/**
 * @author xuhao
 * @date 2018/6/12 22:57
 * @desc 契约类
 */
public interface MyLibraryContract {


    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {

    }

    interface Model extends IModel {


    }

}
