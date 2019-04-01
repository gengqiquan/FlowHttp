package com.gengqiquan.flow;

import com.gengqiquan.flow.interfaces.CallBack;
import com.gengqiquan.flow.interfaces.Func0;
import com.gengqiquan.flow.interfaces.Func1;

public class Result<T> implements CallBack<T> {
    Func0<T> success;
    Func1 error;

    public Result() {
    }

    public Result(Func0<T> success) {
        this.success = success;
    }

    public Result(Func0<T> success, Func1 error) {
        this.success = success;
        this.error = error;
    }

    @Override
    public void success(T bean) {
        if (success != null) {
            success.success(bean);
        }
    }

    @Override
    public void error(Throwable throwable) {
        throwable.printStackTrace();
        if (error != null) {
            error.error(throwable);
        }
    }
}
