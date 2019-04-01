package com.gengqiquan.flow.interfaces;

import com.gengqiquan.flow.R;

import java.io.IOException;

import okhttp3.ResponseBody;

public interface Converter {
    <T> T convert(ResponseBody responseBody)throws IOException;
}
