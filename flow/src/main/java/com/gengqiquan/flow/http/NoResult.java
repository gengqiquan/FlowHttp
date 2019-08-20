package com.gengqiquan.flow.http;

import android.support.annotation.NonNull;

/**
 * 请求结果封装
 *
 * @author gengqiquan
 * @date 2019-07-09 15:28
 */
public class NoResult<T> implements Result<T> {
    @Override
    public void success(@NonNull T model) {

    }

    @Override
    public void start() {
    }

    @Override
    public void error(@NonNull Exception e) {

    }
}
