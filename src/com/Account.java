package com;

public class Account {
    private int id;
    private String name;
    private String cla;
    private String sex;
    private int tele;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getTele() {
        return tele;
    }

    public void setTele(int tele) {
        this.tele = tele;
    }
//Alt+t，选择toString
    @Override
    public String toString() {
        return "Account{" +
                "stu_id=" + id +
                ", stu_name='" + name + '\'' +
                ", stu_cla='" + cla + '\'' +
                ", stu_sex='" + sex + '\'' +
                ", stu_tele='" + tele + '\'' +
                '}';

    }

}
