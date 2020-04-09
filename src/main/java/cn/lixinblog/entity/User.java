package cn.lixinblog.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Accessors(chain = true)
@Table(name = "user")
public class User {

    @Id
    @KeySql(useGeneratedKeys = true)
    private Integer id;

    /**
     * 昵称 用户名
     */
    private String name;

    /**
     * qq
     */
    private String qq;

    /**
     * 状态
     */
    private Integer state;

    /**
     * 访问ip
     */
    private String ip;

    /**
     * 浏览器类型 版本
     */
    private String attendedMode;

    /**
     * 操作系统类型 版本
     */
    private String equipment;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 首次登入时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    private Date createdTime;

}
