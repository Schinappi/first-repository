package haoyu.niubi.community.controller;

import haoyu.niubi.community.dto.CommentDTO;
import haoyu.niubi.community.dto.QuestionDTO;
import haoyu.niubi.community.enums.CommentTypeEnum;
import haoyu.niubi.community.service.CommentService;
import haoyu.niubi.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class QuestionController {
    @Autowired
    private QuestionService questionService;

    @Autowired
    private CommentService commentService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id") Integer id, Model model) {
        List<CommentDTO> commentDTOS = commentService.listByTargetId(id, CommentTypeEnum.QUESTION);
        QuestionDTO questionDTO = questionService.getById(id);
        List<QuestionDTO> relatedQuestions=questionService.selectRelated(questionDTO);
        Integer viewCount = questionService.incView(id);
        model.addAttribute("viewCount", viewCount);
        model.addAttribute("question", questionDTO);
        model.addAttribute("comments", commentDTOS);
        model.addAttribute("relatedQuestions", relatedQuestions);
        return "question";
    }

}
