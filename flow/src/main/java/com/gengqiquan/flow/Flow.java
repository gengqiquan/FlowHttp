package com.gengqiquan.flow;

import android.app.Activity;

import com.gengqiquan.flow.interfaces.Converter;
import com.gengqiquan.flow.interfaces.RetrofitHttpService;
import com.gengqiquan.flow.interfaces.Scheduler;
import com.gengqiquan.flow.interfaces.Stream;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Flow {
    private static final String TAG = "FlowHttp";
    private static volatile RetrofitHttpService mService;

    public static RetrofitHttpService getService() {
        if (mService == null) {
            synchronized (Flow.class) {
                if (mService == null) {
                    Retrofit.Builder builder = new Retrofit.Builder();
                    OkHttpClient client = new OkHttpClient.Builder().connectTimeout(3, TimeUnit.SECONDS).readTimeout(15, TimeUnit.SECONDS)
                            .writeTimeout(15, TimeUnit.SECONDS).build();
                    Retrofit retrofit = builder
                            .baseUrl("https://api.bzqll.com")
//                            .addConverterFactory(ScalarsConverterFactory.create())
                            .client(client).build();
                    mService =
                            retrofit.create(RetrofitHttpService.class);
                }
            }
        }
        return mService;
    }

    public static Builder with(String url) {
        return new Builder(url);
    }

//    public Builder with(File file) {
//        return new Builder();
//    }

    public static class Builder {
        String url;
        Scheduler scheduler;
        Converter converter;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();

        private Builder(String url) {
            this.url = url;
        }

        public Builder Params(Map<String, String> params) {
            this.params.putAll(params);
            return this;
        }

        public Builder Params(String key, String value) {
            this.params.put(key, value);
            return this;
        }

        public Builder ParamsNotNull(String key, String value) {
            if (isNULL(value)) {
                return this;
            }
            this.params.put(key, value);
            return this;
        }

        public Builder Headers(Map<String, String> headers) {
            this.headers.putAll(headers);
            return this;
        }

        public Builder Header(String key, String value) {
            this.headers.put(key, value);
            return this;
        }

        public Builder scheduler(Scheduler scheduler) {
            this.scheduler = scheduler;
            return this;
        }

        public Builder converter(Converter converter) {
            this.converter = converter;
            return this;
        }

        public Builder bind(Activity converter) {
            return this;
        }

        private Call<ResponseBody> builder() {
            return getService().get(url, params, headers);
        }

        public Stream get() {
            Call<ResponseBody> call = builder();
            return new CallProxy(call, converter);
        }
    }

    public static boolean isNULL(String str) {
        return str == null || "null".equals(str) || "".equals(str);

    }
}
