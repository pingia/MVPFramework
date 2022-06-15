package com.github.pingia.ui.framework.base;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.github.pingia.ui.framework.architecture.IPresenter;
import com.github.pingia.ui.framework.architecture.IPresenterProvider;
import com.github.pingia.ui.framework.architecture.IView;



public abstract class BaseMvpDialogFragment<P extends IPresenter> extends DialogFragment implements IView, IPresenterProvider<P> {
    private P mPresenter;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(null == mPresenter) {
            mPresenter = onCreatePresenter();
        }
        mPresenter.attachView(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        //消除dialog的两边间距
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0x00000000));
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
    }

    public void onDestroyView(){
        super.onDestroyView();
        mPresenter.detachView();
    }
    @Override
    public void show(FragmentManager manager, String tag) {
        try{
            super.show(manager, tag);
        }catch (IllegalStateException ignore){
        }
    }
    protected P getPresenter(){
        return this.mPresenter;
    }

}
