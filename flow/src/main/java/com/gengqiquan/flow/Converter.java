package com.gengqiquan.flow;

import java.io.IOException;

import okhttp3.ResponseBody;

/**
 * 请求结果对象类型转换器
 *
 * @author gengqiquan
 * @date 2019-07-09 15:25
 */
public abstract class Converter {
    public abstract <T> T convert(ResponseBody body) throws IOException;

    public static Converter Default() {
        return new Converter() {
            @Override
            public String convert(ResponseBody body) throws IOException {
                try {
                    return body.string();
                } finally {
                    body.close();

                }
            }
        };
    }
}
