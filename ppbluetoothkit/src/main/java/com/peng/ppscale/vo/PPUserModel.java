package com.peng.ppscale.vo;

import java.io.Serializable;
import java.sql.Array;
import java.util.Arrays;

public class PPUserModel implements Serializable {
    public int userHeight;
    public int age;
    public PPUserGender sex;
    public int groupNum;
    public boolean isAthleteMode;//运动员模式
    public boolean isPregnantMode;//孕妇模式
    public String userID;
    public String memberID;
    public String userName = "";
    public int deviceHeaderIndex;
    public double weightKg;
    public double targetWeight;
    public double ideaWeight;
    public double[] userWeightArray;
    public long[] userWeightTimeArray;

    private PPUserModel(Builder builder) {
        this.userHeight = builder.userHeight;
        this.age = builder.age;
        this.sex = builder.sex;
        this.groupNum = builder.groupNum;
        this.isAthleteMode = builder.isAthleteMode;
        this.isPregnantMode = builder.isPregnantMode;
        this.userID = builder.userID;
        this.memberID = builder.memberID;
        this.userName = builder.userName;
        this.deviceHeaderIndex = builder.deviceHeaderIndex;
        this.weightKg = builder.weightKg;
        this.targetWeight = builder.targetWeight;
        this.ideaWeight = builder.ideaWeight;
        this.userWeightArray = builder.userWeightArray;
        this.userWeightTimeArray = builder.userWeightTimeArray;

    }

    public static class Builder {
        private int userHeight = 100;
        private int age = -1;
        private PPUserGender sex = PPUserGender.PPUserGenderFemale;
        private int groupNum = 0;
        private boolean isAthleteMode = false;//运动员模式
        private boolean isPregnantMode = false;//孕妇模式
        private String userID = "";
        private String memberID = "";
        private String userName = "";
        private int deviceHeaderIndex = 0;
        public double weightKg;
        public double targetWeight;
        public double ideaWeight;
        public double[] userWeightArray;
        public long[] userWeightTimeArray;

        public Builder setDeviceHeaderIndex(int deviceHeaderIndex) {
            this.deviceHeaderIndex = deviceHeaderIndex;
            return this;
        }

        public Builder setUserName(String userName) {
            this.userName = userName;
            return this;
        }

        public Builder setUserID(String userID) {
            this.userID = userID;
            return this;
        }

        public Builder setMemberId(String memberID) {
            this.memberID = memberID;
            return this;
        }

        public Builder setAge(int age) {
            this.age = age;
            return this;
        }

        public Builder setHeight(int userHeight) {
            this.userHeight = userHeight;
            return this;
        }

        public Builder setSex(PPUserGender sex) {
            this.sex = sex;
            return this;
        }

        public Builder setGroupNum(int groupNum) {
            this.groupNum = groupNum;
            return this;
        }

        public boolean isAthleteMode() {
            return isAthleteMode;
        }

        public Builder setAthleteMode(boolean athleteMode) {
            isAthleteMode = athleteMode;
            return this;
        }

        public boolean isPregnantMode() {
            return isPregnantMode;
        }

        /**
         * 孕妇模式 默认false 非孕妇模式
         *
         * @param pregnantMode
         * @return
         */
        public Builder setPregnantMode(boolean pregnantMode) {
            isPregnantMode = pregnantMode;
            return this;
        }

        public Builder setWeightKg(double weightKg) {
            this.weightKg = weightKg;
            return this;
        }

        public Builder setTargetWeight(double targetWeight) {
            this.targetWeight = targetWeight;
            return this;
        }

        public Builder setIdeaWeight(double ideaWeight) {
            this.ideaWeight = ideaWeight;
            return this;
        }

        public Builder setUserWeightTimeArray(long[] userWeightTimeArray) {
            this.userWeightTimeArray = userWeightTimeArray;
            return this;
        }

        public Builder setUserWeightArray(double[] userWeightArray) {
            this.userWeightArray = userWeightArray;
            return this;
        }

        public PPUserModel build() {
            return new PPUserModel(this);
        }

    }

    @Override
    public String toString() {
        return "PPUserModel{" + "\n" +
                "userHeight=" + userHeight + "\n" +
                "age=" + age + "\n" +
                "sex=" + sex + "\n" +
                "groupNum=" + groupNum + "\n" +
                "isAthleteMode=" + isAthleteMode + "\n" +
                "isPregnantMode=" + isPregnantMode + "\n" +
                "userID=" + userID + "\n" +
                "memberID=" + memberID + "\n" +
                "userName=" + userName + "\n" +
                "deviceHeaderIndex=" + deviceHeaderIndex + "\n" +
                "weightKg=" + weightKg + "\n" +
                "targetWeight=" + targetWeight + "\n" +
                "ideaWeight=" + ideaWeight + "\n" +
                "userWeightArray=" + Arrays.toString(userWeightArray) + "\n" +
                "userWeightTimeArray=" + Arrays.toString(userWeightTimeArray) +
                '}';
    }
}
