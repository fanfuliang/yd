package com.pay.yudaopay.util;


public class Result<T> {
    public static final Integer OK = 200;
    public static final Integer ERROR = 500;
    public static final String SUCCESS = "成功";
    public static final String SYSTEMERROR = "未知错误";
    // 错误码
    private Integer status;
    // 消息
    private String message;
    // 返回的结果
    private T data = null;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    private String code;
    private String dataType;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    @Override
    public String toString() {
        return "Result{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    public void setData(T data) {

        this.data = data;
    }
}
