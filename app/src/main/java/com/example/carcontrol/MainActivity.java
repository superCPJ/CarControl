package com.example.carcontrol;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Delayed;

public class MainActivity extends AppCompatActivity {
    private BluetoothController mController = new BluetoothController();
    private List<BluetoothDevice> mDeviceList = new ArrayList<>();
    private List<BluetoothDevice> mBondedDeviceList = new ArrayList<>();
    private ListView mListView;
    private DeviceAdapter mAdapter;
    private Toast mToast;
    private Handler mUIHandler = new MyHandler();
    private AcceptThread mAcceptThread;
    private ConnectThread mConnectThread;
    private boolean ConnectFlag = false;
    private ImageButton imageButtonqianjin,imageButtonhoutui,imageButtonzuozhuan,imageButtonyouzhuan;
    private Button buttonsousuo, buttonchakan,buttonchange, button1, button2, button3;
    private TextView textViewmodle, textViewtishi;
    private ImageView imageViewxiaoche, imageViewzhangai, imageViewredqian2,imageViewredzuo, imageViewredyou, imageViewredhou,imageViewqidong;
    int viewflag = 0, youzhuanflag = 0, zuozhuanflag = 0;
    private MediaPlayer music1;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
        //Listening();
        imageButtonqianjin.setVisibility(View.GONE);
        imageButtonhoutui.setVisibility(View.GONE);
        imageButtonzuozhuan.setVisibility(View.GONE);
        imageButtonyouzhuan.setVisibility(View.GONE);
        button1.setVisibility(View.GONE);
        button2.setVisibility(View.GONE);
        button3.setVisibility(View.GONE);
        textViewmodle.setVisibility(View.GONE);
        textViewtishi.setVisibility(View.VISIBLE);
        ///////////定位权限
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},200);
        }
        IntentFilter filter = new IntentFilter();
        //开始查找
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        //结束查找
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        //查找设备
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        //设备扫描状态改变
        filter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        //绑定状态
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);

        registerReceiver(mReceiver,filter);
        mController.turnOnBluetooth(this,0);
        ////////////////////////////////////////////////////////////
        mBondedDeviceList = mController.getBondedDeviceList();
        mAdapter.refresh(mBondedDeviceList);
        mListView.setOnItemClickListener(bondedDeviceClick);
        imageButtonqianjin.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        say("W");
                        //showToast("加油门");
                        //textViewmodle.setText("前进");
                        //imageViewxiaoche.setImageResource(R.drawable.ic_qianjin);
                        break;
                    case MotionEvent.ACTION_UP:
                        say("O");
                        //showToast("松油门");
                        //textViewmodle.setText("停车");
                        //imageViewxiaoche.setImageResource(R.drawable.ic_tingche);
                        break;
                }
                return true;
            }
        });
        imageButtonhoutui.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        say("S");
                        //showToast("停车");
                        //textViewmodle.setText("后退");
                        //imageViewxiaoche.setImageResource(R.drawable.ic_houtui);
                        break;
                    case MotionEvent.ACTION_UP:
                        say("O");
                        //showToast("松刹车");
                        //textViewmodle.setText("停车");
                        //imageViewxiaoche.setImageResource(R.drawable.ic_tingche);
                        break;
                }
                return true;
            }
        });
        imageButtonzuozhuan.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        say("A");
                        //showToast("左转");
                        //textViewmodle.setText("左转");
                        //imageViewxiaoche.setImageResource(R.drawable.ic_zuozhuan);
                        break;
                    case MotionEvent.ACTION_UP:
                        say("O");
                        //showToast("回正");
                        //textViewmodle.setText("停车");
                        //imageViewxiaoche.setImageResource(R.drawable.ic_tingche);
                        break;
                }
                return true;
            }
        });
        imageButtonyouzhuan.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        say("D");
                        //showToast("右转");
                        //textViewmodle.setText("右转");
                        //imageViewxiaoche.setImageResource(R.drawable.ic_youzhuan);
                        break;
                    case MotionEvent.ACTION_UP:
                        say("O");
                        //showToast("回正");
                        //textViewmodle.setText("停车");
                        //imageViewxiaoche.setImageResource(R.drawable.ic_tingche);
                        break;
                }
                return true;
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                say("1");
                showToast("模式一");
                textViewmodle.setText("模式一");
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                say("2");
                showToast("模式二");
                textViewmodle.setText("手动模式");
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                say("3");
                showToast("模式三");
                textViewmodle.setText("智能模式");
            }
        });
    }


    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)){
                setProgressBarIndeterminateVisibility(true);
                //初始化数据列表
                mDeviceList.clear();
                mAdapter.notifyDataSetChanged();
            }else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
                setProgressBarIndeterminateVisibility(false);
            }else if (BluetoothDevice.ACTION_FOUND.equals(action)){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //找到一个添加一个
                mDeviceList.add(device);
                mAdapter.notifyDataSetChanged();
            }else if (BluetoothAdapter.ACTION_SCAN_MODE_CHANGED.equals(action)){
                int scanMode = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE,0);
                if (scanMode == BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE){
                    setProgressBarIndeterminateVisibility(true);
                }else{
                    setProgressBarIndeterminateVisibility(false);
                }
            }else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)){
                BluetoothDevice remoteDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (remoteDevice == null){
                    showToast("没有找到设备");
                    return;
                }
                int status = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE,0);
                if (status == BluetoothDevice.BOND_BONDED){
                    showToast("设备已绑定"+remoteDevice.getName());
                }else if (status == BluetoothDevice.BOND_BONDING){
                    showToast("绑定中..."+remoteDevice.getName());
                }else if (status == BluetoothDevice.BOND_NONE){
                    showToast("绑定失败"+remoteDevice.getName());
                }
            }
        }
    };

    //未绑定设备点击事件
    private AdapterView.OnItemClickListener bindDeviceClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            BluetoothDevice device = mDeviceList.get(position);
            device.createBond();
        }
    };

    //已绑定设备点击事件
    private AdapterView.OnItemClickListener bondedDeviceClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            BluetoothDevice device = mBondedDeviceList.get(position);
            if (mConnectThread != null){
                mConnectThread.cancel();
            }
            mConnectThread = new ConnectThread(device,mController.getAdapter(),mUIHandler);
            mConnectThread.start();
        }
    };

    private void initUI(){
        mListView = (ListView)findViewById(R.id.device_list);
        mAdapter = new DeviceAdapter(mDeviceList,this);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(bindDeviceClick);
        buttonsousuo = findViewById(R.id.buttonsousuo);
        buttonchakan = findViewById(R.id.buttonchakan);
        buttonchange = findViewById(R.id.buttonchange);
        imageButtonqianjin = findViewById(R.id.imageButtonqianjin);
        imageButtonhoutui = findViewById(R.id.imageButtonhoutui);
        imageButtonzuozhuan = findViewById(R.id.imageButtonzuozhuan);
        imageButtonyouzhuan = findViewById(R.id.imageButtonyouzhuan);
        button1 = findViewById(R.id.Button1);
        button2 = findViewById(R.id.Button2);
        button3 = findViewById(R.id.Button3);
        textViewmodle = findViewById(R.id.textViewmodle);
        textViewtishi = findViewById(R.id.textViewtishi);
        imageViewxiaoche = findViewById(R.id.imageViewxiaoche);
        imageViewzhangai = findViewById(R.id.imageViewzhangai);
        imageViewredqian2 = findViewById(R.id.imageViewqianred2);
        imageViewredzuo = findViewById(R.id.imageViewzuored);
        imageViewredyou = findViewById(R.id.imageViewyoured);
        imageViewredhou = findViewById(R.id.imageViewhoured);
        imageViewzhangai.setVisibility(View.GONE);
        imageViewredqian2.setVisibility(View.GONE);
        imageViewredzuo.setVisibility(View.GONE);
        imageViewredyou.setVisibility(View.GONE);
        imageViewredhou.setVisibility(View.GONE);
    }

    private class MyHandler extends Handler {
        ////////////////////接收数据
        public void handleMessage(Message message) {
            super.handleMessage(message);
            switch (message.what) {
                case Constant.MSG_GOT_DATA:
                    //showToast("data:" + message.obj.toString());
                    ////前方出现障碍
                    if ((message.obj.toString().equals("1") || message.obj.toString().equals("4")) && (viewflag == 0)){
                        imageViewzhangai.setVisibility(View.VISIBLE);
                        imageViewredqian2.setVisibility(View.VISIBLE);
                        music1 = MediaPlayer.create(getApplication(),R.raw.warning);
                        music1.start();
                    }
                    if ((message.obj.toString().equals("0")  || message.obj.toString().equals("5")) && (viewflag == 0)){
                        imageViewzhangai.setVisibility(View.GONE);
                        imageViewredqian2.setVisibility(View.GONE);
                    }
                    //右方出现障碍
                    if (message.obj.toString().equals("2") && viewflag == 0){
                        imageViewzhangai.setVisibility(View.VISIBLE);
                        imageViewredyou.setVisibility(View.VISIBLE);
                    }
                    if (message.obj.toString().equals("3") && viewflag == 0){
                        imageViewzhangai.setVisibility(View.GONE);
                        imageViewredyou.setVisibility(View.GONE);
                    }
                    //左方出现障碍
                    if (message.obj.toString().equals("6") && viewflag == 0){
                        imageViewzhangai.setVisibility(View.VISIBLE);
                        imageViewredzuo.setVisibility(View.VISIBLE);
                    }
                    if (message.obj.toString().equals("7") && viewflag == 0){
                        imageViewzhangai.setVisibility(View.GONE);
                        imageViewredzuo.setVisibility(View.GONE);
                    }


                    if (message.obj.toString().equals("Q")){
                        imageViewxiaoche.setImageResource(R.drawable.ic_qianjin);
                        textViewmodle.setText("前进");
                    }
                    if (message.obj.toString().equals("H")){
                        imageViewxiaoche.setImageResource(R.drawable.ic_houtui);
                        textViewmodle.setText("后退");
                    }
                    if (message.obj.toString().equals("Z")){
                        imageViewxiaoche.setImageResource(R.drawable.ic_zuozhuan);
                        ///////////////////左转动画
                        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(imageViewxiaoche,"rotation",0f,-90f);
                        objectAnimator.setDuration(500);
                        objectAnimator.setRepeatCount(1);
                        objectAnimator.setRepeatMode(ValueAnimator.REVERSE);
                        if (youzhuanflag == 0) {
                            objectAnimator.start();
                            youzhuanflag++;
                        }
                        //////////////////
                        textViewmodle.setText("左转");
                    }
                    if (message.obj.toString().equals("Y")){
                        imageViewxiaoche.setImageResource(R.drawable.ic_youzhuan);
                        ///////////////////左转动画
                        ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(imageViewxiaoche,"rotation",0f,90f);
                        objectAnimator2.setDuration(500);
                        objectAnimator2.setRepeatCount(1);
                        objectAnimator2.setRepeatMode(ValueAnimator.REVERSE);
                        if (youzhuanflag == 0) {
                            objectAnimator2.start();
                            youzhuanflag++;
                        }
                        //////////////////
                        textViewmodle.setText("右转");
                    }
                    if (message.obj.toString().equals("S")){
                        imageViewxiaoche.setImageResource(R.drawable.ic_tingche);
                        textViewmodle.setText("停车");
                        youzhuanflag = 0;
                        zuozhuanflag = 0;
                    }
//                    byte[] readBuf = (byte[])message.obj;
//                    String readMessage = new String(readBuf, 0, message.arg1);
//                    textViewmodle.setText(readMessage);
//                    showToast(readMessage);
                    break;
                case Constant.MSG_ERROR:
                    showToast("error:" + String.valueOf(message.obj));
                    imageButtonqianjin.setVisibility(View.GONE);
                    imageButtonhoutui.setVisibility(View.GONE);
                    imageButtonzuozhuan.setVisibility(View.GONE);
                    imageButtonyouzhuan.setVisibility(View.GONE);
                    button1.setVisibility(View.GONE);
                    button2.setVisibility(View.GONE);
                    button3.setVisibility(View.GONE);
                    textViewmodle.setVisibility(View.GONE);
                    textViewtishi.setVisibility(View.VISIBLE);
                    break;
                case Constant.MSG_CONNECTED_TO_SERVER:
                    showToast("连接到服务端");
                    imageButtonqianjin.setVisibility(View.VISIBLE);
                    imageButtonhoutui.setVisibility(View.VISIBLE);
                    imageButtonzuozhuan.setVisibility(View.VISIBLE);
                    imageButtonyouzhuan.setVisibility(View.VISIBLE);
                    button1.setVisibility(View.VISIBLE);
                    button2.setVisibility(View.VISIBLE);
                    button3.setVisibility(View.VISIBLE);
                    textViewmodle.setVisibility(View.VISIBLE);
                    textViewtishi.setVisibility(View.GONE);
                    break;
                case Constant.MSG_GOT_A_CLINET:
                    showToast("找到服务端");
                    imageButtonqianjin.setVisibility(View.VISIBLE);
                    imageButtonhoutui.setVisibility(View.VISIBLE);
                    imageButtonzuozhuan.setVisibility(View.VISIBLE);
                    imageButtonyouzhuan.setVisibility(View.VISIBLE);
                    button1.setVisibility(View.VISIBLE);
                    button2.setVisibility(View.VISIBLE);
                    button3.setVisibility(View.VISIBLE);
                    textViewmodle.setVisibility(View.VISIBLE);
                    textViewtishi.setVisibility(View.GONE);
                    break;
            }
        }
    }

    private void showToast(String text){
        if (mToast == null){
            mToast = Toast.makeText(this,text,Toast.LENGTH_SHORT);
        }else{
            mToast.setText(text);
        }
        mToast.show();
    }

    //判断当前设备是否支持蓝牙
    public void isSupportBluetooth(View view){
        boolean ret = mController.isSupportBlueTooth();
        showToast("当前设备对蓝牙支持性："+ret);
    }
    //打开蓝牙
    public void TurnOnBluetooth(View view){
        mController.turnOnBluetooth(this,0);
    }
    //关闭蓝牙
    public void TurnOffBluetooth(View view){
        mController.turnOffBluetooth();
    }

    //搜索设备
    public void SearchDevice(View view){
        imageViewxiaoche.setVisibility(View.GONE);
        imageViewzhangai.setVisibility(View.GONE);
        imageViewredqian2.setVisibility(View.GONE);
        imageViewredyou.setVisibility(View.GONE);
        imageViewredhou.setVisibility(View.GONE);
        imageViewredzuo.setVisibility(View.GONE);
        mListView.setVisibility(View.VISIBLE);
        viewflag = 1;
        showToast("搜索中...");
        mAdapter.refresh(mDeviceList);
        mController.findDevice();
        mListView.setOnItemClickListener(bindDeviceClick);

        //imageViewAddDevice.setVisibility(View.GONE);
        //imageViewMyDevice.setVisibility(View.VISIBLE);
        //textView.setText("搜索设备");
        //progressBar.setVisibility(View.VISIBLE);
    }
    //查看已绑定设备
    public void ShowBondDevice(View view){
        imageViewxiaoche.setVisibility(View.GONE);
        imageViewzhangai.setVisibility(View.GONE);
        imageViewredqian2.setVisibility(View.GONE);
        imageViewredzuo.setVisibility(View.GONE);
        imageViewredhou.setVisibility(View.GONE);
        imageViewredyou.setVisibility(View.GONE);
        mListView.setVisibility(View.VISIBLE);
        viewflag = 2;
        mBondedDeviceList = mController.getBondedDeviceList();
        mAdapter.refresh(mBondedDeviceList);
        mListView.setOnItemClickListener(bondedDeviceClick);
        //imageViewMyDevice.setVisibility(View.GONE);
        //imageViewAddDevice.setVisibility(View.VISIBLE);
        //textView.setText("我的设备");
        //progressBar.setVisibility(View.GONE);
    }
    //进入遥测界面
    public void YaoCe(View view){
        mListView.setVisibility(View.GONE);
        imageViewxiaoche.setVisibility(View.VISIBLE);
        viewflag = 0;
    }
    //打开可见
    public void openVisible(View view){
        mController.enableVisibly(this,0);
    }
    //位置权限请求回调
    public void onRequestPermission(int requestCode,String[] permissions, int[] grantResults){
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        switch (requestCode){
            case 200:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    return;
                }else{
                    showToast("未开启定位权限，请前往设置手动开启");
                }
                break;
            default:break;
        }
    }
    //进入监听模式
    public void Listening(){
        if (mAcceptThread != null){
            mAcceptThread.cancel();
        }
        mAcceptThread = new AcceptThread(mController.getAdapter(),mUIHandler);
        mAcceptThread.start();
        showToast("已进入监听模式");
    }
    //停止监听
    public void StopListening(View view){
        if (mAcceptThread != null){
            mAcceptThread.cancel();
            showToast("已停止监听");
        }
    }
    //断开连接
    public void disconnect(View view){
        if (mConnectThread != null){
            mConnectThread.cancel();
            showToast("已断开连接");
        }
    }
    //前进
    public  void sendqianjinMessage(View view){
        if (ConnectFlag == true){
            String string = "qianjin";//passwordEdit.getText().toString();
            say(string);
            //////////////////////////解锁音效
            //music1 = MediaPlayer.create(this,R.raw.lock1);
            //music1.start();
            ////锁缩小动画
            //ScaleAnimation scaleAni = new ScaleAnimation(1.0f, 0, 1.0f, 0,
                    //Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                   // 0.5f);
            //scaleAni.setDuration(500);
            //scaleAni.setRepeatCount(1);
            //scaleAni.setRepeatMode(Animation.REVERSE);
            //viewPager.startAnimation(scaleAni);
            //showToast("密钥已发送");

            ///////////////////////////////////////////////////
        }else{
            showToast("请先连接一个设备");
            ////////////////////////////////解锁失败音效
            //music2 = MediaPlayer.create(this,R.raw.lock5);
            //music2.start();
            ////////////////////////////////震动
            Vibrator vibrator = (Vibrator)this.getSystemService(this.VIBRATOR_SERVICE);
            long[] patter = {50, 100, 50, 100, 50, 100};
            vibrator.vibrate(patter, -1);
            ////////////////////////////////锁摆动动画
            //TranslateAnimation translateAnimation = new TranslateAnimation(
                    //Animation.RELATIVE_TO_PARENT, -0.05f, Animation.RELATIVE_TO_PARENT,
                   // 0.05f, Animation.RELATIVE_TO_PARENT, 0,
                    //Animation.RELATIVE_TO_PARENT, 0);
            //translateAnimation.setDuration(100);
            //translateAnimation.setRepeatCount(4);
            //translateAnimation.setRepeatMode(Animation.REVERSE);
            //viewPager.startAnimation(translateAnimation);
        }
//        String string = passwordEdit.getText().toString();
//        say(string);
    }
    //发送数据函数
    private void say(String word) {
        if (mAcceptThread != null) {
            try {
                mAcceptThread.sendData(word.getBytes("utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        else if( mConnectThread != null) {
            try {
                mConnectThread.sendData(word.getBytes("utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }


}
