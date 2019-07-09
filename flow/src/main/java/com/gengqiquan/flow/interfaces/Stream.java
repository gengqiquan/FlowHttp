package com.gengqiquan.flow.interfaces;

import java.io.IOException;

public interface Stream {
    <T> void listen(CallBack callBack);

    <T> T await() throws IOException;

    <T, R> T transform(Transformer<? super T, ? super R> transformer) throws IOException;
}
