package cn.com.yaohao.visa.model.backuser;

import cn.com.yaohao.visa.entity.IntermediateTable.UserRoleRelation;

import java.util.List;

public class RoleAddModel {
    private List<UserRoleRelation> userRoleRelations;

    public List<UserRoleRelation> getUserRoleRelations() {
        return userRoleRelations;
    }

    public void setUserRoleRelations(List<UserRoleRelation> userRoleRelations) {
        this.userRoleRelations = userRoleRelations;
    }
}
