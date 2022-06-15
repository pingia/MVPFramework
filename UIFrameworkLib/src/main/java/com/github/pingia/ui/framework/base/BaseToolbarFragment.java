package com.github.pingia.ui.framework.base;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.CallSuper;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.github.pingia.ui.common.IntentConstant;
import com.github.pingia.ui.common.utils.UIUtils;
import com.github.pingia.ui.common.widget.CustomPageTitleView;
import com.github.pingia.uiframework.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * author：admin on 2017/8/14.
 * mail:zengll@hztxt.com.cn
 * function: 基础的fragment，封装了fragment控制标题栏的方法;注意：fragment默认不显示标题栏，如果有需要显示的需要，请手动设置toolbar视图的可见性
 */
public abstract class BaseToolbarFragment extends Fragment implements ToolbarInterface {
    private Context mContext;

    protected Toolbar mToolbar;
    protected TextView mToolbarTitleTextView;
    protected TextView mToolbarSubTitleTextView;
    protected TextView mPageTitleLeftTv;
    private FrameLayout mPageContent;
    private View rootView;
    private FrameLayout mToolbarContainer;

    private  boolean isFirstViewCreated = true;

    private Unbinder unbinder;

    protected abstract int getPageToolbarResId();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {   // 不在最前端显示 相当于调用了onPause();
            onPause();
        } else {  // 在最前端显示 相当于调用了onResume();
            onResume();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(null != unbinder) {
            unbinder.unbind();
        }

        unbinder = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (null == rootView) {
            View view = inflater.inflate(getPageLayoutModuleId(), container, false);
            rootView = view;
            initFrameView(view);
            if(bindRootViewOrNot()) {
                unbinder = ButterKnife.bind(this, view);
            }
            initView(view);

        }
        // 缓存的rootView需要判断是否已经被加过parent，如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }

    /**
     * 是否绑定当前视图，默认绑定，可重写来绑定其他视图，参考BaseLoadingFragment.
     * @return
     */
    protected boolean bindRootViewOrNot(){
        return true;
    }


    /**
     * 初始化fragment页面中的标题和内容
     * @param frameView
     */
    private void initFrameView(View frameView) {
        mToolbarContainer = frameView.findViewById(R.id.id_toolbar_container);
        mToolbarContainer.removeAllViews();
        mToolbar = (CustomPageTitleView) LayoutInflater.from(frameView.getContext()).inflate(getPageToolbarResId(), mToolbarContainer,false);
        mToolbarContainer.addView(mToolbar);

        mPageContent = frameView.findViewById(R.id.id_content);
        mPageContent.removeAllViews();
        mPageContent.addView(getLayoutView(LayoutInflater.from(getContext())));

        mToolbar.setTitle("");  //不显示toolbar默认的title,因为这个title居左，
        mToolbarTitleTextView =  mToolbar.findViewById(R.id.page_title_tv);
        mToolbarTitleTextView.setText(getCustomeTitle());
        mToolbarSubTitleTextView =  mToolbar.findViewById(R.id.page_sub_title_tv);
        mPageTitleLeftTv = mToolbar.findViewById(R.id.page_left_tv);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView =this.getActivity(). getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            this.getActivity().getWindow().setStatusBarColor(Color.TRANSPARENT);
        }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getActivity().getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        setHasOptionsMenu(true);

        //fragment默认隐藏toolbar
        hideToolBar();
        hideToolBarBackIcon();

    }

    /** ToolBar之外视图的初始化在这里进行 **/
    abstract protected void initView(View view);



    public void onViewCreated(View view, Bundle savedBundleInstance){
        super.onViewCreated(view,savedBundleInstance);

        doOnEachViewCreated();

        if(isFirstViewCreated){
            onFirstViewCreated(view,savedBundleInstance);
            isFirstViewCreated = false;
        }

    }

    protected void doOnEachViewCreated(){
        // default do nothing.
    }

    @CallSuper
    protected void onFirstViewCreated(View view, Bundle savedBundleInstance){

    }

    public void hideToolBar() {
        if (null != mToolbar) {
            mToolbar.setVisibility(View.GONE);
        }
    }

    public void hideToolBarBackIcon() {
        if (null != mToolbar) {
            mToolbar.setNavigationIcon(null);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menu = mToolbar.getMenu();
        menu.clear();
        super.onCreateOptionsMenu(menu, menuInflater);
        menuInflater.inflate(getToolBarMenuResId(), menu); //解析menu布局文件到menu
        UIUtils.disableMenuItemLongPress(getActivity(), menu);
        onInitMenu(menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu){
        menu = mToolbar.getMenu();  //指向toolbar上的menu,解决刷新MenuItem的图标和文字不生效的问题
        super.onPrepareOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id== R.id.toolbar_menu_item){
                if (isTitleRefesh) {
                    doTitleRefresh(item);
                    return true;
                }
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isTitleRefesh = false; //是否需要标题栏上的刷新按钮 默认不需要

    protected void setTitleRefresh(boolean refresh) {
        isTitleRefesh = refresh;
    }

    public void onInitMenu(Menu menu) {

    }

    /**
     * 返回toolbar menu的布局资源ID，如果继承类有自定义的menu布局资源，可以覆写这个方法
     *
     * @return
     */
    public int getToolBarMenuResId() {
        return R.menu.pingia_ui_simple_toolbar_menu;
    }

    public void doTitleRefresh(MenuItem refreshMenuItem) {
        refresh();
    }

    public void refresh() {

    }

    /**
     * 默认toolbar标题文字的获取方法，从intent中的title字段而来，如果没有title字段，则取应用名称显示
     * 可被子类重写覆盖默认行为
     * @return
     */
    protected CharSequence getCustomeTitle() {
        String title = null;
        if (null != getArguments()) {
            title = getArguments().getString(IntentConstant.KEY_TITLE);
        }

        if (title == null) {
            title = "";
        }
        return title;
    }

    /**
     * 手动指定出现在fragment toolbar上的标题，用户动态修改
     * @param title
     */
    protected void setTitle(String title) {
        if (mToolbarTitleTextView != null && !TextUtils.isEmpty(title)) {
            mToolbarTitleTextView.setText(title);
        }

    }

    protected void setTabHostVisible(int tabHostViewId, boolean visible){
        if(getActivity() != null){
            View tabhostView  = getActivity().findViewById(tabHostViewId);
            if(tabhostView != null){
                tabhostView.setVisibility(visible ? View.VISIBLE: View.GONE);
            }
        }

    }

    abstract protected View getLayoutView(LayoutInflater inflater);

    protected int getPageLayoutModuleId(){
        return R.layout.pingia_ui_common_page_layout;
    }

}
