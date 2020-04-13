package haoyu.niubi.community.service;

import haoyu.niubi.community.dto.CommentDTO;
import haoyu.niubi.community.enums.CommentTypeEnum;
import haoyu.niubi.community.enums.NotificationStatusEnum;
import haoyu.niubi.community.enums.NotificationTypeEnum;
import haoyu.niubi.community.exception.CustomizeErrorCode;
import haoyu.niubi.community.exception.CustomizeException;
import haoyu.niubi.community.mapper.CommentMapper;
import haoyu.niubi.community.mapper.NotificationMapper;
import haoyu.niubi.community.mapper.QuestionMapper;
import haoyu.niubi.community.mapper.UserMapper;
import haoyu.niubi.community.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CommentService {
    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private NotificationMapper notificationMapper;
    @Autowired
    private UserMapper userMapper;
    @Transactional
    public void insert(Comment comment, User commentator) {
        if(comment.getParentId() == null || comment.getParentId() == 0){
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARM_NOT_FOUND);
        }
        if(comment.getType()==null || !CommentTypeEnum.isExist(comment.getType())){
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARM_WRONG);
        }
        if(comment.getType() == CommentTypeEnum.COMMENT.getType()){
            //回复评论
          Comment comment1 =  commentMapper.selectByPrimaryKey(comment.getParentId());
          if(comment1 == null){
              throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
          }
            Question question = questionMapper.getById(comment.getParentId());
            commentMapper.insert(comment);
          //增加评论数
            Comment parentComment = new Comment();
            Integer parentId=comment.getParentId();
            parentComment.setId(parentId);
            parentComment.setCommentCount( commentMapper.selectCommentCount(parentId)+1);
            commentMapper.incCommentCount(parentComment);
            //创建通知
            createNotify(comment, comment1.getCommentator(), commentator.getName(), question.getTitle(), NotificationTypeEnum.REPLY_COMMENT);
        }else{
               //回复问题
            Question question = questionMapper.getById(comment.getParentId());
            if(question == null){
                throw  new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            commentMapper.insert(comment);
            question.setCommentCount(question.getCommentCount()+1);
            questionMapper.incCommentCount(question);
            //创建通知
            createNotify(comment, question.getCreator(),commentator.getName(),question.getTitle() ,NotificationTypeEnum.REPLY_QUESTION);
        }
    }

    private void createNotify(Comment comment, Integer receiver, String name, String title, NotificationTypeEnum notificationType) {
        if(receiver == comment.getCommentator()){
            return;
        }
        Notification notification = new Notification();
        notification.setGmt_create(System.currentTimeMillis());
        notification.setType(notificationType.getType());
        notification.setOuterId(comment.getParentId());
        notification.setNotifier(comment.getCommentator());
        notification.setNotifierName(name);
        notification.setOuterTitle(title);
        notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
        notification.setReceiver(receiver);
        notificationMapper.insert(notification);
    }

    public List<CommentDTO> listByTargetId(Integer id, CommentTypeEnum type) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setParentId(id);
        commentDTO.setType(type.getType());
        List<Comment> comments=commentMapper.selectByCommentDTO(commentDTO);
        if(comments.size() == 0){
            return new ArrayList<>();
        }
        //获取去重评论人
        //Set集合避免重复
        Set<Integer> commentators = comments.stream().map(comment -> comment.getCommentator()).collect(Collectors.toSet());
        List<Integer> userIds = new ArrayList<>();
        userIds.addAll(commentators);
        //获取评论人并转化成MAP
        UserExample userExample = new UserExample();
        userExample.setId(userIds);
        List<User> users = userMapper.selectByUser(userExample);
        Map<Integer, User> userMap = users.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));
        //转换comment为commentDTO
        List<CommentDTO> commentDTOS = comments.stream().map(comment -> {
            CommentDTO commentDTO1 = new CommentDTO();
            BeanUtils.copyProperties(comment,commentDTO1);
            commentDTO1.setUser(userMap.get(comment.getCommentator()));
            return commentDTO1;
        }).collect(Collectors.toList());
        return  commentDTOS;
    }
}
