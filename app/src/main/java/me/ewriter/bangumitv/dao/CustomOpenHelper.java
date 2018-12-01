package me.ewriter.bangumitv.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import me.ewriter.bangumitv.BangumiApp;
import me.ewriter.bangumitv.api.LoginManager;
import me.ewriter.bangumitv.utils.LogUtil;

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
