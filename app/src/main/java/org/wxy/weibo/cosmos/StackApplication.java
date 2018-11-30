package org.wxy.weibo.cosmos;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import java.util.Iterator;
import java.util.Stack;


public class StackApplication extends Application implements Application.ActivityLifecycleCallbacks {
    private static Stack<Activity> stack;
    private static StackApplication application;
    private int mActivityCount = 0;


    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        stack = new Stack<>();
        this.registerActivityLifecycleCallbacks(this);
    }

    public static StackApplication getStack() {
        return application;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        stack.add(activity);
    }

    public void removeActivity(Activity activity) {
        if (activity != null) {
            stack.remove(activity);
        }
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        Activity activity = stack.lastElement();
        return activity;
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        Activity activity = stack.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null && !activity.isFinishing()) {
            activity.finish();
            stack.remove(activity);
            //activity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<? extends Activity> cls) {
        for (Iterator<Activity> it = stack.iterator(); it.hasNext(); ) {
            Activity activity = it.next();
            if (activity != null && !activity.isFinishing() && activity.getClass().equals(cls)) {
                activity.finish();
                it.remove();
            }
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<? extends Activity>... classes) {
        if (classes == null || classes.length == 0) {
            return;
        }
        for (Iterator<Activity> it = stack.iterator(); it.hasNext(); ) {
            Activity activity = it.next();
            if (activity != null && !activity.isFinishing()) {
                for (Class<? extends Activity> aClass : classes) {
                    if (activity.getClass().equals(aClass)) {
                        activity.finish();
                        it.remove();
                        break;
                    }
                }
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (Iterator<Activity> it = stack.iterator(); it.hasNext(); ) {
            Activity activity = it.next();
            if (activity != null && !activity.isFinishing()) {
                activity.finish();
                it.remove();
            }
        }
        stack.clear();
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity(Class<? extends Activity> exclude) {
        for (Iterator<Activity> it = stack.iterator(); it.hasNext(); ) {
            Activity activity = it.next();
            if (activity != null && !activity.isFinishing() && !activity.getClass().equals(exclude)) {
                activity.finish();
                it.remove();
            }
        }
    }

    /**
     * 获取指定的Activity
     */
    public Activity getActivity(Class<? extends Activity> cls) {
        if (stack != null)
            for (Activity activity : stack) {
                if (activity.getClass().equals(cls)) {
                    return activity;
                }
            }
        return null;
    }

    public boolean isActivity(int position, Class<? extends Activity> cls) {
        if (stack != null && stack.size() > 0 && position >= 0 && stack.size() > position) {
            Activity activity = stack.get(position);
            return activity != null && activity.getClass().equals(cls);
        }
        return false;
    }

    public boolean isLastActivity(int position, Class<? extends Activity> cls) {
        if (stack != null && stack.size() > 0 && position >= 0 && stack.size() > position) {
            Activity activity = stack.get(stack.size() - 1 - position);
            return activity != null && activity.getClass().equals(cls);
        }
        return false;
    }

    /**
     * 退出应用程序
     */
    @SuppressWarnings("deprecation")
    public void AppExit(Context context) {
        try {
            finishAllActivity();
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            activityManager.restartPackage(context.getPackageName());
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public int getActivityCount() {
        return mActivityCount;
    }


    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        mActivityCount++;
        getStack().addActivity(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        /*if(activity instanceof BaseActivity){
            activity.setTitle("绑卡");
        }*/
    }

    @Override
    public void onActivityPaused(Activity activity) {
    }

    @Override
    public void onActivityStopped(Activity activity) {
        mActivityCount--;
        if (mActivityCount < 0) {
            mActivityCount = 0;
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        getStack().removeActivity(activity);
    }
}
