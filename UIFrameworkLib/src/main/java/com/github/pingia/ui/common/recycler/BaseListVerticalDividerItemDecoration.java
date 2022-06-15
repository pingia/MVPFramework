package com.github.pingia.ui.common.recycler;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

/**
 * author：admin on 2017/11/2.
 * mail:zengll@hztxt.com.cn
 * function: 公用的竖向列表页所使用的分割线，设置了默认的分割线样式
 */
public class BaseListVerticalDividerItemDecoration extends BaseVerticalDividerItemDecoration {

    public BaseListVerticalDividerItemDecoration(Context context) {
        super(context);

        Drawable dividerDrawable = ContextCompat.getDrawable(context, getDividerDrawableResId());
        this.setDrawable(dividerDrawable);
    }

    protected int getDividerDrawableResId(){
        return android.R.drawable.divider_horizontal_dark;
    }
}
