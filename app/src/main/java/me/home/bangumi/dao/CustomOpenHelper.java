package me.home.bangumi.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import me.home.bangumi.BangumiApp;
import me.home.bangumi.api.LoginManager;
import me.home.bangumi.utils.LogUtil;

public class CustomOpenHelper extends DaoMaster.OpenHelper {

    public CustomOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        LogUtil.d(LogUtil.ZUBIN, "update schema from " + oldVersion + " to " + newVersion);
        super.onUpgrade(db, oldVersion, newVersion);

        if (oldVersion < 9) {
            LoginManager.logout(BangumiApp.sAppCtx);
        }
    }


}
