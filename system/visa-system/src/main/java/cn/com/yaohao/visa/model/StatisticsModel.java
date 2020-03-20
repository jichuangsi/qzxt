package cn.com.yaohao.visa.model;

public class StatisticsModel {
    private String id;//订单id
    private String orderNumber;//订单号
    private String name;//姓名
    private long signTime;//签收时间
    private String schedule;//工期
    private Integer passportNum;//护照本数
    private Integer egisNum;//审核通过数
    private Integer failNum;//审核不通过
    private Integer sendNum;//已送签数
    private Integer refuseNum;//拒签数
    private Integer outNum;//出签数
    private Integer sendBackNum;//寄回数
    private Integer exprotNum;//导出数

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSignTime() {
        return signTime;
    }

    public void setSignTime(long signTime) {
        this.signTime = signTime;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public Integer getPassportNum() {
        return passportNum;
    }

    public void setPassportNum(Integer passportNum) {
        this.passportNum = passportNum;
    }

    public Integer getEgisNum() {
        return egisNum;
    }

    public void setEgisNum(Integer egisNum) {
        this.egisNum = egisNum;
    }

    public Integer getFailNum() {
        return failNum;
    }

    public void setFailNum(Integer failNum) {
        this.failNum = failNum;
    }

    public Integer getSendNum() {
        return sendNum;
    }

    public void setSendNum(Integer sendNum) {
        this.sendNum = sendNum;
    }

    public Integer getRefuseNum() {
        return refuseNum;
    }

    public void setRefuseNum(Integer refuseNum) {
        this.refuseNum = refuseNum;
    }

    public Integer getOutNum() {
        return outNum;
    }

    public void setOutNum(Integer outNum) {
        this.outNum = outNum;
    }

    public Integer getSendBackNum() {
        return sendBackNum;
    }

    public void setSendBackNum(Integer sendBackNum) {
        this.sendBackNum = sendBackNum;
    }

    public Integer getExprotNum() {
        return exprotNum;
    }

    public void setExprotNum(Integer exprotNum) {
        this.exprotNum = exprotNum;
    }
}
