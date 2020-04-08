package cn.lixinblog.interceptor;

import cn.lixinblog.entity.User;
import cn.lixinblog.service.UserService;
import cn.lixinblog.utils.IpUtils;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import eu.bitwalker.useragentutils.Version;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Component
public class MyInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ip = IpUtils.getIpAddress(request);
        User user = userService.findUserByIp(ip);
        if(user == null){
            user = new User();
            //获取UserAgent对象
            UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
            //获取系统信息
            OperatingSystem os = userAgent.getOperatingSystem();
            //获取浏览器信息
            Browser browser = userAgent.getBrowser();
            //获取浏览器版本信息
            Version version = browser.getVersion(request.getHeader("User-Agent"));
            user.setIp(ip)
                .setState(1)
                .setEquipment(os.getName())
                .setAttendedMode(browser.getName() + " / " + version.getVersion())
                .setCreatedTime(new Date());
            userService.save(user);
        }
        if(user.getState() == 0){
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

}
