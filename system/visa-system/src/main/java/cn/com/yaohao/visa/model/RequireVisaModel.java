package cn.com.yaohao.visa.model;

public class RequireVisaModel {
    //条件查询签证
    private String orderNumber;//订单号
    private String courierNumber;//快递单号
    private String expressCompany;//快递公司
    private String signatory;//签收人
    private String telephoneNumber;//联系电话
    private String SigningTime;//签收时间
    private  int status;//状态 待处理，已处理，异常
    private int pageNum;
    private int pageSize;



    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getCourierNumber() {
        return courierNumber;
    }

    public void setCourierNumber(String courierNumber) {
        this.courierNumber = courierNumber;
    }

    public String getExpressCompany() {
        return expressCompany;
    }

    public void setExpressCompany(String expressCompany) {
        this.expressCompany = expressCompany;
    }

    public String getSignatory() {
        return signatory;
    }

    public void setSignatory(String signatory) {
        this.signatory = signatory;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public String getSigningTime() {
        return SigningTime;
    }

    public void setSigningTime(String signingTime) {
        SigningTime = signingTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
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
}
