package cn.alees.service;

import cn.alees.dto.Response;
import cn.alees.entity.User;
import cn.alees.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
@SuppressWarnings("all")
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public User findUserByIp(String ip){
        Example example = new Example(User.class);
        example.createCriteria().andEqualTo("ip", ip);
        List<User> userList = userMapper.selectByExample(example);
        if(userList.size() != 0){
            return userList.get(0);
        }
        return null;
    }

    public void save(User user){
        userMapper.insert(user);
    }

    /**
     * 添加用户名
     * @param name
     * @return
     */
    public Response putName(String ip, String name, String avatar) {
        if(userMapper.findByName(name) != null){
            return Response.info(HttpStatus.CONFLICT.value(), "用户名已存在");
        }
        userMapper.putName(ip, name, avatar);
        return Response.info(HttpStatus.OK.value(), "登入成功");
    }
}
