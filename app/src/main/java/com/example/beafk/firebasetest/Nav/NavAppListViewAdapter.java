package com.example.beafk.firebasetest.Nav;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.beafk.firebasetest.R;
import com.example.beafk.firebasetest.main_add_icon.ListViewItem;

import java.util.ArrayList;

public class NavAppListViewAdapter extends BaseAdapter {
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<ListViewItem> listViewItemList = new ArrayList<ListViewItem>() ;

    // ListViewAdapter의 생성자
    public NavAppListViewAdapter() {

    }

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return listViewItemList.size() ;
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override


    //////////////////////////////////////////////////////////////////////////////////////
    // approw.xml를 하나의 아이템으로하여 그 아이템들을 아이폰 폴더 구조처럼 배열하는 어댑터
    // appforder.xml을 통해 아이템들을 배열하여 전시한다.
    //////////////////////////////////////////////////////////////////////////////////////
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.nav_applist_row, parent, false);
        }

        //approw에 있는 뷰들을 하나의 아이템(아이콘+어플명+apk명)으로 만들어줌
        ImageView iconImageView = (ImageView) convertView.findViewById(R.id.appicon) ;
        TextView titleTextView = (TextView) convertView.findViewById(R.id.apptitle) ;
        TextView apkTextView = (TextView) convertView.findViewById(R.id.appapk) ;



        // 몇 번째 순서에 들어가는지 반환
        ListViewItem listViewItem = listViewItemList.get(position);

        //approw에 있는 뷰들에 대한 디자인
        iconImageView.setImageDrawable(listViewItem.getIcon());

        //첫번째칸 텍스트 설정
        titleTextView.setText(listViewItem.getTitle());
        apkTextView.setText(listViewItem.getApk());
        //첫번째칸 색,투명도 설정
        titleTextView.setBackgroundColor(Color.argb(60,120,120, 120));
        apkTextView.setBackgroundColor(Color.argb(60,120,120, 120));
        //글씨색
        titleTextView.setTextColor(Color.rgb(255, 255, 255));
        apkTextView.setTextColor(Color.rgb(255, 255, 255));

        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position ;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position) ;
    }

    // 아이템 내부의 뷰들에 원하는 아이콘이나 어플명이나 apk값들을 입혀주는 곳
    // 내부함수들을 LisViewItem에 존재, addItem함수의 콜은 MainActivity에서
    public void addItem(Drawable icon, String title, String apk) {
        ListViewItem item = new ListViewItem();

        item.setIcon(icon);
        item.setTitle(title);
        item.setApk(apk);




        listViewItemList.add(item);
    }

}
