package me.home.bangumi.ui.calendar;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import me.home.bangumi.BangumiApp;
import me.home.bangumi.api.ApiManager;
import me.home.bangumi.api.response.BgmListItem;
import me.home.bangumi.api.response.DailyCalendar;
import me.home.bangumi.constants.MyConstants;
import me.home.bangumi.dao.BangumiCalendar;
import me.home.bangumi.dao.BangumiCalendarDao;
import me.home.bangumi.dao.DaoSession;
import me.home.bangumi.ui.bangumidetail.BangumiDetailActivity;
import me.home.bangumi.utils.LogUtil;
import me.home.bangumi.utils.PreferencesUtils;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class CalendarPresenter implements CalendarContract.Presenter {
    private CalendarContract.View mCalendarView;
    private CompositeSubscription mSubscriptions;
    private List<BgmListItem> bgmListItems;

    public CalendarPresenter(CalendarContract.View mCalendarView) {
        this.mCalendarView = mCalendarView;

        // 绑定 Presenter 和 View
        mCalendarView.setPresenter(this);
    }

    @Override
    public void subscribe() {
        // do some rx task
        mSubscriptions = new CompositeSubscription();
        requestForCookies();
//        requestData();
    }

    private void requestForCookies() {
        Subscription subscription = ApiManager.getWebInstance()
                .searchItem("bangumi", "all", 1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String s) {
                        // request for cookies, not deal with response
                    }
                });
        mSubscriptions.add(subscription);
    }

    @Override
    public void unsubscribe() {
        mSubscriptions.clear();
    }

    @Override
    public void openBangumiDetail(Activity activity, View view, BangumiCalendar calendar) {
        Intent intent = new Intent(activity, BangumiDetailActivity.class);
        intent.putExtra("bangumiId", calendar.getBangumi_id() + "");
        intent.putExtra("name", !TextUtils.isEmpty(calendar.getName_cn()) ? calendar.getName_cn() : calendar.getName_jp());
        intent.putExtra("common_url", calendar.getCommon_image());
        intent.putExtra("large_url", calendar.getLarge_image());
        activity.startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(activity, view, "img").toBundle());
    }

    @Override
    public void loadData(final int position) {
        // 数据库缓存
        Observable<List<BangumiCalendar>> cache = Observable.create(new Observable.OnSubscribe<List<BangumiCalendar>>() {
            @Override
            public void call(Subscriber<? super List<BangumiCalendar>> subscriber) {
                try {
                    subscriber.onStart();
                    // 查询
                    List<BangumiCalendar> items = new ArrayList<BangumiCalendar>();

                    long deta = System.currentTimeMillis() - PreferencesUtils.getLong(BangumiApp.sAppCtx,
                            MyConstants.CALENDAR_REFRESH_KEY, 0);
                    // 每间隔6小时强制使用网络数据一次
                    if (deta < MyConstants.CALENDAR_REFRESH_TIME) {
                        items.addAll(BangumiApp.sAppCtx.getDaoSession().getBangumiCalendarDao()
                                .queryBuilder().where(BangumiCalendarDao.Properties.Air_weekday.eq(position + 1))
                                .build().list());
                    }
                    LogUtil.d(LogUtil.ZUBIN, "thread = " + Thread.currentThread() + "length = " + items.size());
                    subscriber.onNext(items);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });

        // 网络数据
        Observable<List<BangumiCalendar>> network = ApiManager.getBangumiInstance().listCalendar()
                .map(new Func1<List<DailyCalendar>, List<BangumiCalendar>>() {
                    @Override
                    public List<BangumiCalendar> call(List<DailyCalendar> calendarList) {
                        return saveToDbData(calendarList, position);
                    }
                });
        // concat 两个数据，哪个不为空用哪个，优先取 cache
        Observable<List<BangumiCalendar>> source = Observable.concat(cache, network)
                .first(new Func1<List<BangumiCalendar>, Boolean>() {
                    @Override
                    public Boolean call(List<BangumiCalendar> bangumiCalendars) {
                        return (bangumiCalendars != null && bangumiCalendars.size() != 0);
                    }
                });

        Subscription subscription = source.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<BangumiCalendar>>() {
                    @Override
                    public void onCompleted() {
                        mCalendarView.hideLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.d(LogUtil.ZUBIN, "onError " + e.getMessage());
                        mCalendarView.showError();
                        mCalendarView.hideLoading();
                    }

                    @Override
                    public void onNext(List<BangumiCalendar> bangumiCalendars) {
                        LogUtil.d(LogUtil.ZUBIN, "onNext ");
                        mCalendarView.refresh(bangumiCalendars);

                    }
                });

        mSubscriptions.add(subscription);
    }

    @Override
    public void forceRefresh() {
        PreferencesUtils.putLong(BangumiApp.sAppCtx, MyConstants.CALENDAR_REFRESH_KEY, 0);
    }


    /**
     * 将网络请求的数据存储到数据库并将数据全部返回
     */
    private List<BangumiCalendar> saveToDbData(List<DailyCalendar> mList, int position) {
        Log.d(LogUtil.ZUBIN, "save Thread = " + Thread.currentThread());
        DaoSession daoSession = BangumiApp.sAppCtx.getDaoSession();

        // 存到数据库中的所有数据
        List<BangumiCalendar> mDbList = new ArrayList<>();
        // 返回的周一的数据
        List<BangumiCalendar> mReturenList = new ArrayList<>();
        getBgmListitems();
        for (int i = 0; i < mList.size(); i++) {
            DailyCalendar calendar = mList.get(i);
            for (int j = 0; j < calendar.getItems().size(); j++) {
                BangumiCalendar entity = new BangumiCalendar();
                String time = getTime(bgmListItems,
                        calendar.getItems().get(j).getName_cn(),
                        calendar.getItems().get(j).getName());
                if (!time.equals(""))
                    time = time.substring(0, 2) + ":" + time.substring(2);
                entity.setTime(time);
                entity.setAir_weekday(calendar.getItems().get(j).getAir_weekday());
                entity.setName_cn(calendar.getItems().get(j).getName_cn());
                entity.setBangumi_id(calendar.getItems().get(j).getId());
                if (calendar.getItems().get(j).getRating() != null) {
                    entity.setBangumi_total(calendar.getItems().get(j).getRating().getTotal());
                    entity.setBangumi_average(calendar.getItems().get(j).getRating().getScore());
                }
                if (calendar.getItems().get(j).getImages() != null) {
                    entity.setCommon_image(calendar.getItems().get(j).getImages().getCommon());
                    entity.setLarge_image(calendar.getItems().get(j).getImages().getLarge());
                    entity.setMedium_image(calendar.getItems().get(j).getImages().getMedium());
                    entity.setSmall_image(calendar.getItems().get(j).getImages().getSmall());
                    entity.setGrid_image(calendar.getItems().get(j).getImages().getGrid());
                }

                entity.setName_jp(calendar.getItems().get(j).getName());
                entity.setRank(calendar.getItems().get(j).getRank());

                mDbList.add(entity);

                if (calendar.getItems().get(j).getAir_weekday() == (position + 1)) {
                    mReturenList.add(entity);
                }
            }
        }
        daoSession.getBangumiCalendarDao().deleteAll();
        daoSession.getBangumiCalendarDao().insertInTx(mDbList);

        // 保存更新时间
        PreferencesUtils.putLong(BangumiApp.sAppCtx, MyConstants.CALENDAR_REFRESH_KEY, System.currentTimeMillis());
        return mReturenList;
    }

    private String getTime(List<BgmListItem> bgmListItems, String name_cn, String name_jp) {
        String time = "";
        for (int i = 0; i < bgmListItems.size(); i++) {
            if (bgmListItems.get(i).getTitleCN().equals(name_cn) ||
                    bgmListItems.get(i).getTitleJP().equals(name_jp)) {
                time = bgmListItems.get(i).getTimeCN();
                break;
            }
        }
        return time;
    }

    private void getBgmListitems() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        if (month < 4 && month >= 1)
            month = 1;
        else if (month >= 4 && month < 7)
            month = 4;
        else if (month >= 7 && month < 10)
            month = 7;
        else
            month = 10;
        Observable<Map<String, BgmListItem>> bgmlist = ApiManager.getBgmListInstance().getBgmList(year, month).map(new Func1<Map<String, BgmListItem>, Map<String, BgmListItem>>() {
            @Override
            public Map<String, BgmListItem> call(Map<String, BgmListItem> stringBgmListItemMap) {
                return stringBgmListItemMap;
            }
        });
        Subscription subscription = bgmlist.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Map<String, BgmListItem>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Map<String, BgmListItem> stringBgmListItemMap) {
                        setBgmListItems(new ArrayList<BgmListItem>(stringBgmListItemMap.values()));
                    }
                });
        mSubscriptions.add(subscription);
    }

    public void setBgmListItems(List<BgmListItem> bgmListItems) {
        this.bgmListItems = bgmListItems;
    }
}

