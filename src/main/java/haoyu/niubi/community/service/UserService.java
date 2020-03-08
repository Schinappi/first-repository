package haoyu.niubi.community.service;

import haoyu.niubi.community.mapper.UserMapper;
import haoyu.niubi.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;
    public void createOrUpdate(User user) {
        Integer accountId=user.getAccountId();
        User user1 = userMapper.findByAccountId(accountId);
        String token = user.getToken();
        if(user1 == null){
            userMapper.insert(user);
        }else{
            user1.setGmtModified(System.currentTimeMillis());
            user1.setAvatarUrl(user.getAvatarUrl());
            user1.setName(user.getName());
            user1.setToken(user.getToken());
            userMapper.update(user1);
        }
    }
}
