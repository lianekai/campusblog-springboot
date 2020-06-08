package com.lianyikai.campusblog.utils;

/*
 * 结果集
 * */
public class ResultApi {
    private static int SUCCESS_CODE = 1;
    private static int FAIL_CODE = 0;

    private int code;
    private String message;
    private Object data;

    private ResultApi(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static ResultApi success() {
        return new ResultApi(SUCCESS_CODE,null,null);
    }
    public static ResultApi success(Object data) {
        return new ResultApi(SUCCESS_CODE,"SUCCESS", data);
    }
    public static ResultApi fail(String message) {
        return new ResultApi(FAIL_CODE,message,null);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
