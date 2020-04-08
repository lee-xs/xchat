package cn.lixinblog.service;

import cn.lixinblog.dto.Response;
import cn.lixinblog.entity.User;
import cn.lixinblog.mapper.UserMapper;
import com.alibaba.fastjson.JSONObject;
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
    public Response putName(String ip, String name) {
        int index = userMapper.putName(ip, name);
        JSONObject o = new JSONObject();
        if(index == 1){
            o.put("message", "登入成功");
            return Response.info(HttpStatus.OK.value(), o);
        }
        o.put("message", "用户名已存在");
        return Response.info(HttpStatus.CONFLICT.value(), o);
    }
}
