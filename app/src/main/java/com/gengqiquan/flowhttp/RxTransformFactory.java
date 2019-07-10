package com.gengqiquan.flowhttp;

import com.gengqiquan.flow.http.TypeToken;
import com.gengqiquan.flow.interfaces.Transformer;

import java.lang.reflect.ParameterizedType;
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

    public RxTransformFactory(TypeToken typeToken) {
        Type superclass = typeToken.getClass().getGenericSuperclass();
        ParameterizedType parameterized = (ParameterizedType) superclass;
        this.type = parameterized.getActualTypeArguments()[0];
    }

    Type type;

    @Override
    public Type type() {
        return type;
    }

    public static Transformer<Observable<Modell>, Modell> create() {
        return new RxTransformFactory(new TypeToken<Modell>());
    }
}
