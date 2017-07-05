package com.example.beafk.firebasetest.add_app;

import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;

import java.text.Collator;
import java.util.Comparator;

/**
 * Created by beafk on 2017-04-07.
 */

public class AppInfo {
    public static interface AppFilter {
        public void init();
        public boolean filterApp(ApplicationInfo info);
    }

    // 아이콘
    public Drawable mIcon = null;
    // 어플리케이션 이름
    public String mAppName = null;
    // 패키지 명
    public String mAppPackge = null;

    /**
     * 서드파티 필터
     */
    public static final AppFilter THIRD_PARTY_FILTER = new AppFilter() {
        public void init() {
        }

        @Override
        public boolean filterApp(ApplicationInfo info) {
            if ((info.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
                return true;
            } else if ((info.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                return true;
            }
            return false;
        }
    };

    /**
     * 알파벳 이름으로 정렬
     */
    public static final Comparator<AppInfo> ALPHA_COMPARATOR = new Comparator<AppInfo>() {
        private final Collator sCollator = Collator.getInstance();
        @Override
        public int compare(AppInfo object1, AppInfo object2) {
            return sCollator.compare(object1.mAppName, object2.mAppName);
        }
    };

    public void setIcon(Drawable Icon) {
        mIcon = Icon ;
    }
    public void setTitle(String Title) {mAppName = Title ;}
    public void setApk(String Apk) {
        mAppPackge = Apk ;
    }

    //각각의 아이템 내부에 있는 뷰들의 정보를 가져오는 함수
    public Drawable getIcon() {
        return this.mIcon ;
    }
    public String getTitle() {return this.mAppName ;}
    public String getApk() {return this.mAppPackge ;}
}