package com.example.beafk.firebasetest.DriveAnalysis;

import android.app.TabActivity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.FrameLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.beafk.firebasetest.R;
import com.example.beafk.firebasetest.db.DBHelper;

import static com.example.beafk.firebasetest.R.id.cur_speed;


public class AnalysisActivity extends TabActivity {

    String useridintent;
    String usernameintent;
    String usercolorintent;

    int en_load=0;
    int speed=0;
    int intake=0;
    int rpm=0;
    int cool=0;
    int volt=0;
    float consumption=1;

    DBHelper dbHelper1 = new DBHelper(this, "final", null, 1);
    DBHelper dbHelper2 = new DBHelper(this, "final", null, 1);

    FrameLayout mLayout1;
    FrameLayout mLayout2;

    ProgressHandler1 handler;
    TextView TV1;
    TextView TV2;
    TextView TV3;
    TextView TV4;
    TextView TV5;
    TextView TV6;
    TextView TV7;

    TextView V1;
    TextView V2;
    TextView V3;
    TextView V4;
    TextView V5;
    TextView V6;

    float fe=0;
    float av=0;
    int runttime=0;
    int rundistance=0;

    ProgressHandler2 handler2;

    TextView wTV1;
    TextView wTV2;
    TextView wTV3;
    TextView wTV4;
    TextView wTV5;

