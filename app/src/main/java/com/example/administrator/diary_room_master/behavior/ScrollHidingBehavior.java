package com.example.administrator.diary_room_master.behavior;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

/**
 * 滑动列表时候空间隐藏
 * Created by Administrator on 2018/1/5 - 11:54.
 */

public class ScrollHidingBehavior extends CoordinatorLayout.Behavior {

    private boolean visible = true;

    public ScrollHidingBehavior() {
    }

    public ScrollHidingBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout,
                                       @NonNull View child,
                                       @NonNull View directTargetChild,
                                       @NonNull View target, int axes, int type) {
        //被观察者RecyclerView发生滑动时被回调
        //axes为滑动关联轴，现在只关心垂直滑动
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL
                || super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, axes, type);
    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout,
                               @NonNull View child,
                               @NonNull View target, int dxConsumed,
                               int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        if (dyConsumed >0 && visible) {
            //向下滑动且为可见时触发消隐动画
            visible = false;
            onHide(child);
        } else if (dyConsumed < 0){
            //向上滑动触发显示动画
            visible = true;
            onShow(child);
        }
    }

    public void onHide(View view) {
        if (view instanceof FloatingActionButton) {
            //Toolbar消隐动画
            ViewCompat.animate(view)
                    .translationY(view.getHeight() * 2)
                    .setInterpolator(new AccelerateInterpolator(3));
        }
    }

    public void onShow(View view) {
        //显示动画--属性动画
        ViewCompat.animate(view)
                .translationY(0)
                .setInterpolator(new DecelerateInterpolator(3));
    }
}
