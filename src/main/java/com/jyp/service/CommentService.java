package com.jyp.service;

import com.jyp.dao.CommentDAO;
import com.jyp.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class CommentService {
    @Autowired
    CommentDAO commentDAO;
    @Autowired
    SensitiveService sensitiveService;

    public int addComment(Comment comment)
    {
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        comment.setContent(sensitiveService.filter(comment.getContent()));
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
