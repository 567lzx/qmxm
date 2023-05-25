package com;

public class teaAccount {
    private int id;
    private String name;
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
                "tea_id=" + id +
                ", tea_name='" + name + '\'' +
                ", tea_sex='" + sex + '\'' +
                ", tea_tele='" + tele + '\'' +
                '}';

    }

}
