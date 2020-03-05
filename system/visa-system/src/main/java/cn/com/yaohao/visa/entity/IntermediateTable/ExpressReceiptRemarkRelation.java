package cn.com.yaohao.visa.entity.IntermediateTable;

import javax.persistence.*;

@Entity
@Table(name = "expressreceipt_remark_relation")
public class ExpressReceiptRemarkRelation {
    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    private Integer id;//备注信息与护照信息
    private String remarkId;//备注信息id
    private String expressId;//快件信息id

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRemarkId() {
        return remarkId;
    }

    public void setRemarkId(String remarkId) {
        this.remarkId = remarkId;
    }

    public String getExpressId() {
        return expressId;
    }

    public void setExpressId(String expressId) {
        this.expressId = expressId;
    }
}
