package com.ywhyw.bob.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.ywhyw.bob.R;
import com.ywhyw.bob.adapter.AddMusicAdapter;
import com.ywhyw.bob.base.BaseActivity;
import com.ywhyw.bob.base.BaseApplication;
import com.ywhyw.bob.entity.Music;
import com.ywhyw.bob.util.MusicDao;

import org.xutils.db.Selector;
import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.List;

public class AddMusicActivity extends BaseActivity {

    private BaseApplication app;

    private RelativeLayout llAdd;
    private LinearLayout llList;

    private Button btnAdd;
    private Button btnChoseAll;
    private Button btnAddChose;
    private ListView lv;
    private List<Music> musics;
    private AddMusicAdapter mAdapter;

    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_music);

        app = (BaseApplication) getApplication();

        initView();
        initEvent();
    }

    private void initEvent() {
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.setTitle("添加");
                pd.setMessage("正在扫描，请稍后");
                pd.setCancelable(false);
                pd.show();
                new MusicThread().start();
            }
        });
        btnChoseAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (Music m : musics){
                    m.setChecked(true);
                }
                mAdapter.notifyDataSetChanged();
            }
        });
        btnAddChose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Music> localMusics = new ArrayList<Music>();
                ArrayList<Music> musicList = app.getMusicList();
                if(musicList != null){
                    localMusics.addAll(app.getMusicList());
                }
                int i = 0;
                for (Music m : musics){
                    if (m.isChecked() && !m.isadded()){
                        localMusics.add(m);
                        i++;
                    }
                }
                showToast("add music 共添加首" + i + "歌曲");
                app.setMusicList(localMusics);
                finish();
            }
        });
    }

    private void initView() {
        pd = new ProgressDialog(mContext);
        btnAdd = (Button) findViewById(R.id.btn_add);
        btnChoseAll = (Button) findViewById(R.id.btn_chose_all);
        btnAddChose = (Button) findViewById(R.id.btn_add_chose);
        lv = (ListView) findViewById(R.id.lv_music);
        llAdd = (RelativeLayout) findViewById(R.id.ll_add);
        llList = (LinearLayout) findViewById(R.id.ll_list);
        llAdd.setVisibility(View.VISIBLE);
        llList.setVisibility(View.GONE);
    }

    class MusicThread extends Thread{
        @Override
        public void run() {
            musics = new MusicDao(mContext).getMusicList();
            ArrayList<Music> musicList = app.getMusicList();
            if(musicList != null){
                for (Music music : musics){
                    for(Music m : musicList){
                        if (m.getId() == music.getId()){
                            music.setIsadded(true);
                        }
                    }
                }
            }
            try {
                sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Message msg = new Message();
            musicHandler.sendMessage(msg);
        }
    }

    private Handler musicHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (pd.isShowing()){
                pd.cancel();
            }
            llAdd.setVisibility(View.GONE);
            llList.setVisibility(View.VISIBLE);
            if (musics == null || musics.isEmpty()){
                showToast("未发现音乐文件，请添加音乐到SD卡");
            }else{
                mAdapter = new AddMusicAdapter(mContext,musics);
                lv.setAdapter(mAdapter);
            }
        }
    };

}
