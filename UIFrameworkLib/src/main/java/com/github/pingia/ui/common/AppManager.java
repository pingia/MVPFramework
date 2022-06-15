/**
 * 应用程序管理器。处理activity的添加删除等操作
 */
package com.github.pingia.ui.common;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import java.util.Stack;

/**
 * author：admin on 2017/8/14.
 * mail:zengll@hztxt.com.cn
 * function: 管理一个app，跳转过的页面堆栈，方便统一管理，也便于在应用退出时，清除堆栈里的所有activity，保证应用能正常被杀掉
 */
public enum AppManager {
	INSTANCE;
	private Stack<Activity> activityStack;

	public Stack<Activity> getActivityStack(){
		return this.activityStack;
	}
	
	public void addActivity(Activity activity){
		if(activityStack==null){
			activityStack=new Stack<Activity>();
		}
		activityStack.add(activity);
	}
	
	public Activity currentActivity(){
		Activity activity=activityStack.lastElement();
		return activity;
	}
	
	public void finishActivity(){
		Activity activity=activityStack.lastElement();
		finishActivity(activity);
	}
	
	public void finishActivity(Activity activity){
		if(activity!=null){
			activityStack.remove(activity);
			activity.finish();
			activity=null;
		}
	}
	
	public void finishActivity(Class<?> cls){
		for (Activity activity : activityStack) {
			if(activity.getClass().equals(cls) ){
				finishActivity(activity);
			}
		}
	}
	
	public void finishAllActivity(){
		for (int i = 0, size = activityStack.size(); i < size; i++){
            if (null != activityStack.get(i)){
            	activityStack.get(i).finish();
            }
	    }
		activityStack.clear();
	}
	
	public void AppExit(Context context) {
		try {
			finishAllActivity();
			ActivityManager activityMgr= (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
			activityMgr.restartPackage(context.getPackageName());
			System.exit(0);
		} catch (Exception e) {	}
	}

}
