package haoyu.niubi.community.service;

import haoyu.niubi.community.dto.PaginationDTO;
import haoyu.niubi.community.dto.QuestionDTO;
import haoyu.niubi.community.mapper.QuestionMapper;
import haoyu.niubi.community.mapper.UserMapper;
import haoyu.niubi.community.model.Question;
import haoyu.niubi.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        Integer totalCount = questionMapper.count();
        Integer totalPage;
//        paginationDTO.setPagination(totalCount,page,size);

        if(totalCount %size ==0){
            totalPage = totalCount %size;
        }else{
            totalPage = totalCount %size +1;
        }
        if(page <1)
        {
            page =1;
        }
        if (page > totalPage) {
            page = totalPage;
        }

        Integer offset = size *(page - 1);
        List<Question> questions = questionMapper.list(offset, size);
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        for (Question question : questions) {
            User user = userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);//把question的全部属性赋给questionDTO
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setQuestions(questionDTOList);
        return paginationDTO;
    }

    public PaginationDTO  list(Integer accountId, Integer page, Integer size) {
        Integer totalPage;
        PaginationDTO paginationDTO = new PaginationDTO();
        Integer totalCount = questionMapper.countByAccountId(accountId);

        if(totalCount %size ==0){
            totalPage = totalCount%size;
        }else{
            totalPage = totalCount %size +1;
        }
        if(page <1)
        {
            page =1;
        }
        if(page >totalPage){
            page = totalPage;
        }
        paginationDTO.setPagination(totalPage,page);
        Integer offset = size *(page - 1);
        List<Question> questions = questionMapper.list2(accountId,offset, size);
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        for (Question question : questions) {
            User user = userMapper.findByAccountId(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);//把question的全部属性赋给questionDTO
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setQuestions(questionDTOList);
        return paginationDTO;
    }
    }

