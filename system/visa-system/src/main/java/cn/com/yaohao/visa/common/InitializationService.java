package cn.com.yaohao.visa.common;

import cn.com.yaohao.visa.service.BackUserService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Component
public class InitializationService {
    /*@Resource
    private BackUserService backUserService;*/
    @Resource
    private BackUserService backUserService;

    @PostConstruct
    public void insertSuperMan(){
        try {
            backUserService.insertSuperMan();
            /*backUserService.insertSuperMan();*/
        } catch (Exception e) {
        }
    }
}
