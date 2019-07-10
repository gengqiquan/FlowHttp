package com.gengqiquan.flowhttp;

import com.gengqiquan.flow.interfaces.Transformer;

import java.lang.reflect.Type;

import rx.Observable;
import rx.Subscriber;

public class RxTransformFactory<T> implements Transformer<Observable<T>, T> {


    @Override
    public Observable<T> transform(final T from) {
        return (Observable<T>) Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(final Subscriber<? super T> subscriber) {
                subscriber.onNext(from);
                subscriber.onCompleted();
            }
        });
    }

    public RxTransformFactory(Type type) {
        this.type = type;
    }

    Type type;

    @Override
    public Type type() {
        return type;
    }

    public static <T> Transformer<Observable<T>, T> create(TypeToken typeToken) {
        return new RxTransformFactory(typeToken.getType());
    }
}
