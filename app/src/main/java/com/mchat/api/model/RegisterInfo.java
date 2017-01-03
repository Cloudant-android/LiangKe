package com.mchat.api.model;

import android.widget.ListView;

import java.util.List;

/**
 * Created by CloudAnt on 2016/12/6.
 * 注册javabean
 */

public class RegisterInfo {
    /**
     * code : 1000
     * msg : 注册成功
     * time : 1477303957
     * data : {"uid":"2","token":"314e39720c037d645a6acb98de99a5bed41b284d"}
     */

    private int code;
    private String msg;
    private int time;
    private DataBean data;

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

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * uid : 2
         * token : 314e39720c037d645a6acb98de99a5bed41b284d
         */

        private int uid;
        private String token;

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
