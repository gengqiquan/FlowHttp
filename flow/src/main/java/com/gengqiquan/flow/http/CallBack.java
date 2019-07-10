package com.gengqiquan.flow.http;

public interface CallBack<T> {
     void success(T bean);

    void error(Throwable throwable);
}
