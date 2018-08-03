package org.selfdevourer.demo.springbootshiro.shiro.token.helper;

public interface UserTokenOperHelper {
    /**
     * 根据用户编码获取令牌
     *
     * @param userCode
     * @return
     */
    String getUserToken(String userCode);

    /**
     * 更新令牌， 每次获取令牌成功时更新令牌失效时间
     *
     * @param userCode
     * @param token
     * @param seconds
     */
    void updateUserToken(String userCode, String token, long seconds);

    /**
     * 删除令牌
     *
     * @param userCode
     */
    void deleteUserToken(String userCode);

}
