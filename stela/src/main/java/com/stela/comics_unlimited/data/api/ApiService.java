package com.stela.comics_unlimited.data.api;


import com.lxy.baselibs.net.BaseHttpResult;
import com.stela.comics_unlimited.data.entity.ChapterEntity;
import com.stela.comics_unlimited.data.entity.CollectionEntity;
import com.stela.comics_unlimited.data.entity.CommentsEntity;
import com.stela.comics_unlimited.data.entity.DeeplinkEntity;
import com.stela.comics_unlimited.data.entity.HomeNewBrowseEntity;
import com.stela.comics_unlimited.data.entity.HomePageEntity;
import com.stela.comics_unlimited.data.entity.ImgEntity;
import com.stela.comics_unlimited.data.entity.NotificationEntity;
import com.stela.comics_unlimited.data.entity.PageEntity;
import com.stela.comics_unlimited.data.entity.PersonEntity;
import com.stela.comics_unlimited.data.entity.ScreenShotrEntity;
import com.stela.comics_unlimited.data.entity.SeriesEntity;
import com.stela.comics_unlimited.data.entity.SubscriptionEntity;
import com.stela.comics_unlimited.data.entity.VersionEntity;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * @author xuhao
 * @date 2018/6/11 23:04
 * @desc
 */
public interface ApiService {

    /**
     * 注册
     *
     * @param email    json
     * @param password json
     * @return 登陆数据
     */
    @POST("user/register")
    @FormUrlEncoded
    Observable<BaseHttpResult<String>> register(@Field("email") String email, @Field("password") String password, @Field("version") String version);

    /**
     * 登录
     *
     * @return 个人用户数据
     */
    @POST("user/login")
    @FormUrlEncoded
    Observable<BaseHttpResult<PersonEntity>> login(@Field("email") String email, @Field("password") String password,
                                                   @Field("model") String model, @Field("os") String os, @Field("version") String version,
                                                   @Field("appVersion") String appVersion, @Field("buildNumber") String buildNumber,
                                                   @Field("notificationToken") String notificationToken, @Field("pushEnabled") String pushEnabled);

    /**
     * Google登录
     *
     * @return 个人用户数据
     */
    @POST("user/googleLogin")
    @FormUrlEncoded
    Observable<BaseHttpResult<PersonEntity>> google_login(@Field("email") String email, @Field("source") int source,@Field("nickname") String nickname,
                                                          @Field("model") String model, @Field("os") String os, @Field("version") String version,
                                                          @Field("appVersion") String appVersion, @Field("buildNumber") String buildNumber,
                                                          @Field("notificationToken") String notificationToken,@Field("pushEnabled") String pushEnabled);

    /**
     * POST /v2/user/retrieveEmail 用户注册重新或者email
     *
     * @return
     */
    @POST("user/retrieveEmail")
    @FormUrlEncoded
    Observable<BaseHttpResult<String>> retrieveEmail(@Field("email") String email);

    /**
     * 查询用户信息
     *
     * @return 个人用户数据
     */
    @POST("user/findByUser")
    @FormUrlEncoded
    Observable<BaseHttpResult<PersonEntity>> updataUser(@Field("userId") String userId);

    /**
     * 获取兴趣列表
     *
     * @return 兴趣列表
     */
    @POST("userBehavior/findByInterestList")
    @FormUrlEncoded
    Observable<BaseHttpResult<List<ImgEntity>>> findBySeriesGenresImg(@Field("userId") String userId);

    /**
     * 新老密码验证修改用户密码 POST /v2/user/updatePasswordOldAndNew
     *
     * @return
     */
    @POST("user/updatePasswordOldAndNew")
    @FormUrlEncoded
    Observable<BaseHttpResult<String>> updatePasswordOldAndNew(
            @Field("oldPassword") String oldPassword,
            @Field("newPassword") String newPassword, @Field("userId") String userId);

    /**
     * 用户选择喜欢的漫画系列
     *
     * @return
     */
    @POST("userBehavior/addInterest")
    @FormUrlEncoded
    Observable<BaseHttpResult<String>> addUserLikeCartoonSeries(@Field("userId") String userId, @Field("surveyIds") String seriesIds);

    /**
     * 获取用户头像列表
     *
     * @return 用户头像
     */
    @POST("user/findByUserPortraitList")
    @FormUrlEncoded
    Observable<BaseHttpResult<List<ImgEntity>>> downloadPortrait(@Field("userId") String userId);

