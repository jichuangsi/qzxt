package cn.com.yaohao.visa.model;

import cn.com.yaohao.visa.entity.PassportInformation;

import java.util.List;

public class BatchPassPortModel {
    private String orderId;//签证id
    private List<PassportInformation> informations;//护照信息

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public List<PassportInformation> getInformations() {
        return informations;
    }

    public void setInformations(List<PassportInformation> informations) {
        this.informations = informations;
    }
}
