package haoyu.niubi.community.service;

import haoyu.niubi.community.dto.PaginationDTO;
import haoyu.niubi.community.dto.QuestionDTO;
import haoyu.niubi.community.exception.CustomizeErrorCode;
import haoyu.niubi.community.exception.CustomizeException;
import haoyu.niubi.community.mapper.QuestionMapper;
import haoyu.niubi.community.mapper.UserMapper;
import haoyu.niubi.community.model.Question;
import haoyu.niubi.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private UserMapper userMapper;

    public PaginationDTO list(Integer page, Integer size) {
        PaginationDTO paginationDTO = new PaginationDTO();

        Integer totalPage;

        Integer totalCount = questionMapper.count();

        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }

        if (page < 1) {
            page = 1;
        }
        if (page > totalPage) {
            page = totalPage;
        }
        if(page == 0){
            page = 1;
        }

        paginationDTO.setPagination(totalPage, page);
        //size*(page-1)
        Integer offset = size * (page - 1);
        List<Question> questions = questionMapper.list(offset, size);
        List<QuestionDTO> questionDTOList = new ArrayList<>();

        for (Question question : questions) {
            User user = userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }

        paginationDTO.setQuestions(questionDTOList);
        return paginationDTO;
    }

    public PaginationDTO  list(Integer userId, Integer page, Integer size) {
        PaginationDTO paginationDTO = new PaginationDTO();

        Integer totalPage;

        Integer totalCount = questionMapper.countByUserId(userId);

        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }
        if(totalPage == 0){
            totalPage =1;
        }
        if (page < 1) {
            page = 1;
        }
        if (page > totalPage) {
            page = totalPage;
        }
        if(page ==0){
            page =1;
        }

        paginationDTO.setPagination(totalPage, page);

        //size*(page-1)
        Integer offset = size * (page - 1);
        List<Question> questions = questionMapper.listByUserId(userId, offset, size);
        List<QuestionDTO> questionDTOList = new ArrayList<>();

        for (Question question : questions) {
            User user = userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }

        paginationDTO.setQuestions(questionDTOList);
        return paginationDTO;
    }

    public QuestionDTO getById(Integer id) {
    Question question = questionMapper.getById(id);
     if(question == null){
         throw  new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
     }
        Integer  creatorId = question.getCreator();
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question, questionDTO);
        User user = userMapper.findById(creatorId);
        questionDTO.setUser(user);
        return  questionDTO;
    }
    public void createOrUpdate(Question question){
        if(question.getId() == null){
            question.setGmtModified(question.getGmtCreate());
            questionMapper.create(question);
        }else{
            question.setGmtModified(System.currentTimeMillis());
           int update=questionMapper.update(question);
           if(update != 1){
               throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
}
    }

    public Integer incView(Integer id) {
        Question question1 = questionMapper.getById(id);
        question1.setViewCount(question1.getViewCount()+1);
        questionMapper.updateView(question1);
        return  question1.getViewCount();
    }
}

