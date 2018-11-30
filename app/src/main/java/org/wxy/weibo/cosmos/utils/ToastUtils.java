package org.wxy.weibo.cosmos.utils;

import android.content.Context;
import android.widget.Toast;

import org.wxy.weibo.cosmos.MainActivity;

/**
 * Created by wxy on 2018/7/5.
 */

public class ToastUtils {
    private Toast mToast;
    private static ToastUtils mToastUtils;

    private ToastUtils() {
        mToast = Toast.makeText(MainActivity.mainActivity(), null, Toast.LENGTH_SHORT);
    }

    public static synchronized ToastUtils getInstanc() {
        if (null == mToastUtils) {
            mToastUtils = new ToastUtils();
        }
        return mToastUtils;
    }
    /**
     * 显示toast
     *
     * @param toastMsg
     */
    public void showToast(int toastMsg) {
        mToast.setText(toastMsg);
        mToast.show();
    }

    /**
     * 显示toast
     *
     * @param toastMsg
     */
    public void showToast(String toastMsg) {
        mToast.setText(toastMsg);
        mToast.show();
    }

    /**
     * 取消toast，在activity的destory方法中调用
     */
    public void destory() {
        if (null != mToast) {
            mToast.cancel();
            mToast = null;
        }
        mToastUtils = null;
    }

}
