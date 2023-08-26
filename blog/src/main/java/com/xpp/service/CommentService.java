package com.xpp.service;

import com.xpp.entity.Comment;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 留言区评论Servic层接口
 */
public interface CommentService {
    /**
     * 获取留言区域列表留言
     *
     * @param blogId
     * @return
     */
    List<Comment> listCommentByBlogId(Long blogId);

    /**
     * 保存评论内容
     */
    Comment saveComment(Comment comment);


}
