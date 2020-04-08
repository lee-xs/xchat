package cn.lixinblog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class Response {

    private Integer code;

    private String message;

    private Object data;

    public Response(Integer code, String message){
        this.code = code;
        this.message = message;
    }

    public Response(Integer code, Object data){
        this.code = code;
        this.data = data;
    }

    public static Response info(Integer code, String message){
        return new Response(code, message);
    }

    public static Response info(Integer code, Object data){
        return new Response(code, data);
    }

    public static Response info(Integer code, String message, Object data){
        return new Response(code, message, data);
    }
}
