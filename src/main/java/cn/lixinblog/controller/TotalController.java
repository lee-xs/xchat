package cn.lixinblog.controller;

import cn.lixinblog.dto.Response;
import cn.lixinblog.service.UserService;
import cn.lixinblog.websocket.MyWebSocket;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class TotalController {

    @Autowired
    private UserService userService;

    @GetMapping("/online")
    public Response online(){
        JSONObject o = new JSONObject();
        o.put("online", MyWebSocket.getOnlineCount());
        return Response.info(HttpStatus.OK.value(), o);
    }

    @PostMapping("/put")
    public Response putName(@RequestParam("ip") String ip, @RequestParam("name") String name, @RequestParam("avatar") String avatar){
        return userService.putName(ip, name, avatar);
    }

    @GetMapping("/info/{ip}")
    public Response info(@PathVariable("ip") String ip){
        return Response.info(HttpStatus.OK.value(), userService.findUserByIp(ip));
    }

}
