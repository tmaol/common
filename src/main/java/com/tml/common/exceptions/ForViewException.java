package com.tml.common.exceptions;

import com.tml.common.entities.ResultCode;

public class ForViewException extends RuntimeException {
    private static final long serialVersionUID = 83210409188688329L;
    private int code;
    private String msg;

    private static ForViewException e = new ForViewException();

    public int getCode() {
        return code;
    }
    private void setCode(int code) {
        this.code = code;
    }
    public String getMsg() {
        return msg;
    }
    private void setMsg(String msg) {
        this.msg = msg;
    }

    private ForViewException() {}


    public static ForViewException getInstance(int code,String msg) {
        e.setCode(code);
        e.setMsg(msg);
        return e;
    }


    public static ForViewException getInstance(ResultCode resultCode) {
        e.setCode(resultCode.getCode());
        e.setMsg(resultCode.getMsg());
        return e;
    }
    
}
