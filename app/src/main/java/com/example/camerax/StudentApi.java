package com.example.camerax;

import android.app.Application;

public class StudentApi extends Application {
    private String RollNo;
    private String Name;
    private boolean hadExit = false;
    private static StudentApi instance;

    public static synchronized StudentApi getInstance() {
        if (instance == null) {
            instance = new StudentApi();
        }
        return instance;
    }

    public StudentApi() {
    }

    public StudentApi(String rollNo, boolean hadExit, String name) {
        RollNo = rollNo;
        this.hadExit = hadExit;
        Name = name;
    }

    public String getRollNo() {
        return RollNo;
    }

    public void setRollNo(String rollNo) {
        RollNo = rollNo;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public boolean isHadExit() {
        return hadExit;
    }

    public void setHadExit(boolean hadExit) {
        this.hadExit = hadExit;
    }
}
