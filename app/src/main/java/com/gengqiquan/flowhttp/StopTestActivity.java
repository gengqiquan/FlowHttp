package com.gengqiquan.flowhttp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.gengqiquan.flow.Flow;
import com.gengqiquan.flow.http.SimpleResult;
import com.gengqiquan.flow.lifecycle.LifeEvent;

import java.util.List;

public class StopTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("StopTestActivity");
        findViewById(R.id.tv_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Flow.with("getJoke?page=1&count=2&type=video")
                        .bind(StopTestActivity.this)
                        .lifeCircle(LifeEvent.STOP)
                        .listen(new SimpleResult<Modell<List<Detail>>>() {
                            @Override
                            public void success(Modell<List<Detail>> bean) {
                                ((TextView) findViewById(R.id.tv_btn)).setText(bean.getResult().get(0).toString());
                            }

                            @Override
                            public void error(@NonNull Exception e) {

                            }
                        });
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(StopTestActivity.this, DestroyTestActivity.class));

                    }
                }, 1000);
            }
        });
    }
}
