package com.gengqiquan.flow.http;

import android.support.annotation.NonNull;
import android.util.Log;

import com.gengqiquan.flow.converter.Converter;
import com.gengqiquan.flow.scheduler.AndroidSchedulers;
import com.gengqiquan.flow.scheduler.Scheduler;
import com.gengqiquan.flow.interfaces.Stream;
import com.gengqiquan.flow.interfaces.Transformer;
import com.gengqiquan.flow.lifecycle.LifecycleHolder;
import com.gengqiquan.flow.lifecycle.LifeEvent;
import com.gengqiquan.flow.lifecycle.LifecycleListener;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.Call;
import okhttp3.Response;

/**
 * 实际请求；处理、分发请求结果类
 *
 * @author gengqiquan
 * @date 2019-07-09 15:25
 */
public class CallProxy implements Stream {
    Call call;
    Converter converter;
    Scheduler scheduler;
    LifecycleHolder lifecycleHolder;
    LifeEvent lifeEvent;

    public CallProxy(Call call, Converter converter, LifecycleHolder lifecycleHolder, LifeEvent lifeEvent) {
        this.call = call;
        this.converter = converter;
        this.scheduler = AndroidSchedulers.mainThread();
        this.lifecycleHolder = lifecycleHolder;
        this.lifeEvent = lifeEvent;

    }

    boolean detach() {
        if (lifecycleHolder.isDestroyed && lifeEvent == LifeEvent.DESTROY) {
            return true;
        }
        if (lifecycleHolder.isStop && lifeEvent == LifeEvent.STOP) {
            return true;
        }
        return false;
    }

    void watch() {
        lifecycleHolder.addListener(new LifecycleListener() {
            @Override
            public void onStop() {
                if (lifeEvent == LifeEvent.STOP && !call.isCanceled() && !call.isExecuted()) {
                    call.cancel();
                    Log.e("CallProxy", "onStop:cancel ");
                }
            }

            @Override
            public void onDestroy() {
                if (lifeEvent == LifeEvent.DESTROY && !call.isCanceled() && !call.isExecuted()) {
                    call.cancel();
                    Log.e("CallProxy", "onDestroy:cancel ");
                }
            }
        });

    }

    @Override
    public <T> void listen(@NonNull final Result result) {
        if (detach()) {
            Log.d("CallProxy", "request detach ");
            return;
        }
        watch();
        final Type type = TypeToken.getType(result.getClass(), Result.class)[0];

        result.start();
        call.enqueue(new okhttp3.Callback() {
            @Override
            public void onResponse(Call call, final Response response) {
                if (detach()) {
                    Log.d("CallProxy", "request detach");
                    return;
                }
                if (response.code() == 200) {
                    try {
                        final T bean = (T) converter.convert(response.body(), type);
                        scheduler.schedule(new Runnable() {
                            @Override
                            public void run() {
                                result.success(bean);
                            }
                        });
                    } catch (final Exception e) {
                        scheduler.schedule(new Runnable() {
                            @Override
                            public void run() {
                                result.error(e);
                            }
                        });
                    }
                } else {
                    scheduler.schedule(new Runnable() {
                        @Override
                        public void run() {
                            result.error(new HttpException(response));

                        }
                    });
                }

            }

            @Override
            public void onFailure(Call call, final IOException e) {
                if (detach()) {
                    Log.d("CallProxy", "request detach");
                    return;
                }
                scheduler.schedule(new Runnable() {
                    @Override
                    public void run() {
                        result.error(e);
                    }
                });

            }
        });
    }

    @Override
    public <T> T await(@NonNull Type cls) throws IOException {
        Response response = call.execute();
        System.out.print(cls);
        if (response.code() == 200) {
            return (T) converter.convert(response.body(), cls);
        }
        throw new HttpException(response);
    }

    @Override
    public <T, R> T transform(Transformer<? super T, ? super R> transformer) throws IOException {
        Response response = call.execute();
        if (response.code() == 200) {
            R bean = (R) converter.convert(response.body(), transformer.type());
            return (T) transformer.transform(bean);
        }
        throw new HttpException(response);
    }
}