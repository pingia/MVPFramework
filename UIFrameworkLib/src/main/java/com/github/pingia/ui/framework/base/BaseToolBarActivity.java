package com.github.pingia.ui.framework.base;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.pingia.ui.common.AppManager;
import com.github.pingia.ui.common.IntentConstant;
import com.github.pingia.ui.common.utils.UIUtils;
import com.github.pingia.ui.common.utils.UiTool;
import com.github.pingia.ui.common.widget.CustomPageTitleView;
import com.github.pingia.uiframework.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * author：admin on 2017/8/14.
 * mail:zengll@hztxt.com.cn
 * function: 基础的activity,封装了控制标题栏的一些方法
 */
public abstract class BaseToolBarActivity extends BaseActivity implements ToolbarInterface{


    private LayoutInflater inflater;
    private ViewGroup mPageLayout;
    private FrameLayout mToolbarContainer;
    protected CustomPageTitleView mToolbar;
    private FrameLayout mPageContent;


    private LinearLayout mPageTitleView;
    private TextView mToolbarTitleTextView;
    protected TextView mTitle;
    protected TextView mSubTitle;

    protected TextView mPageTitleLeftTv;

    private Unbinder unbinder;


    private int getPageLayoutModuleId(){
        return R.layout.pingia_ui_common_page_layout;
    }

    protected abstract int getPageToolbarResId();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.INSTANCE.addActivity(this);
        inflater = LayoutInflater.from(this);

        mPageLayout = (ViewGroup) inflater.inflate(getPageLayoutModuleId(), null);
        mToolbarContainer = mPageLayout.findViewById(R.id.id_toolbar_container);
        mToolbarContainer.removeAllViews();
        mToolbar = (CustomPageTitleView) LayoutInflater.from(this).inflate(getPageToolbarResId(), mToolbarContainer,false);
        mToolbarContainer.addView(mToolbar);
        mPageContent =  mPageLayout.findViewById(R.id.id_content);

        setContentView(getLayoutId());
        unbinder = ButterKnife.bind(this);
        onActivityInit(savedInstanceState);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView =this. getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            this.getWindow().setStatusBarColor(Color.TRANSPARENT);
        }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }

    }



    abstract protected void onActivityInit(Bundle savedInstanceState);
    abstract protected int getLayoutId();

    protected void onDestroy() {
        super.onDestroy();
        if(null != unbinder) {
            unbinder.unbind();
        }

        unbinder = null;
        AppManager.INSTANCE.finishActivity(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(getToolBarMenuResId(), menu); //解析menu布局文件到menu
        UIUtils.disableMenuItemLongPress(this, menu);
        onInitMenu(menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.toolbar_menu_item){
                if (needTitleRefresh()) {
                    doTitleRefresh(item);
                    return true;
                }
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    @Deprecated
    public void setContentView(int layoutId) {
        mPageContent.removeAllViews();
        inflater.inflate(layoutId, mPageContent);
        super.setContentView(mPageLayout);
        initBaseView();
    }

    @Override
    @Deprecated
    public void setContentView(View layoutView) {
        mPageContent.removeAllViews();
        mPageContent.addView(layoutView);
        super.setContentView(mPageLayout);
        initBaseView();
    }

    /**
     * 获取根布局
     * @return
     */
    public View getRootView(){
        return mPageLayout;
    }
    private void initBaseView() {
        mToolbar.setTitle("");       //不显示toolbar默认的title,因为这个title居左，
        mToolbar.inflateMenu(getToolBarMenuResId());
        UIUtils.disableMenuItemLongPress(this, mToolbar.getMenu());
        onInitMenu(mToolbar.getMenu());

        setSupportActionBar(mToolbar);

        mPageTitleView =  mToolbar.findViewById(R.id.page_title);
        mToolbarTitleTextView =  mToolbar.findViewById(R.id.page_title_tv);
        mTitle = mToolbarTitleTextView;
        mSubTitle =  mToolbar.findViewById(R.id.page_sub_title_tv);
        mToolbarTitleTextView.setText(getCustomeTitle());
        mPageTitleLeftTv =  mToolbar.findViewById(R.id.page_left_tv);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseToolBarActivity.this.onBackPressed();
            }
        });
    }


    protected CharSequence getCustomeTitle() {
        return getIntent().getStringExtra(IntentConstant.KEY_TITLE);
    }

    protected CharSequence getPageTitle(){
        return mToolbarTitleTextView.getText();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (!isChild()) {
            onTitleChanged(getTitle(), getTitleColor());
        }
    }

    @Override
    protected void onTitleChanged(CharSequence title, int color) {
        super.onTitleChanged(title, color);
        if (TextUtils.isEmpty(getCustomeTitle())) {
            //如果未设置title，则用activity或application的label作为标题
            if (mToolbarTitleTextView != null && TextUtils.isEmpty(mToolbarTitleTextView.getText())) {
                String activityLabel = UiTool.getActivityLabel(this);
                mToolbarTitleTextView.setText(TextUtils.isEmpty(activityLabel) ? UiTool.getApplicationLabel(this): activityLabel);
            }
        }
    }

    public void hideToolBar() {
        if(null != mToolbar) {
            mToolbar.setVisibility(View.GONE);
        }
    }

    public void hideToolBarBackIcon() {
        if (null != mToolbar) {
            mToolbar.setNavigationIcon(null);
        }
    }

    public void onInitMenu(Menu menu) {
        if (needTitleRefresh()) {

        }
    }

    public int getToolBarMenuResId() {
        return R.menu.pingia_ui_simple_toolbar_menu;
    }

    protected boolean needTitleRefresh() {
        return false;
    }

    public void doTitleRefresh(MenuItem refreshMenuItem) {
        refresh();
    }

    protected void refresh() {

    }
}
