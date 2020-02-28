package cn.com.yaohao.visa.entity.IntermediateTable;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "passport_relation")
@GenericGenerator(name = "jpa-uuid",strategy = "uuid")
public class PassportRelation {
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    private String id;//备注信息与护照信息
    private String essentialId;//基本信息id
    private String passportId;//护照信息id

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEssentialId() {
        return essentialId;
    }

    public void setEssentialId(String essentialId) {
        this.essentialId = essentialId;
    }

    public String getPassportId() {
        return passportId;
    }

    public void setPassportId(String passportId) {
        this.passportId = passportId;
    }
}
