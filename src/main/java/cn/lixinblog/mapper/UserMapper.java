package cn.lixinblog.mapper;

import cn.lixinblog.entity.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

public interface UserMapper extends Mapper<User> {

    @Update("UPDATE user SET name = #{name} WHERE ip = #{ip}")
    int putName(@Param("ip") String ip, @Param("name") String name);

}
