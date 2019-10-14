package com.stela.comics_unlimited.data.entity;

import java.io.Serializable;
import java.util.ArrayList;

public class CommentsEntity implements Serializable {
    private static final long serialVersionUID = -7472884335562882730L;
    public String id;
    public String imgPortraitUrl;
    public String seriesId;
    public String chapterId;
    public String content ;//评论内容
    public String nickname;
    public String userId;
    public int likeCount;
    public String createdAtStr;
    public String createdAt;
    public int flagLikeComments;//用户是否喜欢该评论（1喜欢 0未喜欢）
    public ArrayList<CommentsEntity> sonCommentsList;
    public ArrayList<CommentsEntity> userChaoterCommentsReplyList;


}