    TextView wV1;
    TextView wV2;
    TextView wV3;
    TextView wV4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab);

        Intent intent = getIntent();
        useridintent = intent.getStringExtra("id");
        usernameintent = intent.getStringExtra("name");
        usercolorintent = intent.getStringExtra("color");

        TabHost mTabHost = getTabHost();

        mTabHost.addTab(mTabHost.newTabSpec("tab_test1")
                .setIndicator("실시간 주행분석")
                .setContent(R.id.tabview1));
        mTabHost.addTab(mTabHost.newTabSpec("tab_test2")
                .setIndicator("누적 주행분석")
                .setContent(R.id.tabview2));

        mTabHost.setCurrentTab(0);

            TV1 = (TextView) findViewById(R.id.text_engene_load);
            TV2 = (TextView) findViewById(R.id.text_cur_speed);
            TV3 = (TextView) findViewById(R.id.text_air_intake);
            TV4 = (TextView) findViewById(R.id.text_RPM);
            TV5 = (TextView) findViewById(R.id.text_cooling_water);
            TV6 = (TextView) findViewById(R.id.text_volt);
            TV7 = (TextView) findViewById(R.id.text_result);

            V1 = (TextView) findViewById(R.id.engene_load);
            V2 = (TextView) findViewById(cur_speed);
            V3 = (TextView) findViewById(R.id.air_intake);
            V4 = (TextView) findViewById(R.id.RPM);
            V5 = (TextView) findViewById(R.id.cooling_water);
            V6 = (TextView) findViewById(R.id.volt);

            TV1.setTextColor(Color.parseColor(usercolorintent));
            TV2.setTextColor(Color.parseColor(usercolorintent));
            TV3.setTextColor(Color.parseColor(usercolorintent));
            TV4.setTextColor(Color.parseColor(usercolorintent));
            TV5.setTextColor(Color.parseColor(usercolorintent));
            TV6.setTextColor(Color.parseColor(usercolorintent));
            TV7.setTextColor(Color.parseColor(usercolorintent));

            V1.setTextColor(Color.parseColor(usercolorintent));
            V2.setTextColor(Color.parseColor(usercolorintent));
            V3.setTextColor(Color.parseColor(usercolorintent));
            V4.setTextColor(Color.parseColor(usercolorintent));
            V5.setTextColor(Color.parseColor(usercolorintent));
            V6.setTextColor(Color.parseColor(usercolorintent));

            handler = new ProgressHandler1();
            update_realtime();

            wTV1 = (TextView) findViewById(R.id.text_week_afe);
            wTV2 = (TextView) findViewById(R.id.text_week_av);
            wTV3 = (TextView) findViewById(R.id.text_week_run_time);
            wTV4 = (TextView) findViewById(R.id.text_week_run_distance);
            wTV5 = (TextView) findViewById(R.id.text_consume_fuel);

            wV1 = (TextView) findViewById(R.id.week_afe);
            wV2 = (TextView) findViewById(R.id.week_av);
            wV3 = (TextView) findViewById(R.id.week_run_time);
            wV4 = (TextView) findViewById(R.id.week_run_distance);

            wTV1.setTextColor(Color.parseColor(usercolorintent));
            wTV2.setTextColor(Color.parseColor(usercolorintent));
            wTV3.setTextColor(Color.parseColor(usercolorintent));
            wTV4.setTextColor(Color.parseColor(usercolorintent));
            wTV5.setTextColor(Color.parseColor(usercolorintent));

            wV1.setTextColor(Color.parseColor(usercolorintent));
            wV2.setTextColor(Color.parseColor(usercolorintent));
            wV3.setTextColor(Color.parseColor(usercolorintent));
            wV4.setTextColor(Color.parseColor(usercolorintent));

            handler2 = new ProgressHandler2();
            update_saving();

        }

    public void update_realtime() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Message message = handler.obtainMessage();
                        handler.sendMessage(message);

                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                    }
                }
            }
        });
        thread.start();

        //    asyncTask = new UsedAsync();
        //    asyncTask.execute();
    }

    class ProgressHandler1 extends Handler {
        @Override
        public void handleMessage(Message msg) {

            int temp = Integer.parseInt(useridintent);
            // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
            update_realAsyncTask updaterealAsync = new update_realAsyncTask();
            if(updaterealAsync!=null) {
                updaterealAsync.execute();
            }

            String temp1 = String.valueOf(en_load);
            V1.setText(temp1+" %");
            String temp2 = String.valueOf(speed);
            V2.setText(temp2+" Km/h");
            String temp3 = String.valueOf(intake);
            V3.setText(temp3+" ℃");
            String temp4 = String.valueOf(rpm);
            V4.setText(temp4+" RPM");
            String temp5 = String.valueOf(cool);
            V5.setText(temp5+" ℃");
            String temp6 = String.valueOf(volt);
            V6.setText(temp6+" V");

            String s_consumption = null;
            s_consumption = String.format("%10.1f", (0.001 * 0.004 * 4.3 * 1.35 * 1.6 * rpm * 60 * en_load / 20)).trim();
            consumption = Float.parseFloat(s_consumption);

            float fe = speed/consumption*4;
            int realfe = (int) fe;
            if(realfe>30)
            {
                realfe=30;
            }
            String temp7 = String.valueOf(realfe);

            mLayout1 = (FrameLayout) findViewById(R.id.realresult);

            if(realfe<10) {
                TV7.setText("현재 비 경제적 운전 중");
                mLayout1.setBackgroundColor(Color.rgb(204,0,51));
            }
            else if(realfe>=10)
            {
                TV7.setText("현재 경제적 운전 중");
                mLayout1.setBackgroundColor(Color.rgb(0,153,51));
            }

        }
    }

    class update_realAsyncTask extends AsyncTask<Void, String, Void> {
        @Override
        protected void onPostExecute(Void result) {
            // 작업이 완료 된 후 할일
            super.onPostExecute(result);
        }
        @Override
        protected  void onProgressUpdate(String... values){
            super.onProgressUpdate(values);
        }
        @Override
        protected void onPreExecute() {
            // 작업을 시작하기 전 할일
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(Void... params) {
            // 실행할 작업 XXX 초기화 될때까지 기다림.
            Cursor cursor=null;

            cursor = dbHelper1.temp_drivedata_connect(useridintent);
            while (cursor.moveToNext()) {
                en_load = cursor.getInt(1);
                speed = cursor.getInt(2);
                intake = cursor.getInt(3);
                rpm = cursor.getInt(4);
                cool = cursor.getInt(5);
                volt = cursor.getInt(6);
            }
            cursor.close();
            return null;
        }
    }

    public void update_saving() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Message message = handler2.obtainMessage();
                        handler2.sendMessage(message);

                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                    }
                }
            }
        });
        thread.start();

        //    asyncTask = new UsedAsync();
        //    asyncTask.execute();
    }

    class ProgressHandler2 extends Handler {
        @Override
        public void handleMessage(Message msg) {

            int temp = Integer.parseInt(useridintent);
            // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
            update_savedAsyncTask updatesavedAsync = new update_savedAsyncTask();
            if(updatesavedAsync!=null) {
                updatesavedAsync.execute();
            }

            String temp1 = String.format("%.2f", fe);
            wV1.setText(temp1+" km/l");
            String temp2 = String.format("%.2f", av);
            wV2.setText(temp2+" Km/h");

            String temp3 =  String.format( + (runttime / 3600) + " : " + (runttime % 3600 / 60) + " : " + (runttime % 3600 % 60));
            wV3.setText(temp3);
            int distancetemp = (int)rundistance/1000;
            String temp4 = String.valueOf(distancetemp);
            wV4.setText(temp4+" km");

            mLayout2 = (FrameLayout) findViewById(R.id.savingresult);
            String temp5="0";

            if(fe!=0) {
                temp5 = String.format("%.2f", 1500 / fe);
            }
            else if(fe==0)
            {
                temp5 = "0";
            }

            if(fe<10) {
                wTV5.setText("비 경제적 운전 중( km 당 "+temp5+"원 소모  )");
                mLayout2.setBackgroundColor(Color.rgb(204,0,51));
            }
            else if(fe>=10)
            {
                wTV5.setText("경제적 운전 중( km 당 "+temp5+"원 소모  )");
                mLayout2.setBackgroundColor(Color.rgb(0,153,51));
            }

        }
    }

    class update_savedAsyncTask extends AsyncTask<Void, String, Void> {
        @Override
        protected void onPostExecute(Void result) {
            // 작업이 완료 된 후 할일
            super.onPostExecute(result);
        }
        @Override
        protected  void onProgressUpdate(String... values){
            super.onProgressUpdate(values);
        }
        @Override
        protected void onPreExecute() {
            // 작업을 시작하기 전 할일
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(Void... params) {
            // 실행할 작업 XXX 초기화 될때까지 기다림.
            Cursor cursor=null;

            cursor = dbHelper2.saving_drivedata_connect(useridintent);
            while (cursor.moveToNext()) {
                fe = cursor.getFloat(1);
                av = cursor.getFloat(2);
                runttime = cursor.getInt(3);
                rundistance = cursor.getInt(4);
            }
            cursor.close();


            return null;
        }
    }


    }


