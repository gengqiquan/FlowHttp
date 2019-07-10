package com.gengqiquan.flow;

import android.app.Activity;
import android.app.Application;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.gengqiquan.flow.converter.Converter;
import com.gengqiquan.flow.converter.ConverterFactory;
import com.gengqiquan.flow.http.CallProxy;
import com.gengqiquan.flow.http.HttpMethod;
import com.gengqiquan.flow.http.RequestBuilder;
import com.gengqiquan.flow.http.CallBack;
import com.gengqiquan.flow.http.TypeToken;
import com.gengqiquan.flow.scheduler.Scheduler;
import com.gengqiquan.flow.interfaces.Stream;
import com.gengqiquan.flow.interfaces.Transformer;
import com.gengqiquan.flow.lifecycle.LifecycleHolder;
import com.gengqiquan.flow.lifecycle.LifeEvent;
import com.gengqiquan.flow.lifecycle.LifecycleProvider;
import com.gengqiquan.flow.scheduler.AndroidSchedulers;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * 网络请求门面类
 *
 * @author gengqiquan
 * @date 2019-07-09 15:26
 */
public class Flow {
    private static final String TAG = "FlowHttp";
    private static volatile okhttp3.Call.Factory mService;
    private static HttpUrl mBaseUrl;
    private static Converter mConverter = ConverterFactory.StringConverter();
    ;
    private static MediaType mContentType;
    private static Application mApp;
    private static HttpMethod mDefaultMethod = HttpMethod.GET;

    private static okhttp3.Call.Factory getService() {
        if (mService == null) {
            synchronized (Flow.class) {
                if (mService == null) {
                    mService = new OkHttpClient
                            .Builder()
                            .connectTimeout(3, TimeUnit.SECONDS)
                            .readTimeout(15, TimeUnit.SECONDS)
                            .writeTimeout(15, TimeUnit.SECONDS)
                            .build();
                }
            }
        }
        return mService;
    }

    private static void baseUrl(HttpUrl baseUrl) {
        checkNotNull(baseUrl, "baseUrl == null");
        List<String> pathSegments = baseUrl.pathSegments();
        if (!"".equals(pathSegments.get(pathSegments.size() - 1))) {
            throw new IllegalArgumentException("baseUrl must end in /: " + baseUrl);
        }
        Flow.mBaseUrl = baseUrl;
    }

    private static void baseUrl(String baseUrl) {
        checkNotNull(baseUrl, "baseUrl == null");
        HttpUrl httpUrl = HttpUrl.parse(baseUrl);
        if (httpUrl == null) {
            throw new IllegalArgumentException("Illegal URL: " + baseUrl);
        }
        baseUrl(httpUrl);
    }

    public static void init(@NonNull Application application, @NonNull ConfigBuilder builder) {
        synchronized (Flow.class) {
            mApp = application;
            baseUrl(builder.baseUrl);

            if (builder.mService != null) {
                mService = builder.mService;
            }
            if (builder.converter != null) {
                mConverter = builder.converter;
            }
            mContentType = builder.contentType;

            if (builder.defaultMethod != null) {
                mDefaultMethod = builder.defaultMethod;
            }
        }
    }

    public static class ConfigBuilder {
        okhttp3.Call.Factory mService;
        String baseUrl;
        Converter converter;
        MediaType contentType;
        HttpMethod defaultMethod;

        public ConfigBuilder(@NonNull String baseUrl) {
            this.baseUrl = baseUrl;
        }

        public ConfigBuilder client(@NonNull Call.Factory client) {
            this.mService = client;
            return this;
        }

        public ConfigBuilder converter(@NonNull Converter converter) {
            this.converter = converter;
            return this;
        }

        public ConfigBuilder defaultMethod(@NonNull HttpMethod defaultMethod) {
            this.defaultMethod = defaultMethod;
            return this;
        }

        public ConfigBuilder contentType(@NonNull MediaType contentType) {
            this.contentType = contentType;
            return this;
        }
    }

    public static Builder with(String url) {
        return new Builder(url);
    }

    public static class Builder implements Stream {
        HttpMethod method = mDefaultMethod;
        MediaType contentType;
        String url;
        Scheduler scheduler;
        Converter converter;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();

        private Builder(String url) {
            this.url = url;
        }

        public Builder Params(@NonNull Map<String, String> params) {
            this.params.putAll(params);
            return this;
        }

        public Builder Params(@NonNull String key, String value) {
            this.params.put(key, value);
            return this;
        }

        public Builder ParamsNotNull(@NonNull String key, String value) {
            if (isNULL(value)) {
                return this;
            }
            this.params.put(key, value);
            return this;
        }

        public Builder Headers(@NonNull Map<String, String> headers) {
            this.headers.putAll(headers);
            return this;
        }

