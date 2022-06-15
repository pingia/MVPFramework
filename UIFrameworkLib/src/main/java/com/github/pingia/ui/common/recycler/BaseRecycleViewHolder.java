package com.github.pingia.ui.common.recycler;

import android.content.Context;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

/**
 * author：admin on 2017/9/12.
 * mail:zengll@hztxt.com.cn
 * function:
 */
public class BaseRecycleViewHolder extends RecyclerView.ViewHolder {
    private final SparseArray<View> mViewIds;   //保存item视图中的viewId和view的映射
    private View mItemView;
    private Context mContext;
    public BaseRecycleViewHolder(View itemView) {
        super(itemView);
        mItemView = itemView;
        mContext = itemView.getContext();
        mViewIds = new SparseArray<>();
    }

    /**
     * 通过视图id，找到对应视图
     * 说明:
     * 如果在缓存中未找到对应视图，那么通过findViewById返回对应视图并放入缓存；否则直接返回缓存中的视图
     * @param viewId
     * @param <T>
     * @return
     */
    public   <T extends View> T getView(int viewId){
        View view;
        if(null == (view = mViewIds.get(viewId))){
            View createdView = mItemView.findViewById(viewId);

            if(null != createdView) {
                mViewIds.put(viewId, createdView);
                return (T)createdView;
            }else{
                return null;
            }

        }
        return (T)view;
    }

    public BaseRecycleViewHolder setText(int tvId,CharSequence cs){
        TextView tv = getView(tvId);
        if(tv != null) {
            tv.setText(cs);
        }
        return this;
    }

    public BaseRecycleViewHolder setText(int tvId,@StringRes int textResId){
        TextView tv = getView(tvId);
        if(tv != null) {
            tv.setText(textResId);
        }
        return this;
    }

    public BaseRecycleViewHolder setTextColor(int tvId,@ColorRes int textColorResId){
        TextView tv = getView(tvId);
        if(tv!=null) {
            tv.setTextColor(ContextCompat.getColor(mContext,textColorResId));
        }
        return this;
    }

    public BaseRecycleViewHolder setTextSizeDp(int tvId,float dp){
        TextView tv = getView(tvId);
        if(tv!=null) {
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, dp);
        }
        return this;
    }

    public BaseRecycleViewHolder setBgColor(int viewId, @ColorRes int bgColorResId){
        View view = getView(viewId);
        if(view!=null) {
            view.setBackgroundColor(ContextCompat.getColor(mContext,bgColorResId));
        }

        return this;
    }

    public BaseRecycleViewHolder setChecked(int viewId, boolean isChecked){
        CheckBox cb = getView(viewId);
        if(cb!=null) {
            cb.setChecked(isChecked);
        }

        return this;
    }

    public BaseRecycleViewHolder setVisible(int viewId, boolean isVisible){
        View view = getView(viewId);
        if(view!=null) {
            view.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        }

        return this;
    }
}
