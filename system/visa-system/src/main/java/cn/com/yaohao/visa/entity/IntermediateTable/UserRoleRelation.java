package cn.com.yaohao.visa.entity.IntermediateTable;

import javax.persistence.*;

@Entity
@Table(name = "user_role_relation")
public class UserRoleRelation {//一个用户多个角色
    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    private Integer id;
    private  Integer rid;//角色ID
    private String uid;//用户ID

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRid() {
        return rid;
    }

    public void setRid(Integer rid) {
        this.rid = rid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
