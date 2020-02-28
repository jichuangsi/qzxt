package cn.com.yaohao.visa.entity.IntermediateTable;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "passport_supplement_relation")
@GenericGenerator(name = "jpa-uuid",strategy = "uuid")
public class PassportSupplementRelation {
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    private String id;//护照补充说明
    private String passId;//护照id
    private String contentId;//补充条件
    private String illustrated;//说明

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassId() {
        return passId;
    }

    public void setPassId(String passId) {
        this.passId = passId;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getIllustrated() {
        return illustrated;
    }

    public void setIllustrated(String illustrated) {
        this.illustrated = illustrated;
    }
}
