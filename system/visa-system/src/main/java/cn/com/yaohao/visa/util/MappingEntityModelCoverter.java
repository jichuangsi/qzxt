package cn.com.yaohao.visa.util;

import cn.com.yaohao.visa.entity.BackUserInfo;
import cn.com.yaohao.visa.entity.Logistics;
import cn.com.yaohao.visa.model.LogisticsModel;
import cn.com.yaohao.visa.model.UserInfoForToken;
import cn.com.yaohao.visa.model.backuser.BackUserModel;

public class MappingEntityModelCoverter {

   public final static UserInfoForToken CONVERTERFROMBACKUSERINFO(BackUserInfo userInfo) {
       UserInfoForToken userInfoForToken = new UserInfoForToken();
       userInfoForToken.setUserId(userInfo.getId());
       userInfoForToken.setUserName(userInfo.getUserName());
       userInfoForToken.setRoleId(userInfo.getRoleId());
       return userInfoForToken;
   }

    public final static BackUserModel CONVERTERFROMBACKUSERINFOTOMODEL(BackUserInfo userInfo) {
        BackUserModel model=new BackUserModel();
        model.setAccount(userInfo.getAccount());
        model.setUserName(userInfo.getUserName());
        model.setCreatedTime(userInfo.getCreatedTime());
        model.setId(userInfo.getId());
        model.setStatus(userInfo.getStatus());
        model.setPwd(userInfo.getPwd());
        return model;
    }

    public final static Logistics CONVERTERFROMLOGISTICSMODELTOLOGISTIC(LogisticsModel model) {
        Logistics logistics=new Logistics();
        logistics.setGetAddress(model.getGetAddress());
        logistics.setGetP(model.getGet());
        logistics.setGetPhone(model.getGetPhone());
        logistics.setLogisticsNumber(model.getLogisticsNumber());
        logistics.setOrderId(model.getOrderId());
        logistics.setSendAddress(model.getSendAddress());
        logistics.setSender(model.getSender());
        logistics.setSenderPhone(model.getSenderPhone());
        return logistics;
    }
}
