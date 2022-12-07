package com.ly.bean;

public class Student {

    private int sid;
    private String sname;
    private String sgender;

    public Student() {
    }

    public Student(int sid, String sname, String sgender) {
        this.sid = sid;
        this.sname = sname;
        this.sgender = sgender;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getSgender() {
        return sgender;
    }

    public void setSgender(String sgender) {
        this.sgender = sgender;
    }

    @Override
    public String toString() {
        return "Student{" +
                "sid=" + sid +
                ", sname='" + sname + '\'' +
                ", sgender='" + sgender + '\'' +
                '}';
    }

}
