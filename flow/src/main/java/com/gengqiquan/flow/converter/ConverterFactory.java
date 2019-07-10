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
public class ConverterFactory {

    public static Converter StringConverter() {
        return new Converter() {
            @Override
            public String convert(ResponseBody body, Type type) throws IOException {
                try {
                    return body.string();
                } finally {
                    body.close();

                }
            }
        };
    }
}
