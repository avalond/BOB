package com.ywhyw.bob.activity;


import android.app.AlertDialog;
import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.ywhyw.bob.R;
import com.ywhyw.bob.fragment.FragMusic;
import com.ywhyw.bob.fragment.FragPt;
import com.ywhyw.bob.util.Constants;
import java.lang.reflect.Method;

import static java.security.AccessController.getContext;

public class DeviceDetailActivity extends FragmentActivity implements OnClickListener, Constants {


    private static final int SHOW_FRAG_PT = 101;
    private static final int SHOW_FRAG_MUSIC = 102;

    private Context mContext;

    private FragmentManager fm;
    private FragPt fragPt;
    private FragMusic fragMusic;

    private LinearLayout back;
    private TextView tvName;

    private Button btnPt;
    private Button btnMusic;

    private ImageButton ibMusicList;

    private MyReceiver myReceiver;


    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_device_detail);
        mContext = this;
        fm = getSupportFragmentManager();

        initViews();
        initEvent();

        showFragment(SHOW_FRAG_PT);

        initReceiver();

        BluetoothDevice bd = getIntent().getParcelableExtra("device");
        connectUsingBluetoothA2dp(mContext, bd);
    }

    private void initReceiver() {
        myReceiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(DEVICE_STATUS_CHANGED);
        registerReceiver(myReceiver, filter);
    }

    class MyReceiver extends BroadcastReceiver {
        @Override public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (DEVICE_STATUS_CHANGED.equals(action)) {
                new AlertDialog.Builder(mContext).setTitle(
                        getString(R.string.dialog_title_device_no_connect))
                        .setMessage(getString(R.string.dialog_msg_device_no_connect))
                        .show();
                bthandler.sendEmptyMessageDelayed(0, 3000);
            }
        }

    }

    private Handler bthandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            finish();
        }

        ;
    };


    private void initViews() {
        back = (LinearLayout) findViewById(R.id.ll_back);
        tvName = (TextView) findViewById(R.id.tv_name);
        btnPt = (Button) findViewById(R.id.btn_pt);
        btnMusic = (Button) findViewById(R.id.btn_music);
        ibMusicList = (ImageButton) findViewById(R.id.ib_music_type);
    }

    private void showFragment(int position) {
        FragmentTransaction ft = fm.beginTransaction();
        resetBgColors();
        hideFragment(ft);
        switch (position) {
            case SHOW_FRAG_PT:
                if (fragPt == null) {
                    fragPt = new FragPt();
                    ft.add(R.id.fl_content, fragPt);
                } else {
                    ft.show(fragPt);
                }
                btnPt.setBackgroundResource(R.drawable.topbar_shape);
                btnPt.setTextColor(0xff3ad168);
                break;
            case SHOW_FRAG_MUSIC:
                if (fragMusic == null) {
                    fragMusic = new FragMusic();
                    ft.add(R.id.fl_content, fragMusic);
                } else {
                    ft.show(fragMusic);
                }
                btnMusic.setBackgroundResource(R.drawable.topbar_shape);
                btnMusic.setTextColor(0xff3ad168);
                ibMusicList.setVisibility(View.VISIBLE);
                break;

            default:
                break;
        }
        ft.commit();
    }

    private void hideFragment(FragmentTransaction ft) {
        if (fragPt != null) {
            ft.hide(fragPt);
        }
        if (fragMusic != null) {
            ft.hide(fragMusic);
        }
    }

    private void resetBgColors() {
        btnPt.setBackgroundResource(R.drawable.topbar_shape_green);
        btnPt.setTextColor(0xffffffff);
        btnMusic.setBackgroundResource(R.drawable.topbar_shape_green);
        btnMusic.setTextColor(0xffffffff);
        ibMusicList.setVisibility(View.GONE);
    }

    private void initEvent() {
        back.setOnClickListener(this);
        btnPt.setOnClickListener(this);
        btnMusic.setOnClickListener(this);
        ibMusicList.setOnClickListener(this);
    }


    @Override public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.btn_pt:
                showFragment(SHOW_FRAG_PT);
                break;
            case R.id.btn_music:
                showFragment(SHOW_FRAG_MUSIC);
                break;
            case R.id.ib_music_type:
                Intent it = new Intent(mContext, AddMusicActivity.class);
                mContext.startActivity(it);
                break;

            default:
                break;
        }

    }


    @Override protected void onDestroy() {
        unregisterReceiver(myReceiver);
        super.onDestroy();
    }


    public void connectUsingBluetoothA2dp(Context context, final BluetoothDevice deviceToConnect) {


        try {
            Class<?> c2 = Class.forName("android.os.ServiceManager");
            Method m2 = c2.getDeclaredMethod("getService", String.class);
            IBinder b = (IBinder) m2.invoke(c2.newInstance(), "bluetooth_a2dp");
            if (b == null) {
                // For Android 4.2 Above Devices
                BluetoothAdapter.getDefaultAdapter()
                        .getProfileProxy(context, new BluetoothProfile.ServiceListener() {

                            @Override public void onServiceDisconnected(int profile) {

                            }

                            @Override
                            public void onServiceConnected(int profile, BluetoothProfile proxy) {
                                BluetoothA2dp a2dp = (BluetoothA2dp) proxy;
                                try {
                                    a2dp.getClass()
                                            .getMethod("connect", BluetoothDevice.class)
                                            .invoke(a2dp, deviceToConnect);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }, BluetoothProfile.A2DP);
            } else {
                // For Android below 4.2 devices
                //				Class<?> c3 = Class.forName("android.bluetooth.IBluetoothA2dp");
                //				Class<?>[] s2 = c3.getDeclaredClasses();
                //				Class<?> c = s2[0];
                //				Method m = c.getDeclaredMethod("asInterface", IBinder.class);
                //				m.setAccessible(true);
                //				IBluetoothA2dp a2dp = (IBluetoothA2dp) m.invoke(null, b);
                //				a2dp.connect(deviceToConnect);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
