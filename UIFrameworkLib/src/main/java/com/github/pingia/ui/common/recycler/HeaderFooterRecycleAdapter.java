package com.github.pingia.ui.common.recycler;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * author：admin on 2017/10/10.
 * mail:zengll@hztxt.com.cn
 * function:
 */
public abstract class HeaderFooterRecycleAdapter<K  extends BaseRecycleViewHolder> extends RecyclerView.Adapter<K> {

    private LinearLayout mHeaderLayout;
    private LinearLayout mFooterLayout;
    private RecyclerView mBindedRecycleView;

    protected static final int HEADER_VIEW_TYPE = -98;
    protected static final int FOOTER_VIEW_TYPE = -99;
    public static final int EMPTY_VIEW = -100;

    public void bindRecycleView(RecyclerView recyclerView){
        this.mBindedRecycleView = recyclerView;
        recyclerView.setAdapter(this);
    }

    public RecyclerView getBindedRecycleView(){
        return this.mBindedRecycleView;
    }


    protected void setFullSpan(RecyclerView.ViewHolder holder) {
        if (holder.itemView.getLayoutParams() instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) holder
                    .itemView.getLayoutParams();
            params.setFullSpan(true);
        }
    }

    /**
     *
     * @param recyclerView
     */
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if(manager instanceof GridLayoutManager){   // 布局是GridLayoutManager所管理
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) manager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int type = getItemViewType(position);
                    // 如果没有设置外置的lookup,则是Header、Footer的对象则占据spanCount的位置，否则就只占用1个位置
                    if (mSpanSizeLookup == null) {
                        return (isFixedViewType(type)) ? gridLayoutManager.getSpanCount() : 1;
                    } else {
                        return (isFixedViewType(type)) ? gridLayoutManager.getSpanCount() :
                                mSpanSizeLookup.getSpanSize(gridLayoutManager, position - getHeaderLayoutCount());
                    }


                }
            });

            gridLayoutManager.getSpanSizeLookup().setSpanIndexCacheEnabled(true);
        }
    }

    protected boolean isFixedViewType(int type) {
        return type == EMPTY_VIEW || type == HEADER_VIEW_TYPE || type == FOOTER_VIEW_TYPE;
    }

    private SpanSizeLookup mSpanSizeLookup;

    public interface SpanSizeLookup {
        int getSpanSize(GridLayoutManager gridLayoutManager, int position);
    }

    public void setSpanSizeLookup(SpanSizeLookup spanSizeLookup) {
        this.mSpanSizeLookup = spanSizeLookup;
    }

    public int addHeaderView(View header) {
        return addHeaderView(header,true);
    }

    public int addHeaderView(View header, boolean showVerticalDivider) {
        return addHeaderView(header, -1, LinearLayout.VERTICAL,showVerticalDivider);
    }

    public int addHeaderView(View header, int index, int orientation) {
        return addHeaderView(header,index,orientation,true);
    }

    public int addHeaderView(View header, int index, int orientation, boolean showVerticalDivider) {
        if (mHeaderLayout == null) {
            mHeaderLayout = new LinearLayout(header.getContext());
            if (orientation == LinearLayout.VERTICAL) {
                mHeaderLayout.setOrientation(LinearLayout.VERTICAL);
                mHeaderLayout.setLayoutParams(new ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT));

                if(showVerticalDivider) {
                    mHeaderLayout.setDividerDrawable(ContextCompat.getDrawable(header.getContext(), android.R.drawable.divider_horizontal_dark));
                    mHeaderLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_END);
                }
            } else {
                mHeaderLayout.setOrientation(LinearLayout.HORIZONTAL);
                mHeaderLayout.setLayoutParams(new ViewGroup.LayoutParams(WRAP_CONTENT, MATCH_PARENT));
            }
        }

        mHeaderLayout.setGravity(Gravity.CENTER_HORIZONTAL);

        final int childCount = mHeaderLayout.getChildCount();
        if (index < 0 || index > childCount) {
            index = childCount;
        }
        mHeaderLayout.addView(header, index);
        if (mHeaderLayout.getChildCount() == 1) {
            int position = getHeaderViewPosition();
            if (position != -1) {
                notifyItemInserted(position);
            }
        }
        return index;
    }

    public int addFooterView(View footer) {
        return addFooterView(footer, -1, LinearLayout.VERTICAL,true);
    }

    public int addFooterView(View footer, boolean showVerticalDivider) {
        return addFooterView(footer, -1, LinearLayout.VERTICAL,showVerticalDivider);
    }


    public int addFooterView(View footer, int index, int orientation, boolean showVerticalDivider) {
        if (mFooterLayout == null) {
            mFooterLayout = new LinearLayout(footer.getContext());
            if (orientation == LinearLayout.VERTICAL) {
                mFooterLayout.setOrientation(LinearLayout.VERTICAL);
                mFooterLayout.setLayoutParams(new ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
                if(showVerticalDivider) {
                    mFooterLayout.setDividerDrawable(ContextCompat.getDrawable(footer.getContext(), android.R.drawable.divider_horizontal_dark));
                    mFooterLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_BEGINNING);
                }
            } else {
                mFooterLayout.setOrientation(LinearLayout.HORIZONTAL);
                mFooterLayout.setLayoutParams(new ViewGroup.LayoutParams(WRAP_CONTENT, MATCH_PARENT));
            }
        }

        mFooterLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        final int childCount = mFooterLayout.getChildCount();
        if (index < 0 || index > childCount) {
            index = childCount;
        }

        mFooterLayout.addView(footer, index);
        if (mFooterLayout.getChildCount() == 1) {
            int position = getFooterViewPosition();
            if (position != -1) {
                notifyItemInserted(position);
            }
        }
        return index;
    }

    public void removeHeaderView(View header) {
        if (getHeaderLayoutCount() == 0) {
            return;
        }

        mHeaderLayout.removeView(header);
        if (mHeaderLayout.getChildCount() == 0) {
            int position = getHeaderViewPosition();
            if (position != -1) {
                notifyItemRemoved(position);
            }
        }
    }

    public void removeFooterView(View footer) {
        if (getFooterLayoutCount() == 0){
            return;
        }

        mFooterLayout.removeView(footer);
        if (mFooterLayout.getChildCount() == 0) {
            int position = getFooterViewPosition();
            if (position != -1) {
                notifyItemRemoved(position);
            }
        }
    }

    public void removeAllHeaderViews() {
        if (getHeaderLayoutCount() == 0) {
            return;
        }

        mHeaderLayout.removeAllViews();
        int position = getHeaderViewPosition();
        if (position != -1) {
            notifyItemRemoved(position);
        }
    }

    public void removeAllFooterViews() {
        if (getFooterLayoutCount() == 0) {
            return;
        }

        mFooterLayout.removeAllViews();
        int position = getFooterViewPosition();
        if (position != -1) {
            notifyItemRemoved(position);
        }
    }

    protected int getHeaderViewPosition() {
        return 0;
    }

    protected int getFooterViewPosition() {
        return getHeaderLayoutCount();
    }



    protected int getHeaderLayoutCount(){
        if (mHeaderLayout == null || mHeaderLayout.getChildCount() == 0) {
            return 0;
        }
        return 1;
    }

    protected int getFooterLayoutCount(){
        if (mFooterLayout == null || mFooterLayout.getChildCount() == 0) {
            return 0;
        }
        return 1;
    }

    protected abstract int getRealDataCount();

    @Override
    public int getItemCount() {
        return getHeaderLayoutCount() + getRealDataCount() + getFooterLayoutCount();
    }

    public int getItemViewType(int position){
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
                } else {
                    return super.getItemViewType(position);
                }
            }
        }

    }

    protected abstract int getDefItemViewType(int position);

    @Override
    public K onCreateViewHolder(ViewGroup parent, int viewType) {
        K baseViewHolder = null;
        switch (viewType){
            case HEADER_VIEW_TYPE:
                baseViewHolder = createBaseViewHolder(mHeaderLayout);
                break;
            case FOOTER_VIEW_TYPE:
                baseViewHolder = createBaseViewHolder(mFooterLayout);
                break;
        }

        return baseViewHolder;
    }


    @SuppressWarnings("unchecked")
    protected K createBaseViewHolder(View view) {
        Class temp = getClass();
        Class z = null;
        while (z == null && null != temp) {
            z = getInstancedGenericKClass(temp);
            temp = temp.getSuperclass();
        }
        K k;
        // 泛型擦除会导致z为null
        if (z == null) {
            k = (K) new BaseRecycleViewHolder(view);
        } else {
            k = createGenericKInstance(z, view);
        }
        return k != null ? k : (K) new BaseRecycleViewHolder(view);
    }

    /**
     * get generic parameter K
     *
     * @param z
     * @return
     */
    private Class getInstancedGenericKClass(Class z) {
        Type type = z.getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            Type[] types = ((ParameterizedType) type).getActualTypeArguments();
            for (Type temp : types) {
                if (temp instanceof Class) {
                    Class tempClass = (Class) temp;
                    if (BaseRecycleViewHolder.class.isAssignableFrom(tempClass)) {
                        return tempClass;
                    }
                }
            }
        }
        return null;
    }

    /**
     * try to create Generic K instance
     *
     * @param z
     * @param view
     * @return
     */
    @SuppressWarnings("unchecked")
    private K createGenericKInstance(Class z, View view) {
        try {
            Constructor constructor;
            // inner and unstatic class
            if (z.isMemberClass() && !Modifier.isStatic(z.getModifiers())) {
                constructor = z.getDeclaredConstructor(getClass(), View.class);
                constructor.setAccessible(true);
                return (K) constructor.newInstance(this, view);
            } else {
                constructor = z.getDeclaredConstructor(View.class);
                constructor.setAccessible(true);
                return (K) constructor.newInstance(view);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

}
