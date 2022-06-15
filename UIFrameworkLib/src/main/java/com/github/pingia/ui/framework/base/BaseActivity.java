package com.github.pingia.ui.framework.base;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.github.pingia.ui.common.AppManager;



public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedBundleInstance) {
        super.onCreate(savedBundleInstance);
        AppManager.INSTANCE.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.INSTANCE.finishActivity(this);
    }
}
