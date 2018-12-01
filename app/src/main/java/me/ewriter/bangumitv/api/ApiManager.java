package me.ewriter.bangumitv.api;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import java.util.concurrent.TimeUnit;

import me.ewriter.bangumitv.BangumiApp;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ApiManager {

    public static final String BANGUMI_BASE_URL = "https://api.bgm.tv/";
    /** 网页地址 */
    public static final String WEB_BASE_URL = "https://bgm.tv/";

    public static final int DEFAULT_TIMEOUT = 10;

    private static BangumiApi sBangumiApis;

    private static WebApi sWebApis;

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

    private static void initBangumiApi() {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(BANGUMI_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create());

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
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
