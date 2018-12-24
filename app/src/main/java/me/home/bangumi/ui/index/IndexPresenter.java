package me.home.bangumi.ui.index;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.text.TextUtils;
import android.view.View;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import me.home.bangumi.BangumiApp;
import me.home.bangumi.api.ApiManager;
import me.home.bangumi.constants.MyConstants;
import me.home.bangumi.dao.DaoSession;
import me.home.bangumi.dao.Index;
import me.home.bangumi.dao.IndexDao;
import me.home.bangumi.event.CategoryChangeEvent;
import me.home.bangumi.ui.bangumidetail.BangumiDetailActivity;
import me.home.bangumi.utils.LogUtil;
import me.home.bangumi.utils.PreferencesUtils;
import me.home.bangumi.utils.RxBus;
import me.home.bangumi.utils.RxBusSubscriber;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class IndexPresenter implements IndexContract.Presenter {

    private IndexContract.View mIndexView;
    private Subscription mRxChangeCateStub;
    private int mPage = 1;
    private String category = "anime";
    private CompositeSubscription mSubscriptions;


    public IndexPresenter(IndexChildFragment indexChildFragment) {
        this.mIndexView = indexChildFragment;
        this.mIndexView.setPresenter(this);
    }

    @Override
    public void subscribe() {
        mSubscriptions = new CompositeSubscription();
        subscribeChangeCateEvent();
    }

    private void subscribeChangeCateEvent() {
        mRxChangeCateStub = RxBus.getDefault().toObservableSticky(CategoryChangeEvent.class)
                .subscribe(new RxBusSubscriber<CategoryChangeEvent>() {
                    @Override
                    protected void onEvent(CategoryChangeEvent categoryChangeEvent) {
                        mPage = 1;
                        category = categoryChangeEvent.getCategory();
                        mIndexView.onChangeCateEvent();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mIndexView.showToast(e.getMessage());
                    }
                });

        mSubscriptions.add(mRxChangeCateStub);
    }

    @Override
    public void unsubscribe() {
        mSubscriptions.clear();
    }

    @Override
    public void requestData(final String date, final String category) {
        Observable<List<Index>> cache = Observable.create(new Observable.OnSubscribe<List<Index>>() {
            @Override
            public void call(Subscriber<? super List<Index>> subscriber) {
                try {
                    subscriber.onStart();

                    // 查询数据库
                    List<Index> items = new ArrayList<Index>();

                    List<Index> queryList = BangumiApp.sAppCtx.getDaoSession()
                            .getIndexDao().queryBuilder()
                            .where(IndexDao.Properties.Category.eq(category)
                                    , IndexDao.Properties.Date.eq(date))
                            .build().list();

                    items.addAll(queryList);

                    // 超过6小时清空,使用网络, 或者非第一页则使用网络加载
                    long deta = System.currentTimeMillis() - PreferencesUtils.getLong(BangumiApp.sAppCtx,
                            MyConstants.INDEX_REFRESH_NAME, category + date, 0);

                    if (deta > MyConstants.INDEX_REFRESH_TIME || mPage > 1) {
                        items.clear();
                    }

                    subscriber.onNext(items);
                    subscriber.onCompleted();

                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });

        //网络数据
        String[] index_date = date.split("-");
        Observable<List<Index>> network = ApiManager.getWebInstance()
                .getIndex(category, Integer.parseInt(index_date[0]), Integer.parseInt(index_date[1]), mPage)
                .map(new Func1<String, List<Index>>() {
                    @Override
                    public List<Index> call(String s) {
                        return parseHtmlToDb(s, category, date);
                    }
                });

        List<Index> defaultEmpty = new ArrayList<>();
        Observable<List<Index>> source = Observable.concat(cache, network)
                .firstOrDefault(defaultEmpty, new Func1<List<Index>, Boolean>() {
                    @Override
                    public Boolean call(List<Index> myIndex) {
                        return (myIndex.size() != 0);
                    }
                });

        Subscription subscription = source.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Index>>() {
                    @Override
                    public void onCompleted() {
                        LogUtil.d(LogUtil.ZUBIN, "index onCompleted ");
                        mIndexView.hideLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.d(LogUtil.ZUBIN, "collection onError " + e.getMessage());
                        mIndexView.hideLoading();
                        mIndexView.showOnError();
                    }

                    @Override
                    public void onNext(List<Index> myIndex) {
                        LogUtil.d(LogUtil.ZUBIN, "collection onNext ");
                        if (mPage == 1) {
                            mIndexView.clearData();
                        }
                        mPage++;
                        mIndexView.refreshList(myIndex);
                    }
                });


        mSubscriptions.add(subscription);
    }

    @Override
    public String getTime(int position) {
        String year = String.valueOf((int) Math.floor(position / 12 + 1900));
        String month = String.valueOf(position % 12 + 1);
        return year + "-" + month;
    }

    private List<Index> parseHtmlToDb(String html, String category, String date) {
        Document doc = Jsoup.parse(html);
        Elements div = doc.select(".section");
        Elements li = div.select("ul#browserItemList>li");
        List<Index> mList = new ArrayList<>();

        DaoSession daoSession = BangumiApp.sAppCtx.getDaoSession();

        for (int i = 0; i < li.size(); i++) {
            Index entity = new Index();
            Element element = li.get(i);

            String bangumiId = element.select("a").attr("href").replace("/subject/", "").trim();
            String imageUrl = "https:" + element.select("a>span>img").attr("src");
            String name_cn = element.select("div>h3>a").text();
            String name = element.select("div>h3>small").text();
            String info = element.select("p.info").text();

            entity.setBangumi_id(bangumiId);
            entity.setCategory(category);
            entity.setDate(date);
            entity.setImg_url(imageUrl);
            entity.setInfo(info);
            entity.setName(name);
            entity.setName_cn(name_cn);

            mList.add(entity);
        }
        IndexDao indexDao = daoSession.getIndexDao();
        List<Index> queryList = indexDao.queryBuilder()
                .where(IndexDao.Properties.Category.eq(category)
                        , IndexDao.Properties.Date.eq(date)).list();
        indexDao.deleteInTx(queryList);
        indexDao.insertInTx(mList);
        List<Index> list = indexDao.loadAll();
        String key = category + date;
        PreferencesUtils.putLong(BangumiApp.sAppCtx
                , MyConstants.INDEX_REFRESH_NAME, key, System.currentTimeMillis());
        return mList;

    }

    @Override
    public String getCategory() {
        return category;
    }

    @Override
    public void forceRefresh(String category, String date) {
        mPage = 1;
        PreferencesUtils.putLong(BangumiApp.sAppCtx,
                MyConstants.INDEX_REFRESH_NAME, category + date, 0);

    }

    @Override
    public void openBangumiDetail(Activity activity, View view, Index index) {
        String normal_img_url = index.getImg_url();
        String coverImageUrl = normal_img_url.replace("/s/", "/c/");
        String largeImageUrl = normal_img_url.replace("/s/", "/l/");

        Intent intent = new Intent(activity, BangumiDetailActivity.class);
        intent.putExtra("bangumiId", index.getBangumi_id());
        intent.putExtra("name", !TextUtils.isEmpty(index.getName_cn()) ? index.getName_cn() : index.getName());
        intent.putExtra("common_url", coverImageUrl);
        intent.putExtra("large_url", largeImageUrl);
        activity.startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(activity, view, "img").toBundle());
    }
}

