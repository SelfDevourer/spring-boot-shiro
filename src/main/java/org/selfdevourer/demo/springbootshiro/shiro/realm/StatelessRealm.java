package org.selfdevourer.demo.springbootshiro.shiro.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.selfdevourer.demo.springbootshiro.shiro.token.StatelessToken;
import org.selfdevourer.demo.springbootshiro.shiro.token.manager.TokenManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class StatelessRealm extends AuthorizingRealm {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private TokenManager tokenManager;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof StatelessToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        logger.info("StatelessRealm.doGetAuthorizationInfo");
        //根据用户名查找角色，请根据需求实现
        String userCode = (String) principals.getPrimaryPrincipal();
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        List<String> selectRoles = Arrays.asList("role");
        authorizationInfo.addRoles(selectRoles);
        List<String> permissions = Arrays.asList("test1", "test2");
        authorizationInfo.addStringPermissions(permissions);
        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(
            AuthenticationToken token) throws AuthenticationException {
        logger.info("StatelessRealm.doGetAuthenticationInfo");
        StatelessToken statelessToken = (StatelessToken) token;

        String userCode = (String) statelessToken.getPrincipal();

        checkUserExists(userCode);

        String credentials = (String) statelessToken.getCredentials();
        boolean checkToken = tokenManager.checkToken(statelessToken);
        if (checkToken) {
            return new SimpleAuthenticationInfo(userCode, credentials, super.getName());
        } else {
            throw new AuthenticationException("token认证失败");
        }
    }

    private void checkUserExists(String userCode) throws AuthenticationException {
        // 查询用户是否存在 如果不存在则抛出 UnknownAccountException
//        Object principal = principalService.select(userCode);
//        if (principal == null) {
//            throw new UnknownAccountException("userCode " + userCode + " wasn't in the system");
//        }
    }

    public TokenManager getTokenManager() {
        return tokenManager;
    }

    public void setTokenManager(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

}
