package cn.com.yaohao.visa.entity.IntermediateTable;

import javax.persistence.*;

@Entity
@Table(name = "urlrelation")
public class UrlRelation {//角色可访问页面
    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    private Integer id;
    private  String rid;//角色ID
    private String urlid;//urlID

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getUrlid() {
        return urlid;
    }

    public void setUrlid(String urlid) {
        this.urlid = urlid;
    }
}
