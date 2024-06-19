package com.dqw.factory;

import com.dqw.strategy.LoginStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/*
* 登录策略工厂
* */
@Component
public class LoginStrategyFactory {

    @Autowired
    private Map<String, LoginStrategy> loginStrategyMap=new HashMap<>();


    /**
     * 根据登录类型获取具体登录的策略
     * @param loginType
     * @return
     */
    public LoginStrategy getInstance(String loginType){
        return loginStrategyMap.get(loginType);
    }


}
