package com.gengqiquan.flow.interfaces;


import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * Created by 耿 on 2016/6/28.
 */
public interface RetrofitHttpService {

    @GET()
    Call<ResponseBody> get(@Url String url, @QueryMap Map<String, String> params, @HeaderMap Map<String, String> headers);

    @FormUrlEncoded
    @POST()
    Call<ResponseBody> post(@Url String url, @FieldMap Map<String, String> params, @HeaderMap Map<String, String> headers);

    @POST()
    Call<ResponseBody> uploadFile(@Url String url, @Body RequestBody file, @HeaderMap Map<String, String> headers);


}
