package com.lzyc.ybtappcal.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lxx on 2017/5/25.
 */

public class MedicineFamilyBean implements Serializable {
    /**
     * list : [{"ID":"4","UID":"1576","Nickname":"自己","Age":"35","Sex":"1","Allergy":"0","Note":"","m_AddTime":"2017-06-01 13:22:13","Count":0}]
     * count : 1
     */

    private int count;
    private List<ListBean> list;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean implements Serializable {
        /**
         * ID : 4
         * UID : 1576
         * Nickname : 自己
         * Age : 35
         * Sex : 1
         * Allergy : 0
         * Note :
         * m_AddTime : 2017-06-01 13:22:13
         * Count : 0
         */

        private String ID;
        private String UID;
        private String Nickname;
        private String Age;
        private String Sex;
        private String Allergy;
        private String Note;
        private String m_AddTime;
        private int Count;

        private String gender;

        public String getID() {
            return ID;
        }

        public void setID(String ID) {
            this.ID = ID;
        }

        public String getUID() {
            return UID;
        }

        public void setUID(String UID) {
            this.UID = UID;
        }

        public String getNickname() {
            return Nickname;
        }

        public void setNickname(String Nickname) {
            this.Nickname = Nickname;
        }

        public String getAge() {
            return Age;
        }

        public void setAge(String Age) {
            this.Age = Age;
        }

        public String getSex() {
            return Sex;
        }

        public void setSex(String Sex) {
            this.Sex = Sex;
        }

        public String getAllergy() {
            return Allergy;
        }

        public void setAllergy(String Allergy) {
            this.Allergy = Allergy;
        }

        public String getNote() {
            return Note;
        }

        public void setNote(String Note) {
            this.Note = Note;
        }

        public String getM_AddTime() {
            return m_AddTime;
        }

        public void setM_AddTime(String m_AddTime) {
            this.m_AddTime = m_AddTime;
        }

        public int getCount() {
            return Count;
        }

        public void setCount(int Count) {
            this.Count = Count;
        }

        public String getGender(){
            if("1".equals(Sex)){
                gender = "男";
            } else {
                gender = "女";
            }
            return gender;
        }

    }

}
