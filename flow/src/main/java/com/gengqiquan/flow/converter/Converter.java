package com.gengqiquan.flow.converter;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;

/**
 * 请求结果对象类型转换器
 *
 * @author gengqiquan
 * @date 2019-07-09 15:25
 */
public interface Converter<T> {
     T convert(ResponseBody body, Type type) throws IOException;
}
