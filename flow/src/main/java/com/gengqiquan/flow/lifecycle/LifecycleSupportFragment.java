package com.gengqiquan.flow.lifecycle;


import android.content.Context;
import android.support.v4.app.Fragment;

public class LifecycleSupportFragment extends Fragment {
    LifecycleHolder lifecycleHolder = new LifecycleHolder(false);

    public LifecycleHolder getLifecycleHolder() {
        return lifecycleHolder;
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
}
