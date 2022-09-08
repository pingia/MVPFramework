package com.github.pingia.ui.common.utils;

import android.content.Context;

import com.github.pingia.uiframework.R;

import cn.sunline.uicommonlib.utils.ToastUtil;

/**
 * Description:
 * Created by zenglulin@youxiang.com
 * <p>
 * Date: 2022/4/25
 */
public class BizToastUtil {
    public static final void showBizResultToast(Context context, CharSequence bizName, boolean suc, String bizMsg){
        ToastUtil.showToast(context.getApplicationContext(), suc
                        ? context.getString(R.string.do_biz_suc, bizName)
                        : context.getString(R.string.do_biz_fail, bizName, bizMsg)
                );
    }
}
