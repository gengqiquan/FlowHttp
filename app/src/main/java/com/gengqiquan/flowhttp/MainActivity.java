package com.gengqiquan.flowhttp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.gengqiquan.flow.Flow;
import com.gengqiquan.flow.http.TypeToken;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import rx.Subscriber;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("MainActivity");
        Flow.init(new Flow.ConfigBuilder("https://api.apiopen.top/")
                .converter(GsonConverterFactory.create())
                .client(new OkHttpClient
                        .Builder()
                        .addInterceptor(new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                            @Override
                            public void log(String message) {
                                Log.e("EcgSDK", message);
                            }
                        }).setLevel(HttpLoggingInterceptor.Level.BODY))
//                        .addNetworkInterceptor(new Interceptor() {
//                            @Override
//                            public Response intercept(Chain chain) throws IOException {
//                                try {
//                                    Thread.sleep(3000);
//                                } catch (InterruptedException e) {
//                                    e.printStackTrace();
//                                }
//                                return chain.proceed(chain.request());
//                            }
//                        })
                        .connectTimeout(10, TimeUnit.SECONDS)
                        .readTimeout(15, TimeUnit.SECONDS)
                        .writeTimeout(15, TimeUnit.SECONDS)
                        .build()));
        findViewById(R.id.tv_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, StopTestActivity.class));


            }
        });
        findViewById(R.id.tv_await).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                await();
            }
        });
        findViewById(R.id.tv_rx).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rx();
            }
        });
    }

    private void await() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String key=null;
                    final Modell bean = Flow.with("https://api.apiopen.top/getJoke?page=1&count=2&type=video")
                            .asJson()
                            .post()
                            .Params("int", 2)
                            .Params("key", "66666")
                            .Params("null", key)
                            .Params("bool", true + "")
                            .Params("string", "fsfsf")
                            .Params("double", 6.8f)
                            .await(Modell.class);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ((TextView) findViewById(R.id.tv_text)).setText(bean.getResult().toString());

                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();

    }

    void rx() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Flow.with("getJoke?page=1&count=2&type=video")
                            .transform(new RxTransformFactory<Modell<List<Detail>>>(new TypeToken<Modell<List<Detail>>>() {
                            }) {
                            })
                            .subscribeOn(Schedulers.io())
                            .subscribe(new Subscriber<Modell<List<Detail>>>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {
                                    e.printStackTrace();
                                }


                                @Override
                                public void onNext(final Modell<List<Detail>> m) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ((TextView) findViewById(R.id.tv_text)).setText(m.getResult().get(0).toString());

                                        }
                                    });
                                }
                            });

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

}
