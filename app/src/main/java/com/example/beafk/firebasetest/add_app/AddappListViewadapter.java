package com.example.beafk.firebasetest.add_app;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.beafk.firebasetest.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by beafk on 2017-04-07.
 */

public class AddappListViewadapter extends BaseAdapter {
    private Context mContext = null;
    private PackageManager pm;
    private List<ApplicationInfo> mAppList = null;
        private final int MENU_ALL = 1;
    private final int MENU_DOWNLOAD = 0;
    private int MENU_MODE = MENU_DOWNLOAD;
    private ArrayList<AppInfo> mListData = new ArrayList<AppInfo>();

    public AddappListViewadapter(Context mContext) {
        super();
        this.mContext = mContext;
    }
    public AddappListViewadapter() {
    }

    public int getCount() {
        return mListData.size();
    }

    public Object getItem(int position) {
        return mListData.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        AddAppdata holder;

        if (convertView == null) {
            holder = new AddAppdata();

            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.add_applistrow, null);

            holder.mIcon = (ImageView) convertView
                    .findViewById(R.id.app_icon);
            holder.mName = (TextView) convertView
                    .findViewById(R.id.app_name);
//            holder.mPacakge = (TextView) convertView
//                    .findViewById(R.id.app_package);

            convertView.setTag(holder);
        } else {
            holder = (AddAppdata) convertView.getTag();
        }

        AppInfo data = mListData.get(position);

        if (data.mIcon != null) {
            holder.mIcon.setImageDrawable(data.mIcon);
        }

        holder.mName.setText(data.mAppName);
//        holder.mPacakge.setText(data.mAppPackge);

        return convertView;
    }

    public void addItem(Drawable Icon, String title, String Apk) {
        AppInfo item = new AppInfo();
        item.setIcon(Icon);
        item.setTitle(title);
        item.setApk(Apk);
        mListData.add(item);
    }
}