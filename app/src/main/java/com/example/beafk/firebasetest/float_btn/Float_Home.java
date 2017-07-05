package com.example.beafk.firebasetest.float_btn;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.example.beafk.firebasetest.MainActivity;
import com.example.beafk.firebasetest.R;

/**
 * Created by 김동현 on 2017-03-08.
 */

public class Float_Home extends Service {

    String useridintent;
    String usernameintent;
    String usercolorintent;
    String useraddappname;
    String useraddappapk;
    String Add_AppApk2;
    String Add_AppApk1;

    private View mView;
    private WindowManager mManager;
    private WindowManager.LayoutParams mParams;

    private float mTouchX, mTouchY;
    private int mViewX, mViewY;

    private boolean isMove = false;
    private int x = 0;
    private String result;

    @Override
    public void onCreate() {
        super.onCreate();

        LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = mInflater.inflate(R.layout.float_home, null);

        mView.setOnTouchListener(mViewTouchListener);

        mParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_TOAST,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        mParams.gravity = Gravity.BOTTOM | Gravity.RIGHT;

        mManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mManager.addView(mView, mParams);




    }

    private View.OnTouchListener mViewTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    isMove = false;

                    mTouchX = event.getRawX();
                    mTouchY = event.getRawY();
                    mViewX = mParams.x;
                    mViewY = mParams.y;

                    break;

                case MotionEvent.ACTION_UP:
                        performOneClick();

                case MotionEvent.ACTION_MOVE:
                    isMove = true;

                    int x = (int) (event.getRawX() - mTouchX);
                    int y = (int) (event.getRawY() - mTouchY);

                    final int num = 5;
                    if ((x > -num && x < num) && (y > -num && y < num)) {
                        isMove = false;
                        break;
                    }

                    /**
                     * mParams.gravity에 따른 부호 변경
                     *
                     * LEFT : x가 +
                     *
                     * RIGHT : x가 -
                     *
                     * TOP : y가 +
                     *
                     * BOTTOM : y가 -
                     */
                    mParams.x = mViewX + x;
                    mParams.y = mViewY + y;

                    mManager.updateViewLayout(mView, mParams);

                    break;
            }
            return true;
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startid){
        Add_AppApk1 = intent.getStringExtra("App1");
        Add_AppApk2 = intent.getStringExtra("App2");
        useridintent = intent.getStringExtra("id");
        usernameintent = intent.getStringExtra("name");
        usercolorintent = intent.getStringExtra("color");
        Log.d("home", String.valueOf(Add_AppApk2));
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mView != null) {
            mManager.removeView(mView);
            mView = null;
        }
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    private void performOneClick(){
        Intent intent = new Intent(Float_Home.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("id", useridintent);
        intent.putExtra("name", usernameintent);
        intent.putExtra("color",usercolorintent);
        intent.putExtra("addapp",useraddappname);
        intent.putExtra("addapk",useraddappapk);
        intent.putExtra("App1",Add_AppApk1);
        intent.putExtra("App2",Add_AppApk2);

        startActivity(intent);
    }
}