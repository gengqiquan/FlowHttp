package com.gengqiquan.flow.lifecycle;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * 生命周期调度提供类
 * Reference Glide
 *
 * @author gengqiquan
 * @date 2019-07-09 17:57
 */
public class LifecycleProvider implements Handler.Callback {
    static final String FRAGMENT_TAG = "com.gqq.flow.manager";

    /**
     * The singleton instance of LifecycleRetriever.
     */
    private static final LifecycleProvider INSTANCE = new LifecycleProvider();

    private static final int ID_REMOVE_FRAGMENT_MANAGER = 1;
    private static final int ID_REMOVE_SUPPORT_FRAGMENT_MANAGER = 2;
    private static final String TAG = "Lifecycle";
    Handler handler;

    public LifecycleProvider() {
        handler = new Handler(Looper.getMainLooper(), this /* Callback */);
    }

    /**
     * Retrieves and returns the LifecycleRetriever singleton.
     */
    public static LifecycleProvider get() {
        return INSTANCE;
    }

    /**
     * Application can not listen
     */
    private LifecycleHolder get(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("You cannot start a load on a null Context");
        } else if (!isOnMainThread() && !(context instanceof Application)) {
            if (context instanceof FragmentActivity) {
                return get((FragmentActivity) context);
            } else if (context instanceof Activity) {
                return get((Activity) context);
            } else if (context instanceof ContextWrapper) {
                return get(((ContextWrapper) context).getBaseContext());
            }
        }

        return getApplicationLifecycle();
    }

    private volatile LifecycleHolder applicationLifecycle;

    public LifecycleHolder getApplicationLifecycle() {
        // Either an application context or we're on a background thread.
        if (applicationLifecycle == null) {
            synchronized (this) {
                if (applicationLifecycle == null) {
                    // Normally pause/resume is taken care of by the fragment we add to the fragment or activity.
                    // However, in this case since the manager attached to the application will not receive lifecycle
                    // events,
                    applicationLifecycle = new LifecycleHolder(true);
                }
            }
        }
        return applicationLifecycle;
    }

    public LifecycleHolder get(FragmentActivity activity) {
        if (!isOnMainThread()) {
            return get(activity.getApplicationContext());
        } else {
            assertNotDestroyed(activity);
            FragmentManager fm = activity.getSupportFragmentManager();
            return supportFragmentGet(activity, fm);
        }
    }

    public LifecycleHolder get(Activity activity) {
        if (!isOnMainThread()) {
            return get(activity.getApplicationContext());
        } else {
            assertNotDestroyed(activity);
            android.app.FragmentManager fm = activity.getFragmentManager();
            return fragmentGet(activity, fm);
        }
    }

    public LifecycleHolder get(Fragment fragment) {
        if (fragment.getActivity() == null) {
            throw new IllegalArgumentException("You cannot start a load on a fragment before it is attached");
        }
        if (!isOnMainThread()) {
            return get(fragment.getActivity().getApplicationContext());
        } else {
            FragmentManager fm = fragment.getChildFragmentManager();
            return supportFragmentGet(fragment.getActivity(), fm);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public LifecycleHolder get(android.app.Fragment fragment) {
        if (fragment.getActivity() == null) {
            throw new IllegalArgumentException("You cannot start a load on a fragment before it is attached");
        }
        if (!isOnMainThread() || Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return get(fragment.getActivity().getApplicationContext());
        } else {
            android.app.FragmentManager fm = fragment.getChildFragmentManager();
            return fragmentGet(fragment.getActivity(), fm);
        }
    }


    LifecycleHolder fragmentGet(Context context, android.app.FragmentManager fm) {
        LifecycleFragment current = getLifecycleFragment(fm);
        return current.getLifecycleHolder();

    }

    LifecycleHolder supportFragmentGet(Context context, FragmentManager fm) {
        LifecycleSupportFragment current = getLifecycleSupportFragment(fm);
        return current.getLifecycleHolder();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    LifecycleFragment getLifecycleFragment(final android.app.FragmentManager fm) {
        LifecycleFragment current = (LifecycleFragment) fm.findFragmentByTag(FRAGMENT_TAG);
        if (current == null) {
            current = pendingLifecycleFragments.get(fm);
            if (current == null) {
                current = new LifecycleFragment();
                pendingLifecycleFragments.put(fm, current);
                fm.beginTransaction().add(current, FRAGMENT_TAG).commitAllowingStateLoss();
                handler.obtainMessage(ID_REMOVE_FRAGMENT_MANAGER, fm).sendToTarget();
            }
        }
        return current;
    }

    LifecycleSupportFragment getLifecycleSupportFragment(final FragmentManager fm) {
        LifecycleSupportFragment current = (LifecycleSupportFragment) fm.findFragmentByTag(FRAGMENT_TAG);
        if (current == null) {
            current = pendingLifecycleSupportFragments.get(fm);
            if (current == null) {
                current = new LifecycleSupportFragment();
                pendingLifecycleSupportFragments.put(fm, current);
                fm.beginTransaction().add(current, FRAGMENT_TAG).commitAllowingStateLoss();
                handler.obtainMessage(ID_REMOVE_SUPPORT_FRAGMENT_MANAGER, fm).sendToTarget();
            }
        }
        return current;
    }

    final Map<android.app.FragmentManager, LifecycleFragment> pendingLifecycleFragments =
            new HashMap<>();
    /**
     * Pending adds for SupportLifecycleFragments.
     */
    final Map<FragmentManager, LifecycleSupportFragment> pendingLifecycleSupportFragments =
            new HashMap<>();

    @Override
    public boolean handleMessage(Message message) {
        boolean handled = true;
        Object removed = null;
        Object key = null;
        switch (message.what) {
            case ID_REMOVE_FRAGMENT_MANAGER:
                android.app.FragmentManager fm = (android.app.FragmentManager) message.obj;
                key = fm;
                removed = pendingLifecycleFragments.remove(fm);
                break;
            case ID_REMOVE_SUPPORT_FRAGMENT_MANAGER:
                FragmentManager supportFm = (FragmentManager) message.obj;
                key = supportFm;
                removed = pendingLifecycleSupportFragments.remove(supportFm);
                break;
            default:
                handled = false;
        }
        if (handled && removed == null && Log.isLoggable(TAG, Log.WARN)) {
            Log.w(TAG, "Failed to remove expected request manager fragment, manager: " + key);
        }
        return handled;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private static void assertNotDestroyed(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && activity.isDestroyed()) {
            throw new IllegalArgumentException("You cannot start a load for a destroyed activity");
        }
    }

    /**
     * Returns {@code true} if called on the main thread, {@code false} otherwise.
     */
    private static boolean isOnMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

}
