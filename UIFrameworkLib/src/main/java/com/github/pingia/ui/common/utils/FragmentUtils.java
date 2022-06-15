package com.github.pingia.ui.common.utils;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import static androidx.core.util.Preconditions.checkNotNull;

/**
 * Created by zenglulin on 2017/8/17.
 */

public class FragmentUtils {
    /**
     * The {@code fragment} is added to the container view with id {@code frameId}. The operation is
     * performed by the {@code fragmentManager}.
     *
     */
    public static void addFragmentToActivity (@NonNull FragmentManager fragmentManager,
                                              @NonNull Fragment fragment, int frameId) {
        checkNotNull(fragmentManager);
        checkNotNull(fragment);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(frameId, fragment);
        transaction.commit();
    }

    public static void showFragment (@NonNull FragmentManager fragmentManager,
                                              @NonNull Fragment fragment) {
        checkNotNull(fragmentManager);
        checkNotNull(fragment);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.show(fragment);
        transaction.commit();
    }

    public static void hideFragment (@NonNull FragmentManager fragmentManager,
                                     @NonNull Fragment fragment) {
        checkNotNull(fragmentManager);
        checkNotNull(fragment);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.hide(fragment);
        transaction.commit();
    }

    public static void replaceFragment (@NonNull FragmentManager fragmentManager,
                                        @NonNull Fragment fragment, int frameId) {
        replaceFragment(fragmentManager,fragment,frameId, false);
    }

    public static void replaceFragment (@NonNull FragmentManager fragmentManager,
                                        @NonNull Fragment fragment, int frameId, boolean addToBackStack) {
        checkNotNull(fragmentManager);
        checkNotNull(fragment);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if(addToBackStack){
            transaction.addToBackStack(null);
        }
        transaction.replace(frameId,fragment);
        transaction.commit();
    }

    /**
     *
     *
     * @param context
     * @param fragmentManager
     * @param srcFragment
     * @param destFragment
     * @param destFragmentClass
     * @param destFragmentBundle
     * @param frameId
     * @return
     */
    public static Fragment switchFragment(Context context, @NonNull FragmentManager fragmentManager,
                                          Fragment srcFragment,
                                          Fragment destFragment,
                                          Class<?> destFragmentClass, Bundle destFragmentBundle, int frameId){
        if (srcFragment != null) {
            FragmentUtils.hideFragment(fragmentManager, srcFragment);
        }

        if (destFragment == null) {
            //实例化的时候注意要用getName，而不要使用其他
            Fragment newDestFragment = Fragment.instantiate(context, destFragmentClass.getName(),destFragmentBundle);
            destFragment = newDestFragment;
            FragmentUtils.addFragmentToActivity(fragmentManager, newDestFragment, frameId);
        }else{
            if(null != destFragmentBundle && null != destFragment.getArguments()) {
                destFragment.getArguments().putAll(destFragmentBundle);
            }
            FragmentUtils.showFragment(fragmentManager, destFragment );
        }

        return destFragment;
    }

    public static Fragment switchFragment(Context context, @NonNull FragmentManager fragmentManager,
                                          Fragment srcFragment,
                                          Fragment destFragment,
                                          Class<?> destFragmentClass, int frameId){
        return switchFragment(context, fragmentManager, srcFragment, destFragment, destFragmentClass,null, frameId);
    }

}
