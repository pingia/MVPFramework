package com.github.pingia.ui.common.viewpager;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

/**
 * <p>文件描述：<p>
 * <p>作者: zengll@jrrcapital.com<p>
 * <p>创建时间：2018/8/25<p>
 */
public class BaseDynamicFragmentPagerAdapter extends FragmentStatePagerAdapter {
    private Context mContext;
    private List<String > mTabNameList;
    private List<Class<? extends Fragment>> mFragmentClassList;
    public BaseDynamicFragmentPagerAdapter(FragmentManager fm, Context context, List<String> tabNameList,
                                           List<Class<? extends Fragment>> fragmentClassList) {
        super(fm);
        this.mContext = context;
        this.mTabNameList=tabNameList;
        this.mFragmentClassList = fragmentClassList;

    }


    @Override
    public Fragment getItem(int position) {
        String fragmentClassName = this.mFragmentClassList.get(position).getName();
        Fragment fragment = Fragment.instantiate(mContext, fragmentClassName);
        return fragment;
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public int getCount() {
        return (mFragmentClassList == null|| mTabNameList == null)  ? 0 :
                Math.min(mFragmentClassList.size(),mTabNameList.size());
    }

    public CharSequence getPageTitle(int position) {
        return mTabNameList == null ? null : mTabNameList.get(position);
    }
}
