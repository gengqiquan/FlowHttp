package com.gengqiquan.flow.lifecycle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.WeakHashMap;


public class ActivityFragmentLifecycle implements LifecycleListener {
    private final Set<LifecycleListener> lifecycleListeners =
            Collections.newSetFromMap(new WeakHashMap<LifecycleListener, Boolean>());
    private boolean isDestroyed;
    private boolean isStop;

    public void addListener(LifecycleListener listener) {
        lifecycleListeners.add(listener);

        if (isDestroyed) {
            listener.onDestroy();
        } else if (isStop) {
            listener.onStop();
        }
    }

    @Override
    public void onStop() {
        isStop = true;
        for (LifecycleListener lifecycleListener : getSnapshot(lifecycleListeners)) {
            lifecycleListener.onStop();
        }
    }

    @Override
    public void onDestroy() {
        isDestroyed = true;
        for (LifecycleListener lifecycleListener : getSnapshot(lifecycleListeners)) {
            lifecycleListener.onDestroy();
        }
    }

    /**
     * Returns a copy of the given list that is safe to iterate over and perform actions that may
     * modify the original list.
     *
     * <p> See #303 and #375. </p>
     */
    public static <T> List<T> getSnapshot(Collection<T> other) {
        // toArray creates a new ArrayList internally and this way we can guarantee entries will not
        // be null. See #322.
        List<T> result = new ArrayList<T>(other.size());
        for (T item : other) {
            result.add(item);
        }
        return result;
    }
}
