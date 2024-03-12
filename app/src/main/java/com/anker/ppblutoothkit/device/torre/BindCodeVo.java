package com.anker.ppblutoothkit.device.torre;

import java.io.Serializable;

public class BindCodeVo implements Serializable {

    String code;
    String message;
    int res_code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getRes_code() {
        return res_code;
    }

    public void setRes_code(int res_code) {
        this.res_code = res_code;
    }
}
