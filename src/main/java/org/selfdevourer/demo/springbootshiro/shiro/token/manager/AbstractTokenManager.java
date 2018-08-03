package org.selfdevourer.demo.springbootshiro.shiro.token.manager;

import org.selfdevourer.demo.springbootshiro.shiro.token.StatelessToken;
import org.selfdevourer.demo.springbootshiro.shiro.token.helper.UserTokenOperHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

public abstract class AbstractTokenManager implements TokenManager {

    /**
     * 失效时间 单位秒
     */
    protected long expirateTime;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    protected String userTokenPrefix = "token_";

    protected UserTokenOperHelper userTokenOperHelper;

    //protected LoginFlagOperHelper loginFlagOperHelper;

    @Override
    public StatelessToken createToken(String userCode) {
        logger.info("AbstractTokenManager.createToken");
        StatelessToken tokenModel = null;
        String token = userTokenOperHelper.getUserToken(userTokenPrefix + userCode);
        if (StringUtils.isEmpty(token)) {
            token = createStringToken(userCode);
        }
        userTokenOperHelper.updateUserToken(userTokenPrefix + userCode, token, expirateTime);
        tokenModel = new StatelessToken(userCode, token);
        return tokenModel;
    }

    public abstract String createStringToken(String userCode);

    protected boolean checkMemoryToken(StatelessToken model) {
        logger.info("AbstractTokenManager.checkMemoryToken");
        if (model == null) {
            return false;
        }
        String userCode = (String) model.getPrincipal();
        String credentials = (String) model.getCredentials();
        String token = userTokenOperHelper.getUserToken(userTokenPrefix + userCode);
        if (token == null || !credentials.equals(token)) {
            return false;
        }
        return true;
    }

    @Override
    public StatelessToken getToken(String authentication) {
        logger.info("AbstractTokenManager.getToken");
        if (StringUtils.isEmpty(authentication)) {
            return null;
        }
        String[] au = authentication.split("_");
        if (au.length <= 1) {
            return null;
        }
        String userCode = au[0];
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < au.length; i++) {
            sb.append(au[i]);
            if (i < au.length - 1) {
                sb.append("_");
            }
        }
        return new StatelessToken(userCode, sb.toString());
    }

    @Override
    public boolean check(String authentication) {
        logger.info("AbstractTokenManager.check");
        StatelessToken token = getToken(authentication);
        if (token == null) {
            return false;
        }
        return checkMemoryToken(token);
    }

    @Override
    public void deleteToken(String userCode) {
        userTokenOperHelper.deleteUserToken(userTokenPrefix + userCode);
    }

    public long getExpirateTime() {
        return expirateTime;
    }

    public void setExpirateTime(long expirateTime) {
        this.expirateTime = expirateTime;
    }

    public UserTokenOperHelper getUserTokenOperHelper() {
        return userTokenOperHelper;
    }

    public void setUserTokenOperHelper(UserTokenOperHelper userTokenOperHelper) {
        this.userTokenOperHelper = userTokenOperHelper;
    }

}
