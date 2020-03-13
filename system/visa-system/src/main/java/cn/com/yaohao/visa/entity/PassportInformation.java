package cn.com.yaohao.visa.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "passport_information")
@GenericGenerator(name = "jpa-uuid",strategy = "uuid")
public class PassportInformation {
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    private String id;//护照信息
    private String name;//姓名
    private String sex;//性别
    private long birthDay;//出生日期
    private String birthPlace;//出生地
    private String habitation;//居住地
    private String passportEncoding;//护照编码
    private long expiryDate;//有效期
    private String telephoneNumber;//联系电话
    private String returnAddress;//寄回地址
    private String luggage;//运费
    private String picPath;//图片地址
    private String status;//状态=》P通过。F=》没通过。W=》待审核
    private String sendStatus;//送签状态 已送=》S，出签=》O，拒签=》R，寄回=》SB
    private long checkTime;//审核时间
    private String expireTime;//工期到期时间
    private String orderId;//订单编号
    private String travel;//旅行社
    private String visaType;//签证种类
    private long signDate;//签发日期
    private String signAddress;//签发地
    private String remarks;
    private String isSendBack;//是否寄回
    private String isExport;//是否导出
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

    public long getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(long birthDay) {
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

    public long getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(long expiryDate) {
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

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(long checkTime) {
        this.checkTime = checkTime;
    }

    public String getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(String sendStatus) {
        this.sendStatus = sendStatus;
    }

    public String getTravel() {
        return travel;
    }

    public void setTravel(String travel) {
        this.travel = travel;
    }

    public String getVisaType() {
        return visaType;
    }

    public void setVisaType(String visaType) {
        this.visaType = visaType;
    }

    public String getSignAddress() {
        return signAddress;
    }

    public void setSignAddress(String signAddress) {
        this.signAddress = signAddress;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public long getSignDate() {
        return signDate;
    }

    public void setSignDate(long signDate) {
        this.signDate = signDate;
    }

    public String getIsSendBack() {
        return isSendBack;
    }

    public void setIsSendBack(String isSendBack) {
        this.isSendBack = isSendBack;
    }

    public String getIsExport() {
        return isExport;
    }

    public void setIsExport(String isExport) {
        this.isExport = isExport;
    }
}
