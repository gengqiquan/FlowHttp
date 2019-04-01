package com.gengqiquan.flow.interfaces;

public interface CallBack<T> {
     void success(T bean);

    void error(Throwable throwable);
}
