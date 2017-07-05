package com.example.beafk.firebasetest;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.beafk.firebasetest.DriveAnalysis.AnalysisActivity;
import com.example.beafk.firebasetest.Setting.SettingActivity;
import com.example.beafk.firebasetest.add_app.AddappListViewadapter;
import com.example.beafk.firebasetest.add_app.AppInfo;
import com.example.beafk.firebasetest.db.DBHelper;
import com.example.beafk.firebasetest.float_btn.AlwaysTopServiceTouch;
import com.example.beafk.firebasetest.float_btn.Float_Home;
import com.example.beafk.firebasetest.main_add_icon.ListViewAdapter;
import com.example.beafk.firebasetest.main_add_icon.ListViewAdapter2;
import com.example.beafk.firebasetest.main_icon.DriveListViewAdapter;
import com.example.beafk.firebasetest.main_icon.NavListViewAdapter;
import com.example.beafk.firebasetest.main_icon.UiListViewAdapter;
import com.example.beafk.firebasetest.obd.BluetoothService;
import com.example.beafk.firebasetest.obd.DeviceListActivity;
import com.facebook.stetho.Stetho;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static com.example.beafk.firebasetest.R.id.listview2;
import static com.example.beafk.firebasetest.R.id.listview4;
import static com.example.beafk.firebasetest.R.id.speed;
import static java.lang.Thread.sleep;


public class MainActivity extends AppCompatActivity {

    ListView mListView; // 추가어플목록리스트
    AddappListViewadapter mAdapter; // 추가어플목록리스트어댑터
    final int MENU_ALL = 1;
    final int MENU_DOWNLOAD = 0;
    int MENU_MODE = MENU_DOWNLOAD;
    public static int Connected=0;

    Drawable d = null;
    Drawable Add_App_Icon1 = null;
    Drawable Add_App_Icon2 = null;
    String Add_AppName1 = null;
    String Add_AppName2 = null;
    String Add_AppApk1 = null;
    String Add_AppApk2 = null;
    String useridintent;
    String usernameintent;
    String usercolorintent;
    int rayout;
    int bk;

    DBHelper dbHelper = new DBHelper(this, "final", null, 1);
    DBHelper dbHelper1 = new DBHelper(this, "final", null, 1);
    DBHelper dbHelper2 = new DBHelper(this, "final", null, 1);

    //사용자정보 객체생성
    UserData userdata = new UserData(0);
    Intent home;

    ymdhmsHandler handler1;
    TimeHandler handler2;
    speedHandler handler3;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
    SimpleDateFormat sdf2 = new SimpleDateFormat("hh:mm:ss");
    String time;
    String ymd;
    TextView YMD;
    TextView Time;
    TextView cur_speed;
    int db_speed=1;


    ProgressBar progBar;
    Handler mHandler = new Handler();
    int mProgressStatus = 0;

    /////////////////////////////OBD////////////////////////////////////
    private static final String TAG = "MainActivity";
    public static final int MESSAGE_STATE_CHANGE = 1;
    /*0	Automatic protocol detection
   1	SAE J1850 PWM (41.6 kbaud)
   2	SAE J1850 VPW (10.4 kbaud)
   3	ISO 9141-2 (5 baud init, 10.4 kbaud)
   4	ISO 14230-4 KWP (5 baud init, 10.4 kbaud)
   5	ISO 14230-4 KWP (fast init, 10.4 kbaud)
   6	ISO 15765-4 CAN (11 bit ID, 500 kbaud)
   7	ISO 15765-4 CAN (29 bit ID, 500 kbaud)
   8	ISO 15765-4 CAN (11 bit ID, 250 kbaud) - used mainly on utility vehicles and Volvo
   9	ISO 15765-4 CAN (29 bit ID, 250 kbaud) - used mainly on utility vehicles and Volvo


    01 04 - ENGINE_LOAD
    01 05 - ENGINE_COOLANT_TEMPERATURE
    01 0C - ENGINE_RPM
    01 0D - VEHICLE_SPEED
    01 0F - INTAKE_AIR_TEMPERATURE
    01 10 - MASS_AIR_FLOW
    01 11 - THROTTLE_POSITION_PERCENTAGE
    01 1F - ENGINE_RUN_TIME
    01 2F - FUEL_LEVEL
    01 46 - AMBIENT_AIR_TEMPERATURE
    01 51 - FUEL_TYPE
    01 5E - FUEL_CONSUMPTION_1
    01 5F - FUEL_CONSUMPTION_2

   */

    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;
    // Key names received from the BluetoothChatService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

    protected final static char[] dtcLetters = {'P', 'C', 'B', 'U'};
    protected final static char[] hexArray = "0123456789ABCDEF".toCharArray();

    private static final String[] PIDS = {
            "01", "02", "03", "04", "05", "06", "07", "08",
            "09", "0A", "0B", "0C", "0D", "0E", "0F", "10",
            "11", "12", "13", "14", "15", "16", "17", "18",
            "19", "1A", "1B", "1C", "1D", "1E", "1F", "20",
            "21", "22", "23", "24", "25", "26", "27", "28",
            "29", "2A", "2B", "2C", "2D", "2E", "2F", "30"};

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE = 2;
    private static final int REQUEST_ENABLE_BT = 3;
    private static final float APPBAR_ELEVATION = 14f;
    private static boolean actionbar = true;
    final List<String> commandslist = new ArrayList<String>();
    ;
    final List<Double> avgconsumption = new ArrayList<Double>();
    final List<String> troubleCodesArray = new ArrayList<String>();
    MenuItem itemtemp;
    BluetoothDevice currentdevice;
    boolean commandmode = false, initialized = false, m_getPids = false, tryconnect = false, defaultStart = false;
    String devicename = null, deviceprotocol = null;

    String[] initializeCommands;
    Intent serverIntent = null;
    String S_VOLTAGE = "ATRV",
            PROTOCOL = "ATDP",
            RESET = "ATZ",
            S_ENGINE_COOLANT_TEMP = "0105",  //A-40
            S_ENGINE_RPM = "010C",  //((A*256)+B)/4
            S_ENGINE_LOAD = "0104",  // A*100/255
            S_VEHICLE_SPEED = "010D",  //A
            S_INTAKE_AIR_TEMP = "010F",  //A-40
            MAF_AIR_FLOW = "0110", //MAF air flow rate 0 - 655.35	grams/sec ((256*A)+B) / 100  [g/s]
            ENGINE_OIL_TEMP = "015C",  //A-40
            FUEL_RAIL_PRESSURE = "0122", // ((A*256)+B)*0.079
            INTAKE_MAN_PRESSURE = "010B", //Intake manifold absolute pressure 0 - 255 kPa
            CONT_MODULE_VOLT = "0142",  //((A*256)+B)/1000
            AMBIENT_AIR_TEMP = "0146",  //A-40
            CATALYST_TEMP_B1S1 = "013C",  //(((A*256)+B)/10)-40
            STATUS_DTC = "0101", //Status since DTC Cleared
            THROTTLE_POSITION = "0111", //Throttle position 0 -100 % A*100/255
            OBD_STANDARDS = "011C", //OBD standards this vehicle
            PIDS_SUPPORTED = "0120", //PIDs supported
            S_Fuel_Tank_Level_Input = "012F", // A*100/255
            S_RUN_TIME_SINCE_ENGINE_START = "011F"; //(A*256)+B

