package com.github.pingia.ui.common.recycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.IntRange;
import androidx.annotation.Nullable;

import com.github.pingia.ui.common.listener.OnItemChildViewClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * author：admin on 2017/9/12.
 * mail:zengll@hztxt.com.cn
 * function: RecycleView的基类适配器，在这里可实现RecycleView相对ListView缺少的那些功能，比如addheader和addfooter，onitemclick等
 * RecycleView 结合ItemTouchHelper可支持swipe删除和drag排序。
 */
public abstract class BaseRecycleAdapter<K extends BaseRecycleViewHolder, T> extends HeaderFooterRecycleAdapter<K> {


    private BaseLoadMore mLoadMore = new SimpleLoadMore();

    public static final int LOAD_MORE_VIEW_TYPE  = 3;

    //empty
    private FrameLayout mEmptyLayout;
    private boolean mIsUseEmpty = true;
    private boolean mHeadAndEmptyEnable;
    private boolean mFootAndEmptyEnable;

    private boolean mLoadMoreEnabled;   //是否开启下拉加载
    private boolean isLoading;  //当前是否正在加载更多状态...
    private boolean mLoadMoreDividerShowned = true;    //loadmore 分割线需要吗？false 不需要 true:需要 默认需要

    private LayoutInflater mInflater;
    private int mLayoutResId;
    private List<T> mDatas;

    private OnLoadMoreRequestListener mLoadMoreRequestListener;
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    protected OnItemChildViewClickListener<T> mOnItemChildViewClickListener;
    private OnEmptyLayoutVisibleListener mEmptyVisibleListener;


    public interface OnLoadMoreRequestListener{
        void onRequestLoadMore();
    }

    public interface  OnItemClickListener{
        void onItemClick(BaseRecycleAdapter adapter, BaseRecycleViewHolder holder, int dataPosition);
    }

    public interface  OnItemLongClickListener{
        boolean onItemLongClick(BaseRecycleAdapter adapter, BaseRecycleViewHolder holder, int dataPosition);
    }

    public interface OnEmptyLayoutVisibleListener{
        void onEmptyVisible(boolean visible);
    }

    public BaseRecycleAdapter(int layoutResId){
        this(layoutResId,null);
    }

    public BaseRecycleAdapter(int layoutResId, @Nullable List<T> datas){
        mLayoutResId = layoutResId;
        mDatas = (datas == null ? new ArrayList<T>():datas);
    }

    public BaseRecycleAdapter(@Nullable List<T> data) {
        this(0, data);
    }

    public void setLoadMoreRequestListener(OnLoadMoreRequestListener listener){
        this.mLoadMoreRequestListener = listener;
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mOnItemClickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener){
        this.mOnItemLongClickListener = listener;
    }

    public void setOnItemChildViewClickListener(OnItemChildViewClickListener<T> listener){
        this.mOnItemChildViewClickListener = listener;
    }

    public void setOnEmptyLayoutVisibleListener(OnEmptyLayoutVisibleListener listener){
        this.mEmptyVisibleListener = listener;
    }

    public void setLoadMoreEnable(boolean enabled) {
        int oldLoadMoreCount = getLoadMoreViewCount();
        mLoadMoreEnabled = enabled;
        int newLoadMoreCount = getLoadMoreViewCount();

        if (oldLoadMoreCount == 1) {
            if (newLoadMoreCount == 0) {
                notifyItemRemoved(getLoadMoreViewPosition());
            }
        } else {
            if (newLoadMoreCount == 1) {
                mLoadMore.setLoadState(BaseLoadMore.STATE_NORMAL);
                notifyItemInserted(getLoadMoreViewPosition());
            }
        }
    }

    public void setScrollToLoadMoreEnable(boolean enabled){
        if(null != mLoadMore){
            mLoadMore.setScrollToLoadMoreEnable(enabled);
        }
    }

    public void setLoadMoreDividerShowned(boolean shown){
        this.mLoadMoreDividerShowned = shown;
    }

    /**
     * 可自定义loadMore
     * @param loadMore
     */
    public void setLoadMore(BaseLoadMore loadMore){
        this.mLoadMore = loadMore;
    }

    public BaseLoadMore getLoadMore(){
        return this.mLoadMore;
    }


    public void notifyLoadMoreToLoading() {
        if (mLoadMore.getLoadState() == BaseLoadMore.STATE_LOADING) {
            return;
        }
        mLoadMore.setLoadState(BaseLoadMore.STATE_NORMAL);
        notifyItemChanged(getLoadMoreViewPosition());
    }

