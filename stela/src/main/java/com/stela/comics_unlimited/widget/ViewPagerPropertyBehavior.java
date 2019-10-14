package com.stela.comics_unlimited.widget;

import android.content.Context;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.View;

public class ViewPagerPropertyBehavior extends CoordinatorLayout.Behavior<ViewPager> {
    /**
     * 运行时通过这个构造函数获取Behavior对象
     *
     * @param context
     * @param attrs
     */
    public ViewPagerPropertyBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 依赖条件，true表示绑定关系成立
     *
     * @param parent
     * @param child
     * @param dependency
     * @return
     */
    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, ViewPager child, View dependency) {
        return dependency instanceof ViewPager;
    }

    /**
     * 属性依赖逻辑，返回true表示要执行
     *
     * @param parent
     * @param child
     * @param dependency
     * @return
     */
    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, ViewPager child, View dependency) {
        int offset = dependency.getTop() - child.getTop();
        //纵向移动
        ViewCompat.offsetTopAndBottom(child, offset);
        return true;
    }
}
