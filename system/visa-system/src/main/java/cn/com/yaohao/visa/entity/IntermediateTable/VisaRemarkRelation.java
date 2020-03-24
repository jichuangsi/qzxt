package cn.com.yaohao.visa.entity.IntermediateTable;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "visa_remark_relation")
@GenericGenerator(name = "jpa-uuid",strategy = "uuid")
public class VisaRemarkRelation {
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    private String id;//备注信息与护照信息
    private String remarkId;//备注信息id
    private String visaId;//护照信息id

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRemarkId() {
        return remarkId;
    }

    public void setRemarkId(String remarkId) {
        this.remarkId = remarkId;
    }

    /*public String getPassportId() {
        return passportId;
    }

    public void setPassportId(String passportId) {
        this.passportId = passportId;
    }*/

    public String getVisaId() {
        return visaId;
    }

    public void setVisaId(String visaId) {
        this.visaId = visaId;
    }
}
