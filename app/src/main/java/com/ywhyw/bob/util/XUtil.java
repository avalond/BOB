package com.ywhyw.bob.util;

import android.os.Environment;

import org.xutils.DbManager;

import java.io.File;

import static org.xutils.DbManager.*;

/**
 * Created by yangfan on 2016/3/26.
 */
public class XUtil {
    static DaoConfig daoConfig;
    public static DaoConfig getDaoConfig(){
        File file=new File(Environment.getExternalStorageDirectory().getPath());
        if(daoConfig==null){
            daoConfig=new DaoConfig()
                    .setDbName("mybob.db")
                    .setDbDir(file)
                    .setDbVersion(1)
                    .setAllowTransaction(true)
                    .setDbUpgradeListener(new DbUpgradeListener() {
                        @Override
                        public void onUpgrade(DbManager db, int oldVersion, int newVersion) {

                        }
                    });
        }
        return daoConfig;
    }
}
