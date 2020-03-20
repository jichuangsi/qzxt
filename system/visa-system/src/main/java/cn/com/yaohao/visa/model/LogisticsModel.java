package cn.com.yaohao.visa.model;

public class LogisticsModel {//顺丰接入物流
    private String orderId;//订单号
    private String sender;//寄件方联系人
    private String senderPhone;//寄件方联系电话
    private String sendAddress;//	寄件方详细地址
    private String get;//到件方联系人
    private String getPhone;//到件方联系电话
    private String getAddress;//	到件方详细地址
    private String sendStarTime;
    private String logisticsNumber;//1：人工确认  2：可收派  3：不可以收派
    private String message;
    private String passportId;//护照id
    private int sendBackNum;//寄回本数
    private String sfExpress;//顺丰单号

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

    public String getGet() {
        return get;
    }

    public void setGet(String get) {
        this.get = get;
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

    public String getPassportId() {
        return passportId;
    }

    public void setPassportId(String passportId) {
        this.passportId = passportId;
    }

    public int getSendBackNum() {
        return sendBackNum;
    }

    public void setSendBackNum(int sendBackNum) {
        this.sendBackNum = sendBackNum;
    }

    public String getSfExpress() {
        return sfExpress;
    }

    public void setSfExpress(String sfExpress) {
        this.sfExpress = sfExpress;
    }
}
