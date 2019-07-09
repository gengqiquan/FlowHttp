package com.gengqiquan.flowhttp;

import android.content.Intent;
import android.os.HandlerThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.gengqiquan.flow.Flow;
import com.gengqiquan.flow.Result;
import com.gengqiquan.flow.interfaces.Converter;
import com.gengqiquan.flow.interfaces.Func0;
import com.gengqiquan.flow.interfaces.Transformer;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Flow.baseUrl("https://api.apiopen.top/");
        findViewById(R.id.tv_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Converter converter = new Converter() {
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
//                Flow.with("music/netease/song?key=579621905&id=526307800")
//                        .converter(converter)
//                        .get()
//                        .async(new Result<Modell>() {
//                            @Override
//                            public void success(Modell bean) {
//                                ((TextView) findViewById(R.id.tv_btn)).setText(bean.toString());
//                            }
//                        });
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Flow.with("recommendPoetry")
                                    .converter(converter)
                                    .get()
                                    .transform(new RxTransformFactory<Observable<Modell>, Modell>())
                                    .subscribeOn(Schedulers.io())
                                    .subscribe(new Subscriber<Modell>() {
                                        @Override
                                        public void onCompleted() {

                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            e.printStackTrace();
                                        }

                                        @Override
                                        public void onNext(Modell s) {
                                            Log.e("MainActivity", "success: " + s.toString());
                                        }
                                    });

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }).start();
            }
        });
    }
}
