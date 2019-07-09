package com.gengqiquan.flow.interfaces;

import java.io.IOException;

import okhttp3.Response;

public interface Transformer<T,R> {
     T transform(R from) throws IOException;
}
