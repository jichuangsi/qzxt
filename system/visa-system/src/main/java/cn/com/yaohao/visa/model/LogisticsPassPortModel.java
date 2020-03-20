package cn.com.yaohao.visa.model;

import cn.com.yaohao.visa.entity.ExpressReceipt;
import cn.com.yaohao.visa.entity.Logistics;
import cn.com.yaohao.visa.entity.PassportInformation;

public class LogisticsPassPortModel {
    private Logistics logistics;//寄回
    private ExpressReceipt expressReceipt;//快件
    private PassportInformation passportInformation;//护照

    public Logistics getLogistics() {
        return logistics;
    }

    public void setLogistics(Logistics logistics) {
        this.logistics = logistics;
    }

    public ExpressReceipt getExpressReceipt() {
        return expressReceipt;
    }

    public void setExpressReceipt(ExpressReceipt expressReceipt) {
        this.expressReceipt = expressReceipt;
    }

    public PassportInformation getPassportInformation() {
        return passportInformation;
    }

    public void setPassportInformation(PassportInformation passportInformation) {
        this.passportInformation = passportInformation;
    }
}
