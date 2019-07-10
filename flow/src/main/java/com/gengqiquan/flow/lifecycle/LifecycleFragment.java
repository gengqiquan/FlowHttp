package com.gengqiquan.flow.lifecycle;


import android.app.Fragment;

public class LifecycleFragment extends Fragment {
    LifecycleHolder lifecycleHolder = new LifecycleHolder(false);

    public LifecycleHolder getLifecycleHolder() {
        return lifecycleHolder;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        lifecycleHolder.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        lifecycleHolder.onDestroy();
    }
}
