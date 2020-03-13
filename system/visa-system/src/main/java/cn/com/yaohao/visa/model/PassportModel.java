package cn.com.yaohao.visa.model;

import cn.com.yaohao.visa.entity.ExpressReceipt;
import cn.com.yaohao.visa.entity.PassportEssential;
import cn.com.yaohao.visa.entity.PassportInformation;

public class PassportModel {
    private String visaId;//签证id
    private String passportId;//护照id
    //护照录入
    private PassportEssential essential;//基本信息
    private ExpressReceipt expressReceipt;//快递信息
    private PassportInformation information;//护照信息

    public String getVisaId() {
        return visaId;
    }

    public void setVisaId(String visaId) {
        this.visaId = visaId;
    }

    public String getPassportId() {
        return passportId;
    }

    public void setPassportId(String passportId) {
        this.passportId = passportId;
    }

    public PassportEssential getEssential() {
        return essential;
    }

    public void setEssential(PassportEssential essential) {
        this.essential = essential;
    }

    public PassportInformation getInformation() {
        return information;
    }

    public void setInformation(PassportInformation information) {
        this.information = information;
    }

    public ExpressReceipt getExpressReceipt() {
        return expressReceipt;
    }

    public void setExpressReceipt(ExpressReceipt expressReceipt) {
        this.expressReceipt = expressReceipt;
    }
}
