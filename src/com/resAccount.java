package com;

public class resAccount {
    private int id;
    private int stuid;
    private String name;
    private String cla;
    private String subName;
    private int num;
    private String term;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStuId() {
        return stuid;
    }

    public void setStuId(int stuid) {
        this.stuid = stuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCla() {
        return cla;
    }

    public void setCla(String cla) {
        this.cla = cla;
    }

    public String getSubName() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }
//Alt+t，选择toString
    @Override
    public String toString() {
        return "Account{" +
                "res_id=" + id +
                ", stu_name='" + name + '\'' +
                ", stu_cla='" + cla + '\'' +
                ", res_=subName'" + subName + '\'' +
                ", res_num='" + num + '\'' +
                ", res_term='" + term + '\'' +
                '}';

    }

}
