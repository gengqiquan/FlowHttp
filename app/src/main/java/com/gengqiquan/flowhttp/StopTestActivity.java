package com.gengqiquan.flowhttp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.gengqiquan.flow.Flow;
import com.gengqiquan.flow.http.Result;
import com.gengqiquan.flow.lifecycle.LifeEvent;

public class StopTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("StopTestActivity");
        findViewById(R.id.tv_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Flow.with("recommendPoetry")
                        .bind(StopTestActivity.this)
                        .lifeCircle(LifeEvent.STOP)
                        .listen(new Result<Modell>() {
                            @Override
                            public void success(Modell bean) {
                                ((TextView) findViewById(R.id.tv_btn)).setText(bean.toString());
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
