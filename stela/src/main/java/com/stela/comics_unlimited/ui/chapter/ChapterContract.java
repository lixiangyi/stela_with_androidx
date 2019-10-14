package com.stela.comics_unlimited.ui.chapter;

import com.lxy.baselibs.mvp.IModel;
import com.lxy.baselibs.mvp.IView;
import com.lxy.baselibs.net.BaseHttpResult;
import com.stela.comics_unlimited.data.entity.ChapterEntity;
import com.stela.comics_unlimited.data.entity.ScreenShotrEntity;

import io.reactivex.Observable;

public class ChapterContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {
        void likeSuccess();
        void storeLastSuccess();
        void showlastChapterError(String s);
        void showChapter(ChapterEntity s);
        void screenshots(ScreenShotrEntity s, String path);
    }

    interface Model extends IModel {
        Observable<BaseHttpResult<String>> userLikeChapter(String chapterId, String seriesId, String userId,int flag);
        Observable<BaseHttpResult<ChapterEntity>> getChapterList(String chapterId, String seriesId, String userId);
        Observable<BaseHttpResult<String>> addChapter(String chapterId, String seriesId, String userId,String imgId);
        Observable<BaseHttpResult<ScreenShotrEntity>> screenshotsCount( String userId);
    }
}
