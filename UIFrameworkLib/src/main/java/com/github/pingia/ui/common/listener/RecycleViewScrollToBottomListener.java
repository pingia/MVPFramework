package com.github.pingia.ui.common.listener;


import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

/**
 * author：admin on 2017/9/20.
 * mail:zengll@hztxt.com.cn
 * function:
 */
public class RecycleViewScrollToBottomListener extends RecyclerView.OnScrollListener {

    private OnScrolledToEndListener mScrolledToEndListener;
    public RecycleViewScrollToBottomListener(OnScrolledToEndListener listener){
        this.mScrolledToEndListener = listener;
    }

    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (null == manager) {
            throw new RuntimeException("you should call setLayoutManager() first!!");
        }
        if (null == adapter) {
            throw new RuntimeException("you should call setAdapter() first!!");
        }
        if (manager instanceof LinearLayoutManager) {
            int lastCompletelyVisibleItemPosition = ((LinearLayoutManager) manager).findLastCompletelyVisibleItemPosition();

            boolean vertical = ((LinearLayoutManager)manager).getOrientation() == LinearLayoutManager.VERTICAL;
            boolean hasScrolled = vertical ? dy!=0:dx!=0;   //是否有屏幕的滑动事件

            if (adapter.getItemCount() > 1 && lastCompletelyVisibleItemPosition >= adapter.getItemCount() - 1) {
                if(null != this.mScrolledToEndListener && hasScrolled){
                    this.mScrolledToEndListener.scrollToEnd();
                }
            }
        } else if (manager instanceof StaggeredGridLayoutManager) {
            int count = ((StaggeredGridLayoutManager) manager).getSpanCount();
            int[] itemPositions = new int[count];
            ((StaggeredGridLayoutManager) manager).findLastVisibleItemPositions(itemPositions);
            int lastVisibleItemPosition = itemPositions[0];
            for (int i = count - 1; i > 0; i--) {
                if (lastVisibleItemPosition < itemPositions[i]) {
                    lastVisibleItemPosition = itemPositions[i];
                }
            }

            boolean vertical = ((StaggeredGridLayoutManager) manager).getOrientation() == LinearLayoutManager.VERTICAL;
            boolean hasScrolled = vertical ? dy!=0:dx!=0;   //是否有屏幕的滑动事件
            if (lastVisibleItemPosition >= adapter.getItemCount() - 1) {
                if(null != this.mScrolledToEndListener && hasScrolled){
                    this.mScrolledToEndListener.scrollToEnd();
                }
            }
        }
    }
}
