package com.mchat.api.model;

/**
 * Created by CloudAnt on 2016/12/6.
 * 修改用户信息javabean
 */

public class SetUserInfo {

    /**
     * code : 1000
     * msg : 修改用户信息成功
     * time : 1481169316
     * data : {"uid":1,"username":"zhoudengjun","nickname":"","logo":"/upload/20161208\\27854a008751ca1787e1ecb4c755cce3.jpg","gender":"男","profession":"软件工程师","reg_time":1480940634}
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
         * uid : 1
         * username : zhoudengjun
         * nickname :
         * logo : /upload/20161208\27854a008751ca1787e1ecb4c755cce3.jpg
         * gender : 男
         * profession : 软件工程师
         * reg_time : 1480940634
         */

        private String uid;
        private String username;
        private String nickname;
        private String logo;
        private String gender;
        private String profession;
        private int reg_time;

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

        public int getReg_time() {
            return reg_time;
        }

        public void setReg_time(int reg_time) {
            this.reg_time = reg_time;
        }
    }
}
