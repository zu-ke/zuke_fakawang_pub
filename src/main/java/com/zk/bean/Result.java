package com.zk.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*解读：1.用于返回结果，利于json*/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {

    private String code = "";
    private String msg = "";
    private T data;

    /*解读：自定义构造器*/

    /*解读：static，响应成功信息，如果没有指定T，默认是Object*/
    public static Result success() {
        Result<Object> result = new Result<>();
        result.setCode("200");
        result.setMsg("success");
        return result;
    }

    /*解读：响应带data的成功信息，如果没有指定T，默认是Object，如果需要在static方法中要使用泛型，需要加上<T>*/
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode("200");
        result.setMsg("success");
        result.setData(data);
        return result;
    }

    /*解读：static，响应失败信息，如果没有指定T，默认是Object*/
    public static Result error(String code, String msg) {
        Result<Object> result = new Result<>();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }

    /*解读：响应带data的失败信息，如果没有指定T，默认是Object，如果需要在static方法中要使用泛型，需要加上<T>*/
    public static <T> Result<T> error(String code, String msg, T data) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }
}
