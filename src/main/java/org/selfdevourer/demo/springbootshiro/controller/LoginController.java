package org.selfdevourer.demo.springbootshiro.controller;


import org.apache.shiro.SecurityUtils;
import org.selfdevourer.demo.springbootshiro.shiro.token.StatelessToken;
import org.selfdevourer.demo.springbootshiro.shiro.token.manager.TokenManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private TokenManager tokenManager;

    protected static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @RequestMapping(value = "", method = RequestMethod.GET)
    public StatelessToken login(String userCode, String password) {
        logger.info("userCode:" + userCode);
//        Map<String, String> user = principalService.select(userCode);
        Map<String, String> user = new HashMap<>();
        user.put("password", "123456");
        if (user == null) {
            return new StatelessToken(userCode, "valid user");
        }
        if (!password.equals(user.get("password"))) {
            return new StatelessToken(userCode, "valid user password");
        }
        //成功创建token返回给客户端保存
        StatelessToken createToken = tokenManager.createToken(userCode);
        return createToken;
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpServletRequest request) {
        String authorization = request.getHeader("authorization");
//        String authorization = RequestUtil.newInstance().getRequestHeader(request, "authorization");
        StatelessToken token = tokenManager.getToken(authorization);
        if (token != null) {
            tokenManager.deleteToken(token.getUserCode());
        }
        SecurityUtils.getSubject().logout();
        logger.info("用户登出");
        return "logout success";
    }
}
