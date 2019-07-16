package com.example.myapplication;

public class User {
    private String nick, mail, pwd;

    public User(String nick, String mail, String pwd) {
        this.nick = nick;
        this.mail = mail;
        this.pwd = pwd;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}
