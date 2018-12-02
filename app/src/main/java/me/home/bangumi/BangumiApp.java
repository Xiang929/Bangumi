package me.home.bangumi;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatDelegate;

import com.squareup.leakcanary.LeakCanary;
import com.tencent.bugly.crashreport.CrashReport;

import me.drakeet.multitype.MultiTypePool;
import me.home.bangumi.constants.MyConstants;
import me.home.bangumi.dao.CustomOpenHelper;
import me.home.bangumi.dao.DaoMaster;
import me.home.bangumi.dao.DaoSession;
import me.home.bangumi.ui.bangumidetail.adapter.DetailCharacterItemViewProvider;
import me.home.bangumi.ui.bangumidetail.adapter.DetailCharacterList;
import me.home.bangumi.ui.bangumidetail.adapter.DetailEpItemViewProvider;
import me.home.bangumi.ui.bangumidetail.adapter.DetailEpList;
import me.home.bangumi.ui.characters.adapter.CharacterItem;
import me.home.bangumi.ui.characters.adapter.CharacterItemViewProvider;
import me.home.bangumi.ui.persons.adapter.PersonItemList;
import me.home.bangumi.ui.persons.adapter.PersonItemViewProvider;
import me.home.bangumi.widget.commonAdapter.TextItem;
import me.home.bangumi.widget.commonAdapter.TextItemViewProvider;
import me.home.bangumi.widget.commonAdapter.TitleItem;
import me.home.bangumi.widget.commonAdapter.TitleItemViewProvider;
import me.home.bangumi.widget.commonAdapter.TitleMoreItem;
import me.home.bangumi.widget.commonAdapter.TitleMoreViewProvider;
import me.home.bangumi.utils.PreferencesUtils;

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
