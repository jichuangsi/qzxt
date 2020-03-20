package cn.com.yaohao.visa.model;

public class ValidationModel {
    //信息验证
    private String id;//护照信息id
    private String name;//姓名
    private String orderNumber;//订单号
    private String expressReceiptId;//快件id
    private String passportEncoding;//护照编码
    private String tradeName;//商品名称
    private String birthDay;//出生日期
    private String telephoneNumber;//联系电话
    private String returnAddress;//寄回地址
    private String schedule;//工期
    private long expiryDate;//有效期
    private String problem;
    private String status;//状态 待处理，已处理，异常
    private String sendStatus;//送签状态S-已送
    private String isSendBack;//是否寄回
    private int DIFF_DATE;//剩余工期
    private long checkTime;//审核时间
    private String expireTime;//工期到期时间
    private int pageNum;
    private int pageSize;


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

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getPassportEncoding() {
        return passportEncoding;
    }

    public void setPassportEncoding(String passportEncoding) {
        this.passportEncoding = passportEncoding;
    }

    public String getTradeName() {
        return tradeName;
    }

    public void setTradeName(String tradeName) {
        this.tradeName = tradeName;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
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

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public long getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(long expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getExpressReceiptId() {
        return expressReceiptId;
    }

    public void setExpressReceiptId(String expressReceiptId) {
        this.expressReceiptId = expressReceiptId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
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

    public String getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(String sendStatus) {
        this.sendStatus = sendStatus;
    }

    public int getDIFF_DATE() {
        return DIFF_DATE;
    }

    public void setDIFF_DATE(int DIFF_DATE) {
        this.DIFF_DATE = DIFF_DATE;
    }

    public String getIsSendBack() {
        return isSendBack;
    }

    public void setIsSendBack(String isSendBack) {
        this.isSendBack = isSendBack;
    }
}
