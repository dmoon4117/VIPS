package com.example.beafk.firebasetest.login;

public class userListViewItem {

    private String userid ;
    private String username ;

    public void setid (String id ) {
        userid = id ;
    }
    public void setname(String name) {
        username = name ;
    }

    public String getid() {
        return this.userid ;
    }
    public String getname() {
        return this.username ;
    }
}