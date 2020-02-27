package haoyu.niubi.community.dto;

import lombok.Data;

@Data
public class GithubUser {
    private String name;
    private  Integer id;
    private  String bio;//描述
    private String avatar_url;
}
