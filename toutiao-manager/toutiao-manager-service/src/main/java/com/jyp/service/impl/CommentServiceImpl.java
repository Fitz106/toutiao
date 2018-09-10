package com.jyp.service.impl;


import com.jyp.common.SensitiveUtil;
import com.jyp.dao.CommentDAO;
import com.jyp.pojo.Comment;
import com.jyp.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service("commentService")
public class CommentServiceImpl implements CommentService {
    @Autowired
    CommentDAO commentDAO;

    public int addComment(Comment comment)
    {
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        comment.setContent(SensitiveUtil.filter(comment.getContent()));
        return commentDAO.addComment(comment);
    }
    public List<Comment> getCommentsByEntity(int entityId,int entityType)
    {
        return commentDAO.selectByEntityIdAndEntityType(entityId,entityType);
    }
    public int getCommentCount(int entityId, int entityType) {
        return commentDAO.getCommentCount(entityId, entityType);
    }
}