    Toolbar toolbar;
    AppBarLayout appbar;
    String trysend = null;
    private PowerManager.WakeLock wl;
    private Menu menu;
    private String mConnectedDeviceName = "Ecu";
    private int rpmval = 0, intakeairtemp = 0, ambientairtemp = 0, coolantTemp = 0,
            engineoiltemp = 0, b1s1temp = 0, Enginetype = 0, FaceColor = 0,
            whichCommand = 0, m_dedectPids = 0, connectcount = 0, trycount = 0;
    private double Enginedisplacement = 1500;
    // Local Bluetooth adapter
    private BluetoothAdapter mBluetoothAdapter = null;
    // Member object for the chat services
    private BluetoothService mBtService = null;

    StringBuilder inStream = new StringBuilder();

    // The Handler that gets information back from the BluetoothChatService
    // Array adapter for the conversation thread
    private ArrayAdapter<String> mConversationArrayAdapter;

    float[] testlist = new float[20];


    private final Handler mBtHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:


                            try {
                                itemtemp = menu.findItem(R.id.menu_connect_bt);
                                itemtemp.setTitle(R.string.disconnectbt);


                            } catch (Exception e) {
                            }

                            tryconnect = false;
                            resetvalues();
                            sendEcuMessage(RESET);

                            break;
                        case BluetoothService.STATE_CONNECTING:
                            break;
                        case BluetoothService.STATE_LISTEN:

                        case BluetoothService.STATE_NONE:

                            itemtemp = menu.findItem(R.id.menu_connect_bt);
                            itemtemp.setTitle(R.string.connectbt);
                            if (tryconnect) {
                                mBtService.connect(currentdevice);
                                connectcount++;
                                if (connectcount >= 2) {
                                    tryconnect = false;
                                }
                            }
                            resetvalues();

