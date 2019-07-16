package com.gengqiquan.flow.interfaces;

import android.support.annotation.NonNull;

import com.gengqiquan.flow.http.Result;

import java.io.IOException;
import java.lang.reflect.Type;

public interface Stream {
    <T> void listen(@NonNull Result result);

    <T> T await(Type cls) throws IOException;

    <T, R> T transform(Transformer<? super T, ? super R> transformer) throws IOException;
}
