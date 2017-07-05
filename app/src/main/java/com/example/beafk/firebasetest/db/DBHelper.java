package com.example.beafk.firebasetest.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by beafk on 2017-03-31.
 */

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    // DB를 새로 생성할 때 호출되는 함수
    @Override
    public void onCreate(SQLiteDatabase db) {
        //무쓸모

        //db.execSQL("create table if not exists user(id int, name char(15));");
        //db.execSQL("INSERT INTO user VALUES(0,'aaa');");
    }

    // DB 업그레이드를 위해 버전이 변경될 때 호출되는 함수
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public Cursor synch(String id) {
        // 읽고 쓰기가 가능하게 DB 열기
        int temp = Integer.parseInt(id);
        SQLiteDatabase db = getWritableDatabase();

        Cursor cursor2 = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name ='saving_drivedata'" , null);
        cursor2.moveToFirst();
        Cursor cursor = null;
        cursor = db.rawQuery("SELECT * FROM user where id='" + temp + "';", null);

        cursor2.close();
        return cursor;
    }

    public void login_insert(String id, String name,String apk1, String apk2, int rayout, String color, int bk) {
        // 읽고 쓰기가 가능하게 DB 열기
        int temp = Integer.parseInt(id);
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor1 = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name ='user'" , null);
        cursor1.moveToFirst();

        Cursor cursor2 = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name ='drivedata'" , null);
        cursor2.moveToFirst();

        if(cursor1.getCount()==0){
            db.execSQL("create table user(id int, name char(15), apk1 char(50), apk2 char(50), rayout int, color char(15), bk int);");
            db.execSQL("INSERT INTO user VALUES(0,'aaa','null','null',1,'#FFFFFFFF',0);");

            db.execSQL("create table drivedata(id int, week_fe float, week_av float, week_runtime int, week_rundistance float);");
            db.execSQL("INSERT INTO user VALUES(0,0,0,0,0);");
        }
        // DB에 입력한 값으로 행 추가
        db.execSQL("INSERT INTO user VALUES(" + temp + ", '" + name + "','"+apk1+"','"+apk2+"',"+rayout+",'"+color+"',"+bk+");");

        cursor1.close();
        cursor2.close();
    }

    public void saving_drivedata_create(String id) {
        // 읽고 쓰기가 가능하게 DB 열기
        int temp = Integer.parseInt(id);
        SQLiteDatabase db = getWritableDatabase();

        Cursor cursor2 = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name ='saving_drivedata'" , null);
        cursor2.moveToFirst();

        if(cursor2.getCount()==0){
            db.execSQL("create table saving_drivedata(id int, fe float, av float, runtime int, rundistance int);");
            ///db.execSQL("INSERT INTO saving_drivedata VALUES(999,1,1,1,1);");
        }
        // DB에 입력한 값으로 행 추가
        db.execSQL("INSERT INTO saving_drivedata VALUES(" + temp + ", 1, 1, 1, 1);");
        cursor2.close();
    }

    public Cursor saving_drivedata_connect(String id) {
        // 읽고 쓰기가 가능하게 DB 열기
        int temp = Integer.parseInt(id);
        SQLiteDatabase db = getWritableDatabase();

        Cursor cursor2 = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name ='saving_drivedata'" , null);
        cursor2.moveToFirst();

        Cursor cursor=null;
        cursor = db.rawQuery("SELECT * FROM saving_drivedata where id="+temp+"", null);

        cursor2.close();
        return cursor;
    }

    public void saving_drivedata_update(String id, float fe, float av, int runtime, int rundistance) {
        // 읽고 쓰기가 가능하게 DB 열기
        int temp = Integer.parseInt(id);
        String field="";
        SQLiteDatabase db = getReadableDatabase();

        //db table없으면 만들어서 기본 1명 (0, aaa) 집어넣어라
        Cursor cursor1 = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name ='saving_drivedata'" , null);
        cursor1.moveToFirst();

        db.execSQL("UPDATE saving_drivedata set fe="+fe+" where id="+temp+";");
        db.execSQL("UPDATE saving_drivedata set av="+av+" where id="+temp+";");
        db.execSQL("UPDATE saving_drivedata set runtime="+runtime+" where id="+temp+";");
        db.execSQL("UPDATE saving_drivedata set rundistance="+rundistance+" where id="+temp+";");

        cursor1.close();

    }

    public Cursor temp_drivedata_connect(String id) {
        // 읽고 쓰기가 가능하게 DB 열기
        int temp = Integer.parseInt(id);
        SQLiteDatabase db = getWritableDatabase();

        Cursor cursor2 = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name ='temp_drivedata'" , null);
        cursor2.moveToFirst();
        Cursor cursor = null;
        cursor = db.rawQuery("SELECT * FROM temp_drivedata where id="+temp+"", null);
        cursor2.close();
        return cursor;
    }

    public void temp_drivedata_create(String id) {
        // 읽고 쓰기가 가능하게 DB 열기
        int temp = Integer.parseInt(id);
        SQLiteDatabase db = getWritableDatabase();

        Cursor cursor2 = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name ='temp_drivedata'" , null);
        cursor2.moveToFirst();

        if(cursor2.getCount()==0){
            db.execSQL("create table temp_drivedata(id int, en_load int, speed int, intake_air int, rpm int, cool_water int, volt int);");
            //db.execSQL("INSERT INTO temp_drivedata VALUES(999,0,0,0,0,0,0);");
        }
        // DB에 입력한 값으로 행 추가
        db.execSQL("INSERT INTO temp_drivedata VALUES(" + temp + ", 0, 0, 0, 0, 0, 0);");

        cursor2.close();
    }

    public void temp_drivedata_update(String id, int index, int data) {
        // 읽고 쓰기가 가능하게 DB 열기
        int temp = Integer.parseInt(id);
        String field="";
        SQLiteDatabase db = getReadableDatabase();

        switch (index)
        {
            case 1 :
                field = "en_load";
                break;
            case 2 :
                field = "speed";
                break;
            case 3 :
                field = "intake_air";
                break;
            case 4 :
                field = "rpm";
                break;
            case 5 :
                field = "cool_water";
                break;
            case 6 :
                field = "volt";
                break;
        }
        //db table없으면 만들어서 기본 1명 (0, aaa) 집어넣어라
        Cursor cursor1 = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name ='temp_drivedata'" , null);

        db.execSQL("UPDATE temp_drivedata set "+field+"="+data+" where id="+temp+";");
        cursor1.close();

    }

    public Cursor login_connect_db() {
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();

        //db table없으면 만들어서 기본 1명 (0, aaa) 집어넣어라


        Cursor cursor1 = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name ='user'" , null);
        cursor1.moveToFirst();

        if(cursor1.getCount()==0){
            db.execSQL("create table user(id int, name char(15), apk1 char(50), apk2 char(50), rayout int, color char(15), bk int);");
            ///db.execSQL("INSERT INTO user VALUES(999,'aaa','null','null',1,'#FFFFFFFF',0);");

        }

        Cursor cursor = null;
        cursor = db.rawQuery("SELECT * FROM user", null);

        return cursor;
    }


    public void rayout_set(int id, int sel_rayout) {
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();

        //db table없으면 만들어서 기본 1명 (0, aaa) 집어넣어라
        Cursor cursor1 = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name ='user'" , null);
        cursor1.moveToFirst();

        db.execSQL("UPDATE user set rayout='"+sel_rayout+"' where id="+id+";");
        cursor1.close();
    }

    public void color_set(int id, String sel_color) {
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();

        //db table없으면 만들어서 기본 1명 (0, aaa) 집어넣어라
        Cursor cursor1 = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name ='user'" , null);
        cursor1.moveToFirst();

        db.execSQL("UPDATE user set color='"+sel_color+"' where id="+id+";");
        cursor1.close();
    }

    public void bk_set(int id, int sel_bk) {
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();

        //db table없으면 만들어서 기본 1명 (0, aaa) 집어넣어라
        Cursor cursor1 = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name ='user'" , null);
        cursor1.moveToFirst();

        db.execSQL("UPDATE user set bk="+sel_bk+" where id="+id+";");
        cursor1.close();
    }


    public void addapk1_set(int id, String sel_app) {
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();

        //db table없으면 만들어서 기본 1명 (0, aaa) 집어넣어라
        Cursor cursor1 = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name ='user'" , null);
        cursor1.moveToFirst();

        db.execSQL("UPDATE user set apk1='"+sel_app+"' where id="+id+";");
        cursor1.close();
    }

    public void addapk2_set(int id, String sel_app) {
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();

        //db table없으면 만들어서 기본 1명 (0, aaa) 집어넣어라
        Cursor cursor1 = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name ='user'" , null);
        cursor1.moveToFirst();

        db.execSQL("UPDATE user set apk2='"+sel_app+"' where id="+id+";");
        cursor1.close();
    }


}
