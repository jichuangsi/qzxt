package cn.com.yaohao.visa.model.backuser;

public class UpdatePwdModel {

    private String id;
    private String firstPwd;
    private String secondPwd;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstPwd() {
        return firstPwd;
    }

    public void setFirstPwd(String firstPwd) {
        this.firstPwd = firstPwd;
    }

    public String getSecondPwd() {
        return secondPwd;
    }

    public void setSecondPwd(String secondPwd) {
        this.secondPwd = secondPwd;
    }
}
