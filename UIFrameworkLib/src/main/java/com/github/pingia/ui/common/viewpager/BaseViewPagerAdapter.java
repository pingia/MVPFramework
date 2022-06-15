package com.github.pingia.ui.common.viewpager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>文件描述：适配动态数据的pagerAdapter,使用方式类似BaseAdapter<p>
 * <p>作者: zengll@jrrcapital.com<p>
 * <p>创建时间：2018/8/23<p>
 */
public abstract class BaseViewPagerAdapter<T> extends PagerAdapter {
    private List<T> mDataList;
    private List<View> mViewList;

    private LayoutInflater mInflater;
    private Context mContext;

    private int notifyCount;

    public BaseViewPagerAdapter(Context context){
        this.mViewList = new ArrayList<>();
        this.mContext = context;
    }

    protected Context getContext() {
        return this.mContext;
    }

        protected LayoutInflater getLayoutInflater(){
            if(null == mInflater){
                mInflater = LayoutInflater.from(mContext);
            }

            return mInflater;
        }

        public void setItems(List<T> list){
            this.mDataList = list;
            notifyDataSetChanged();
        }

        public void addItems(List<T> items) {
            if (mDataList != null && items != null && !items.isEmpty()) {
                mDataList.addAll(items);
                notifyDataSetChanged();
            }

        }

        public void addItem(T obj) {
            if (mDataList != null) {
                mDataList.add(obj);
                notifyDataSetChanged();
            }

        }

        public void addItem(int pos, T obj) {
            if (mDataList != null) {
                mDataList.add(pos, obj);
                notifyDataSetChanged();
            }

        }

        public void removeItem(T obj) {
            if (mDataList != null) {
                mDataList.remove(obj);
                notifyDataSetChanged();
            }
        }

        public void removeItem(int position) {
            if(mDataList != null && mDataList.size() > position) {
                mDataList.remove(position);
                notifyDataSetChanged();
            }
        }

        public void clear() {
            if (mDataList != null) {
                mDataList.clear();
                notifyDataSetChanged();
            }
        }

        @Override
        public int getCount() {
            return mDataList == null ? 0:mDataList.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container,int position){
            View view;
            if (mViewList.size() <= position) {
                view = getView(position, container);
                mViewList.add(view);
            } else {
                view = mViewList.get(position);
            }
            if (view.getParent() == null){
                container.addView(view);
            }
            return view;
        }

        @Override
        public void destroyItem (ViewGroup container,int position,Object object){
            if (mViewList.size() > position) {
                container.removeView(mViewList.get(position));
            }
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        public T getItem(int position) {
            return mDataList.get(position);
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            if (notifyCount > 0) {
                notifyCount--;
                return POSITION_NONE;
            }
            return super.getItemPosition(object);
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
            notifyCount = getCount();
        }

        public abstract View getView(int position, ViewGroup parent);

    }
