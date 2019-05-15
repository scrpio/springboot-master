package com.springboot.common.exception;

import com.springboot.common.enums.TransCode;

public class TransException extends RuntimeException {
    private int code;
    private String msg;
    public TransException(TransCode transCode){
        super(transCode.getMsg());
        this.code = transCode.getCode();
    }

    public String getMsg() {
        return msg;
    }
}
