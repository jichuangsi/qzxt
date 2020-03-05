package cn.com.yaohao.visa.util;

import cn.com.yaohao.visa.entity.BackUserInfo;
import cn.com.yaohao.visa.entity.PassportInformation;
import cn.com.yaohao.visa.model.PassportModel;
import cn.com.yaohao.visa.model.UserInfoForToken;
import cn.com.yaohao.visa.model.backuser.BackUserModel;

public class MappingEntityModelCoverter {

   /* public static PassportInformation CONVERTERFROMBACKPASSPORTINFORMATION(PassportModel model){

    }*/
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
}
