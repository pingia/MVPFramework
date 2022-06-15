package com.github.pingia.ui.common.recycler;

import android.view.Gravity;
import android.widget.LinearLayout;

import androidx.annotation.CallSuper;

import com.github.pingia.uiframework.R;

import java.util.Map;

/**
 * <p>文件描述：<p>
 * <p>作者: zengll@jrrcapital.com<p>
 * <p>创建时间：2018/10/30<p>
 */
public class BaseSimpleTitleTextAdapter extends BaseRecycleAdapter<BaseRecycleViewHolder,  Map.Entry<String, String>> {

    public BaseSimpleTitleTextAdapter() {
        super(R.layout.item_simple_list_three_columns);
    }

    @Override
    @CallSuper
    protected void convert(BaseRecycleViewHolder holder, Map.Entry<String, String> item) {
        holder.setVisible(R.id.second_column_tv, false);
        LinearLayout linearLayout = (LinearLayout) holder.itemView;
        linearLayout.setGravity(Gravity.CENTER_VERTICAL);

        holder.setText(R.id.first_column_tv, item.getKey());
        holder.setText(R.id.third_column_tv, item.getValue());

    }
}
