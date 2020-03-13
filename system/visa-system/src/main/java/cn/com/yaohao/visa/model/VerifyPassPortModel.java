package cn.com.yaohao.visa.model;

public class VerifyPassPortModel {
    private String status;//审核状态
    private String pid;//护照id
    private String problem;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }
}
