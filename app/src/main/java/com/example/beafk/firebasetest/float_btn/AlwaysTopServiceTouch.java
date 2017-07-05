package com.example.beafk.firebasetest.float_btn;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.beafk.firebasetest.MainActivity;
import com.example.beafk.firebasetest.R;

/**
 * Created by 김동현 on 2017-03-08.
 */

public class AlwaysTopServiceTouch extends Service {

    String useridintent;
    String usernameintent;

    private View mView;
    private WindowManager mManager;
    private WindowManager.LayoutParams mParams;

    private float mTouchX, mTouchY;
    private int mViewX, mViewY;

    private boolean isMove = false;
    private int x = 0;
    private int result;

    //longclik을 위한 변수
    private boolean mHasPerFormedLongPress;
    private CheckForLongPress mPendingCheckForLongPress;

    private Handler mHandler = null;


    @Override
    public void onCreate() {
        super.onCreate();

        mHandler = new Handler();



        LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = mInflater.inflate(R.layout.always_on_top_view_touch, null);

        mView.setOnTouchListener(mViewTouchListener);

        mParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_TOAST,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        mParams.gravity = Gravity.BOTTOM | Gravity.LEFT;

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

                    mHasPerFormedLongPress = false;
                    postCheckForLongClick(0);

                    break;

                case MotionEvent.ACTION_UP:
                    if (!mHasPerFormedLongPress) {
                        removeLongPressCallback();
                        performOneClick();
                    } else
                        performLongClick();
                    break;

                case MotionEvent.ACTION_MOVE:
                    removeLongPressCallback();
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

    private void postCheckForLongClick(int delayOffset){
        mHasPerFormedLongPress = false;
        if(mPendingCheckForLongPress == null){
            mPendingCheckForLongPress = new CheckForLongPress();
        }

        mHandler.postDelayed(mPendingCheckForLongPress,
                ViewConfiguration.getLongPressTimeout() - delayOffset);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startid){
        useridintent = intent.getStringExtra("id");
        usernameintent = intent.getStringExtra("name");
        Log.d("result", String.valueOf(result));
        result = intent.getIntExtra("num",0);
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

    class CheckForLongPress implements Runnable{
        public void run(){
            if(performLongClick()){
                mHasPerFormedLongPress = true;
            }
        }
    }

    private void removeLongPressCallback(){
        if(mPendingCheckForLongPress != null){
            mHandler.removeCallbacks(mPendingCheckForLongPress);
        }
    }
    public boolean performLongClick(){
        Toast.makeText(AlwaysTopServiceTouch.this, "Long Click OK!!", Toast.LENGTH_SHORT).show();
        //KeyEvent.KEYCODE_MENU;
        return true;
    }

    private void performOneClick(){
        Log.d("x값은","x");
        if (!isMove) {
            if (x == 0) {
                Log.d("x값","0");
                x=1;
                Intent intent = new Intent(AlwaysTopServiceTouch.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("id", useridintent);
                intent.putExtra("name", usernameintent);
                startActivity(intent);
            }
            else {
                Log.d("x값","1");
                switch(result) {
                    case 1: {
                        Log.d("x값","1");
                        PackageManager packageManager = getPackageManager();
                        x = 0;
                        Intent intent1 = packageManager.getLaunchIntentForPackage("com.locnall.KimGiSa");
                        //intent0.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        if (null != intent1) {
                            startActivity(intent1);
                        } else {
                            Intent intenta = new Intent(Intent.ACTION_VIEW);
                            intenta.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.locnall.KimGiSa&hl=ko"));
                            intenta.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intenta);
                        }
                        break;
                    }
                    case 2: {
                        PackageManager packageManager = getPackageManager();
                        x = 0;
                        Intent intent1 = packageManager.getLaunchIntentForPackage("com.skt.tmap.ku");
                        //intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        if (null != intent1) {
                            startActivity(intent1);
                        } else {
                            Intent intenta = new Intent(Intent.ACTION_VIEW);
                            intenta.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.skt.tmap.ku&hl=ko"));
                            intenta.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intenta);
                        }
                        break;
                    }
                    case 3:{
                        PackageManager packageManager = getPackageManager();
                        x = 0;
                        Intent intent1 = packageManager.getLaunchIntentForPackage("kr.mappers.AtlanSmart");
                        //intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        if (null != intent1) {
                            startActivity(intent1);
                        } else {
                            Intent intenta = new Intent(Intent.ACTION_VIEW);
                            intenta.setData(Uri.parse("https://play.google.com/store/apps/details?id=kr.mappers.AtlanSmart&hl=ko"));
                            intenta.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intenta);
                        }
                        break;
                    }
                    case 4:{
                        PackageManager packageManager = getPackageManager();
                        x = 0;
                        Intent intent1 = packageManager.getLaunchIntentForPackage("com.thinkware.inaviair");
                        //intent0.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        if (null != intent1) {
                            startActivity(intent1);
                        } else {
                            Intent intenta = new Intent(Intent.ACTION_VIEW);
                            intenta.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.thinkware.inaviair&hl=ko"));
                            intenta.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intenta);
                        }
                        break;
                    }
                    default:{
                        Log.d("x값","5");
                        Intent intent = new Intent(AlwaysTopServiceTouch.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("id", useridintent);
                        intent.putExtra("name", usernameintent);
                        startActivity(intent);
                        break;
                    }
                }
            }
        }
    }
}