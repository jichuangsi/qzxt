package cn.com.yaohao.visa.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "logistics")
@GenericGenerator(name = "jpa-uuid",strategy = "uuid")
public class Logistics {//顺丰接入物流
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    private String id;
    private String orderId;//客户订单号
    private String sender;//寄件方联系人
    private String senderPhone;//寄件方联系电话
    private String sendAddress;//	寄件方详细地址
    private String getP;//到件方联系人
    private String getPhone;//到件方联系电话
    private String getAddress;//	到件方详细地址
    private String sendStarTime;
    private String logisticsNumber;//1：人工确认  2：可收派  3：不可以收派
    private String message;
    private long createTime=new Date().getTime();
    private String sfExpressId;//顺丰运单号
    private String code;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSenderPhone() {
        return senderPhone;
    }

    public void setSenderPhone(String senderPhone) {
        this.senderPhone = senderPhone;
    }

    public String getSendAddress() {
        return sendAddress;
    }

    public void setSendAddress(String sendAddress) {
        this.sendAddress = sendAddress;
    }

    public String getSendStarTime() {
        return sendStarTime;
    }

    public void setSendStarTime(String sendStarTime) {
        this.sendStarTime = sendStarTime;
    }

    public String getLogisticsNumber() {
        return logisticsNumber;
    }

    public void setLogisticsNumber(String logisticsNumber) {
        this.logisticsNumber = logisticsNumber;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getGetP() {
        return getP;
    }

    public void setGetP(String getP) {
        this.getP = getP;
    }

    public String getGetPhone() {
        return getPhone;
    }

    public void setGetPhone(String getPhone) {
        this.getPhone = getPhone;
    }

    public String getGetAddress() {
        return getAddress;
    }

    public void setGetAddress(String getAddress) {
        this.getAddress = getAddress;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getSfExpressId() {
        return sfExpressId;
    }

    public void setSfExpressId(String sfExpressId) {
        this.sfExpressId = sfExpressId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