        public Builder Header(@NonNull String key, String value) {
            this.headers.put(key, value);
            return this;
        }

        public Builder contentType(@NonNull MediaType contentType) {
            this.contentType = contentType;
            return this;
        }

        @Deprecated
        public Builder scheduler(@NonNull Scheduler scheduler) {
            this.scheduler = scheduler;
            return this;
        }

        public Builder converter(@NonNull Converter converter) {
            this.converter = converter;
            return this;
        }

        LifecycleHolder lifecycleHolder;
        LifeEvent lifeEvent = LifeEvent.DESTROY;

        /**
         * 绑定生命周期自动取消请求
         *
         * @author gengqiquan
         * @date 2019-07-09 14:57
         */

        public Builder bind(@NonNull Activity activity) {
            lifecycleHolder = LifecycleProvider.get().get(activity);
            return this;
        }

        public Builder bind(@NonNull FragmentActivity activity) {
            lifecycleHolder = LifecycleProvider.get().get(activity);
            return this;
        }

        public Builder bind(@NonNull Fragment fragment) {
            lifecycleHolder = LifecycleProvider.get().get(fragment);
            return this;
        }


        public Builder bind(@NonNull android.app.Fragment fragment) {
            lifecycleHolder = LifecycleProvider.get().get(fragment);
            return this;
        }

        public Builder lifeCircle(@NonNull LifeEvent lifeEvent) {
            this.lifeEvent = lifeEvent;
            return this;
        }

        public Builder get() {
            this.method = HttpMethod.GET;
            return this;
        }

        public Builder post() {
            this.method = HttpMethod.POST;
            return this;
        }

        public Builder put() {
            this.method = HttpMethod.PUT;
            return this;
        }

        public Builder delete() {
            this.method = HttpMethod.DELETE;
            return this;
        }

        public Builder head() {
            this.method = HttpMethod.HEAD;
            return this;
        }

        private Stream builder() {
            RequestBuilder builder = new RequestBuilder(this.method.getValue(), Flow.mBaseUrl, this.url, parseHeaders(), contentType, hasBody(), isFormEncoded(), isMultipart());
            if (!params.isEmpty()) {
                for (Map.Entry<String, String> entry : this.headers.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    builder.addQueryParam(key, value, false);
                }
            }
            Request request = builder.build();
            final Call call = getService().newCall(request);
            if (scheduler == null) {
                scheduler = AndroidSchedulers.mainThread();
            }
            if (converter == null) {
                converter = Flow.mConverter;
            }
            if (lifecycleHolder != null) {
                LifecycleProvider.get().get(mApp);
            }
            return new CallProxy(call, converter, scheduler, lifecycleHolder, lifeEvent);
        }


        @Override
        public void listen(@NonNull CallBack callBack) {
            builder().listen(callBack);
        }

        @Override
        public <T> T await(Type cls) throws IOException {
            return builder().await(cls);
        }

        public <T> T await() throws IOException {
            return builder().await(new TypeToken<String>() {
            }.getType());
        }

        public <T> T await(TypeToken typeToken) throws IOException {
            return builder().await(typeToken.getType());
        }

        @Override
        public <T, R> T transform(@NonNull Transformer<? super T, ? super R> transformer) throws IOException {
            return (T) builder().transform(transformer);
        }

        private boolean hasBody() {
            return HttpMethod.POST == method || HttpMethod.PUT == method;
        }

        private boolean isFormEncoded() {
            return HttpMethod.POST == method || HttpMethod.PUT == method;
        }

        private boolean isMultipart() {
            return false;
        }

        private Headers parseHeaders() {
            Headers.Builder builder = new Headers.Builder();
            for (Map.Entry<String, String> entry : this.headers.entrySet()) {
                String headerName = entry.getKey();
                String headerValue = entry.getValue();
                if ("Content-Type".equalsIgnoreCase(headerName)) {
                    MediaType type = MediaType.parse(headerValue);
                    if (type == null) {
                        throw methodError("Malformed content type: %s", headerValue);
                    }
                    contentType = type;
                } else {
                    builder.add(headerName, headerValue);
                }
            }
            return builder.build();
        }

        private RuntimeException methodError(String message, Object... args) {
            return methodError(null, message, args);
        }

        private RuntimeException methodError(Throwable cause, String message, Object... args) {
            message = String.format(message, args);
            return new IllegalArgumentException(message, cause);
        }
    }

    public static boolean isNULL(String str) {
        return str == null || "null".equals(str) || "".equals(str);

    }

    static <T> T checkNotNull(@Nullable T object, String message) {
        if (object == null) {
            throw new NullPointerException(message);
        }
        return object;
    }

}
