package com.gengqiquan.flow.http;

/**
 * 请求结果封装
 *
 * @author gengqiquan
 * @date 2019-07-09 15:28
 */
public class SimpleResult<T> implements Result<T> {
    @Override
    public void success(T bean) {

    }

    @Override
    public void start() {
    }

    @Override
    public void error(Exception e) {
        e.printStackTrace();
    }
}
