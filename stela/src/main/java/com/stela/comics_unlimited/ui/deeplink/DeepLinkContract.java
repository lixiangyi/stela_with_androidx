package com.stela.comics_unlimited.ui.deeplink;

import com.lxy.baselibs.mvp.IModel;
import com.lxy.baselibs.mvp.IView;
import com.lxy.baselibs.net.BaseHttpResult;
import com.stela.comics_unlimited.data.entity.ChapterEntity;
import com.stela.comics_unlimited.data.entity.DeeplinkEntity;

import io.reactivex.Observable;

public class DeepLinkContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {
        void showChapter(DeeplinkEntity s);

        void closeOut();
    }

    interface Model extends IModel {
        Observable<BaseHttpResult<DeeplinkEntity>> getDeeplinkList(String chapterId);
    }
}
