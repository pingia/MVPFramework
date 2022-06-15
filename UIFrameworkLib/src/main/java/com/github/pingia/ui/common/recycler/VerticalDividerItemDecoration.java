package com.github.pingia.ui.common.recycler;

/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class VerticalDividerItemDecoration extends RecyclerView.ItemDecoration {
    public static final int VERTICAL = LinearLayout.VERTICAL;

    private static final int[] ATTRS = new int[]{ android.R.attr.listDivider };

    private Drawable mDivider;

    private final Rect mBounds = new Rect();

    /**
     * Creates a divider {@link RecyclerView.ItemDecoration} that can be used with a
     * {@link LinearLayoutManager}.
     *
     * @param context Current context, it will be used to access resources.
     */
    public VerticalDividerItemDecoration(Context context) {
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        a.recycle();
    }

    /**
     * Sets the {@link Drawable} for this divider.
     *
     * @param drawable Drawable that should be used as a divider.
     */
    public void setDrawable(@NonNull Drawable drawable) {
        if (drawable == null) {
            throw new IllegalArgumentException("Drawable cannot be null.");
        }
        mDivider = drawable;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (parent.getLayoutManager() == null) {
            return;
        }

        drawVertical(c, parent);

    }

    private void drawVertical(Canvas canvas, RecyclerView parent) {
        canvas.save();
        final int left;
        final int right;
        if (parent.getClipToPadding()) {
            left = parent.getPaddingLeft();
            right = parent.getWidth() - parent.getPaddingRight();
            canvas.clipRect(left, parent.getPaddingTop(), right,
                    parent.getHeight() - parent.getPaddingBottom());
        } else {
            left = 0;
            right = parent.getWidth();
        }

        //表格布局 ，默认数据项之间不绘制分割线
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            canvas.restore();
            return;
        }

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            int adapterPosition = parent.getChildAdapterPosition(child);

            //只有数据项之间才绘制分割线
            if(!isFixedViewType(parent.getAdapter(), adapterPosition)) {

                boolean needDivider = true;
                int itemCount = parent.getAdapter().getItemCount();
                if( adapterPosition == itemCount -1){       //最后一个数据项底部不绘制分割线
                    needDivider  = false;
                }

                int nextAdapterPostion = adapterPosition + 1;

                //如果最后一个数据项的下一项是footer/more（加载更多），则不绘制分割线，因为他们默认在开头带了分割线
                if(nextAdapterPostion < parent.getAdapter().getItemCount()){
                    if(isFixedViewType(parent.getAdapter(), nextAdapterPostion)){
                        needDivider = false;
                    }
                }

                if(needDivider) {
                    parent.getDecoratedBoundsWithMargins(child, mBounds);
                    final int bottom = mBounds.bottom + Math.round(ViewCompat.getTranslationY(child));
                    final int top = bottom - mDivider.getIntrinsicHeight();
                    mDivider.setBounds(left, top, right, bottom);
                    mDivider.draw(canvas);
                }
            }
        }
        canvas.restore();
    }

    protected boolean isFixedViewType(RecyclerView.Adapter adapter, int adapterPosition) {
        return false;       //默认没有fixviewtype类型
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
    }
}

