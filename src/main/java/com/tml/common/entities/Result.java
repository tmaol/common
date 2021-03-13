package com.tml.common.entities;

public class Result {
    private int code;
    private String msg;
    private Object data;

    private static Result RESULT = new Result();
    
    private Result () {}

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public Object getData() {
        return data;
    }

    public static Result getInstance(int code,String msg) {
        return getInstance(code,msg,null);
    }

    public static Result getInstance(int code,String msg,Object data) {
        RESULT.code = code;
        RESULT.msg = msg;
        RESULT.data = data;
        return RESULT;
    }

    public static Result getInstance(ResultCode resultCode) {
        return getInstance(resultCode.getCode(),resultCode.getMsg());
    }
    
    public static Result getInstance(ResultCode resultCode,Object data) {
        return getInstance(resultCode.getCode(),resultCode.getMsg(),data);
    }

}
