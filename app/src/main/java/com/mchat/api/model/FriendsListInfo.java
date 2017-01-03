package com.mchat.api.model;

import java.util.List;

/**
 * Created by CloudAnt on 2016/12/20.
 * 好友实体类
 */

public class FriendsListInfo {

    /**
     * code : 1000
     * msg : 请求成功
     * time : 1481190600
     * data : {"total_page":1,"total_rows":3,"list":[{"friend_id":"2","notes":"","username":"bsjjdj","nickname":"","logo":"/upload/","gender":"男","profession":""},{"friend_id":"3","notes":"","username":"111","nickname":"","logo":"/upload/","gender":"男","profession":""},{"friend_id":"4","notes":"","username":"44","nickname":"","logo":"/upload/","gender":"男","profession":""}]}
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
         * total_page : 1
         * total_rows : 3
         * list : [{"friend_id":"2","notes":"","username":"bsjjdj","nickname":"","logo":"/upload/","gender":"男","profession":""},{"friend_id":"3","notes":"","username":"111","nickname":"","logo":"/upload/","gender":"男","profession":""},{"friend_id":"4","notes":"","username":"44","nickname":"","logo":"/upload/","gender":"男","profession":""}]
         */

        private int total_page;
        private int total_rows;
        private List<ListBean> list;

        public int getTotal_page() {
            return total_page;
        }

        public void setTotal_page(int total_page) {
            this.total_page = total_page;
        }

        public int getTotal_rows() {
            return total_rows;
        }

        public void setTotal_rows(int total_rows) {
            this.total_rows = total_rows;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * friend_id : 2
             * notes :
             * username : bsjjdj
             * nickname :
             * logo : /upload/
             * gender : 男
             * profession :
             */

            private String friend_id;
            private String notes;
            private String username;
            private String nickname;
            private String logo;
            private String gender;
            private String profession;

            public String getFriend_id() {
                return friend_id;
            }

            public void setFriend_id(String friend_id) {
                this.friend_id = friend_id;
            }

            public String getNotes() {
                return notes;
            }

            public void setNotes(String notes) {
                this.notes = notes;
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
        }
    }
}
