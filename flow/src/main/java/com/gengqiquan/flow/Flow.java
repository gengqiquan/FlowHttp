package com.gengqiquan.flow;

import android.app.Activity;
import android.support.annotation.Nullable;

import com.gengqiquan.flow.interfaces.CallBack;
import com.gengqiquan.flow.interfaces.Converter;
import com.gengqiquan.flow.interfaces.Scheduler;
import com.gengqiquan.flow.interfaces.Stream;
import com.gengqiquan.flow.interfaces.Transformer;

import java.io.IOException;
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

public class Flow {
    private static final String TAG = "FlowHttp";
    private static volatile okhttp3.Call.Factory mService;
    private static volatile HttpUrl baseUrl;
    final public static String GET = "GET";
    final public static String POST = "POST";

    public static okhttp3.Call.Factory getService() {
        if (mService == null) {
            synchronized (Flow.class) {
                if (mService == null) {
                    mService = new OkHttpClient.Builder().connectTimeout(3, TimeUnit.SECONDS).readTimeout(15, TimeUnit.SECONDS)
                            .writeTimeout(15, TimeUnit.SECONDS).build();
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
        Flow.baseUrl = baseUrl;
    }

    public static void baseUrl(String baseUrl) {
        checkNotNull(baseUrl, "baseUrl == null");
        HttpUrl httpUrl = HttpUrl.parse(baseUrl);
        if (httpUrl == null) {
            throw new IllegalArgumentException("Illegal URL: " + baseUrl);
        }
        baseUrl(httpUrl);
    }

    public static Builder with(String url) {
        return new Builder(url);
    }

//    public Builder with(File file) {
//        return new Builder();
//    }

    public static class Builder implements Stream {
        String method = GET;
        MediaType contentType;
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

        public Builder contentType(MediaType contentType) {
            this.contentType = contentType;
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

        private Stream builder() {
            RequestBuilder builder = new RequestBuilder(this.method, Flow.baseUrl, this.url, parseHeaders(), contentType, hasBody(), isFormEncoded(), isMultipart());
            if (!params.isEmpty()) {
                for (Map.Entry<String, String> entry : this.headers.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    builder.addQueryParam(key, value, false);
                }
            }
            Request request = builder.build();
            Call call = getService().newCall(request);
            if (scheduler == null) {
                scheduler = AndroidSchedulers.mainThread();
            }

            return new CallProxy(call, converter, scheduler);
        }

        public Builder get() {
            this.method = GET;
            return this;
        }

        public Builder post() {
            this.method = POST;
            return this;
        }

        @Override
        public void async(CallBack callBack) {
            builder().async(callBack);
        }

        @Override
        public <T> T sync() throws IOException {
            return builder().sync();
        }

        @Override
        public <T> T transform(Transformer transformer) throws IOException {
            return (T)builder().transform(transformer);
        }

        private boolean hasBody() {
            return POST.equals(method);
        }

        private boolean isFormEncoded() {
            return POST.equals(method);
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
