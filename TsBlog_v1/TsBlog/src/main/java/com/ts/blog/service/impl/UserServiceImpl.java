package com.ts.blog.service.impl;

import com.ts.blog.dao.UserRepository;
import com.ts.blog.po.User;
import com.ts.blog.service.UserService;
import com.ts.blog.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @BelongsProject: TsBlog
 * @BelongsPackage: com.ts.blog.service.impl
 * @Author: ts
 * @CreateTime: 2022-11-22  21:19
 * @Description: TODO
 * @Version: 1.0
*/

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;


    @Override
    public User checkUser(String username, String password) {

        User user = userRepository.findByUsernameAndPassword(username, MD5Utils.code(password));
        return user;
    }
}

