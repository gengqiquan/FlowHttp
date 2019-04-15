package com.gengqiquan.flow.interfaces;

import java.io.IOException;

public interface Stream {
    void async(CallBack callBack);

    <T> T sync() throws IOException;

    <T> T transform(Transformer<T>  transformer) throws IOException;
}
