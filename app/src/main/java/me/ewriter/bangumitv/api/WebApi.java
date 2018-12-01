package me.ewriter.bangumitv.api;


import android.text.GetChars;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface WebApi {

    @GET("subject/{subject_id}")
    Observable<String> getAnimeDetail(@Path("subject_id") String subject_id);

    @GET("subject_search/{keyword}")
    Observable<String> searchItem(@Path("keyword") String keyword,
                                  @Query("cat") String cat,
                                  @Query("page") int i);

    @GET("subject/{subject_id}/characters")
    Observable<String> getAnimeCharacters(@Path("subject_id") String subject_id);

    @GET("{category}/list/{userName}/{type}")
    Observable<String> listCollection(@Path("category") String category,
                                      @Path("userName") String username,
                                      @Path("type") String type,
                                      @Query("page") int i);

    @GET("subject/{subject_id}/persons")
    Observable<String> getAnimePersons(@Path("subject_id") String subject_id);

    @GET("subject/{subject_id}/ep")
    Observable<String> getAnimeEp(@Path("subject_id") String subject_id);

    @GET("{subject_type}/browser/airtime/{year}-{month}")
    Observable<String> getIndex(@Path("subject_id") String subject_type,
                                @Path("year") int year,
                                @Path("month") int month,
                                @Query("page") int i);
}
