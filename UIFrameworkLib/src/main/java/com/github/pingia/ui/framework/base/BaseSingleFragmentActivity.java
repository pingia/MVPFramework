package com.github.pingia.ui.framework.base;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.github.pingia.ui.common.IntentConstant;
import com.github.pingia.ui.common.utils.FragmentUtils;
import com.github.pingia.uiframework.R;

import java.io.Serializable;

public abstract class BaseSingleFragmentActivity extends BaseToolBarActivity {
    private FragmentManager mFragmentManager;
    private Class<?> mFragmentClass;

    @Override
    public void onActivityInit(Bundle savedInstanceState) {
        init();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_container;
    }

    private void init(){
        initViews();
    }

    private void initViews(){
        mFragmentManager = getSupportFragmentManager();

        Serializable serializable = getIntent().getSerializableExtra(IntentConstant.KEY_FRAGMENT_CLASS);

        if(null != serializable){
            mFragmentClass = (Class<?>)serializable;
        }

        Fragment fragment =  getSupportFragmentManager()
                .findFragmentById(R.id.container_framelayout);

        if(null == fragment) {

            if (null != mFragmentClass) {
                fragment = Fragment.instantiate(this, mFragmentClass.getName());
                fragment.setArguments(getIntent().getBundleExtra(IntentConstant.KEY_FRAGMENT_ARGUMENTS));

                FragmentUtils.addFragmentToActivity(getSupportFragmentManager(),
                        fragment, R.id.container_framelayout);
            }
        }
    }

}
