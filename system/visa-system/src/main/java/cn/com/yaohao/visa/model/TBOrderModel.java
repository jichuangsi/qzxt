package cn.com.yaohao.visa.model;

public class TBOrderModel {
    private String orderId;
    private String phone;
    private String schedule;//工期
    private String returnAddress;//寄回地址
    private String address;//快件地址
    private int count;//护照数
    private String courierNumber;//快递单号

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getReturnAddress() {
        return returnAddress;
    }

    public void setReturnAddress(String returnAddress) {
        this.returnAddress = returnAddress;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getCourierNumber() {
        return courierNumber;
    }

    public void setCourierNumber(String courierNumber) {
        this.courierNumber = courierNumber;
    }

    public TBOrderModel() {
    }

    public TBOrderModel(String orderId, String phone, String schedule, String returnAddress, String address, int count, String courierNumber) {
        this.orderId = orderId;
        this.phone = phone;
        this.schedule = schedule;
        this.returnAddress = returnAddress;
        this.address = address;
        this.count = count;
        this.courierNumber = courierNumber;
    }
}
