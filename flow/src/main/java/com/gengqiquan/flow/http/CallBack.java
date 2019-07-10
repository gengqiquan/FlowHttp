package com.gengqiquan.flow.http;

public interface CallBack<T> {
    void success(T bean);

    void start();

    void error(Throwable throwable);
}
