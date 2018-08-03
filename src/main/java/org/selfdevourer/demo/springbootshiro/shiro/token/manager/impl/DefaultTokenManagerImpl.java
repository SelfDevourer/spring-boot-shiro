package org.selfdevourer.demo.springbootshiro.shiro.token.manager.impl;

import org.selfdevourer.demo.springbootshiro.shiro.token.StatelessToken;
import org.selfdevourer.demo.springbootshiro.shiro.token.manager.AbstractTokenManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class DefaultTokenManagerImpl extends AbstractTokenManager {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public String createStringToken(String userCode) {
        logger.info("DefaultTokenManagerImpl.createStringToken");
        //创建简易的32为uuid
        return UUID.randomUUID().toString().replace("-", "");
    }

    @Override
    public boolean checkToken(StatelessToken model) {
        return super.checkMemoryToken(model);
    }

}