    /**
     * 用户确认头像
     *
     * @return
     */
    @POST("user/updateUserPortrait")
    @FormUrlEncoded
    Observable<BaseHttpResult<String>> uploadPortrait(@Field("userId") String userId, @Field("imgPortrait") String imgName);

    /**
     * 忘记密码发送邮件
     *
     * @return
     */
    @POST("user/forgetPassword")
    @FormUrlEncoded
    Observable<BaseHttpResult<String>> forgetPassword(@Field("email") String email,@Field("type") int type);

    /**
     * 更新昵称
     *
     * @return
     */
    @POST("user/updateUsername")
    @FormUrlEncoded
    Observable<BaseHttpResult<String>> updateUsername(@Field("nickname") String Username, @Field("userId") String userId);

    /**
     * 首页home
     *
     * @return
     */
    @POST("page/findByPageView")
    @FormUrlEncoded
    Observable<BaseHttpResult<HomePageEntity>> pageHome(@Field("pageId") String pageId, @Field("userId") String userId, @Field("deepLinkId") String deepLinkId);

    /**
     * 首页Browse tab
     *
     * @return
     */
    @POST("page/findByBrowserList")
    @FormUrlEncoded
    Observable<BaseHttpResult<List<ImgEntity>>> getBrowseTabList(@Field("userId") String userId);

    /**
     * 首页Browse inner
     *
     * @return
     */
    @POST("stelaResource/findBySeriesContent")
    @FormUrlEncoded
    Observable<BaseHttpResult<HomeNewBrowseEntity>> getBrowseList(@Field("pageNo") int pageNo, @Field("pageSize") int pageSize,
                                                                  @Field("seriesId") String seriesId, @Field("seriesName") String seriesName);

    /**
     * series info
     * series/findBySeries
     *
     * @return
     */
    @POST("series/findBySeries")
    @FormUrlEncoded
    Observable<BaseHttpResult<SeriesEntity>> getSeriesInfo(@Field("seriesId") String seriesId, @Field("userId") String userId);

    /**
     * POST series/findBySeriesChapter
     * 查询章节列表
     *
     * @return
     */
    @POST("series/findBySeriesChapter")
    @FormUrlEncoded
    Observable<BaseHttpResult<ChapterEntity>> findByChapters(@Field("chapterId") String chapterId, @Field("seriesId") String seriesId,
                                                             @Field("userId") String userId);


    /**
     * 用户添加series喜欢或者取消
     *
     * @return
     */
    @POST("userBehavior/insertUserLikeSeriesOrNotLikeSeries")
    @FormUrlEncoded
    Observable<BaseHttpResult<String>> userLikes(@Field("seriesId") String seriesId, @Field("userId") String userId, @Field("flag") int flag);

    /**
     * 用户添加chapter喜欢或者取消
     *
     * @return
     */
    @POST("userBehavior/insertUserLikeChapterOrNotLikeChapter")
    @FormUrlEncoded
    Observable<BaseHttpResult<String>> userLikeChapter(@Field("chapterId") String chapterId, @Field("seriesId") String seriesId,
                                                       @Field("userId") String userId, @Field("flag") int flag);

    /**
     * 用户添加收藏或者取消
     *
     * @return
     */
    @POST("userBehavior/insertUserCollectSeriesOrNotCollectSeries")
    @FormUrlEncoded
    Observable<BaseHttpResult<String>> addUserCollect(@Field("seriesId") String seriesId, @Field("userId") String userId,
                                                      @Field("flag") int flag, @Field("seriesType") String seriesType);

    /**
     * 保存当前章节
     *
     * @return
     */
    @POST("userBehavior/insertRecentRead")
    @FormUrlEncoded
    Observable<BaseHttpResult<String>> addChapter(@Field("chapterId") String chapterId, @Field("seriesId") String seriesId,
                                                  @Field("userId") String userId, @Field("chapterImgId") String assetsId);

    /**
     * 查询作品评论列表(series)
     *
     * @return
     */
    @POST("userBehavior/findSeriesComments")
    @FormUrlEncoded
    Observable<BaseHttpResult<PageEntity<CommentsEntity>>> findUserComment(@Field("userId") String userId, @Field("seriesId") String seriesId,
                                                                           @Field("pageNum") int pageNo, @Field("pageSize") int pageSize);

