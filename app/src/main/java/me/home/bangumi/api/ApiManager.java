package me.home.bangumi.api;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import java.util.concurrent.TimeUnit;

import me.home.bangumi.BangumiApp;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ApiManager {

    public static final String BANGUMI_BASE_URL = "https://api.bgm.tv/";
    /**
     * 网页地址
     */
    public static final String WEB_BASE_URL = "https://bgm.tv/";

    public static final String BGMLIST_BASE_URL = "https://bgmlist.com/";

    public static final int DEFAULT_TIMEOUT = 100;

    private static BangumiApi sBangumiApis;

    private static WebApi sWebApis;

    private static BgmListApi sBgmListApis;

    protected static final Object monitor = new Object();

    public static BangumiApi getBangumiInstance() {
        synchronized (monitor) {
            if (sBangumiApis == null) {
                initBangumiApi();
            }
            return sBangumiApis;
        }
    }

    public static WebApi getWebInstance() {
        synchronized (monitor) {
            if (sWebApis == null) {
                initWebApi();
            }
            return sWebApis;
        }
    }

    public static BgmListApi getBgmListInstance() {
        synchronized (monitor) {
            if (sBgmListApis == null) {
                initBgmListApi();
            }
            return sBgmListApis;
        }
    }

    private static void initBgmListApi() {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(BGMLIST_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create());

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = builder.client(client).build();

        sBgmListApis = retrofit.create(BgmListApi.class);
    }

    private static void initBangumiApi() {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(BANGUMI_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create());

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = builder.client(client).build();

        sBangumiApis = retrofit.create(BangumiApi.class);
    }

    private static void initWebApi() {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(WEB_BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create());

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        ClearableCookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(),
                new SharedPrefsCookiePersistor(BangumiApp.sAppCtx));

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .cookieJar(cookieJar)
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = builder.client(client).build();

        sWebApis = retrofit.create(WebApi.class);
    }

}
