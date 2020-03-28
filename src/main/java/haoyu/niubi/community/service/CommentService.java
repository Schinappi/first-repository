package haoyu.niubi.community.service;

import haoyu.niubi.community.dto.CommentDTO;
import haoyu.niubi.community.enums.CommentTypeEnum;
import haoyu.niubi.community.exception.CustomizeErrorCode;
import haoyu.niubi.community.exception.CustomizeException;
import haoyu.niubi.community.mapper.CommentMapper;
import haoyu.niubi.community.mapper.QuestionMapper;
import haoyu.niubi.community.mapper.UserMapper;
import haoyu.niubi.community.model.Comment;
import haoyu.niubi.community.model.Question;
import haoyu.niubi.community.model.User;
import haoyu.niubi.community.model.UserExample;
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
    private UserMapper userMapper;
    @Transactional
    public void insert(Comment comment) {
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
          commentMapper.insert(comment);
        }else{
               //回复问题
            Question question = questionMapper.getById(comment.getParentId());
            if(question == null){
                throw  new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            commentMapper.insert(comment);
            question.setCommentCount(question.getCommentCount()+1);
            questionMapper.updateComment(question);
        }
    }

    public List<CommentDTO> listByQuestionId(Integer id) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setParentId(id);
        commentDTO.setType(CommentTypeEnum.QUESTION.getType());
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
