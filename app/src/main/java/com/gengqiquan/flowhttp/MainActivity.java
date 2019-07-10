package com.gengqiquan.flowhttp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.gengqiquan.flow.Flow;
import com.gengqiquan.flow.converter.Converter;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("MainActivity");
        Flow.init(getApplication(), new Flow.ConfigBuilder("https://api.apiopen.top/")
                .converter(modellConverter)
                .client(new OkHttpClient
                        .Builder()
                        .addNetworkInterceptor(new Interceptor() {
                            @Override
                            public Response intercept(Chain chain) throws IOException {
                                try {
                                    Thread.sleep(3000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                return chain.proceed(chain.request());
                            }
                        })
                        .connectTimeout(10, TimeUnit.SECONDS)
                        .readTimeout(15, TimeUnit.SECONDS)
                        .writeTimeout(15, TimeUnit.SECONDS)
                        .build()));
        findViewById(R.id.tv_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, StopTestActivity.class));

//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            Flow.with("recommendPoetry")
//                                    .converter(modellConverter)
//                                    .get()
//                                    .transform(RxTransformFactory.Default())
//                                    .subscribeOn(Schedulers.io())
//                                    .subscribe(new Subscriber<Modell>() {
//                                        @Override
//                                        public void onCompleted() {
//
//                                        }
//
//                                        @Override
//                                        public void onError(Throwable e) {
//                                            e.printStackTrace();
//                                        }
//
//                                        @Override
//                                        public void onNext(Modell s) {
//                                            Log.e("MainActivity", "success: " + s.toString());
//                                        }
//                                    });
//
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                }).start();
            }
        });
    }

    final Converter modellConverter = new Converter() {
        Gson gson = new Gson();

        @Override
        public Modell convert(ResponseBody value) throws IOException {
            String json = "";
            try {
                json = value.string();
            } finally {
                value.close();

                Modell m = null;
                try {
                    m = gson.fromJson(json, Modell.class);
                } catch (JsonSyntaxException e) {
                    m = new Modell();
                } finally {
                    return m;
                }
            }

        }
    };
}
