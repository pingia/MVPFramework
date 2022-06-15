package com.github.pingia.ui.common.recycler;


import android.util.SparseIntArray;

import androidx.annotation.LayoutRes;

import java.util.List;


public abstract class MultiItemTypeDelegate<T> {

    private static final int DEFAULT_VIEW_TYPE = -99;
    public static final int TYPE_NOT_FOUND = -1;
    private SparseIntArray layouts;

    public final int getItemViewType(List<T> data, int position) {
        T item = data.get(position);
        return item != null ? getItemType(item) : DEFAULT_VIEW_TYPE;
    }

    protected abstract int getItemType(T t);

    public final int getLayoutId(int viewType) {
        return this.layouts.get(viewType, TYPE_NOT_FOUND);
    }

    private void addItemType(int type, @LayoutRes int layoutResId) {
        if (this.layouts == null) {
            this.layouts = new SparseIntArray();
        }
        this.layouts.put(type, layoutResId);
    }

    public MultiItemTypeDelegate registerItemType(int type, @LayoutRes int layoutResId) {
        addItemType(type, layoutResId);
        return this;
    }

    public MultiItemTypeDelegate registerItemTypeAutoIncrease(@LayoutRes int... layoutResIds) {
        for (int i = 0; i < layoutResIds.length; i++) {
            addItemType(i, layoutResIds[i]);
        }
        return this;
    }

}