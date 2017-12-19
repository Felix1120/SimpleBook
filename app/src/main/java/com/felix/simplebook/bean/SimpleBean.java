package com.felix.simplebook.bean;

/**
 * Created by chaofei.xue on 2017/11/30.
 */

public class SimpleBean {
    private String inMoney;
    private String outMoney;
    private String type;
    private String money;
    private String status;
    private String proportion;
    private String in_or_out;

    public SimpleBean() {
    }

    public SimpleBean(String inMoney, String outMoney, String type, String money, String status, String proportion, String in_or_out) {
        this.in_or_out = in_or_out;
        this.inMoney = inMoney;
        this.outMoney = outMoney;
        this.type = type;
        this.money = money;
        this.status = status;
        this.proportion = proportion;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProportion() {
        return proportion;
    }

    public void setProportion(String proportion) {
        this.proportion = proportion;
    }

    public String getInMoney() {
        return inMoney;
    }

    public void setInMoney(String inMoney) {
        this.inMoney = inMoney;
    }

    public String getOutMoney() {
        return outMoney;
    }

    public void setOutMoney(String outMoney) {
        this.outMoney = outMoney;
    }

    public String getIn_or_out() {
        return in_or_out;
    }

    public void setIn_or_out(String in_or_out) {
        this.in_or_out = in_or_out;
    }
}
