package com.gengqiquan.flowhttp;

import com.gengqiquan.flow.interfaces.Transformer;

import rx.Observable;
import rx.Subscriber;

public class RxTransformFactory<T> implements Transformer<T> {
    @Override
    public T transform(final String from) {
        return (T)Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> subscriber) {
                subscriber.onNext(from);
                subscriber.onCompleted();
            }
        });
    }

}