    /**
     * 添加漫画用户评论
     *
     * @return
     */
    @POST("userBehavior/insertSeriesComments")
    @FormUrlEncoded
    Observable<BaseHttpResult<String>> addComment(@Field("content") String content, @Field("seriesId") String seriesId, @Field("userId") String userId);

    /**
     * POST userBehavior/insertSeriesSecondComments
     * 添加漫画用户评论回复
     *
     * @return
     */
    @POST("userBehavior/insertSeriesSecondComments")
    @FormUrlEncoded
    Observable<BaseHttpResult<String>> addCommentReply(@Field("content") String content, @Field("parentId") String commentableId,
                                                       @Field("userId") String userId, @Field("seriesId") String seriesId);

    /**
     * POST userBehavior/findSeriesChapterComments
     * 章节评论列表
     *
     * @return
     */
    @POST("userBehavior/findSeriesChapterComments")
    @FormUrlEncoded
    Observable<BaseHttpResult<PageEntity<CommentsEntity>>> findByChapterComments(@Field("userId") String userId, @Field("seriesId") String seriesId,
                                                                                 @Field("chapterId") String chapterId, @Field("pageNum") int pageNo,
                                                                                 @Field("pageSize") int pageSize);

    /**
     * POST userBehavior/insertChapterComments
     * 添加章节评论
     *
     * @return
     */
    @POST("userBehavior/insertChapterComments")
    @FormUrlEncoded
    Observable<BaseHttpResult<String>> addUserChapterComments(@Field("content") String content, @Field("seriesId") String seriesId,
                                                              @Field("userId") String userId, @Field("chapterId") String chapterId);

    /**
     * POST userBehavior/insertChapterSecondComments
     * 添加章节回复评论
     *
     * @return
     */
    @POST("userBehavior/insertChapterSecondComments")
    @FormUrlEncoded
    Observable<BaseHttpResult<String>> addUserChapterCommentsReply(@Field("content") String content, @Field("parentId") String chapterCommentId,
                                                                   @Field("userId") String userId, @Field("seriesId") String seriesId,
                                                                   @Field("chapterId") String replyUserId);

    /**
     * POST userBehavior/insertUserLikeChapterOrNotLikeSeriesComments
     * 给评论点赞
     *
     * @return
     */
    @POST("userBehavior/insertUserLikeChapterOrNotLikeSeriesComments")
    @FormUrlEncoded
    Observable<BaseHttpResult<String>> addSeriesCommentsLike(@Field("commentsId") String commentsId, @Field("userId") String userId, @Field("flag") int flag);

    /**
     * POST /userBehavior/findByUserCollextion
     * 用户收藏列表查询
     *
     * @return
     */
    @POST("userBehavior/findByUserCollextion")
    @FormUrlEncoded
    Observable<BaseHttpResult<List<CollectionEntity>>> findByUserCollectComicList(@Field("userId") String userId);

    /**
     * POST /stelaResource/findByUserCollectSeries
     * 用户收藏列表see_all查询
     *
     * @return
     */
    @POST("userBehavior/findByUserCollextionSeeMore")
    @FormUrlEncoded
    Observable<BaseHttpResult<PageEntity<SeriesEntity>>> findByUserCollectSeries(@Field("userId") String userId, @Field("seeMoreId") String conllectsId, @Field("pageNo") int pageNo);

    /**
     * POST userBehavior/findByUserRecentRead
     * 查询浏览记录
     *
     * @return
     */
    @POST("userBehavior/findByUserRecentRead")
    @FormUrlEncoded
    Observable<BaseHttpResult<PageEntity<SeriesEntity>>> findByUserBrowseList(@Field("userId") String userId, @Field("pageNum") int pageNo);

    /**
     * POST userBehavior/deleteUserRecentRead
     * 删除浏览记录
     *
     * @return
     */
    @POST("userBehavior/deleteUserRecentRead")
    @FormUrlEncoded
    Observable<BaseHttpResult<String>> deleteRecentReadList(@Field("userId") String userId, @Field("seriesId") String seriesId);

    /**
     * POST userBehavior/findByUserRecentRead
     * 查询消息提醒记录列表
     *
     * @return
     */
    @POST("notification/selectNotificationTargetByUser")
    @FormUrlEncoded
    Observable<BaseHttpResult<PageEntity<NotificationEntity>>> getNotificationList(@Field("userId") String userId, @Field("pageNum") int pageNo);

