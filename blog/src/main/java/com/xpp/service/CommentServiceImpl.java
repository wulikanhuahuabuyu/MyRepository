package com.xpp.service;

import com.xpp.dao.CommentRepository;
import com.xpp.entity.Comment;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 留言区评论Servic层接口实现类
 */
@Service
public class CommentServiceImpl implements CommentService {

    //用于存放迭代找出的所有子代的集合
    private List<Comment> tempReplys = new ArrayList<>();

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public List<Comment> listCommentByBlogId(Long blogId) {
        Sort sort = new Sort(Sort.Direction.ASC, "createdTime");
        List<Comment> comments = commentRepository.findByBlogIdAndParentCommentIsNull(blogId, sort);
        return eachComments(comments);
    }

    @Transactional
    @Override
    public Comment saveComment(Comment comment) {
        //先获得父级回复的id
        Long parentCommentId = comment.getParentComment().getId();
        //如果此回复有父级回复
        if (parentCommentId != -1) {
            comment.setParentComment(commentRepository.findById(parentCommentId).get());
        } else {//如果此回复没有父级回复
            comment.setParentComment(null);
        }
        comment.setCreatedTime(new Date());
        return commentRepository.save(comment);
    }


    /**
     * 将当前文章的所有一级回复的内含回复都拿出来放在相应的ReplyComments中
     *
     * @param comments
     * @return
     */
    public List<Comment> eachComments(List<Comment> comments) {
        List<Comment> commentsView = new ArrayList<>();
        for (Comment comment : comments) {
            Comment c = new Comment();
            BeanUtils.copyProperties(comment, c);
            commentsView.add(c);
        }
        //合并评论的各层子代到第一级子代集合中
        mergeChild(commentsView);
        return commentsView;
    }

    /**
     * @param comments root根节点，blog不为空的对象集合
     */
    private void mergeChild(List<Comment> comments) {
        for (Comment comment : comments) {
            List<Comment> replys1 = comment.getReplyComment();
            //遍历一级回复
            for (Comment reply1 : replys1) {
                //循环迭代，找出子代，存放在tempReplys中
                recursively(reply1);
            }
            //修改头节点的reply集合为迭代处理后的集合
            //这样做的话就把这个comment的二级回复已及它的子级回复都放在了同一级别，
            // 并且comment可以直接访问
            comment.setReplyComment(tempReplys);
            //清除临时存放区
            tempReplys = new ArrayList<>();
        }
    }

    /**
     * 递归迭代
     *
     * @param comment 被迭代的对象
     *                出来之后就是这个样子：
     *                ]
     *                ]
     *                ]
     *                ...
     *                ]
     *                ]
     *                ]
     *                ...
     */
    private void recursively(Comment comment) {
        tempReplys.add(comment);  //头节点添加到临时存放集合
        if (comment.getReplyComment().size() > 0) {
            List<Comment> replys = comment.getReplyComment();//二级回复
            for (Comment reply : replys) {
                tempReplys.add(reply);
                if (reply.getReplyComment().size() > 0) {   //三级回复
                    recursively(reply);
                }

            }
        }
    }
}
