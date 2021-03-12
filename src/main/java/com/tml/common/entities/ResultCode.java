package com.tml.common.entities;

public enum ResultCode {
    OK(0, "请求成功"),PARAM_EOOR(10001,"参数错误"),THIRD_REQ_FAIL(10002,"第三方请求失败")
    

    ;
    private int code;
    private String msg;
    private ResultCode(int code,String msg) {
        this.code = code;
        this.msg = msg;
    }
    public int getCode() {
        return code;
    }
    public void setCode(int code) {
        this.code = code;
    }
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
    
}