    /**
     * POST notification/find
     * 查询消息提醒记录
     *
     * @return
     */
    @POST("notification/find")
    @FormUrlEncoded
    Observable<BaseHttpResult<NotificationEntity>> getNotification(@Field("id") String id);
    /**
     * POST notification/deletebyUserId
     * 删除全部浏览记录
     *
     * @return
     */
    @POST("notification/deletebyUserId")
    @FormUrlEncoded
    Observable<BaseHttpResult<String>> deleteNotificationList(@Field("userId") String userId);
    /**
     * POST notification/deleteById
     * 删除浏览记录
     *
     * @return
     */
    @POST("notification/deleteById")
    @FormUrlEncoded
    Observable<BaseHttpResult<Object>> deleteNotificationById(@Field("id") String id);
    /**
     * notification/updateById
     * 去除单个未读状态
     *
     * @return
     */
    @POST("notification/updateById")
    @FormUrlEncoded
    Observable<BaseHttpResult<Object>> updateNotification(@Field("id") String id);
    /**
     * notification/updateById
     * 去除所有未读状态
     *
     * @return
     */
    @POST("notification/updateByUserId")
    @FormUrlEncoded
    Observable<BaseHttpResult<String>> updateAllNotificationState(@Field("userId") String id);
    /**
     * notification/updateById
     * 去除所有未读状态
     *
     * @return
     */
    @POST("notification/selectUserNotificationByCount")
    @FormUrlEncoded
    Observable<BaseHttpResult<NotificationEntity>> selectUserNotificationByCount(@Field("userId") String id);

    /**
     * POST play/findBySubscriptionView
     * 订阅页面信息
     *
     * @return
     */
    @POST("play/findBySubscriptionView")
    @FormUrlEncoded
    Observable<BaseHttpResult<SubscriptionEntity>> getSubscriptionView(@Field("state") int state, @Field("seriesId") String seriesId);

    /**
     * POST /v1/play/findByUserAppReceipts 查询用户订单记录
     *
     * @return
     */
    @POST("play/findByUserAppReceipts")
    @FormUrlEncoded
    Observable<BaseHttpResult<SubscriptionEntity>> getMySubscribeInfo(@Field("userId") String userId);

    /**
     * POST /v1/play/googlePlaySubscription google play支付验证
     *
     * @return
     */
    @POST("play/googlePlaySubscription")
    @FormUrlEncoded
    Observable<BaseHttpResult<String>> googlePlaySubscription(@Field("userId") String userId, @Field("purchaseToken") String purchaseToken,
                                                              @Field("packageName") String packageName, @Field("productId") String productId);
    /**
     * POST  日活接口
     *
     * @return
     */
    @POST("userBehavior/userActive")
    @FormUrlEncoded
    Observable<BaseHttpResult<Object>> userActive(@Field("userId") String userId);
    /**
     * POST series/findBySeriesChapter
     * deeplink
     *
     * @return
     */
    @POST("deepLink/getDeepLinkPage")
    @FormUrlEncoded
    Observable<BaseHttpResult<DeeplinkEntity>> getDeeplinkList(@Field("id") String userId);
    /**
     * POST  日活接口
     *
     * @return
     */
    @POST("userBehavior/screenshotsCount")
    @FormUrlEncoded
    Observable<BaseHttpResult<ScreenShotrEntity>> screenshotsCount(@Field("userId") String userId);
    /**
     * POST /v2/user/updatePin 设置或关闭儿童模式
     *
     * @return
     */
    @POST("user/updatePin")
    @FormUrlEncoded
    Observable<BaseHttpResult<String>> updatePin(@Field("childrenState") int childrenState/*设置时传1关闭时传0*/,
                                                 @Field("pin") String pin,@Field("userId") String userId);
    /**
     * POST /v2/version/selectVersionCount 查询是否最新版本
     *
     * @return
     */
    @POST("version/selectVersionCount")
    @FormUrlEncoded
    Observable<BaseHttpResult<VersionEntity>> selectVersionCount(@Field("androidVersion") String androidVersion);
    /**
     * /v2/user/updateNotificationToken 更新用户NotificationToken
     *
     * @return
     */
    @POST("user/updateNotificationToken")
    @FormUrlEncoded
    Observable<BaseHttpResult<String>> updateNotificationToken(@Field("notificationToken") String notificationToken,@Field("userId") String userId);
}
