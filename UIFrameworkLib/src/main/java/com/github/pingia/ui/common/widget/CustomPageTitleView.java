package com.github.pingia.ui.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.github.pingia.uiframework.R;

import java.util.ArrayList;
import java.util.List;


public class CustomPageTitleView extends Toolbar {

    private List<View> mDefaultToolbarChildViews;
    private List<View> mPreToolbarChildViews;

    public CustomPageTitleView(Context context) {
        this(context, null);
    }

    public CustomPageTitleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomPageTitleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        FrameLayout framelayout = (FrameLayout) inflate(getContext(), R.layout.pingia_ui_toolbar_content, null);
        this.mDefaultToolbarChildViews = getChildViews(framelayout);
        switchToolbarContent(this.mDefaultToolbarChildViews);
    }

    public void switchDefaultToolbarContent(){
        switchToolbarContent(mDefaultToolbarChildViews);
    }

    public List<View>  getChildViews(ViewGroup contentFrameLayout){
        int count = contentFrameLayout.getChildCount();
        ArrayList<View> views = new ArrayList<>();
        for (int i =0; i< count; i++){
            View view = contentFrameLayout.getChildAt(i);
            views.add(view);
        }

        return views;
    }

    public void switchToolbarContent(List<View > childViews){
        if(mPreToolbarChildViews!=null){
            for (View v: mPreToolbarChildViews){
                removeView(v);
            }
        }

        if(null == childViews || childViews.isEmpty()) return;

        for (View v : childViews){
            Toolbar.LayoutParams lp = new Toolbar.LayoutParams(v.getLayoutParams());
            if(v.getLayoutParams() instanceof FrameLayout.LayoutParams) {
                lp.gravity = ((FrameLayout.LayoutParams) v.getLayoutParams()).gravity;
            }
            if(v.getLayoutParams() instanceof Toolbar.LayoutParams) {
                lp.gravity = ((Toolbar.LayoutParams) v.getLayoutParams()).gravity;
            }

            if(v.getParent() !=null) {
                ((ViewGroup)v.getParent()).removeView(v);
            }

            addView(v,lp);
        }

        mPreToolbarChildViews = childViews;
    }


}
