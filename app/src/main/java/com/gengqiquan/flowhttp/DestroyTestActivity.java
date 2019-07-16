package com.gengqiquan.flowhttp;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.gengqiquan.flow.Flow;
import com.gengqiquan.flow.http.SimpleResult;

public class DestroyTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("DestroyTestActivity");
        findViewById(R.id.tv_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Flow.with("recommendPoetry")
                        .bind(DestroyTestActivity.this)
                        .listen(new SimpleResult<Modell>() {
                            @Override
                            public void success(Modell bean) {
                                ((TextView) findViewById(R.id.tv_btn)).setText(bean.toString());
                            }
                        });
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 1000);
            }
        });
    }


}