                            break;
                    }
                    break;
                case MESSAGE_WRITE:

                    byte[] writeBuf = (byte[]) msg.obj;
                    String writeMessage = new String(writeBuf);

                    if (commandmode || !initialized) {
                        mConversationArrayAdapter.add("Command:  " + writeMessage);
                    }

                    break;
                case MESSAGE_READ:

                    String tmpmsg = clearMsg(msg);

                    if (commandmode || !initialized) {
                        mConversationArrayAdapter.add(mConnectedDeviceName + ":  " + tmpmsg);
                    }

                    analysMsg(msg);

                    break;
                case MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                    break;
                case MESSAGE_TOAST:
                    Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST),
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private void removePID(String pid)
    {
        int index = commandslist.indexOf(pid);

        if (index != -1)
        {
            commandslist.remove(index);
        }
    }

    // The action listener for the EditText widget, to listen for the return key
    private TextView.OnEditorActionListener mWriteListener =
            new TextView.OnEditorActionListener() {
                public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                    // If the action is a key-up event on the return key, send the message
                    if (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_UP) {
                        String message = view.getText().toString();
                        sendEcuMessage(message);
                    }
                    return true;
                }
            };

    public static boolean isHexadecimal(String text) {
        text = text.trim();

        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f', 'A', 'B', 'C', 'D', 'E', 'F'};

        int hexDigitsCount = 0;

        for (char symbol : text.toCharArray()) {
            for (char hexDigit : hexDigits) {
                if (symbol == hexDigit) {
                    hexDigitsCount++;
                    break;
                }
            }
        }

        return true ? hexDigitsCount == text.length() : false;
    }
    ////////////////////////OBD변수
    int engine_load = 1;
    int air_intake = 1;
    int rpm = 1;
    int vehicle_speed = 1;
    int engine_coolant_temp = 1;
    String battery = "0";
    int fe=0;

    float fetemp=1;
    float avtemp=1;
    int runttimetemp=1;
    int rundistancetemp=1;
    ////////////////////////////

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        Stetho.initializeWithDefaults(this);

        YMD = (TextView) findViewById(R.id.YMD);
        Time = (TextView) findViewById(R.id.Time);
        progBar = (ProgressBar) findViewById(R.id.main_progressBar);
        cur_speed = (TextView) findViewById(R.id.speed);


        Intent intent = getIntent();
        useridintent = intent.getStringExtra("id");
        usernameintent = intent.getStringExtra("name");
        synchronize_db();

        if (rayout == 1) {
            setContentView(R.layout.activity_main);
        } else if (rayout == 2) {
            setContentView(R.layout.activity_main2);
        }

        MainInit(rayout, bk);
        handler1 = new ymdhmsHandler();
        handler3 = new speedHandler();
        SideInit();
        add_app_loading_main();
        runTime();
        update_speed(useridintent);
        Obd_Start();

    }

    public void Obd_Start()
    {
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "My Tag");
        wl.acquire();

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        //ATZ reset all
        //ATDP Describe the current Protocol
        //ATAT0-1-2 Adaptive Timing Off - daptive Timing Auto1 - daptive Timing Auto2
        //ATE0-1 Echo Off - Echo On
        //ATSP0 Set Protocol to Auto and save it
        //ATMA Monitor All
        //ATL1-0 Linefeeds On - Linefeeds Off
        //ATH1-0 Headers On - Headers Off
        //ATS1-0 printing of Spaces On - printing of Spaces Off
        //ATAL Allow Long (>7 byte) messages
        //ATRD Read the stored data
        //ATSTFF Set time out to maximum
        //ATSTHH Set timeout to 4ms

        initializeCommands = new String[]{"ATZ", "ATL0", "ATE1", "ATH1", "ATAT1", "ATSTFF", "ATI", "ATDP", "ATSP0", "ATSP0"};

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(), "Bluetooth is not available", Toast.LENGTH_LONG).show();
        }
        else
        {
            if (mBtService != null) {
                if (mBtService.getState() == BluetoothService.STATE_NONE) {
                    mBtService.start();
                }
            }
        }

        // Initialize the array adapter for the conversation thread
        mConversationArrayAdapter = new ArrayAdapter<String>(this, R.layout.message) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // Get the Item from ListView
                View view = super.getView(position, convertView, parent);

                // Initialize a TextView for ListView each Item
                TextView tv = (TextView) view.findViewById(R.id.listText);

                // Set the text color of TextView (ListView Item)
                tv.setTextColor(Color.parseColor("#3ADF00"));
                tv.setTextSize(10);

                // Generate ListView Item using TextView
                return view;
            }
        };
    }
    public void db_runtime(final String id, final int rpm, final int engine_load, final int vehicle_speed ) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {

                        Message message = handler2.obtainMessage();
                        handler2.sendMessage(message);

                        sleep(1000);
                    } catch (InterruptedException ex) {
                    }
                }
            }
        });
        thread.start();

        //    asyncTask = new UsedAsync();
        //    asyncTask.execute();
    }

    public void update_speed(final String useridintent) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() { // Thread 로 작업할 내용을 구현
                while (true) {
                    try {
                        update_speedAsyncTask updatespeedAsync = new update_speedAsyncTask();
                        if(updatespeedAsync!=null) {
                            updatespeedAsync.execute();
                        }
                        Message message = handler3.obtainMessage();
                        handler3.sendMessage(message);
                        sleep(1000); // 시간지연
                    } catch (InterruptedException e) {
                    }
                } // end of while
            }
        });
        t.start(); // 쓰레드 시작
    }



    public void runTime() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        ymd = sdf.format(new Date(System.currentTimeMillis()));
                        time = sdf2.format(new Date(System.currentTimeMillis()));
                        Message message = handler1.obtainMessage();
                        handler1.sendMessage(message);
                        sleep(1000);
                    } catch (InterruptedException ex) {
                    }
                }
            }
        });
        thread.start();

        //    asyncTask = new UsedAsync();
        //    asyncTask.execute();
    }

    class update_speedAsyncTask extends AsyncTask<Void, String, Void> {
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
                int temp = Integer.parseInt(useridintent);
                // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
///////////////오류나면 여기////////////////////

            cursor = dbHelper1.temp_drivedata_connect(useridintent);
            while (cursor.moveToNext()) {
                db_speed = cursor.getInt(2);
            }
            cursor.moveToFirst();
            cursor.close();

            return null;
        }
    }
    class update_savingAsyncTask extends AsyncTask<Void, String, Void> {
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
            // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력

            cursor = dbHelper2.saving_drivedata_connect(useridintent);
            while (cursor.moveToNext()) {
                fetemp = cursor.getFloat(1);
                avtemp = cursor.getFloat(2);
                runttimetemp = cursor.getInt(3);
                rundistancetemp = cursor.getInt(4);
            }
            cursor.moveToFirst();
            cursor.close();


            return null;
        }
    }

    class TimeHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Cursor cursor = null;

            update_savingAsyncTask updatesavingAsync = new update_savingAsyncTask();
            if(updatesavingAsync!=null) {
                updatesavingAsync.execute();
            }
            String consumtemp = null;
            consumtemp = String.format("%10.1f", (0.625 * 0.001 * 0.004 * 4.3 * 1.35 * 1.6 * rpm * 60 * engine_load  / 20)).trim();
            float consumfloattemp = Float.parseFloat(consumtemp);

            if(consumfloattemp==0)
            {
                consumfloattemp=1;
            }
            float fe = (vehicle_speed/consumfloattemp); //현재 연비
            if(fe>15)
            {
                fe=15;
            }
            fetemp = ((fetemp*runttimetemp)+fe)/(runttimetemp+1); // 갱신될 누적평균연비
            avtemp = ((avtemp*runttimetemp)+vehicle_speed)/(runttimetemp+1); // 갱신될 누적평균속도
            runttimetemp += 1; // 갱신될 누적주행시간

            rundistancetemp= (int) (avtemp*runttimetemp*1000)/3600; // 갱신될 누적주행거리


            if(rpm>300 && vehicle_speed>0 ) {
                dbHelper2.saving_drivedata_update(useridintent,fetemp,avtemp,runttimetemp,rundistancetemp);
            }

            if(cursor!=null) {
                cursor.moveToFirst();
                cursor.close();
            }
        }

    }


    class ymdhmsHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Calendar cal;
            cal = new GregorianCalendar();
            YMD.setText(ymd);
            String timeToString;
            String hour;
            int isAMorPM = cal.get(Calendar.AM_PM);
            switch (isAMorPM) {
                case Calendar.AM:
                    hour = String.format("%02d",cal.get(Calendar.HOUR));
                    timeToString = String.format("%02d:%02d:%02d", cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND));
                    if(hour.equals("00")){
                        timeToString = String.format("12:%02d:%02d", cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND));
                    }
                    Time.setText("오전 " + timeToString);
                    break;

                case Calendar.PM:
                    hour = String.format("%02d",cal.get(Calendar.HOUR));
                    timeToString = String.format("%02d:%02d:%02d", cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND));
                    if(hour.equals("00")){
                        timeToString = String.format("12:%02d:%02d", cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND));
                    }
                    Time.setText("오후 " + timeToString);
                    break;
            }
        }
    }

    class speedHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            int hadspeed = db_speed;
            String temp90;
            temp90 = String.format("%d Km/h", hadspeed);
            cur_speed = (TextView) findViewById(R.id.speed);
            progBar = (ProgressBar) findViewById(R.id.main_progressBar);
            cur_speed.setText(temp90);
            progBar.setProgress(hadspeed);
            }
    }


    public void synchronize_db() {
        Cursor cursor=null;
        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력

        cursor = dbHelper.synch(useridintent);
        while (cursor.moveToNext()) {
            Add_AppApk1 = cursor.getString(2);
            Add_AppApk2 = cursor.getString(3);
            rayout = cursor.getInt(4);
            usercolorintent = cursor.getString(5);
            bk = cursor.getInt(6);
        }
        cursor.moveToFirst();
        cursor.close();

    }

    public void add_app_loading_main() {
        if (Add_AppApk2.length() > 6) {
            PackageManager pm = getPackageManager();
            try {
                Add_App_Icon2 = pm.getApplicationIcon(Add_AppApk2);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            Log.d("result", Add_AppApk2);
            final ImageButton plus2 = (ImageButton) findViewById(R.id.imageButton2);
            plus2.setVisibility(View.GONE);
            ListViewAdapter2 adapter4;
            ListView listView4 = (ListView) findViewById(R.id.listview4);
            listView4.setVisibility(View.VISIBLE);
            adapter4 = new ListViewAdapter2(usercolorintent);
            // 아이템 커스텀마이징(설치된 어플 리스트에서 반환된 정보를 통해 어플아이템에 반영)
            //adapter4.addItem(Add_App_Icon2, "변경", Add_AppApk2);
            adapter4.addItem(Add_App_Icon2, "제거");
            listView4.setAdapter(adapter4);
        }
        if (Add_AppApk1.length() > 6) {
            PackageManager pm = getPackageManager();
            try {
                Add_App_Icon1 = pm.getApplicationIcon(Add_AppApk1);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            final ImageButton plus1 = (ImageButton) findViewById(R.id.imageButton1);

            plus1.setVisibility(View.GONE);
            ListViewAdapter adapter2;
            ListView listView2 = (ListView) findViewById(R.id.listview2);
            listView2.setVisibility(View.VISIBLE);
            adapter2 = new ListViewAdapter(usercolorintent);
            // 아이템 커스텀마이징(설치된 어플 리스트에서 반환된 정보를 통해 어플아이템에 반영)
            //adapter2.addItem(Add_App_Icon1, "변경", Add_AppApk1);
            adapter2.addItem(Add_App_Icon1, "제거");
            listView2.setAdapter(adapter2);
        }
    }

    public void go_Setting(View v) {
        Intent home = new Intent(MainActivity.this, Float_Home.class);
        home.putExtra("id", useridintent);
        home.putExtra("name", usernameintent);
        startService(home);

        Intent intent = new Intent(MainActivity.this, SettingActivity.class);
        intent.putExtra("id", useridintent);
        intent.putExtra("name", usernameintent);
        startActivity(intent);
    }

    public void go_Analysis(View v) {
        Intent home = new Intent(MainActivity.this, Float_Home.class);
        home.putExtra("id", useridintent);
        home.putExtra("name", usernameintent);
        startService(home);

        Intent intent = new Intent(MainActivity.this, AnalysisActivity.class);
        intent.putExtra("id", useridintent);
        intent.putExtra("name", usernameintent);
        intent.putExtra("color", usercolorintent);
        startActivity(intent);
    }

    public void addapp_Click(View v) {
        Intent home = new Intent(MainActivity.this, Float_Home.class);
        home.putExtra("id", useridintent);
        home.putExtra("name", usernameintent);
        startService(home);
        PackageManager packageManager = getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(Add_AppApk1);
        startActivity(intent);
    }


    public void addapp_Click2(View v) {
        Intent home = new Intent(MainActivity.this, Float_Home.class);
        home.putExtra("id", useridintent);
        home.putExtra("name", usernameintent);
        startService(home);

        PackageManager packageManager = getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(Add_AppApk2);
        startActivity(intent);
    }

    public void addapp1_delete(View v) {
        Add_App_Icon1 = null;
        Intent home = new Intent(MainActivity.this, Float_Home.class);
        home.putExtra("App2", Add_AppApk2);
        startService(home);

        Add_AppApk1 = null;
        int idtemp = Integer.parseInt(useridintent);
        dbHelper.addapk1_set(idtemp, Add_AppApk1);

        ImageButton plus1 = (ImageButton) findViewById(R.id.imageButton1);
        plus1.setVisibility(View.VISIBLE);
        ListView listView2 = (ListView) findViewById(R.id.listview2);
        listView2.setVisibility(View.GONE);
    }

    public void addapp2_delete(View v) {
        Add_App_Icon2 = null;
        Intent home = new Intent(MainActivity.this, Float_Home.class);
        home.putExtra("App1", Add_AppApk1);
        startService(home);

        Add_AppApk2 = null;
        int idtemp = Integer.parseInt(useridintent);
        dbHelper.addapk2_set(idtemp, Add_AppApk2);

        ImageButton plus2 = (ImageButton) findViewById(R.id.imageButton2);
        plus2.setVisibility(View.VISIBLE);
        ListView listView4 = (ListView) findViewById(R.id.listview4);
        listView4.setVisibility(View.GONE);
    }

    void SideInit() {
        TableLayout Side = (TableLayout) findViewById(R.id.Side);
        Side.setBackgroundResource(R.drawable.back_side);

        YMD = (TextView) findViewById(R.id.YMD);
        Time = (TextView) findViewById(R.id.Time);
        TextView Speed = (TextView) findViewById(speed);

        Calendar cal = Calendar.getInstance();
        int temp = cal.get(Calendar.MONTH) + 1;
        YMD.setText(cal.get(Calendar.YEAR) + "년 " + temp + "월 " + cal.get(Calendar.DATE) + "일");

        String timeToString;

        int isAMorPM = cal.get(Calendar.AM_PM);
        switch (isAMorPM) {
            case Calendar.AM:
                timeToString = String.format("%02d:%02d:%02d", cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND));
                Time.setText("오전 " + timeToString);
                break;

            case Calendar.PM:
                timeToString = String.format("%02d:%02d:%02d", cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND));
                Time.setText("오후 " + timeToString);
                break;
        }


        //★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★//
        YMD.setTextColor(Color.parseColor(usercolorintent));
        Time.setTextColor(Color.parseColor(usercolorintent));
        Speed.setTextColor(Color.parseColor(usercolorintent));
        //★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★//

    }

    void MainInit(int rayout, int bk) {
        LinearLayout mylayout;
        if (rayout == 1) {
            mylayout = (LinearLayout) findViewById(R.id.activity_main);
        } else {
            mylayout = (LinearLayout) findViewById(R.id.activity_main2);
        }

        switch (bk) {
            case 0:
                mylayout.setBackgroundResource(R.drawable.ryan);
                break;
            case 1:
                mylayout.setBackgroundResource(R.drawable.bk1);
                break;
            case 2:
                mylayout.setBackgroundResource(R.drawable.bk2);
                break;
            case 3:
                mylayout.setBackgroundResource(R.drawable.bk3);
                break;
            case 4:
                mylayout.setBackgroundResource(R.drawable.bk4);
                break;
            case 5:
                mylayout.setBackgroundResource(R.drawable.bk5);
                break;
            case 6:
                mylayout.setBackgroundResource(R.drawable.bk6);
                break;
            case 7:
                mylayout.setBackgroundResource(R.drawable.bk7);
                break;
        }

        ///즐겨찾기 추가갯수

        ListView listview1;
        ListView listview2;
        ListView listview3;
        ListView listview4;
        ListView listview5;

        NavListViewAdapter adapter1;
        ListViewAdapter adapter2;
        DriveListViewAdapter adapter3;
        ListViewAdapter adapter4;
        UiListViewAdapter adapter5;

        // Adapter 생성
        adapter1 = new NavListViewAdapter(usercolorintent); // 네비
        adapter2 = new ListViewAdapter(usercolorintent); //추가1
        adapter3 = new DriveListViewAdapter(usercolorintent); // 주행기록
        adapter4 = new ListViewAdapter(usercolorintent); //추가2
        adapter5 = new UiListViewAdapter(usercolorintent); // 사용자정의 UI

        ImageButton plus1 = (ImageButton) findViewById(R.id.imageButton1);
        findViewById(R.id.imageButton1).setOnClickListener(first);

        ImageButton plus2 = (ImageButton) findViewById(R.id.imageButton2);
        findViewById(R.id.imageButton2).setOnClickListener(second);
        ///////////////플러스 버튼 어플 추가갯수에 따라 보이기 안보이기
        adapter1.addItem(ContextCompat.getDrawable(this, R.drawable.nav),
                "  내비게이션 선택", "  내비게이션 실행");
        adapter3.addItem(ContextCompat.getDrawable(this, R.drawable.drive),
                "  실시간주행기록", "  주간주행기록");
        adapter5.addItem(ContextCompat.getDrawable(this, R.drawable.set),
                "  레이아웃 구성 선택", "  글자색 및 배경설정");

        // 리스트뷰 참조 및 Adapter달기
        listview1 = (ListView) findViewById(R.id.listview1);
        listview1.setAdapter(adapter1);
        listview3 = (ListView) findViewById(R.id.listview3);
        listview3.setAdapter(adapter3);
        listview5 = (ListView) findViewById(R.id.listview5);
        listview5.setAdapter(adapter5);

    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////네비다이얼로그
    public void navClick(View v) {
        show(v);
    }

    void show(View v) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.nav_appfolder, null);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
        //★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★//
        //각각의어플 아이콘 실행동작 리스너
        //Drawable test = ContextCompat.getDrawable(v.getContext(), test);
        TextView navname1 = (TextView) view.findViewById(R.id.navname1);
        navname1.setTextColor(Color.parseColor(usercolorintent));
        TextView navname2 = (TextView) view.findViewById(R.id.navname2);
        navname2.setTextColor(Color.parseColor(usercolorintent));
        TextView navname3 = (TextView) view.findViewById(R.id.navname3);
        navname3.setTextColor(Color.parseColor(usercolorintent));
        TextView navname4 = (TextView) view.findViewById(R.id.navname4);
        navname4.setTextColor(Color.parseColor(usercolorintent));

        //★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★//

        ImageView.OnClickListener onClickListener = new ImageView.OnClickListener() {
            @Override
            public void onClick(View view) {
                PackageManager packageManager = getPackageManager();
                Intent home = new Intent(MainActivity.this, Float_Home.class);
                home.putExtra("id", useridintent);
                home.putExtra("name", usernameintent);
                startService(home);
                Intent intent = new Intent(MainActivity.this, AlwaysTopServiceTouch.class);
                switch (view.getId()) {
                    case R.id.navicon1:
                        Intent intent0 = packageManager.getLaunchIntentForPackage("com.locnall.KimGiSa");
                        intent.putExtra("id", useridintent);
                        intent.putExtra("name", usernameintent);
                        intent.putExtra("num", 1);
                        startService(intent);
                        if (null != intent0) {
                            startActivity(intent0);
                            dialog.dismiss();
                        } else {
                            Intent intenta = new Intent(Intent.ACTION_VIEW);
                            intenta.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.locnall.KimGiSa&hl=ko"));
                            startActivity(intenta);
                            dialog.dismiss();
                        }

                        break;
                    case R.id.navicon2:
                        Intent intent1 = packageManager.getLaunchIntentForPackage("com.skt.tmap.ku");
                        intent.putExtra("id", useridintent);
                        intent.putExtra("name", usernameintent);
                        intent.putExtra("num", 2);
                        startService(intent);

                        if (null != intent1) {
                            startActivity(intent1);
                            dialog.dismiss();
                        } else {
                            Intent intenta = new Intent(Intent.ACTION_VIEW);
                            intenta.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.skt.tmap.ku&hl=ko"));
                            startActivity(intenta);
                            dialog.dismiss();
                        }
                        break;
                    case R.id.navicon3:
                        Intent intent2 = packageManager.getLaunchIntentForPackage("kr.mappers.AtlanSmart");
                        intent.putExtra("id", useridintent);
                        intent.putExtra("name", usernameintent);
                        intent.putExtra("num", 3);
                        startService(intent);

                        if (null != intent2) {
                            startActivity(intent2);
                            dialog.dismiss();
                        } else {
                            Intent intenta = new Intent(Intent.ACTION_VIEW);
                            intenta.setData(Uri.parse("https://play.google.com/store/apps/details?id=kr.mappers.AtlanSmart&hl=ko"));
                            startActivity(intenta);
                            dialog.dismiss();
                        }
                        break;
                    case R.id.navicon4:
                        Intent intent3 = packageManager.getLaunchIntentForPackage("com.thinkware.inaviair");
                        intent.putExtra("id", useridintent);
                        intent.putExtra("name", usernameintent);
                        intent.putExtra("num", 4);
                        startService(intent);

                        if (null != intent3) {
                            startActivity(intent3);
                            dialog.dismiss();
                        } else {
                            Intent intenta = new Intent(Intent.ACTION_VIEW);
                            intenta.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.thinkware.inaviair&hl=ko"));
                            startActivity(intenta);
                            dialog.dismiss();
                        }
                        break;
                }
            }
        };
        ImageView nav1 = (ImageView) view.findViewById(R.id.navicon1);
        nav1.setOnClickListener(onClickListener);
        ImageView nav2 = (ImageView) view.findViewById(R.id.navicon2);
        nav2.setOnClickListener(onClickListener);
        ImageView nav3 = (ImageView) view.findViewById(R.id.navicon3);
        nav3.setOnClickListener(onClickListener);
        ImageView nav4 = (ImageView) view.findViewById(R.id.navicon4);
        nav4.setOnClickListener(onClickListener);


        builder.show();
    }

    //////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////플러스버튼 리스너
    ImageButton.OnClickListener first = new View.OnClickListener() {
        public void onClick(View v) {

            // 첫번째 플러스 버튼 사라짐
            final ImageButton plus1 = (ImageButton) findViewById(R.id.imageButton1);
            //plus1.setVisibility(View.GONE);

            //다이얼로그 만들기
            final AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(R.layout.add_existing_applist, null);

            ArrayList<AppInfo> mListData = new ArrayList<AppInfo>();
            PackageManager pm;
            List<ApplicationInfo> mAppList = null;
            String TAG = "ContentValues";
            Context mContext = v.getContext();

            mAdapter = new AddappListViewadapter(view.getContext());
            mListView = (ListView) view.findViewById(R.id.existing_applist);
            mListView.setAdapter(mAdapter);
            pm = mContext.getPackageManager();
            // 설치된 어플리케이션 취득
            mAppList = pm.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES
                    | PackageManager.GET_DISABLED_COMPONENTS);

            AppInfo.AppFilter filter;
            switch (MENU_MODE) {
                case MENU_DOWNLOAD:
                    filter = AppInfo.THIRD_PARTY_FILTER;
                    break;
                default:
                    filter = null;
                    break;
            }
            if (filter != null) {
                filter.init();
            }
            // 기존 데이터 초기화
            mListData.clear();
            AppInfo addInfo = null;
            ApplicationInfo info = null;
            for (ApplicationInfo app : mAppList) {
                info = app;
                if (filter == null || filter.filterApp(info)) {
                    // 필터된 데이터
                    // 음악 관련 어플만 필터
                    if(     app.packageName.equals("com.iloen.melon")||
                            app.packageName.equals("com.ktmusic.geniemusic")||
                            app.packageName.equals("com.nhn.android.music ")||
                            app.packageName.equals("com.zentertain.freemusic")||
                            app.packageName.equals("com.mnet.app")||
                            app.packageName.equals("com.neowiz.android.bugs"))
                        mAdapter.addItem(app.loadIcon(pm), app.loadLabel(pm).toString(), app.packageName);
                }
            }
            // 알파벳 이름으로 소트(한글, 영어)
            Collections.sort(mListData, AppInfo.ALPHA_COMPARATOR);
            //////////////////////////////////////////////////////

            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView parent, View v, int position, long id) {
                    // get item
                    AppInfo item = (AppInfo) mAdapter.getItem(position);
                    String sel_name = item.getTitle();
                    Add_App_Icon1 = item.getIcon();
                    Add_AppName1 = item.getTitle();
                    Add_AppApk1 = item.getApk();

                    Toast toast = Toast.makeText(getApplicationContext(), sel_name, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    // TODO : use item data.
                }
            });

            // 다이얼로그에 뷰적용
            builder.setView(view);
            AlertDialog dialog = builder.create();
            //영역밖에 누르면 다이얼로그 꺼짐
            dialog.setCanceledOnTouchOutside(true);

            //ok버트 리스너 (추가버튼으로 바꾸셈)
            builder.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            if (Add_App_Icon1 != null) {
                                plus1.setVisibility(View.GONE);
                                ListViewAdapter adapter2;
                                ListView listView2 = (ListView) findViewById(listview2);
                                listView2.setVisibility(View.VISIBLE);
                                adapter2 = new ListViewAdapter(usercolorintent);
                                // 아이템 커스텀마이징(설치된 어플 리스트에서 반환된 정보를 통해 어플아이템에 반영)
                                //adapter2.addItem(Add_App_Icon1, "변경", Add_AppApk1);
                                adapter2.addItem(Add_App_Icon1, "제거");
                                listView2.setAdapter(adapter2);

                                int idtemp = Integer.parseInt(useridintent);
                                dbHelper.addapk1_set(idtemp, Add_AppApk1);

                                Intent home = new Intent(MainActivity.this, Float_Home.class);
                                home.putExtra("id", useridintent);
                                home.putExtra("name", usernameintent);

                                startService(home);
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
            ////////////////////////////////////////////////////////////
        }
    };
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ImageButton.OnClickListener second = new View.OnClickListener() {
        public void onClick(View v) {

            // 첫번째 플러스 버튼 사라짐
            final ImageButton plus2 = (ImageButton) findViewById(R.id.imageButton2);
            //plus1.setVisibility(View.GONE);

            //다이얼로그 만들기
            final AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(R.layout.add_existing_applist, null);

            ArrayList<AppInfo> mListData = new ArrayList<AppInfo>();
            PackageManager pm;
            List<ApplicationInfo> mAppList = null;
            String TAG = "ContentValues";
            Context mContext = v.getContext();

            mAdapter = new AddappListViewadapter(view.getContext());
            mListView = (ListView) view.findViewById(R.id.existing_applist);
            mListView.setAdapter(mAdapter);
            pm = mContext.getPackageManager();
            // 설치된 어플리케이션 취득
            mAppList = pm.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES
                    | PackageManager.GET_DISABLED_COMPONENTS);

            AppInfo.AppFilter filter;
            switch (MENU_MODE) {
                case MENU_DOWNLOAD:
                    filter = AppInfo.THIRD_PARTY_FILTER;
                    break;
                default:
                    filter = null;
                    break;
            }
            if (filter != null) {
                filter.init();
            }
            // 기존 데이터 초기화
            mListData.clear();
            AppInfo addInfo = null;
            ApplicationInfo info = null;
            for (ApplicationInfo app : mAppList) {
                info = app;
                if (filter == null || filter.filterApp(info)) {
                    // 필터된 데이터
                    mAdapter.addItem(app.loadIcon(pm), app.loadLabel(pm).toString(), app.packageName);
                }
            }
            // 알파벳 이름으로 소트(한글, 영어)
            Collections.sort(mListData, AppInfo.ALPHA_COMPARATOR);
            //////////////////////////////////////////////////////

            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView parent, View v, int position, long id) {
                    // get item
                    AppInfo item = (AppInfo) mAdapter.getItem(position);
                    String sel_name = item.getTitle();
                    Add_App_Icon2 = item.getIcon();
                    Add_AppName2 = item.getTitle();
                    Add_AppApk2 = item.getApk();

                    Toast toast = Toast.makeText(getApplicationContext(), sel_name, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    // TODO : use item data.
                }
            });

            // 다이얼로그에 뷰적용
            builder.setView(view);
            AlertDialog dialog = builder.create();
            //영역밖에 누르면 다이얼로그 꺼짐
            dialog.setCanceledOnTouchOutside(true);

            //ok버트 리스너 (추가버튼으로 바꾸셈)
            builder.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            if (Add_App_Icon2 != null) {
                                plus2.setVisibility(View.GONE);

                                ListViewAdapter2 adapter4;
                                ListView listView4 = (ListView) findViewById(listview4);
                                listView4.setVisibility(View.VISIBLE);
                                adapter4 = new ListViewAdapter2(usercolorintent);
                                adapter4.addItem(Add_App_Icon2, "제거");
                                listView4.setAdapter(adapter4);

                                int idtemp = Integer.parseInt(useridintent);
                                dbHelper.addapk2_set(idtemp, Add_AppApk2);

                                Intent home = new Intent(MainActivity.this, Float_Home.class);
                                home.putExtra("id", useridintent);
                                home.putExtra("name", usernameintent);

                                startService(home);
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
            ////////////////////////////////////////////////////////////
        }
    };



    //////////////////////////////////////////////////////OBD관련////////////////////////////////////////////////
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        this.menu = menu;

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.menu_connect_bt:

                handler2 = new TimeHandler();
                db_runtime(useridintent,rpm,engine_load,vehicle_speed);
                handler3 = new speedHandler();
                update_speed(useridintent);

                if (!mBluetoothAdapter.isEnabled()) {
                    Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
                    return false;
                }

                if (mBtService == null) setupChat();

                if (item.getTitle().equals("ConnectBT")) {
                    // Launch the DeviceListActivity to see devices and do scan
                    serverIntent = new Intent(this, DeviceListActivity.class);
                    startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
                } else {
                    if (mBtService != null)
                    {
                        mBtService.stop();
                        item.setTitle(R.string.connectbt);
                    }
                }

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == MainActivity.RESULT_OK) {
                    connectDevice(data);
                }
                break;

            case REQUEST_ENABLE_BT:

                if (mBtService == null) setupChat();

                if (resultCode == MainActivity.RESULT_OK) {
                    serverIntent = new Intent(this, DeviceListActivity.class);
                    startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
                } else {
                    Toast.makeText(getApplicationContext(), "BT device not enabled", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    ///////////////////////////////////////////////////////////////////////

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        setDefaultOrientation();
    }

    @Override
    public synchronized void onResume() {
        super.onResume();
        getPreferences();
    }

    @Override
    public synchronized void onPause() {
        super.onPause();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mBtService != null) mBtService.stop();
        wl.release();
    }


    @Override
    public void onStart() {
        super.onStart();
        getPreferences();
        setDefaultOrientation();
        resetvalues();

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (!commandmode) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setMessage("Are you sure you want exit?");
                alertDialogBuilder.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                exit();
                            }
                        });

                alertDialogBuilder.setNegativeButton("cancel",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            } else {
                commandmode = false;
                sendEcuMessage(S_VOLTAGE);
            }

            return false;
        }

        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if (mBtService != null) mBtService.stop();
        wl.release();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    private void getPreferences() {

        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(getBaseContext());

        FaceColor = Integer.parseInt(preferences.getString("FaceColor", "0"));

        Enginedisplacement = Integer.parseInt(preferences.getString("Enginedisplacement", "1600"));
        Enginetype = Integer.parseInt(preferences.getString("EngineType", "1"));

        Enginedisplacement = Enginedisplacement / 1000;

        //Toast.makeText(this,String.valueOf(Enginedisplacement),Toast.LENGTH_SHORT).show();

        m_dedectPids = Integer.parseInt(preferences.getString("DedectPids", "0"));

        if (m_dedectPids == 0) {

            commandslist.clear();

            int i = 0;

            commandslist.add(0, S_ENGINE_LOAD);
            commandslist.add(1, S_INTAKE_AIR_TEMP);
            commandslist.add(2, S_Fuel_Tank_Level_Input);
            commandslist.add(3, S_ENGINE_RPM);
            commandslist.add(4, S_VEHICLE_SPEED);
            commandslist.add(5, S_RUN_TIME_SINCE_ENGINE_START);
            commandslist.add(6, S_ENGINE_COOLANT_TEMP);
            commandslist.add(7, S_VOLTAGE);


            whichCommand = 0;
        }
    }

    private void setDefaultOrientation() {

        try {

            settextsixe();

        } catch (Exception e) {
        }
    }

    private void settextsixe() {
        int txtsize = 14;
        int sttxtsize = 12;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

    }


    public void resetvalues() {

        m_getPids = false;
        whichCommand = 0;
        trycount = 0;
        initialized = false;
        defaultStart = false;
        avgconsumption.clear();
        mConversationArrayAdapter.clear();

        sendEcuMessage(RESET);
    }

    private void connectDevice(Intent data) {
        tryconnect = true;
        // Get the device MAC address
        String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        // Get the BluetoothDevice object
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        try {
            // Attempt to connect to the device
            mBtService.connect(device);
            currentdevice = device;

        } catch (Exception e) {
        }
    }

    private void setupChat() {

        // Initialize the BluetoothChatService to perform bluetooth connections
        mBtService = new BluetoothService(this, mBtHandler);

    }

    private void sendEcuMessage(String message) {

        if (mBtService != null)
        {
            // Check that we're actually connected before trying anything
            if (mBtService.getState() != BluetoothService.STATE_CONNECTED) {
                //Toast.makeText(this, R.string.not_connected, Toast.LENGTH_LONG).show();
                return;
            }
            try {
                if (message.length() > 0) {

                    message = message + "\r";
                    // Get the message bytes and tell the BluetoothChatService to write
                    byte[] send = message.getBytes();
                    mBtService.write(send);
                }
            } catch (Exception e) {
            }
        }
    }

    private void sendInitCommands() {
        if (initializeCommands.length != 0) {

            if (whichCommand < 0) {
                whichCommand = 0;
            }

            String send = initializeCommands[whichCommand];
            sendEcuMessage(send);

            if (whichCommand == initializeCommands.length - 1) {
                initialized = true;
                whichCommand = 0;
                sendDefaultCommands();
            } else {
                whichCommand++;
            }
        }
    }

    private void sendDefaultCommands() {

        if (commandslist.size() != 0) {

            if (whichCommand < 0) {
                whichCommand = 0;
            }

            String send = commandslist.get(whichCommand);
            sendEcuMessage(send);

            if (whichCommand >= commandslist.size() - 1) {
                whichCommand = 0;
            } else {
                whichCommand++;
            }
        }
    }

    private String clearMsg(Message msg) {
        String tmpmsg = msg.obj.toString();

        tmpmsg = tmpmsg.replace("null", "");
        tmpmsg = tmpmsg.replaceAll("\\s", ""); //removes all [ \t\n\x0B\f\r]
        tmpmsg = tmpmsg.replaceAll(">", "");
        tmpmsg = tmpmsg.replaceAll("SEARCHING...", "");
        tmpmsg = tmpmsg.replaceAll("ATZ", "");
        tmpmsg = tmpmsg.replaceAll("ATI", "");
        tmpmsg = tmpmsg.replaceAll("atz", "");
        tmpmsg = tmpmsg.replaceAll("ati", "");
        tmpmsg = tmpmsg.replaceAll("ATDP", "");
        tmpmsg = tmpmsg.replaceAll("atdp", "");
        tmpmsg = tmpmsg.replaceAll("ATRV", "");
        tmpmsg = tmpmsg.replaceAll("atrv", "");

        return tmpmsg;
    }

    private void checkPids(String tmpmsg) {
        if (tmpmsg.indexOf("41") != -1) {
            int index = tmpmsg.indexOf("41");

            String pidmsg = tmpmsg.substring(index, tmpmsg.length());

            if (pidmsg.contains("4100")) {

                setPidsSupported(pidmsg);
                return;
            }
        }
    }

    private void analysMsg(Message msg) {

        String tmpmsg = clearMsg(msg);

        generateVolt(tmpmsg);

        getElmInfo(tmpmsg);

        if (!initialized) {

            sendInitCommands();

        } else {

            checkPids(tmpmsg);

            if (!m_getPids && m_dedectPids == 1) {
                String sPIDs = "0100";
                sendEcuMessage(sPIDs);
                return;
            }

            if (commandmode) {
                getFaultInfo(tmpmsg);
                return;
            }

            try {

                analysPIDS(tmpmsg);
            } catch (Exception e) {
            }

            sendDefaultCommands();
        }
    }

    private void getFaultInfo(String tmpmsg) {

        String substr = "43";

        int index = tmpmsg.indexOf(substr);

        if (index == -1)
        {
            substr = "47";
            index = tmpmsg.indexOf(substr);
        }

        if (index != -1) {

            tmpmsg = tmpmsg.substring(index, tmpmsg.length());

            if (tmpmsg.substring(0, 2).equals(substr)) {

                performCalculations(tmpmsg);

                String faultCode = null;
                String faultDesc = null;

                if (troubleCodesArray.size() > 0) {

                    for (int i = 0; i < troubleCodesArray.size(); i++) {
                        faultCode = troubleCodesArray.get(i);

                        Log.e(TAG, "Fault Code: " + substr + " : " + faultCode + " desc: " + faultDesc);

                        if (faultCode != null && faultDesc != null) {
                            mConversationArrayAdapter.add(mConnectedDeviceName + ":  TroubleCode -> " + faultCode + "\n" + faultDesc);

                        } else if (faultCode != null && faultDesc == null) {
                            mConversationArrayAdapter.add(mConnectedDeviceName + ":  TroubleCode -> " + faultCode +
                                    "\n" + "Definition not found for code: " + faultCode);
                        }
                    }
                } else {
                    faultCode = "No error found...";
                    mConversationArrayAdapter.add(mConnectedDeviceName + ":  TroubleCode -> " + faultCode);
                }
            }
        }
    }

    protected void performCalculations(String fault) {

        final String result = fault;
        String workingData = "";
        int startIndex = 0;
        troubleCodesArray.clear();

        try{

            if(result.indexOf("43") != -1)
            {
                workingData = result.replaceAll("^43|[\r\n]43|[\r\n]", "");
            }else if(result.indexOf("47") != -1)
            {
                workingData = result.replaceAll("^47|[\r\n]47|[\r\n]", "");
            }

            for (int begin = startIndex; begin < workingData.length(); begin += 4) {
                String dtc = "";
                byte b1 = hexStringToByteArray(workingData.charAt(begin));
                int ch1 = ((b1 & 0xC0) >> 6);
                int ch2 = ((b1 & 0x30) >> 4);
                dtc += dtcLetters[ch1];
                dtc += hexArray[ch2];
                dtc += workingData.substring(begin + 1, begin + 4);

                if (dtc.equals("P0000")) {
                    continue;
                }

                troubleCodesArray.add(dtc);
            }
        }catch(Exception e)
        {
            Log.e(TAG, "Error: " + e.getMessage());
        }
    }

    private byte hexStringToByteArray(char s) {
        return (byte) ((Character.digit(s, 16) << 4));
    }

    private void getElmInfo(String tmpmsg) {

        if (tmpmsg.contains("ELM") || tmpmsg.contains("elm")) {
            devicename = tmpmsg;
        }

        if (tmpmsg.contains("SAE") || tmpmsg.contains("ISO")
                || tmpmsg.contains("sae") || tmpmsg.contains("iso") || tmpmsg.contains("AUTO")) {
            deviceprotocol = tmpmsg;
        }

        if (deviceprotocol != null && devicename != null) {
            devicename = devicename.replaceAll("STOPPED", "");
            deviceprotocol = deviceprotocol.replaceAll("STOPPED", "");
        }
    }


    private void setPidsSupported(String buffer) {

        trycount++;

        StringBuilder flags = new StringBuilder();
        String buf = buffer.toString();
        buf = buf.trim();
        buf = buf.replace("\t", "");
        buf = buf.replace(" ", "");
        buf = buf.replace(">", "");

        if (buf.indexOf("4100") == 0 || buf.indexOf("4120") == 0) {

            for (int i = 0; i < 8; i++) {
                String tmp = buf.substring(i + 4, i + 5);
                int data = Integer.valueOf(tmp, 16).intValue();
//                String retStr = Integer.toBinaryString(data);
                if ((data & 0x08) == 0x08) {
                    flags.append("1");
                } else {
                    flags.append("0");
                }

                if ((data & 0x04) == 0x04) {
                    flags.append("1");
                } else {
                    flags.append("0");
                }

                if ((data & 0x02) == 0x02) {
                    flags.append("1");
                } else {
                    flags.append("0");
                }

                if ((data & 0x01) == 0x01) {
                    flags.append("1");
                } else {
                    flags.append("0");
                }
            }

            commandslist.clear();
            //commandslist.add(7, S_VOLTAGE);
            int pid = 1;

            StringBuilder supportedPID = new StringBuilder();
            supportedPID.append("Supported PIDS:\n");
            for (int j = 0; j < flags.length(); j++) {
                if (flags.charAt(j) == '1') {
                    supportedPID.append(" " + PIDS[j] + " ");
                    if (!PIDS[j].contains("11") && !PIDS[j].contains("01") && !PIDS[j].contains("20")) {
                        commandslist.add(pid, "01" + PIDS[j]);
                        pid++;
                    }
                }
            }

            m_getPids = true;
            mConversationArrayAdapter.add(mConnectedDeviceName + ": " + supportedPID.toString());
            whichCommand = 0;
            sendEcuMessage("ATRV");

        } else {

            return;
        }
    }

    private double calculateAverage(List<Double> listavg) {
        Double sum = 0.0;
        for (Double val : listavg) {
            sum += val;
        }
        return sum.doubleValue() / listavg.size();
    }

    private void analysPIDS(String dataRecieved) {

        int A = 0;
        int B = 0;
        int PID = 0;

        if ((dataRecieved != null) && (dataRecieved.matches("^[0-9A-F]+$"))) {

            dataRecieved = dataRecieved.trim();

            int index = dataRecieved.indexOf("41");

            String tmpmsg = null;

            if (index != -1) {

                tmpmsg = dataRecieved.substring(index, dataRecieved.length());

                if (tmpmsg.substring(0, 2).equals("41")) {

                    PID = Integer.parseInt(tmpmsg.substring(2, 4), 16);
                    A = Integer.parseInt(tmpmsg.substring(4, 6), 16);
                    if(tmpmsg.length()>11) {
                        B = Integer.parseInt(tmpmsg.substring(6, 8), 16);
                    }

                    calculateEcuValues(PID, A, B);
                }
            }
        }
    }

    private void generateVolt(String msg) {

        String VoltText = null;

        if ((msg != null) && (msg.matches("\\s*[0-9]{1,2}([.][0-9]{1,2})?V\\s*"))) {

            VoltText = msg;
            battery = VoltText;
            mConversationArrayAdapter.add(mConnectedDeviceName + ": " + msg);
            float temp = Float.parseFloat(msg.substring(0, 2));
            int temp2 = (int) temp;
            dbHelper.temp_drivedata_update(useridintent,6,temp2);
        }

        //testlist[7] = Integer.parseInt(msg);

    }

    private void calculateEcuValues(int PID, int A, int B) {

        double val = 0;
        int intval = 0;
        int tempC = 0;

        switch (PID) {

            case 4://PID(04): Engine Load

                // A*100/255
                val = A * 100 / 255;
                int calcLoad = (int) val;

                mConversationArrayAdapter.add("Engine Load: " + Integer.toString(calcLoad) + " %");

                //엔진부화율
                engine_load = calcLoad;
                dbHelper.temp_drivedata_update(useridintent,1,engine_load);


                break;

            case 5://PID(05): Coolant Temperature

                // A-40
                tempC = A - 40;
                engine_coolant_temp = tempC;
                mConversationArrayAdapter.add("Enginetemp: " + Integer.toString(tempC) + " C°");
                dbHelper.temp_drivedata_update(useridintent,5,engine_coolant_temp);

                break;


            case 12: //PID(0C): RPM

                //((A*256)+B)/4
                val = ((A * 256) + B) / 4;
                intval = (int) val;
                rpmval = intval;
                // rpm
                rpm = rpmval;
                dbHelper.temp_drivedata_update(useridintent,4,rpm);
                break;


            case 13://PID(0D): KM

                // A
                vehicle_speed = A;
                dbHelper.temp_drivedata_update(useridintent,2,vehicle_speed);
                break;

            case 15://PID(0F): Intake Temperature

                // A - 40
                tempC = A - 40;
                air_intake = tempC;
                mConversationArrayAdapter.add("Intakeairtemp: " + Integer.toString(intakeairtemp) + " C°");
                dbHelper.temp_drivedata_update(useridintent,3,air_intake);
                //흡기온도
                break;



            default:
        }
    }
}
