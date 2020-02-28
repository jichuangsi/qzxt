package cn.com.yaohao.visa.model;

import java.util.List;

public class PassportCheckModel {
    private String passprtId ;
    private String status;//A通过P不通过
    private List<String> sId;//补充条件

    public String getPassprtId() {
        return passprtId;
    }

    public void setPassprtId(String passprtId) {
        this.passprtId = passprtId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getsId() {
        return sId;
    }

    public void setsId(List<String> sId) {
        this.sId = sId;
    }
}
