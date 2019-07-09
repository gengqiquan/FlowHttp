package com.gengqiquan.flow.lifecycle;


import android.app.Fragment;

public class RequestManagerFragment extends Fragment {
    ActivityFragmentLifecycle fragmentLifecycle = new ActivityFragmentLifecycle();

    public ActivityFragmentLifecycle getFragmentLifecycle() {
        return fragmentLifecycle;
    }

    @Override
    public void onStop() {
        super.onStop();
        fragmentLifecycle.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        fragmentLifecycle.onDestroy();
    }
}
