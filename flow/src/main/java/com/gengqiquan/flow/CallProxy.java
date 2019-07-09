package com.gengqiquan.flow;

import com.gengqiquan.flow.interfaces.CallBack;
import com.gengqiquan.flow.interfaces.Converter;
import com.gengqiquan.flow.interfaces.Scheduler;
import com.gengqiquan.flow.interfaces.Stream;
import com.gengqiquan.flow.interfaces.Transformer;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

public class CallProxy implements Stream {
    Call call;
    Converter converter;
    Scheduler scheduler;

    public CallProxy(Call call, Converter converter, Scheduler scheduler) {
        this.call = call;
        this.converter = converter;
        this.scheduler = scheduler;


    }

    @Override
    public void async(final CallBack callBack) {
        call.enqueue(new okhttp3.Callback() {
            @Override
            public void onResponse(Call call, final Response response) {
                scheduler.schedule(new Runnable() {
                    @Override
                    public void run() {
                        if (response.code() == 200) {
                            try {
                                callBack.success(converter.convert(response.body()));
                            } catch (IOException e) {
                                callBack.error(e);
                            }
                        } else {
                            callBack.error(new HttpException(response));
                        }
                    }
                });

            }

            @Override
            public void onFailure(Call call, final IOException e) {
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
    public <T> T sync() throws IOException {
        Response response = call.execute();
        if (response.code() == 200) {
            return converter.convert(response.body());
        }
        throw new HttpException(response);
    }

    @Override
    public <T, R> T transform(Transformer<? super T, ? super R> transformer) throws IOException {
        Response response = call.execute();
        if (response.code() == 200) {
            R bean = converter.convert(response.body());
            return (T) transformer.transform(bean);
        }
        throw new HttpException(response);
    }
}