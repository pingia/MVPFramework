package com.github.pingia.ui.common.viewpager;

import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

/**
 * Created by zenglulin on 2017/10/15.
 */

public class SimpleViewPagerAdapter extends PagerAdapter {
    private List<View> mViews;
    public void setViews(List<View> views){
        this.mViews = views;
        notifyDataSetChanged();
    }

    private boolean checkPageIndex(int postion){
        return (null != this.mViews && this.mViews.size() > postion);
    }

    @Override
    public Object  instantiateItem(ViewGroup container,
                                   int position){
        if(checkPageIndex(position)) {
            View view = mViews.get(position);
            container.addView(view);
            return view;
        }

        return null;


    }

    @Override
    public void destroyItem (ViewGroup container,
                             int position,
                             Object object){
        if(checkPageIndex(position)) {
            container.removeView(mViews.get(position));
        }
    }

    @Override
    public int getCount() {
        if(null != mViews) {
            return this.mViews.size();
        }
        return 0;
    }

    public Object getItem(int position){
        if(checkPageIndex(position)){
            return this.mViews.get(position);
        }

        return null;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
