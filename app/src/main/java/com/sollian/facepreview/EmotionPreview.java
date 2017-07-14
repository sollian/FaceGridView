package com.sollian.facepreview;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.util.Pair;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;

import com.sollian.demo.MyApplication;

import org.roisoleil.gifview.GifView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sollian on 2017/7/14.
 */

public class EmotionPreview
        implements View.OnTouchListener, AdapterView.OnItemLongClickListener {
    private static final int VIEW_SIZE  = Util.dip2px(MyApplication.getInstance(), 90);
    private static final int TOP_MARGIN = Util.dip2px(MyApplication.getInstance(), -40);

    private final Context context;
    private final int[]                      gvLoc    = new int[2];
    private final List<Pair<Integer, Point>> gifInfos = new ArrayList<>();

    private int parentWidth;
    private int childWidth;
    private int childHeight;

    private final WindowManager              wm;
    private final WindowManager.LayoutParams layoutParams;

    private GifView  vGif;
    private GridView vCurGrid;

    public EmotionPreview(Context context) {
        this.context = context;
        wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        layoutParams.format = PixelFormat.TRANSLUCENT;
        layoutParams.width = VIEW_SIZE;
        layoutParams.height = VIEW_SIZE;
        layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (vGif == null) {
            return false;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                vCurGrid.getParent().requestDisallowInterceptTouchEvent(true);
                float rawX = event.getRawX();
                float rawY = event.getRawY();

                int size = gifInfos.size();
                boolean findTarget = false;
                for (int i = 0; i < size; i++) {
                    Pair<Integer, Point> info = gifInfos.get(i);
                    Point loc = info.second;
                    if (rawX >= loc.x && rawX <= loc.x + childWidth
                            && rawY > loc.y && rawY < loc.y + childHeight) {
                        if (info.first.equals(vGif.getTag())) {
                            // 如果是正在播放的gif则返回
                            return false;
                        }

                        findTarget = true;
                        updateGif(info.first, new int[]{info.second.x, info.second.y});
                        break;
                    }
                }
                if (!findTarget) {
                    pauseGif();
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                vCurGrid.getParent().requestDisallowInterceptTouchEvent(false);
                hideGif();
                gifInfos.clear();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        String resName = (String) parent.getItemAtPosition(position);

        int resId = Util.getResourceId(context, resName);

        int[] loc = new int[2];
        view.getLocationOnScreen(loc);

        vCurGrid = (GridView) parent;
        parent.getLocationOnScreen(gvLoc);
        parentWidth = parent.getWidth();
        childWidth = view.getWidth();
        childHeight = view.getHeight();

        showGif(resId, loc);

        int start = parent.getFirstVisiblePosition();
        int end = parent.getLastVisiblePosition();
        for (int i = 0; i <= end - start; i++) {
            View v = parent.getChildAt(i);
            if (null == v) {
                continue;
            }
            v.getLocationOnScreen(loc);
            String name = (String) parent.getItemAtPosition(i + start);
            int rid = Util.getResourceId(context, name);
            Point point = new Point(loc[0], loc[1]);
            Pair<Integer, Point> pair = new Pair<>(rid, point);
            gifInfos.add(pair);
        }

        return true;
    }

    private void showGif(int resId, int[] loc) {
        if (vGif == null) {
            vGif = new GifView(context);
        }
        updateLayoutParams(loc);
        wm.addView(vGif, layoutParams);
        vGif.setMovieResource(resId);
        vGif.setTag(resId);
    }

    private void pauseGif() {
        if (vGif.getParent() != null) {
            wm.removeView(vGif);
        }
        vGif.setTag(null);
    }

    private void updateGif(int resId, int[] loc) {
        updateLayoutParams(loc);
        if (vGif.getParent() == null) {
            wm.addView(vGif, layoutParams);
        } else {
            wm.updateViewLayout(vGif, layoutParams);
        }
        vGif.setMovieResource(resId);
        vGif.setTag(resId);
    }

    private void hideGif() {
        if (vGif.getParent() != null) {
            wm.removeView(vGif);
        }
        vGif = null;
        vCurGrid = null;
    }

    private void updateLayoutParams(int[] location) {
        layoutParams.y = location[1] - VIEW_SIZE + TOP_MARGIN;
        layoutParams.x = location[0] + childWidth / 2 - VIEW_SIZE / 2;
        if (layoutParams.y < 0) {
            layoutParams.y = 0;
        }
        if (layoutParams.x < 0) {
            layoutParams.x = 0;
        }
        if (layoutParams.x > parentWidth - VIEW_SIZE / 2) {
            layoutParams.x = parentWidth - VIEW_SIZE / 2;
        }
    }
}