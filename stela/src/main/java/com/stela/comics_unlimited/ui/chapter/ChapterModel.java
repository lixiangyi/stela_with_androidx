package com.stela.comics_unlimited.ui.chapter;

import com.lxy.baselibs.mvp.BaseModel;
import com.lxy.baselibs.net.BaseHttpResult;
import com.stela.comics_unlimited.data.entity.ChapterEntity;
import com.stela.comics_unlimited.data.entity.ScreenShotrEntity;
import com.stela.comics_unlimited.data.repository.RetrofitUtils;

import io.reactivex.Observable;

public class ChapterModel extends BaseModel implements ChapterContract.Model {


    @Override
    public Observable<BaseHttpResult<String>> userLikeChapter(String chapterId, String seriesId, String seriesName,int flag) {
        return RetrofitUtils.getHttpService().userLikeChapter(chapterId,seriesId, seriesName, flag);
    }

    @Override
    public Observable<BaseHttpResult<ChapterEntity>> getChapterList(String chapterId, String seriesId, String userId) {
        return RetrofitUtils.getHttpService().findByChapters(chapterId,seriesId, userId);
    }

    @Override
    public Observable<BaseHttpResult<String>> addChapter(String chapterId, String seriesId, String userid,String imgId) {
        return RetrofitUtils.getHttpService().addChapter(chapterId,seriesId, userid,imgId);
    }

    @Override
    public Observable<BaseHttpResult<ScreenShotrEntity>> screenshotsCount(String userId) {
        return RetrofitUtils.getHttpService().screenshotsCount( userId);
    }
}
