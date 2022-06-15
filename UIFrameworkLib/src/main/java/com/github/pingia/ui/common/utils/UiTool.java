package com.github.pingia.ui.common.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

/**
 * author：admin on 2017/8/16.
 * mail:zengll@hztxt.com.cn
 * function:
 */
public final class UiTool {


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int getVersionCode(Context context) {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static String getVersionName(Context context) {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            String version = pi.versionName;
            if (TextUtils.isEmpty(version)) {
                return "";
            } else {
                return version;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getActivityLabel(Activity activity){
        try {
            ActivityInfo ai = activity.getPackageManager().getActivityInfo(activity.getComponentName(), 0);
            int resId = ai.labelRes;
            if(resId!=0) {
                return activity.getString(resId);
            }else{
                return String.valueOf(ai.nonLocalizedLabel);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getApplicationLabel(Context context){
        try {
            ApplicationInfo pi = context.getPackageManager().getApplicationInfo(context.getPackageName(), 0);

            int resId = pi.labelRes;
            if(resId!=0) {
                return context.getString(resId);
            }else{
                return String.valueOf(pi.nonLocalizedLabel);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
