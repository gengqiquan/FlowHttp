package com.gengqiquan.flow.interfaces;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * 流变换类，实现该接口具备变换请求结果类型能力
 *
 * @author gengqiquan
 * @date 2019-07-09 15:29
 */
public interface Transformer<T, R> {
    T transform(R from) throws IOException;

    Type type();
}
