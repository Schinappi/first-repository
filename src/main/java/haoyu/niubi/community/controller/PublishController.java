package haoyu.niubi.community.controller;

import haoyu.niubi.community.mapper.QuestionMapper;
import haoyu.niubi.community.model.Question;
import haoyu.niubi.community.model.User;
import haoyu.niubi.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishController {
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private QuestionService questionService;
    @GetMapping("/publish/{id}")
public String edit(@PathVariable(name="id")Integer id,
                   Model model){
        Question question = questionMapper.getById(id);
        model.addAttribute("title",question.getTitle());
        model.addAttribute("description",question.getDescription());
        model.addAttribute("tag",question.getTag());
        model.addAttribute("id",question.getId());
        return "publish";
    }

    @GetMapping("/publish")
    public String publish() {
        return "publish";
    }
    @PostMapping("/publish")
    public String doPublish(
            @RequestParam(value ="title",required = false) String title,
            @RequestParam(value ="description",required = false) String description,
            @RequestParam(value ="tag",required = false) String tag,
            @RequestParam(value = "id",required = false)Integer id,
            HttpServletRequest request, Model model
    ) {
        User user = (User)request.getSession().getAttribute("user");
        if (user == null) {
            model.addAttribute("error", "用户未登录");
            return "publish";
        }  if (tag == null) {
            model.addAttribute("error", "标签不能为空");
            return "publish";
        }  if (description == null) {
            model.addAttribute("error", "描述不能为空");
            return "publish";
        }  if (title == null) {
            model.addAttribute("error", "标题不能为空");
            return "publish";
        }
        Question question = new Question();
        question.setTitle(title);
        question.setTag(tag);
        question.setDescription(description);
        question.setCreator(user.getId());
        question.setId(id);
       questionService.createOrUpdate(question);
        return "redirect:/";
    }
}
