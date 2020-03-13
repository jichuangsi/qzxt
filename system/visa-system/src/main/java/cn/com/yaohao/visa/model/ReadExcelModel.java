package cn.com.yaohao.visa.model;

import cn.com.yaohao.visa.entity.PassportInformation;

import java.util.List;

public class ReadExcelModel {
    private List<PassportInformation> informationList;
    private String errorRow;

    public List<PassportInformation> getInformationList() {
        return informationList;
    }

    public void setInformationList(List<PassportInformation> informationList) {
        this.informationList = informationList;
    }

    public String getErrorRow() {
        return errorRow;
    }

    public void setErrorRow(String errorRow) {
        this.errorRow = errorRow;
    }
}
