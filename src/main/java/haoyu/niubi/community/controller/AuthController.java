package haoyu.niubi.community.controller;

import haoyu.niubi.community.dto.AccesstokenDTO;
import haoyu.niubi.community.dto.GithubUser;
import haoyu.niubi.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    @GetMapping("/callback")
    public String callback(@RequestParam(name="code")String code,
                           @RequestParam(name="state")String state){
        AccesstokenDTO accesstokenDTO = new AccesstokenDTO();
        accesstokenDTO.setCode(code);
        accesstokenDTO.setRedirect_uri(redirect_uri);
        accesstokenDTO.setState(state);
        accesstokenDTO.setClient_secret(client_secret);
        accesstokenDTO.setClient_id(client_id);
        String accessToken = githubProvider.getAccessToken(accesstokenDTO);
        GithubUser user = githubProvider.getuser(accessToken);
        System.out.println(user.getName());
        return "index";
    }
}
