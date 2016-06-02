package com.ywhyw.bob.base;

import android.app.Application;

import com.ywhyw.bob.entity.Music;
import com.ywhyw.bob.util.LogUtil;
import com.ywhyw.bob.util.MusicDao;
import com.ywhyw.bob.util.ToastUtil;
import com.ywhyw.bob.util.XUtil;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangfan on 2016/3/25.
 */
public class BaseApplication extends Application {

    public static DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
            .setDbName("test.db")
            // 不设置dbDir时, 默认存储在app的私有目录.
            .setDbDir(new File("/sdcard")) // "sdcard"的写法并非最佳实践, 这里为了简单, 先这样写了.
            .setDbVersion(2)
            .setDbOpenListener(new DbManager.DbOpenListener() {
                @Override
                public void onDbOpened(DbManager db) {
                    // 开启WAL, 对写入加速提升巨大
                    db.getDatabase().enableWriteAheadLogging();
                }
            })
            .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                @Override
                public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                    // TODO: ...
                    // db.addColumn(...);
                    // db.dropTable(...);
                    // ...
                    // or
                    // db.dropDb();
                }
            });

    /**
     * 歌曲的List集合
     */
    private ArrayList<Music> musics;
    /**
     * 当前播放的歌曲
     */
    private int currentMusicIndex;
    /**
     * 播放模式
     */
    private int playMode;

    private DbManager dbUtils;

    @Override
    public void onCreate() {
        // 获取歌曲的List集合
//        setMusicList(new MusicDao(this).getMusicList());
        getMusicFromDB();
        x.Ext.init(this);
        super.onCreate();


    }


    /**
     * 获取歌曲的List集合
     *
     * @return 歌曲的List集合
     */
    public ArrayList<Music> getMusicList() {
        return this.musics;
    }

    public void setMusicList(ArrayList<Music> ms) {
        if (ms == null) {
            musics = new ArrayList<Music>();
        } else {
            musics = ms;
            saveMusic(ms);
        }
    }

    /**
     * 获取当前正在播放的歌曲的索引
     *
     * @return 当前正在播放的歌曲的索引
     */
    public int getCurrentMusicIndex() {
        return currentMusicIndex;
    }

    /**
     * 设置当前播放的歌曲的索引
     *
     * @param currentMusicIndex
     *            当前播放的歌曲的索引
     */
    public void setCurrentMusicIndex(int currentMusicIndex) {
        this.currentMusicIndex = currentMusicIndex;
    }

    public int getPlayMode() {
        return playMode;
    }

    public void setPlayMode(int playMode) {
        this.playMode = playMode;
    }


    public void saveMusic(ArrayList<Music> ms){
        dbUtils = x.getDb(XUtil.getDaoConfig());
        try {
            dbUtils.delete(Music.class);
            dbUtils.saveBindingId(ms);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public void getMusicFromDB(){
        dbUtils = x.getDb(XUtil.getDaoConfig());
        try {
            List<Music> ms = dbUtils.findAll(Music.class);
            if (ms == null){
                this.musics = new ArrayList<>();
            }else{
                this.musics = (ArrayList<Music>) ms;
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }


}
