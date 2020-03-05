package cn.com.yaohao.visa.entity.IntermediateTable;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "visa_passport_relation")
@GenericGenerator(name = "jpa-uuid",strategy = "uuid")
public class VisaPassportRelation {
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    private String id;//
    private String passId;//护照id
    private String vid;//快件id

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

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }
}
