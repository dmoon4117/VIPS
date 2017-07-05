package com.example.beafk.firebasetest.main_add_icon;

import android.graphics.drawable.Drawable;

/**
 * Created by beafk on 2017-02-08.
 */
//~~row.xml의 레이아웃에 포함된 뷰들을 하나의 아이템으로 만들어주기 위한 클래스
public class ListViewItem {

    private Drawable iconDrawable ; // 아이콘
    private String titleStr ;   // 어플명
    private String apkStr ; // apk값

    //각각의 아이템 내부에 있는 뷰들을 원하는 데이터로 세팅해주는 함수들
    public void setIcon(Drawable icon) {
        iconDrawable = icon ;
    }
    public void setTitle(String title) {
        titleStr = title ;
    }
    public void setApk(String apk) {
        apkStr = apk ;
    }

    //각각의 아이템 내부에 있는 뷰들의 정보를 가져오는 함수
    public Drawable getIcon() {
        return this.iconDrawable ;
    }
    public String getTitle() {
        return this.titleStr ;
    }
    public String getApk() {
        return this.apkStr ;
    }

}
