package com.gengqiquan.flow.interfaces;

import java.io.IOException;

public interface Stream {
    void async(CallBack callBack);

    <T> T sync() throws IOException;

    <T,R> T transform(Transformer<? super T, ? super R>  transformer) throws IOException;
}
