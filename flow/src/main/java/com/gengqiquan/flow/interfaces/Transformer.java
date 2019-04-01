package com.gengqiquan.flow.interfaces;

import java.io.IOException;

import okhttp3.Response;

public interface Transformer<T> {
     T transform(String from) throws IOException;
}
