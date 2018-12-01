package me.ewriter.bangumitv;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatDelegate;

import com.squareup.leakcanary.LeakCanary;
import com.tencent.bugly.crashreport.CrashReport;

import me.drakeet.multitype.MultiTypePool;
import me.ewriter.bangumitv.constants.MyConstants;
import me.ewriter.bangumitv.dao.CustomOpenHelper;
import me.ewriter.bangumitv.dao.DaoMaster;
import me.ewriter.bangumitv.dao.DaoSession;
import me.ewriter.bangumitv.ui.bangumidetail.adapter.DetailCharacterItemViewProvider;
import me.ewriter.bangumitv.ui.bangumidetail.adapter.DetailCharacterList;
import me.ewriter.bangumitv.ui.bangumidetail.adapter.DetailEpItemViewProvider;
import me.ewriter.bangumitv.ui.bangumidetail.adapter.DetailEpList;
import me.ewriter.bangumitv.ui.characters.adapter.CharacterItem;
import me.ewriter.bangumitv.ui.characters.adapter.CharacterItemViewProvider;
import me.ewriter.bangumitv.ui.persons.adapter.PersonItemList;
import me.ewriter.bangumitv.ui.persons.adapter.PersonItemViewProvider;
import me.ewriter.bangumitv.widget.commonAdapter.TextItem;
import me.ewriter.bangumitv.widget.commonAdapter.TextItemViewProvider;
import me.ewriter.bangumitv.widget.commonAdapter.TitleItem;
import me.ewriter.bangumitv.widget.commonAdapter.TitleItemViewProvider;
import me.ewriter.bangumitv.widget.commonAdapter.TitleMoreItem;
import me.ewriter.bangumitv.widget.commonAdapter.TitleMoreViewProvider;
import me.ewriter.bangumitv.utils.PreferencesUtils;

public class BangumiApp extends Application {

    public static BangumiApp sAppCtx;
    private DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();

        sAppCtx = this;


        initTheme();

        registerMutiType();

        if (BuildConfig.DEBUG) {
            CrashReport.initCrashReport(getApplicationContext(), MyConstants.BUGLY_APPID, true);
        } else {
            CrashReport.initCrashReport(getApplicationContext(), MyConstants.BUGLY_APPID, false);
        }

        LeakCanary.install(this);

        CustomOpenHelper helper = new CustomOpenHelper(this, MyConstants.DB_NAME, null);
        SQLiteDatabase sqLiteDatabase = helper.getWritableDatabase();
        daoSession = new DaoMaster(sqLiteDatabase).newSession();
    }

    private void registerMutiType() {
        MultiTypePool.register(TextItem.class, new TextItemViewProvider());
        MultiTypePool.register(TitleItem.class, new TitleItemViewProvider());
        MultiTypePool.register(TitleMoreItem.class, new TitleMoreViewProvider());

        MultiTypePool.register(DetailCharacterList.class, new DetailCharacterItemViewProvider());
        MultiTypePool.register(DetailEpList.class, new DetailEpItemViewProvider());

        MultiTypePool.register(PersonItemList.class, new PersonItemViewProvider());
        MultiTypePool.register(CharacterItem.class, new CharacterItemViewProvider());
    }

    @SuppressWarnings("WrongConstant")
    private void initTheme() {
        boolean isNight = PreferencesUtils.getBoolean(BangumiApp.sAppCtx, MyConstants.THEME_KEY, false);
        if (isNight) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }


    /**
     * 返回 data/data/package/ 的路径
     */
    public String getBangumiDirPath() {
        return sAppCtx.getDir(MyConstants.APP_NAME, Context.MODE_PRIVATE).getAbsolutePath();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}
