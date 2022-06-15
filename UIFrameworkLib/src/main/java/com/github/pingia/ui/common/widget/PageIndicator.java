package com.github.pingia.ui.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class PageIndicator extends LinearLayout{

    private int mHorizontalSpacing = 10;
    private int mImgResId;  //
    private int mIndicatorImgResId;//当前页指引器图标

    private int mPageCount = 3;     //默认初始化3个指示器图标
    private int mCurrentPage = -1;       //当前页索引        -1代表当前未选中任何索引页

    public PageIndicator(Context context) {
        super(context);
        init();
    }

    public PageIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);

        mHorizontalSpacing = attrs.getAttributeIntValue(null, "horizontalSpacing", 0);
        mImgResId = attrs.getAttributeResourceValue(null,"image",0);
        mIndicatorImgResId = attrs.getAttributeResourceValue(null, "indicatorImage",0);

        init();
    }

    public void setImageResId(int resImgId){
        this.mImgResId = resImgId;

        int size = getChildCount();
        for (int i =0; i<size; i ++){
            ImageView iv = (ImageView) getChildAt(i);
            iv.setImageResource(mImgResId);
        }
    }

    public void setIndicatorImageResId(int indicatorImageResId){
        this.mIndicatorImgResId = indicatorImageResId;
    }

    private void init(){
        this.removeAllViews();
        LayoutParams childlp = new LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        childlp.leftMargin = mHorizontalSpacing;
        childlp.rightMargin = childlp.leftMargin;

        for (int i =0; i<mPageCount; i ++){
            ImageView iv = new ImageView(this.getContext());
            iv.setImageResource(mImgResId);

            iv.setLayoutParams(childlp);

            addView(iv);
        }

    }

    private boolean checkPageIndex(int postion){
        return (mPageCount > postion && postion >=0);
    }

    public void setPageCount(int count){
        if(count != mPageCount) {
            mPageCount = count;
            init();
        }
    }

    public int getPageCount(){
        return this.mPageCount;
    }

    public void setCurrentPage(int page){
        if(page != this.mCurrentPage) {

            if(checkPageIndex(page)){
                ((ImageView)this.getChildAt(page)).setImageResource(mIndicatorImgResId);
            }

            if(checkPageIndex(this.mCurrentPage)) {
                ((ImageView) this.getChildAt(this.mCurrentPage)).setImageResource(mImgResId);
            }

            this.mCurrentPage = page;

        }


    }

    public int getCurrentPage(){
        return this.mCurrentPage;
    }
}
