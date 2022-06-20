package com.petterp.navigationdrawertest.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import androidx.drawerlayout.widget.DrawerLayout;

/**
 * by pang,完美解决drawerLayout嵌套recyclerView滑动事件冲突的问题，https://blog.csdn.net/pang9998/article/details/120230771
 */
public class CustomDrawerLayout extends DrawerLayout {
    private int mTouchSlop;
    private float mLastMotionX;
    private float mLastMotionY;

    public CustomDrawerLayout(Context context) {
        this(context, null);
    }

    public CustomDrawerLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomDrawerLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        final ViewConfiguration configuration = ViewConfiguration.get(getContext());
        mTouchSlop = configuration.getScaledTouchSlop();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            final float x = ev.getX();
            final float y = ev.getY();

            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mLastMotionX = x;
                    mLastMotionY = y;
                    break;

                case MotionEvent.ACTION_MOVE:
                    int xDiff = (int) Math.abs(x - mLastMotionX);
                    int yDiff = (int) Math.abs(y - mLastMotionY);
                    final int x_yDiff = xDiff * xDiff + yDiff * yDiff;

                    boolean xMoved = x_yDiff > mTouchSlop * mTouchSlop;

                    if (xMoved) {
                        if (xDiff > yDiff * 4) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                    break;
                default:

                    break;
            }
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException ex) {
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        try {
            return super.onTouchEvent(ev);
        } catch (IllegalArgumentException ex) {
        }
        return false;
    }
}