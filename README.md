
* 基于OKhttp的网络封装库
* 简单，高可配置，易使用
* 直接返回Java对象
### 示例
* 同步
```
 Modell model=Flow.with("recommendPoetry")
                       .await();
```
* 异步 回调在主线程
```
               Flow.with("recommendPoetry")
                        .listen(new Result<Modell>() {
                            @Override
                            public void success(Modell bean) {
                                ((TextView) findViewById(R.id.tv_btn)).setText(bean.toString());
                            }
                        });
```
* 变换流，例：转换为Rx流
```
 Flow.with("recommendPoetry")
       .transform(new RxTransformFactory<Observable<Modell>, Modell>())
                                    .subscribeOn(Schedulers.io())
                                    .subscribe(new Subscriber<Modell>() {
                                        @Override
                                        public void onCompleted() {

                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            e.printStackTrace();
                                        }

                                        @Override
                                        public void onNext(Modell s) {
                                            Log.e("MainActivity", "success: " + s.toString());
                                        }
                                    });
```

### Future
绑定界面生命周期ondistory自动取消 提供两个参数重载绑定到具体生命周期
上传文件统一服务bind
请求出错参数环境写入本地文件