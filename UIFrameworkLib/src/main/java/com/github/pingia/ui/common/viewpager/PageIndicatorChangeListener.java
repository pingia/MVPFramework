package com.github.pingia.ui.common.viewpager;


import androidx.viewpager.widget.ViewPager;

import com.github.pingia.ui.common.widget.PageIndicator;

public class PageIndicatorChangeListener extends ViewPager.SimpleOnPageChangeListener{
    private PageIndicator mIndicator;
    public PageIndicatorChangeListener(PageIndicator indicator){
        this.mIndicator = indicator;
    }

    @Override
    public void onPageSelected(int position) {
        if(null == this.mIndicator){
            return;
        }

        this.mIndicator.setCurrentPage(position);
    }
}
