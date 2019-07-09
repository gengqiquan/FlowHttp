package com.gengqiquan.flowhttp;

import com.gengqiquan.flow.interfaces.Transformer;
import com.google.gson.Gson;

import rx.Observable;
import rx.Subscriber;

public class RxTransformFactory<T, R> implements Transformer<T, R> {
    @Override
    public T transform(final R from) {
        return (T) Observable.create(new Observable.OnSubscribe<R>() {
            @Override
            public void call(final Subscriber<? super R> subscriber) {
                subscriber.onNext(from);
                subscriber.onCompleted();
            }
        });
    }

}
