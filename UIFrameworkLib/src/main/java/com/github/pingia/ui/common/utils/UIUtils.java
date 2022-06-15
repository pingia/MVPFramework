package com.github.pingia.ui.common.utils;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.text.Editable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import java.util.regex.Pattern;

import cn.sunline.uicommonlib.ui.widget.ForegroundColorURLSpan;


public class UIUtils {
    public static void setTextDrawable(TextView tv, int leftIconResId, int topIconResId, int rightIconResId, int bottomIconResId){
        tv.setCompoundDrawablesWithIntrinsicBounds(leftIconResId, topIconResId, rightIconResId, bottomIconResId);
    }

    public static void setLeftIconTextView(final TextView tv, int iconResId, CharSequence text, View.OnClickListener listener){
        tv.setText(text);
        UIUtils.setTextDrawable(tv,iconResId,0,0,0);
        tv.setOnClickListener(listener);
    }

    public static CharSequence getTintColorString(CharSequence cs, int color){
        SpannableString ss = new SpannableString(cs);
        ss.setSpan(new ForegroundColorSpan(color),0, cs.length(), SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
        return ss;
    }

    public static void setTextViewTintLink(TextView tipTv,
                                           String clickableText, boolean underline, String tintText, int tintColor,  final Runnable linkEventRunnable){
        setTextViewTintLink(tipTv, clickableText, underline, new String[]{tintText}, new int[]{tintColor}, new Runnable[]{linkEventRunnable});
    }

    /**
     * 增加一段可点击文本
     * @param tipTv
     * @param clickableText
     * @param underline
     * @param tintTexts
     * @param tintColors
     * @param linkEventRunnables
     */
    public static void setTextViewTintLink(TextView tipTv,
                                           String clickableText, boolean underline, String[] tintTexts, int[] tintColors,  final Runnable[] linkEventRunnables){

        if(null == tintTexts || null ==  tintColors  || null == linkEventRunnables) return;

        int len = tintTexts.length;
        if(len != tintColors.length || len != linkEventRunnables.length) return;

        ForegroundColorURLSpan[] urlSpans = new ForegroundColorURLSpan[len];

        for (int i = 0; i<len; i++ ) {
            final Runnable linkEventRunnable = linkEventRunnables[i];
            urlSpans[i] = new ForegroundColorURLSpan(clickableText, tintColors[i], underline) {
                @Override
                public void onClick(View widget) {
                    linkEventRunnable.run();
                }
            };
        }

        setTextClickableForegroundSpan(tipTv, clickableText,    //全部文本可点击
                tintTexts,     //着色部分文本可点击
                urlSpans);

        tipTv.setHighlightColor(Color.TRANSPARENT);
        tipTv.setMovementMethod(LinkMovementMethod.getInstance());
    }


    /**
     * 给textview中的部分文本设置可点击的样式span
     *
     * @param tv
     * @param wholeStr  textview的全部文本
     * @param partClickableStrs   textview中可点击的部分文本
     * @param clickableSpans    可点击部分的文本样式
     */
    private static void setTextClickableForegroundSpan(TextView tv, String wholeStr, String[] partClickableStrs, ClickableSpan[] clickableSpans) {
        if (TextUtils.isEmpty(wholeStr) || null == partClickableStrs || null == clickableSpans) {
            return;
        }

        if (partClickableStrs.length != clickableSpans.length) {
            return;
        }

        SpannableStringBuilder ssb = new SpannableStringBuilder(wholeStr);


        int len = partClickableStrs.length;

        for (int i = 0; i < len; i++) {
            int start = wholeStr.indexOf(partClickableStrs[i]);
            int end = start + partClickableStrs[i].length();

            ClickableSpan span = clickableSpans[i];

            ssb.setSpan(span, start, end, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        }

        tv.setText(ssb);
    }
    public static void addEnabled(final EditText[] ets, final View targetView, final String[] regexs) {
        if (null == ets || null == regexs
                || ets.length != regexs.length || null == targetView) {
            return;
        }
        final int len = ets.length;
        for (int i = 0; i < len; i++) {
            final EditText et = ets[i];
            final int index = i;
            et.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    boolean b = Pattern.compile(regexs[index]).matcher(s.toString()).matches();
                    et.setTag(et.getId(), b);

                    boolean targetEnabled = true;

                    for (int i = 0; i < len; i++) {
                        Object obj = ets[i].getTag(ets[i].getId());

                        if (null == obj || (obj instanceof Boolean && !((Boolean) obj).booleanValue())) {
                            targetEnabled = false;
                            break;
                        }

                    }

                    if (targetEnabled) {
                        targetView.setEnabled(true);
                    } else {
                        targetView.setEnabled(false);
                    }

                }
            });
        }
    }


    /**
     * 禁止toolbar menuitem的长按事件
     * @param menu
     */
    public static void disableMenuItemLongPress(Activity activity, Menu menu){
        final Menu newMenu = menu;

        final int size = menu.size();

        //屏蔽menuitem的 长按事件
        for (int i = 0; i < size; i++) {
            final View v = activity.findViewById(newMenu.getItem(i).getItemId());
            if (v != null) {
                v.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        return false;
                    }
                });
            }
        }
    }

    /**
     * 从context中返回activity
     * @return
     */
    public static Activity getActivityFromContext(Context context){
        while(context instanceof ContextWrapper){
            if(context instanceof Activity){
                return (Activity)context;
            }else {
                context = ((ContextWrapper) context).getBaseContext();
            }
        }

        return null;

    }

    public static void setHalfViewHeightRoundCornerRadius(final View view){
        view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                view.getViewTreeObserver().removeOnPreDrawListener(this);
                int height = view.getMeasuredHeight();
                int cornerRadius = height/2;

                UIUtils.setViewBgGradientDrawableCorners(view,cornerRadius,cornerRadius,cornerRadius,cornerRadius);

                return true;
            }
        });
    }

    public static Drawable setGradientDrawableColors(Drawable drawable, int strokeWidth, @ColorInt int strokeColor,@ColorInt int solidColor) {
        if (drawable instanceof GradientDrawable) {
            Drawable muteDrawable = drawable.mutate();

            GradientDrawable gradientDrawable = (GradientDrawable) muteDrawable;
            gradientDrawable.setStroke(strokeWidth,strokeColor);
            gradientDrawable.setColor(solidColor);
            return muteDrawable;
        }else{
            GradientDrawable gradientDrawable = new GradientDrawable();
            gradientDrawable.setStroke(strokeWidth,strokeColor);
            gradientDrawable.setColor(solidColor);
            return gradientDrawable;
        }

    }

    public static Drawable setGradientDrawableCorners(Drawable drawable, int leftTopRadius, int rightTopRadius,
                                                      int rightBottomRadius, int leftBottomRadius) {
        if (drawable instanceof GradientDrawable) {
            Drawable muteDrawable = drawable.mutate();
            float[] radius = new float[]{leftTopRadius, leftTopRadius, rightTopRadius, rightTopRadius, rightBottomRadius, rightBottomRadius, leftBottomRadius, leftBottomRadius};
            ((GradientDrawable) muteDrawable).setCornerRadii(radius);

            return muteDrawable;
        }else{
            GradientDrawable gradientDrawable = new GradientDrawable();
            float[] radius = new float[]{leftTopRadius, leftTopRadius, rightTopRadius, rightTopRadius, rightBottomRadius, rightBottomRadius, leftBottomRadius, leftBottomRadius};
            gradientDrawable.setCornerRadii(radius);
            return gradientDrawable;
        }

    }

    public static Drawable setViewBgGradientDrawableCorners(View view, int leftTopRadius, int rightTopRadius,
                                                            int rightBottomRadius, int leftBottomRadius ){
        Drawable drawable = view.getBackground();

        if(drawable instanceof StateListDrawable){
            Drawable statusDrawable = drawable.getCurrent();
            return setGradientDrawableCorners(statusDrawable,leftTopRadius,rightTopRadius,rightBottomRadius,leftBottomRadius);
        }else{
            return setGradientDrawableCorners(drawable,leftTopRadius,rightTopRadius,rightBottomRadius,leftBottomRadius);
        }
    }

    public static void setViewBg(View view,  int strokeWidth, @ColorInt int strokeColor, @ColorInt int solidColor, int radius){
        setViewBg(view,strokeWidth, strokeColor, solidColor, new int[]{radius, radius, radius, radius});
    }

    public static void setViewBg(View view, int strokeWidth, int strokeColor, int solidColor, int[] radiusArray){
        if(null == radiusArray || radiusArray.length != 4){
            return;
        }

        Drawable cornerDrawable = UIUtils.setViewBgGradientDrawableCorners(view, radiusArray[0], radiusArray[1], radiusArray[2], radiusArray[3]);
        Drawable strokeDrawable= UIUtils.setGradientDrawableColors(cornerDrawable,strokeWidth, strokeColor, solidColor);

        view.setBackground(strokeDrawable);
    }

    public static Drawable getTintColorDrawable(Context context, Drawable srcDrawable, int colorResId){
        Drawable modeDrawable = srcDrawable.mutate();
        Drawable temp = DrawableCompat.wrap(modeDrawable);
        ColorStateList colorStateList =     ColorStateList.valueOf(ContextCompat.getColor(context,colorResId));
        DrawableCompat.setTintList(temp, colorStateList);
        return temp;        //着色后的drawable
    }

    public static void setTextViewDrawableTintColor(TextView tv, int colorResId){
        Drawable[] drawables =  tv.getCompoundDrawables();

        Drawable leftTintDrawable = null ;
        if(drawables[0] != null){
            Drawable tintDrawable = getTintColorDrawable(tv.getContext(), drawables[0], colorResId);
            leftTintDrawable = tintDrawable;
        }

        Drawable topTintDrawable = null ;
        if(drawables[1] != null){
            Drawable tintDrawable = getTintColorDrawable(tv.getContext(), drawables[1], colorResId);
            topTintDrawable = tintDrawable;
        }

        Drawable rightTintDrawable = null;
        if(drawables[2] != null){
            Drawable tintDrawable = getTintColorDrawable(tv.getContext(), drawables[2], colorResId);
            rightTintDrawable = tintDrawable;
        }

        Drawable bottomTintDrawable = null;
        if(drawables[3] != null){
            Drawable tintDrawable = getTintColorDrawable(tv.getContext(), drawables[3], colorResId);
            bottomTintDrawable = tintDrawable;
        }

        tv.setCompoundDrawables(leftTintDrawable, topTintDrawable,rightTintDrawable,bottomTintDrawable);
    }

    public static final void focusAndPopKeyBoard(EditText et){
        et.setFocusable(true);
        et.setFocusableInTouchMode(true);
        et.requestFocus();
        InputMethodManager inputManager = (InputMethodManager) et
                .getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(et, 0);
    }

    /**
     * 隐藏 指定view呼出来的键盘，一般是edittext
     *
     * @param view
     */
    public static void hideInputKeyboard(final View view ) {
        InputMethodManager mInputKeyBoard = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        mInputKeyBoard.hideSoftInputFromWindow(view.getWindowToken(), 0);

    }

}
