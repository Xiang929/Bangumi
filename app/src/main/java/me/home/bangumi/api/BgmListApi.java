package me.home.bangumi.api;

import java.util.Map;

import me.home.bangumi.api.response.BgmListItem;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

public interface BgmListApi {
        @GET("/tempapi/bangumi/{year}/{month}/json")
        Observable<Map<String, BgmListItem>> getBgmList(@Path("year") int year,
                                                        @Path("month") int month);
}
