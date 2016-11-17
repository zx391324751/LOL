package com.best.android.loler;

import android.app.Application;

import com.best.android.loler.db.DatabaseHelper;
import com.douyu.lib.xdanmuku.x.DanmuClient;
import com.douyu.lib.xdanmuku.x.JniDanmu;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.net.URLEncoder;

/**
 * Created by BL06249 on 2015/12/17.
 */
public class LOLApplication extends Application {

    static final String TAG = "LOLApplication";

    static LOLApplication lolApplication;
    DatabaseHelper databaseHelper;
    public static DanmuClient danmuClient;

    @Override
    public void onCreate() {
        super.onCreate();
        lolApplication = this;
        databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        danmuClient = DanmuClient.init(getApplicationContext());
    }

    public static LOLApplication getInstance(){
        return lolApplication;
    }

    public DatabaseHelper getDatabaseHelper(){
        return databaseHelper;
    }

}
