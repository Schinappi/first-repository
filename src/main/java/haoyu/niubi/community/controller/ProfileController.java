package haoyu.niubi.community.controller;

import haoyu.niubi.community.dto.PaginationDTO;
import haoyu.niubi.community.mapper.UserMapper;
import haoyu.niubi.community.model.User;
import haoyu.niubi.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class ProfileController {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionService questionService;

    @GetMapping("/profile/{action}")
    public String profile(@PathVariable(name = "action") String action,
                          Model model,
                          HttpServletRequest request,
                          @RequestParam(name = "page", defaultValue = "1") Integer page,
                          @RequestParam(name = "size", defaultValue = "4") Integer size) {
        User user = (User) request.getSession().getAttribute("user");
        if(user ==null){
            return "redirect:/";
        }
        if ("question".equals(action)) {
            model.addAttribute("section", "question");
            model.addAttribute("sectionName", "我的问题");
            PaginationDTO paginationDTO = questionService.list(user.getAccountId(), page, size);
            model.addAttribute("pagination", paginationDTO);
        } else if ("replies".equals(action)) {
            model.addAttribute("section", "replies");
            model.addAttribute("sectionName", "最新回复");
        }
        PaginationDTO paginationDTO = questionService.list(user.getAccountId(), page, size);
        model.addAttribute("paginationDTO",paginationDTO);
        return "profile";
    }
}
