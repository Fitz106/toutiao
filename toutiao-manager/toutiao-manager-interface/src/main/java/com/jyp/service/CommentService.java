package com.jyp.service;

import com.jyp.pojo.Comment;

import java.util.List;

public interface CommentService {
    public int addComment(Comment comment);

    public List<Comment> getCommentsByEntity(int entityId, int entityType);

    public int getCommentCount(int entityId, int entityType) ;

}
