package cn.com.yaohao.visa.entity;

import javax.persistence.*;

@Entity
@Table(name = "passport_img")
public class PassPortImg {
    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    private Integer id;
    private String picPath;//图片地址
    private String pid;//护照id
    private String passPortCode;//护照编码
    private String orderNumber;//订单号
    private String fileName;//文件名

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPassPortCode() {
        return passPortCode;
    }

    public void setPassPortCode(String passPortCode) {
        this.passPortCode = passPortCode;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
