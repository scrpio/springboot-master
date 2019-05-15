package com.springboot.common.enums;

public enum TransCode {
    INSERT_FAIL(400, "插入数据失败"),
    UPDATE_FAIL(400, "更新数据失败"),
    DELETE_FAIL(400, "删除数据失败");

    private int code;
    private String msg;

    TransCode(String msg) {
        this.msg = msg;
    }

    TransCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return this.code;
    }

    public String getMsg() {
        return msg;
    }
}
