
* 基于OKhttp的网络封装库
* 简单，高可配置，易使用
* 直接返回Java对象,对象支持泛型
* 异步请求直接绑定activity or fragment生命周期自动取消请求

### 示例

* 初始化

```
Flow.init(getApplication(), new Flow.ConfigBuilder("https://api.apiopen.top/")
                .converter( new Converter() {
                                   Gson gson = new Gson();

                                   @Override
                                   public Modell convert(ResponseBody value) throws IOException {
                                       String json = "";
                                       try {
                                           json = value.string();
                                       } finally {
                                           value.close();

                                           Modell m = null;
                                           try {
                                               m = gson.fromJson(json, Modell.class);
                                           } catch (JsonSyntaxException e) {
                                               m = new Modell();
                                           } finally {
                                               return m;
                                           }
                                       }

                                   }
                               })
              );
```

* 同步

```
 Modell model=Flow.with("recommendPoetry").await();

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

* 指定请求方式

```
                    Flow.with("recommendPoetry")
                        .put()
                        .listen(new Result<Modell>() {
                            @Override
                            public void success(Modell bean) {
                                ((TextView) findViewById(R.id.tv_btn)).setText(bean.toString());
                            }
                        });
```

* 绑定生命周期

```
                    Flow.with("recommendPoetry")
                        .bind(StopTestActivity.this)
                  //    .lifeCircle(LifeEvent.STOP)//绑定到指定生命周期
                        .listen(new Result<Modell>() {
                            @Override
                            public void success(Modell bean) {
                                ((TextView) findViewById(R.id.tv_btn)).setText(bean.toString());
                            }
                        });
```

* 变换流

 例：转换为Rx流

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
###Gradle

```
compile 'com.gengqiquan:flow:0.0.2'
```

### Future

* loading显示取消绑定请求
* 上传文件统一服务bind
* 请求出错参数环境写入本地文件