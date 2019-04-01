package com.gengqiquan.flow;

import com.gengqiquan.flow.interfaces.CallBack;
import com.gengqiquan.flow.interfaces.Converter;
import com.gengqiquan.flow.interfaces.Stream;

import java.io.IOException;

import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CallProxy implements Stream {
    Call<ResponseBody> call;
    Converter converter;

    public CallProxy(Call call, Converter converter) {
        this.call = call;
        this.converter = converter;


    }

    @Override
    public void async(final CallBack callBack) {
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> stringResponse) {
                if (stringResponse.code() == 200) {
                    try {
                        callBack.success(converter.convert(stringResponse.body()));
                    } catch (IOException e) {
                        callBack.error(e);
                    }
                } else {
                    callBack.error(new HttpException(stringResponse));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callBack.error(new HttpException(t));
            }
        });
    }

    @Override
    public <T> T sync() throws IOException {
        Response<ResponseBody> stringResponse = call.execute();
        if (stringResponse.code() == 200) {
            return converter.convert(stringResponse.body());
        }
        return null;
    }
}
