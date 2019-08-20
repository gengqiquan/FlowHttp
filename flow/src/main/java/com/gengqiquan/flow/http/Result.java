package com.gengqiquan.flow.http;

import android.support.annotation.NonNull;

public interface Result<T> {
    void success(@NonNull T model);

    void start();

    void error(@NonNull Exception e);
}
