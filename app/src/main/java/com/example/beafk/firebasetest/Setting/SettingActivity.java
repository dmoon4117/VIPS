package com.example.beafk.firebasetest.Setting;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.beafk.firebasetest.MainActivity;
import com.example.beafk.firebasetest.R;
import com.example.beafk.firebasetest.db.DBHelper;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorChangedListener;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;


public class SettingActivity extends AppCompatActivity {

    String useridintent;
    String usernameintent;
    String usercolorintent;
    String address;
    int rayout;
    int temp_sel;

    DBHelper dbHelper = new DBHelper(this, "final", null, 1);

    private int currentBackgroundColor = 0xffffffff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);


        Intent intent = getIntent();
        useridintent = intent.getStringExtra("id");
        usernameintent = intent.getStringExtra("name");
        address = intent.getStringExtra("EXTRA_DEVICE_ADDRESS");

        TextView editText1 = (TextView) findViewById(R.id.editText1);

        findViewById(R.id.btn_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Context context = SettingActivity.this;

                ColorPickerDialogBuilder
                        .with(context)
                        .setTitle(R.string.color_dialog_title)
                        .initialColor(currentBackgroundColor)
                        .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                        .density(12)
                        .setOnColorChangedListener(new OnColorChangedListener() {
                            @Override
                            public void onColorChanged(int selectedColor) {
                                // Handle on color change
                                Log.d("ColorPicker", "onColorChanged: 0x" + Integer.toHexString(selectedColor));
                            }
                        })
                        .setOnColorSelectedListener(new OnColorSelectedListener() {
                            @Override
                            public void onColorSelected(int selectedColor) {
                                toast("선택한색상: 0x" + Integer.toHexString(selectedColor));
                            }
                        })
                        .setPositiveButton("ok", new ColorPickerClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                                if (allColors != null) {
                                    StringBuilder sb = null;
                                    StringBuilder intent_temp = null;
                                    for (Integer color : allColors) {
                                        if (color == null)
                                            continue;
                                        if (sb == null)
                                            sb = new StringBuilder("선택한 색상");
                                        sb.append("\r\n#" + Integer.toHexString(color).toUpperCase());
                                        intent_temp = new StringBuilder("#");
                                        intent_temp.append(Integer.toHexString(color).toUpperCase());
                                    }
                                    usercolorintent= intent_temp.toString();
                                    int idtemp = Integer.parseInt(useridintent);
                                    dbHelper.color_set(idtemp,usercolorintent);
                                    if (sb != null)
                                        Toast.makeText(getApplicationContext(), intent_temp.toString(), Toast.LENGTH_SHORT).show();
                                }
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();}
                        })
                        .showColorEdit(true)
                        .setColorEditTextColor(ContextCompat.getColor(SettingActivity.this, android.R.color.holo_blue_bright))
                        .build()
                        .show();
            }
        });

    }

    private void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    public void go_main(View v) {
        address=null;
        Intent intent = new Intent(SettingActivity.this,MainActivity.class);
        intent.putExtra("id", useridintent);
        intent.putExtra("name", usernameintent);
        intent.putExtra("EXTRA_DEVICE_ADDRESS", address);
        startActivity(intent);
    }

    public void rayout_change_to1(View v) {
            rayout=1;
            int idtemp = Integer.parseInt(useridintent);
            dbHelper.rayout_set(idtemp,rayout);
            String alert = "좌측형 레이아웃으로 변경되었습니다.";
            Toast toast = Toast.makeText(getApplicationContext(),alert, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

    }

    public void rayout_change_to2(View v) {
        rayout=2;
        int idtemp = Integer.parseInt(useridintent);
        dbHelper.rayout_set(idtemp,rayout);
        String alert = "우측형 레이아웃으로 변경되었습니다.";
        Toast toast = Toast.makeText(getApplicationContext(),alert, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public void set_bk(View v){

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.bk_list, null);
        builder.setView(view);

        final ImageView bk0 = (ImageView) view.findViewById(R.id.bk0) ;
        final ImageView bk1 = (ImageView) view.findViewById(R.id.bk1) ;
        final ImageView bk2 = (ImageView) view.findViewById(R.id.bk2) ;
        final ImageView bk3 = (ImageView) view.findViewById(R.id.bk3) ;
        final ImageView bk4 = (ImageView) view.findViewById(R.id.bk4) ;
        final ImageView bk5 = (ImageView) view.findViewById(R.id.bk5) ;
        final ImageView bk6 = (ImageView) view.findViewById(R.id.bk6) ;
        final ImageView bk7 = (ImageView) view.findViewById(R.id.bk7) ;

        ImageView.OnClickListener onClickListener = new ImageView.OnClickListener() {
            @Override
            public void onClick(View view) {
                PackageManager packageManager = getPackageManager();
                switch (view.getId()) {
                    case R.id.bk0 :
                        temp_sel = 0;
                        bk0.setAlpha(1.0f);
                        bk1.setAlpha(0.5f);
                        bk2.setAlpha(0.5f);
                        bk3.setAlpha(0.5f);
                        bk4.setAlpha(0.5f);
                        bk5.setAlpha(0.5f);
                        bk6.setAlpha(0.5f);
                        bk7.setAlpha(0.5f);
                        break;
                    case R.id.bk1 :
                        temp_sel = 1;
                        bk0.setAlpha(0.5f);
                        bk1.setAlpha(1.0f);
                        bk2.setAlpha(0.5f);
                        bk3.setAlpha(0.5f);
                        bk4.setAlpha(0.5f);
                        bk5.setAlpha(0.5f);
                        bk6.setAlpha(0.5f);
                        bk7.setAlpha(0.5f);
                        break;
                    case R.id.bk2 :
                        temp_sel = 2;
                        bk0.setAlpha(0.5f);
                        bk1.setAlpha(0.5f);
                        bk2.setAlpha(1.0f);
                        bk3.setAlpha(0.5f);
                        bk4.setAlpha(0.5f);
                        bk5.setAlpha(0.5f);
                        bk6.setAlpha(0.5f);
                        bk7.setAlpha(0.5f);
                        break;
                    case R.id.bk3 :
                        temp_sel = 3;
                        bk0.setAlpha(0.5f);
                        bk1.setAlpha(0.5f);
                        bk2.setAlpha(0.5f);
                        bk3.setAlpha(1.0f);
                        bk4.setAlpha(0.5f);
                        bk5.setAlpha(0.5f);
                        bk6.setAlpha(0.5f);
                        bk7.setAlpha(0.5f);
                        break;
                    case R.id.bk4 :
                        temp_sel = 4;
                        bk0.setAlpha(0.5f);
                        bk1.setAlpha(0.5f);
                        bk2.setAlpha(0.5f);
                        bk3.setAlpha(0.5f);
                        bk4.setAlpha(1.0f);
                        bk5.setAlpha(0.5f);
                        bk6.setAlpha(0.5f);
                        bk7.setAlpha(0.5f);
                        break;
                    case R.id.bk5 :
                        temp_sel = 5;
                        bk0.setAlpha(0.5f);
                        bk1.setAlpha(0.5f);
                        bk2.setAlpha(0.5f);
                        bk3.setAlpha(0.5f);
                        bk4.setAlpha(0.5f);
                        bk5.setAlpha(1.0f);
                        bk6.setAlpha(0.5f);
                        bk7.setAlpha(0.5f);
                        break;
                    case R.id.bk6 :
                        temp_sel = 6;
                        bk0.setAlpha(0.5f);
                        bk1.setAlpha(0.5f);
                        bk2.setAlpha(0.5f);
                        bk3.setAlpha(0.5f);
                        bk4.setAlpha(0.5f);
                        bk5.setAlpha(0.5f);
                        bk6.setAlpha(1.0f);
                        bk7.setAlpha(0.5f);
                        break;
                    case R.id.bk7 :
                        temp_sel = 7;
                        bk0.setAlpha(0.5f);
                        bk1.setAlpha(0.5f);
                        bk2.setAlpha(0.5f);
                        bk3.setAlpha(0.5f);
                        bk4.setAlpha(0.5f);
                        bk5.setAlpha(0.5f);
                        bk6.setAlpha(0.5f);
                        bk7.setAlpha(1.0f);
                        break;
                }
            }
        } ;

        bk0.setOnClickListener(onClickListener);
        bk1.setOnClickListener(onClickListener);
        bk2.setOnClickListener(onClickListener);
        bk3.setOnClickListener(onClickListener);
        bk4.setOnClickListener(onClickListener);
        bk5.setOnClickListener(onClickListener);
        bk6.setOnClickListener(onClickListener);
        bk7.setOnClickListener(onClickListener);

        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);

        builder.setPositiveButton("Select",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        int idtemp = Integer.parseInt(useridintent);
                        dbHelper.bk_set(idtemp,temp_sel);
                        dialog.dismiss();
                    }
                }
        );
        builder.setNegativeButton("Cancle",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                }
        );
        builder.show();
    }






}