    /**
     * 开始启动加载更多，一般是多余一页数据时，下滑到当前页数据的底部时触发
     */
    public void startLoadMore(){
        if (mLoadMore.getLoadState() != BaseLoadMore.STATE_NORMAL) {
            return;
        }

        mLoadMore.setLoadState(BaseLoadMore.STATE_LOADING);
        if (!isLoading) {
            isLoading = true;
            if(null != mLoadMoreRequestListener) {
                mLoadMoreRequestListener.onRequestLoadMore();
            }
        }
    }

    /**
     * 当数据全部加载完毕时调用这个方法
     * @param showEnded     是否显示“已加载全部数据”
     */
    public void loadMoreEnded(boolean showEnded){
        mLoadMore.setLoadMoreEndShow(showEnded);

        if (showEnded) {
            mLoadMore.setLoadState(BaseLoadMore.STATE_LOAD_ENDED);
            notifyItemChanged(getLoadMoreViewPosition());
        } else {
            notifyItemRemoved(getLoadMoreViewPosition());
        }
    }

    /**
     * 当数据加载完成后调用这个方法，一般是一页数据加载完毕
     */
    public void loadMoreSuccess(){
        isLoading = false;
        mLoadMore.setLoadState(BaseLoadMore.STATE_NORMAL);
        notifyItemChanged(getLoadMoreViewPosition());
    }

    public void loadMoreFailed(){
        isLoading = false;
        mLoadMore.setLoadState(BaseLoadMore.STATE_LOAD_FAILED);
        notifyItemChanged(getLoadMoreViewPosition());
    }

