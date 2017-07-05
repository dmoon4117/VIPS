package com.example.beafk.firebasetest.login;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.PaintDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.beafk.firebasetest.MainActivity;
import com.example.beafk.firebasetest.R;
import com.example.beafk.firebasetest.db.DBHelper;

public class preMainActivity extends AppCompatActivity {

    String useridintent;
    String usernameintent;
    String usercolorintent;
    String useraddappname;
    String useraddappapk;

    String Add_apk1;
    String Add_apk2;
    int rayout;
    int bk;



    boolean id_gen_check=false;
    DBHelper dbHelper = new DBHelper(this, "final", null, 1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_premain);

    }

    public void new_memberClick(View v){

        usernameintent= null;
        id_gen_check = false;
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.new_member, null);
        builder.setView(view);
        final EditText temptext = (EditText)view.findViewById(R.id.name_insert);

        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);


        builder.setPositiveButton("Login",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        usernameintent = temptext.getText().toString();
                        if(usernameintent.length() <= 0 || id_gen_check==false)
                        {
                            String m = "성명확인 및 ID를 생성해주세요" ;
                            Toast toast = Toast.makeText(getApplicationContext(),m, Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();

                        }
                        else {
                            Add_apk1=null;
                            Add_apk2=null;
                            rayout=1;
                            usercolorintent = "#FFFFFFFF";
                            bk=0;
                            String address = "null";

                            dbHelper.temp_drivedata_create(useridintent);
                            dbHelper.saving_drivedata_create(useridintent);
                            dbHelper.login_insert(useridintent,usernameintent,Add_apk1,Add_apk2,rayout,usercolorintent,bk);
                            Intent intent = new Intent(preMainActivity.this, MainActivity.class);
                            intent.putExtra("id", useridintent);
                            intent.putExtra("name", usernameintent);
                            intent.putExtra("EXTRA_DEVICE_ADDRESS", address);
                            //intent.putExtra("color",usercolorintent);
                            //intent.putExtra("addapp",useraddappname);
                            //intent.putExtra("addapk",useraddappapk);
                            startActivity(intent);
                            dialog.dismiss();
                        }
                    }
                }
        );
        builder.setNegativeButton("CANCLE",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                }
        );


        builder.show();
    }

    public void existing_memberClick(View v){

        usernameintent= null;
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.existing_member, null);

        /////사용자 리스트 추가코드
        final ListView userlistview ;
        userListViewAdapter adapter;
        // Adapter 생성
        adapter = new userListViewAdapter() ;
        // 리스트뷰 참조 및 Adapter달기
        userlistview = (ListView)view.findViewById(R.id.userlist);
        userlistview.setAdapter(adapter);
        userlistview.setSelector( new PaintDrawable( 0xFFC0C0C0 )) ;

        ////////////////////db에서 사용자 목록 가져와서 출력하는 부분

        userdata userlist = new userdata("","");
        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor=null;

            cursor = dbHelper.login_connect_db();
            while (cursor.moveToNext()) {
                int a= cursor.getInt(0);
                String temp = String.valueOf(a);
                String id = temp;
                String name = cursor.getString(1);
                /////////////////////////////만들어서 넣어주는 부분
                adapter.addItem(id, name) ;
            }


        cursor.close();

        ////////////////////////////////////////////////////////////////

        userlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // get item
                userListViewItem item = (userListViewItem) parent.getItemAtPosition(position) ;

                String sel_id = item.getid() ;
                String sel_name = item.getname() ;

                useridintent = sel_id;
                usernameintent = sel_name;

                Toast toast = Toast.makeText(getApplicationContext(),sel_name, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();

                // TODO : use item data.
            }
        }) ;

        ////////////////////////////////

        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
        builder.setPositiveButton("Login",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        if(usernameintent ==null) {
                            String m = "리스트에서 선택해주세요";
                            Toast toast = Toast.makeText(getApplicationContext(), m, Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                        else {
                            //////////////////////////////디비에서 사용자 색가져오기///////////////////////////
                            usercolorintent = "#FFFFFFFF";
                            String address = "null";
                            /////////////////////////////////////////////////////
                            //dbHelper.temp_drivedata_create(useridintent);
                            //dbHelper.saving_drivedata_create(useridintent);
                            Intent intent = new Intent(preMainActivity.this, MainActivity.class);
                            intent.putExtra("id", useridintent);
                            intent.putExtra("name", usernameintent);
                            intent.putExtra("color",usercolorintent);
                            intent.putExtra("addapp",useraddappname);
                            intent.putExtra("addapk",useraddappapk);
                            intent.putExtra("EXTRA_DEVICE_ADDRESS", address);
                            dialog.dismiss();
                            startActivity(intent);
                        }

                    }
                }
        );
        builder.setNegativeButton("CANCLE",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                }
        );

        builder.show();

    }



    public void id_generate(View v){

        id_gen_check=true;

        /*
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.new_member, null);
        builder.setView(layout);

        AlertDialog dialog = builder.create();
        */
        Cursor cursor=null;
        try{
            cursor = dbHelper.login_connect_db();

            int temp = cursor.getCount();

            String stemp = String.valueOf(temp);

            useridintent = stemp;
            String a = "생성된 ID : "+useridintent ;

            Toast toast = Toast.makeText(getApplicationContext(),a, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }

        finally {
            // this gets called even if there is an exception somewhere above
            if(cursor != null)
                cursor.close();
        }

    }



}
