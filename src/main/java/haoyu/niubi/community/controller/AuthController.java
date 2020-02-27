package haoyu.niubi.community.controller;

import haoyu.niubi.community.dto.AccesstokenDTO;
import haoyu.niubi.community.dto.GithubUser;
import haoyu.niubi.community.mapper.UserMapper;
import haoyu.niubi.community.model.User;
import haoyu.niubi.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
     public class AuthController {
    @Autowired
    private GithubProvider githubProvider;
    @Value("${github.client.id}")
    private  String client_id;
    @Value("${github.client.secret}")
    private String client_secret;
    @Value("${github.redirect_uri}")
    private  String redirect_uri;
    @Autowired
    private UserMapper userMapper;
    @GetMapping("/callback")
    public String callback(@RequestParam(name="code")String code,
                           @RequestParam(name="state")String state
              , HttpServletResponse response) {
        AccesstokenDTO accesstokenDTO = new AccesstokenDTO();
        accesstokenDTO.setCode(code);
        accesstokenDTO.setRedirect_uri(redirect_uri);
        accesstokenDTO.setState(state);
        accesstokenDTO.setClient_secret(client_secret);
        accesstokenDTO.setClient_id(client_id);
        String accessToken = githubProvider.getAccessToken(accesstokenDTO);
        GithubUser githubUser = githubProvider.getuser(accessToken);
         if(githubUser != null && githubUser.getId() != null){
            User user = new User();
            String token = UUID.randomUUID().toString();//随机创建值赋值给token
            user.setToken(token);
            user.setAccountId(githubUser.getId());
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            user.setAvatarUrl(githubUser.getAvatar_url());
            user.setName(githubUser.getName());
            userMapper.insert(user);
            response.addCookie(new Cookie("token",token));//Cookie 是通过response创建的
        return "redirect:/";
        }
        else {
            return "redirect:/";
        }
    }
}
