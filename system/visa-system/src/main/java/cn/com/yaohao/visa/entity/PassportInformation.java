package cn.com.yaohao.visa.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "passport_information")
@GenericGenerator(name = "jpa-uuid",strategy = "uuid")
public class PassportInformation {
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    private String id;//护照信息
    private String name;//姓名
    private String sex;//性别
    private String birthDay;//出生日期
    private String birthPlace;//出生地
    private String habitation;//居住地
    private String passportEncoding;//护照编码
    private String expiryDate;//有效期
    private String telephoneNumber;//联系电话
    private String returnAddress;//寄回地址
    private String luggage;//运费
    private byte[] passprot;//护照图片
    private String status;//状态=》A通过。P=》没通过
    private long createTime=System.currentTimeMillis();//创建时间


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public String getHabitation() {
        return habitation;
    }

    public void setHabitation(String habitation) {
        this.habitation = habitation;
    }

    public String getPassportEncoding() {
        return passportEncoding;
    }

    public void setPassportEncoding(String passportEncoding) {
        this.passportEncoding = passportEncoding;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public String getReturnAddress() {
        return returnAddress;
    }

    public void setReturnAddress(String returnAddress) {
        this.returnAddress = returnAddress;
    }

    public String getLuggage() {
        return luggage;
    }

    public void setLuggage(String luggage) {
        this.luggage = luggage;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public byte[] getPassprot() {
        return passprot;
    }

    public void setPassprot(byte[] passprot) {
        this.passprot = passprot;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
