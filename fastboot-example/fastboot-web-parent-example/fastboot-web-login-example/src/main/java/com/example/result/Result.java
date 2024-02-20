package com.example.result;

import java.util.List;

/**
 * 描述: json格式数据返回对象，使用CustomResultSerializer 来序列化
 *
 * @author : chenye
 */
public class Result<T> {

    private String code;
    private String msg;
    private T data;
    private Long count;
    private List<T> list;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "code=" + code + " message=" + msg + " data=" + data;
    }

    public static <T> Result<T> fail() {
        Result<T> ret = new Result<>();
        ret.setCode(ReturnCode.FAIL.getCode());
        ret.setMsg(ReturnCode.FAIL.getDesc());
        return ret;
    }

    public static <T> Result<T> fail(T data) {
        Result<T> ret = Result.fail();
        ret.setData(data);
        return ret;
    }

    public static <T> Result<T> failMessage(String msg) {
        Result<T> ret = Result.fail();
        ret.setMsg(msg);
        return ret;
    }

    public static <T> Result<T> successMessage(String msg) {
        Result<T> ret = Result.success();
        ret.setMsg(msg);
        return ret;
    }

    public static <T> Result<T> success() {
        Result<T> ret = new Result<>();
        ret.setCode(ReturnCode.SUCCESS.getCode());
        ret.setMsg(ReturnCode.SUCCESS.getDesc());
        return ret;
    }

    public static <T> Result<T> success(T data) {
        Result<T> ret = Result.success();
        ret.setData(data);
        return ret;
    }


    public static <T> Result<T> bool(Boolean b) {
        if (b) {
            return Result.success();
        } else {
            return Result.fail();
        }
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