    private void bindViewClickListener(final BaseRecycleViewHolder baseViewHolder) {
        if (baseViewHolder == null) {
            return;
        }

        final View view = baseViewHolder.itemView;
        if (view == null) {
            return;
        }

        if (mOnItemClickListener != null) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(BaseRecycleAdapter.this,baseViewHolder,baseViewHolder.getLayoutPosition() - getHeaderLayoutCount());
                }
            });
        }

        if (mOnItemLongClickListener != null) {
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return mOnItemLongClickListener.onItemLongClick(BaseRecycleAdapter.this,baseViewHolder, baseViewHolder.getLayoutPosition() - getHeaderLayoutCount());
                }
            });
        }
    }

    @Override
    public void onViewAttachedToWindow(K holder) {
        super.onViewAttachedToWindow(holder);
        int type = holder.getItemViewType();
        if (type == EMPTY_VIEW || type == HEADER_VIEW_TYPE || type == FOOTER_VIEW_TYPE || type == LOAD_MORE_VIEW_TYPE) {
            setFullSpan(holder);
        }
    }

    @Override
    public K onCreateViewHolder(ViewGroup parent, int viewType) {
        //根据viewType创建不同的holder
        K baseViewHolder = null;
        mInflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case LOAD_MORE_VIEW_TYPE:
                baseViewHolder = createLoadMoreViewHolder(parent);
                break;

            case HEADER_VIEW_TYPE:
            case FOOTER_VIEW_TYPE:
                baseViewHolder = super.onCreateViewHolder(parent,viewType);
                break;
            case EMPTY_VIEW:
                baseViewHolder = createBaseViewHolder(mEmptyLayout);
                break;

            default:
                baseViewHolder = createDataItemViewHolder(parent,viewType);
                bindViewClickListener(baseViewHolder);      //item项目会有点击和长按等事件，需要在这里绑定

                break;
        }


        return baseViewHolder;

    }

    private K createLoadMoreViewHolder(ViewGroup parent){
        final View loadMoreView  = mInflater.inflate(mLoadMore.getLayoutId(), parent, false);

        View dividerView = loadMoreView.findViewById(mLoadMore.getDividerViewId());
        if(mLoadMoreDividerShowned){
            dividerView.setVisibility(View.VISIBLE);
        }else {
            dividerView.setVisibility(View.GONE);
        }

//        RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams)loadMoreView.getLayoutParams();
//        lp.bottomMargin = UiTool.dip2px(mInflater.getContext(), 16);

        K holder = createBaseViewHolder(loadMoreView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLoadMore.getLoadState() == BaseLoadMore.STATE_LOAD_FAILED) {
                    notifyLoadMoreToLoading();
                    startLoadMore();
                }
                if (mLoadMore.getLoadState() == BaseLoadMore.STATE_NORMAL) {
                    notifyLoadMoreToLoading();
                    startLoadMore();
                }
            }
        });
        return holder;
    }

    protected K createDataItemViewHolder(ViewGroup parent, int viewType){
        if(null != mMultiItemTypeDelegate){
            mLayoutResId  = mMultiItemTypeDelegate.getLayoutId(viewType);
        }

        return createBaseViewHolder(getItemView(mLayoutResId, parent));
    }

    public View getItemView(int layoutResId, ViewGroup parent){
        View itemView = mInflater.inflate(layoutResId, parent, false);

        return itemView;
    }

    private int getLoadMoreViewCount(){
        if( !mLoadMoreEnabled ) {        //不开启加载更多，不显示布局
            return 0;
        }

        if (mLoadMore.getLoadState()  == BaseLoadMore.STATE_LOAD_ENDED && !mLoadMore.isLoadMoreEndShow()) { //全部加载完成且不显示全部加载完毕
            return 0;
        }

        if (mDatas.size() == 0) {
            return 0;
        }
        return 1;

    }


    public void setEmptyView(int layoutResId, ViewGroup viewGroup) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(layoutResId, viewGroup, false);
        setEmptyView(view);
    }

    public void setEmptyView(View emptyView) {
        boolean insert = false;
        if (mEmptyLayout == null) {
            mEmptyLayout = new FrameLayout(emptyView.getContext());
            final ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            final ViewGroup.LayoutParams lp = emptyView.getLayoutParams();
            if (lp != null) {
                layoutParams.width = lp.width;
                layoutParams.height = lp.height;
            }
            mEmptyLayout.setLayoutParams(layoutParams);
            insert = true;
        }
        mEmptyLayout.removeAllViews();
        mEmptyLayout.addView(emptyView);
        mIsUseEmpty = true;
        if (insert) {
            if (getEmptyViewCount() == 1) {
                int position = 0;
                if (mHeadAndEmptyEnable && getHeaderLayoutCount() != 0) {
                    position++;
                }
                notifyItemInserted(position);
            }
        }
    }

    public void setHeaderAndEmpty(boolean isHeadAndEmpty) {
        setHeaderFooterEmpty(isHeadAndEmpty, false);
    }

    public void setHeaderFooterEmpty(boolean isHeadAndEmpty, boolean isFootAndEmpty) {
        mHeadAndEmptyEnable = isHeadAndEmpty;
        mFootAndEmptyEnable = isFootAndEmpty;
    }

    public void isUseEmpty(boolean isUseEmpty) {
        mIsUseEmpty = isUseEmpty;
    }

    public View getEmptyView() {
        return mEmptyLayout;
    }

    protected int getHeaderViewPosition() {
        //Return to header view notify position
        if (getEmptyViewCount() == 1) {
            if (mHeadAndEmptyEnable) {
                return 0;
            }
        } else {
            return 0;
        }
        return -1;
    }

    protected int getFooterViewPosition() {
        //Return to footer view notify position
        if (getEmptyViewCount() == 1) {
            int position = 1;
            if (mHeadAndEmptyEnable && getHeaderLayoutCount() != 0) {
                position++;
            }
            if (mFootAndEmptyEnable) {
                return position;
            }
        }else {
            return getHeaderLayoutCount() + mDatas.size();
        }
        return -1;

    }

    private int getLoadMoreViewPosition(){
        return getHeaderLayoutCount() + mDatas.size() + getFooterLayoutCount() ;
    }

    public int getItemViewType(int position){
        if (getEmptyViewCount() == 1) {
            boolean header = mHeadAndEmptyEnable && getHeaderLayoutCount() != 0;
            switch (position) {
                case 0:
                    if (header) {
                        return HEADER_VIEW_TYPE;
                    } else {
                        return EMPTY_VIEW;
                    }
                case 1:
                    if (header) {
                        return EMPTY_VIEW;
                    } else {
                        return FOOTER_VIEW_TYPE;
                    }
                case 2:
                    return FOOTER_VIEW_TYPE;
                default:
                    return EMPTY_VIEW;
            }
        }
        int headerLayoutCount = getHeaderLayoutCount();

        if(position < headerLayoutCount){
            return HEADER_VIEW_TYPE;
        }else {
            int newPos = position - headerLayoutCount;
            int dataCount = getRealDataCount();
            if (newPos < dataCount) { //代表是实体数据,返回view_type为0
                return getDefItemViewType(newPos);
            } else {
                newPos = newPos - dataCount;
                int footerLayoutCount = getFooterLayoutCount();
                if (newPos < footerLayoutCount) {      //代表是footer布局
                    return FOOTER_VIEW_TYPE;
                } else {                        //footer布局之后是loadMore布局
                    return LOAD_MORE_VIEW_TYPE;
                }
            }
        }

    }

    protected int getDefItemViewType(int position){
        if (mMultiItemTypeDelegate != null) {
            return mMultiItemTypeDelegate.getItemViewType(mDatas, position);
        }

        return 0;

    }


    private MultiItemTypeDelegate<T> mMultiItemTypeDelegate;        //满足可能需要多个itemtype来展示数据的需求

    public void setMultiItemTypeDelegate(MultiItemTypeDelegate<T> multiTypeDelegate) {
        mMultiItemTypeDelegate = multiTypeDelegate;
    }

    @Override
    public void onBindViewHolder(K holder, int position) {
        int viewType = holder.getItemViewType();

        switch (viewType) {
            case LOAD_MORE_VIEW_TYPE:
                mLoadMore.convert(holder);
                break;
            case HEADER_VIEW_TYPE:
                break;
            case FOOTER_VIEW_TYPE:
                break;
            case EMPTY_VIEW:
                break;
            default:
                //比如有1个header。data总数为9，那么getItemCount返回10。那么header的postion为0，而item的postion是从1-9. 减去1之后，getItem的参数是从0-8
                convert(holder, getItem(position - getHeaderLayoutCount()));
                break;
        }
    }

    public void setListData(List<T> datas){
        this.mDatas = datas == null ? new ArrayList<T>() : datas;
        isLoading = false;
        mLoadMore.setLoadState(BaseLoadMore.STATE_NORMAL);
        notifyDataSetChanged();
    }

    public void appendData(List<T> newData){
        this.mDatas.addAll(newData);

        notifyItemRangeInserted(mDatas.size() - newData.size() + getHeaderLayoutCount(), newData.size());
        compatibilityDataSizeChanged(newData.size());
    }

    public void remove(@IntRange(from = 0) int position) {
        mDatas.remove(position);
        int internalPosition = position + getHeaderLayoutCount();
        notifyItemRemoved(internalPosition);
        compatibilityDataSizeChanged(0);
        notifyItemRangeChanged(internalPosition, mDatas.size() - internalPosition);
    }

    public void remove(List<T> list){
        if(list!=null) {
            mDatas.removeAll(list);
            notifyDataSetChanged();
        }
    }

    public void remove(T item){
        mDatas.remove(item);
        notifyDataSetChanged();
    }

    public void clear() {
        if (mDatas != null) {
            mDatas.clear();
            notifyDataSetChanged();
        }
    }

    public List<T> getDatas(){
        return this.mDatas;
    }

    private void compatibilityDataSizeChanged(int size) {
        final int dataSize = mDatas == null ? 0 : mDatas.size();
        if (dataSize == size) {
            notifyDataSetChanged();
        }
    }


    public int getEmptyViewCount() {
        if (mEmptyLayout == null || mEmptyLayout.getChildCount() == 0) {
            return 0;
        }
        if (!mIsUseEmpty) {
            return 0;
        }
        if (mDatas.size() != 0) {
            if(mEmptyVisibleListener != null){
                mEmptyVisibleListener.onEmptyVisible(false);
            }
            return 0;
        }
        if(mEmptyVisibleListener != null){
            mEmptyVisibleListener.onEmptyVisible(true);
        }
        return 1;
    }

    @Override
    public int getItemCount() {
        int count;
        if (getEmptyViewCount() == 1) {
            count = 1;
            if (mHeadAndEmptyEnable && getHeaderLayoutCount() != 0) {
                count++;
            }
            if (mFootAndEmptyEnable && getFooterLayoutCount() != 0) {
                count++;
            }
        } else {
            count = getHeaderLayoutCount() + getRealDataCount() + getFooterLayoutCount() + getLoadMoreViewCount();
        }
        return count;
    }

    public int getRealDataCount(){
        return mDatas.size();
    }

    public void notifyDataItemChanged(int dataPos){
        notifyItemChanged(getHeaderLayoutCount() + dataPos);
    }

    @Nullable
    public T getItem(@IntRange(from = 0) int position) {
        if (position < mDatas.size())
            return mDatas.get(position);
        else
            return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    abstract protected void convert(K holder,T item);

}
