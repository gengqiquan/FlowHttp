package com.gengqiquan.flow.http;

public interface Result<T> {
    void success(T bean);

    void start();

    void error(Throwable throwable);
}
