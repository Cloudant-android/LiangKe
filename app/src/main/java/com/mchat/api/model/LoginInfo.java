package com.mchat.api.model;

/**
 * Created by CloudAnt on 2016/12/6.
 * 登录javabean
 */

public class LoginInfo {

    /**
     * code : 1000
     * msg : 登录成功
     * time : 1478073864
     * data : {"uid":"29","username":"admin1234","nickname":"","token":"14780738640V4GPwd9BaRYB6wIlr4Q","logo":"","gender":"男","profession":"","reg_time":"1477987321"}
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
         * uid : 29
         * username : admin1234
         * nickname :
         * token : 14780738640V4GPwd9BaRYB6wIlr4Q
         * logo :
         * gender : 男
         * profession :
         * reg_time : 1477987321
         */

        private String uid;
        private String username;
        private String nickname;
        private String token;
        private String logo;
        private String gender;
        private String profession;
        private String reg_time;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getProfession() {
            return profession;
        }

        public void setProfession(String profession) {
            this.profession = profession;
        }

        public String getReg_time() {
            return reg_time;
        }

        public void setReg_time(String reg_time) {
            this.reg_time = reg_time;
        }
    }
}
