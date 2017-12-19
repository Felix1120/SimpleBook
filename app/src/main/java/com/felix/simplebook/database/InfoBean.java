package com.felix.simplebook.database;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by chaofei.xue on 2017/11/24.
 */

public class InfoBean extends DataSupport implements Serializable{
    private long id;
    private String time;
    private String year;
    private String month;
    private String day;
    private String type;
    private String money;
    private String status;
    private String inOrOut;

    public InfoBean() {
    }

    public InfoBean(String year, String month, String day, String time, String type, String money, String status, String inOrOut) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.time = time;
        this.type = type;
        this.money = money;
        this.status = status;
        this.inOrOut = inOrOut;
    }

    public InfoBean(long id, String year, String month, String day, String time, String type, String money, String status, String inOrOut) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.time = time;
        this.type = type;
        this.money = money;
        this.status = status;
        this.inOrOut = inOrOut;
        this.id = id;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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

    public String getInOrOut() {
        return inOrOut;
    }

    public void setInOrOut(String inOrOut) {
        this.inOrOut = inOrOut;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
