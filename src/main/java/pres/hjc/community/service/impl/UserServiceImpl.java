package pres.hjc.community.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import pres.hjc.community.dao.UserMapper;
import pres.hjc.community.entity.UserPO;
import pres.hjc.community.service.UserService;

/**
 * @author HJC
 * @version 1.0
 * 谦谦君子 卑以自牧也
 * @date 2020/8/2  13:10
 * @description :
 */
@Service
@Primary
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;

    @Override
    public UserPO selectById(int id) {
        return userMapper.selectById(id);
    }
}
