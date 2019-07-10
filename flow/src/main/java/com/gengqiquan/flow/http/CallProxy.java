package com.gengqiquan.flow.http;

import android.support.annotation.NonNull;
import android.util.Log;

import com.gengqiquan.flow.converter.Converter;
import com.gengqiquan.flow.scheduler.Scheduler;
import com.gengqiquan.flow.interfaces.Stream;
import com.gengqiquan.flow.interfaces.Transformer;
import com.gengqiquan.flow.lifecycle.LifecycleHolder;
import com.gengqiquan.flow.lifecycle.LifeEvent;
import com.gengqiquan.flow.lifecycle.LifecycleListener;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
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

    public CallProxy(Call call, Converter converter, Scheduler scheduler, LifecycleHolder lifecycleHolder, LifeEvent lifeEvent) {
        this.call = call;
        this.converter = converter;
        this.scheduler = scheduler;
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

    Type getType(CallBack callBack) {
        Type[] types = ((ParameterizedType) callBack.getClass().getGenericSuperclass()).getActualTypeArguments();
        if (types.length < 1) {
            return null;
        }
        return types[0];
    }

    @Override
    public <T> void listen(@NonNull final CallBack callBack) {
        if (detach()) {
            Log.d("CallProxy", "request detach ");
            return;
        }
        watch();
        final Type type = getType(callBack);
        System.out.println(type);

        callBack.start();
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
                                callBack.success(bean);
                            }
                        });
                    } catch (final Exception e) {
                        scheduler.schedule(new Runnable() {
                            @Override
                            public void run() {
                                callBack.error(e);
                            }
                        });
                    }
                } else {
                    scheduler.schedule(new Runnable() {
                        @Override
                        public void run() {
                            callBack.error(new HttpException(response));

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
                        callBack.error(new HttpException(e));
                    }
                });

            }
        });
    }

    @Override
    public <T> T await() throws IOException {
        Response response = call.execute();
        
        if (response.code() == 200) {
            return (T) converter.convert(response.body(), null);
        }
        throw new HttpException(response);
    }

    @Override
    public <T, R> T transform(Transformer<? super T, ? super R> transformer) throws IOException {
        Response response = call.execute();
        if (response.code() == 200) {
            R bean = (R) converter.convert(response.body(), null);
            return (T) transformer.transform(bean);
        }
        throw new HttpException(response);
    }
}