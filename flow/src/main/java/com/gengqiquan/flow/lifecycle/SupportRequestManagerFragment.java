package com.gengqiquan.flow.lifecycle;


import android.support.v4.app.Fragment;

public class SupportRequestManagerFragment extends Fragment {
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
