package cn.com.yaohao.visa.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "supplement")
@GenericGenerator(name = "jpa-uuid",strategy = "uuid")
public class Supplement {
    //护照不通过的补充说明
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    private String id;//护照补充说明
    private String content;//补充条件

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
