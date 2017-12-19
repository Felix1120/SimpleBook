package com.felix.simplebook.database;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by chaofei.xue on 2017/11/29.
 */

public class TypeBean extends DataSupport implements Serializable {
    private String type;
    private long id;

    public TypeBean() {
    }

    public TypeBean(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getId() {
        return id;
    }
}
