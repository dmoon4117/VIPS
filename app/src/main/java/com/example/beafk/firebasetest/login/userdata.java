package com.example.beafk.firebasetest.login;

/**
 * Created by beafk on 2017-03-31.
 */

public class userdata {

    private String _id;
    private String _name;

    public userdata(){
    }
    public userdata(String id, String name){
        this._id=id;
        this._name = name;
    }

    public void setID(String id){        this._id = id;    }
    public String getID(){
        return _id;
    }
    public void setName(String Name){
        this._name = Name;
    }
    public String getName(){
        return _name;
    }

}


