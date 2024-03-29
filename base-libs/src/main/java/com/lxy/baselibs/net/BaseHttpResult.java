package com.lxy.baselibs.net;

import java.io.Serializable;

/**
 * @author xuhao
 * @date 2018/6/12 00:58
 * @desc 抽取的一个基类的bean, 直接在泛型中传data就行
 */
public class BaseHttpResult<T> implements Serializable {
    private static final long serialVersionUID = 2690553609250007325L;
    public static final int SUCCESS_CODE = 200;
    public static final int OUT_DATA_CODE = 10000;

    private int code;
    private String msg;
    private T data;

    public int getStatus() {
        return code;
    }

    public void setStatus(int status) {
        this.code = status;
    }

    public String getMessage() {
        return msg;
    }

    public void setMessage(String message) {
        this.msg = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }


    /**
     * 正常返回
     *
     * @return
     */
    public boolean isSuccessFul() {
        return getStatus() == SUCCESS_CODE;
    }

    /**
     * 登陆过期
     *
     * @return
     */
    public boolean isOutData() {
        return getStatus() == OUT_DATA_CODE;
    }

//    /** test**/
//    private boolean error;
//
//    private T results;
//
//    public boolean isError() {
//        return error;
//    }
//
//    public void setError(boolean error) {
//        this.error = error;
//    }
//
//    public T getData() {
//        return results;
//    }
//
//    public void setData(T data) {
//        this.results = data;
//    }
//
//    @Override
//    public String toString() {
//        return "BaseHttpResult{" +
//                "error=" + error +
//                ", results=" + results +
//                '}';
//    }
//
//    /**
//     * 正常返回
//     *
//     * @return
//     */
//    public boolean isSuccessFul() {
//        return !isError();
//    }
}
