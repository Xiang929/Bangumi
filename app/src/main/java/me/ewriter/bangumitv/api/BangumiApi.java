package me.ewriter.bangumitv.api;

import java.util.List;

import me.ewriter.bangumitv.api.response.BaseResponse;
import me.ewriter.bangumitv.api.response.DailyCalendar;
import me.ewriter.bangumitv.api.response.SubjectComment;
import me.ewriter.bangumitv.api.response.SubjectProgress;
import me.ewriter.bangumitv.api.response.Token;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface BangumiApi {

    @FormUrlEncoded
    @POST("auth?source=onAir")
    Observable<Token> login(@Field("username") String username,
                            @Field("password") String password);

    @FormUrlEncoded
    @POST("collection/{subjectId}/update?source=onAir")
    Observable<SubjectComment> updateComment(@Path("subjectId") String subjectId,
                                             @Field("status") String status,
                                             @Field("rating") int i,
                                             @Field("comment") String comment,
                                             @Field("auth") String auth);

    @GET("calendar")
    Observable<List<DailyCalendar>> listCalendar();

    @GET("user/{userId}/progress?source=onAir")
    Observable<SubjectProgress> getSubjectProgress(@Path("userId") int userId,
                                                   @Query("auth") String auth,
                                                   @Query("subject_id") String subject_id);

    @FormUrlEncoded
    @POST("ep/{epId}/status/{status}?source=onAir")
    Observable<BaseResponse> updateEp(@Path("epId") int i,
                                      @Path("status") String status,
                                      @Field("auth") String auth);

    @GET("collection/{subjectId}?source=onAir")
    Observable<SubjectComment> getSubjectComment(@Path("subjectId") String subjectId,
                                                 @Query("auth") String auth);

}
