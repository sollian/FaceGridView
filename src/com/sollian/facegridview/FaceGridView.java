package com.sollian.facegridview;

import java.util.ArrayList;
import java.util.HashMap;

import org.roisoleil.gifview.GifView;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.view.View.OnTouchListener;

/**
 * 仿QQ长按表情查看动图，移动手指可切换图片。
 * 
 * @author sollian
 * 
 */
public class FaceGridView extends FrameLayout implements OnTouchListener,
		OnItemLongClickListener {
	private Context context;
	/**
	 * 参数
	 */
	// 动态图的topmargin，以dp为单位
	private int nDynamicMarginTop = 0;
	// 震动时长，以ms为单位
	private int nVibrateTime = 300;
	/**
	 * 动态表情相关
	 */
	// FaceGridView在屏幕中的位置，对应x,y值
	private int gvLocation[] = new int[2];
	private ArrayList<HashMap<String, String>> listDynamicFace = new ArrayList<HashMap<String, String>>();
	/**
	 * 控件
	 */
	private GridView gridview;
	private LinearLayout ll;
	private GifView gifview;

	public FaceGridView(Context context) {
		this(context, null);
	}

	public FaceGridView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public FaceGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		LayoutInflater layoutInflater = LayoutInflater.from(getContext());
		View view = layoutInflater.inflate(R.layout.face_gridview, this, false);
		gridview = (GridView) view.findViewById(R.id.gridview);
		ll = (LinearLayout) view.findViewById(R.id.ll);
		gifview = (GifView) view.findViewById(R.id.gifview);
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		addView(view, params);

		gridview.setOnItemLongClickListener(this);
		gridview.setOnTouchListener(this);
	}

	/**
	 * 设置动态图的topmargin，以dp为单位
	 * 
	 * @param margin
	 */
	// public void setDynamicMarginTop(int margin) {
	// this.nDynamicMarginTop = margin;
	// }

	/**
	 * 设置动态图的背景
	 * 
	 * @param resId
	 */
	public void setDynamicBackground(int resid) {
		ll.setBackgroundResource(resid);
	}

	/**
	 * 设置动态图的背景
	 * 
	 * @param background
	 */
	@SuppressWarnings("deprecation")
	public void setDynamicBackground(Drawable background) {
		ll.setBackgroundDrawable(background);
	}

	/**
	 * 设置动态图的背景色
	 * 
	 * @param color
	 */
	public void setDynamicBackgroundColor(int color) {
		ll.setBackgroundColor(color);
	}

	/**
	 * 设置gridview的背景
	 * 
	 * @param resid
	 */
	public void setBackground(int resid) {
		gridview.setBackgroundResource(resid);
	}

	/**
	 * 设置gridview的背景
	 * 
	 * @param background
	 */
	@SuppressWarnings("deprecation")
	public void setBackgroundDrawable(Drawable background) {
		gridview.setBackgroundDrawable(background);
	}

	/**
	 * 设置gridview的背景
	 * 
	 * @param color
	 */
	public void setBackgroundColor(int color) {
		gridview.setBackgroundColor(color);
	}

	/**
	 * 设置震动时长，以ms为单位
	 * 
	 * @param time
	 */
	public void setVibrateTime(int time) {
		this.nVibrateTime = time;
	}

	/**
	 * 设置gridview的列数
	 * 
	 * @param numColumns
	 */
	public void setGridViewNumColumns(int numColumns) {
		gridview.setNumColumns(numColumns);
	}

	/**
	 * 设置gridview的adapter
	 * 
	 * @param adapter
	 */
	public void setAdapter(ListAdapter adapter) {
		gridview.setAdapter(adapter);
	}

	public void setOnItemClickListener(OnItemClickListener listener) {
		gridview.setOnItemClickListener(listener);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_MOVE:
			// 获取触摸点相对于屏幕的坐标
			float eventX = gvLocation[0] + event.getX();
			float eventY = gvLocation[1] + event.getY();

			int x = 0;
			int y = 0;
			for (int i = 0; i < listDynamicFace.size(); i++) {
				HashMap<String, String> map = listDynamicFace.get(i);
				x = Integer.parseInt(map.get("x"));
				y = Integer.parseInt(map.get("y"));
				if (eventX >= x && eventX <= x + Util.dip2px(context, 40)
						&& eventY >= y
						&& eventY <= y + Util.dip2px(context, 40)) {
					if (i == gifview.getId()) {
						// 如果是正在播放的gif则返回
						return false;
					}
					// 更新显示的gif
					String imgName = map.get("imgName").toString();
					int resId = getResources().getIdentifier(imgName,// 需要转换的资源名称
							"drawable", // 资源类型
							context.getPackageName());// R类所在的包名
					gifview.setMovieResource(resId);
					gifview.setId(i);
					// 更新ll的位置
					LayoutParams params = new LayoutParams(Util.dip2px(context,
							70), Util.dip2px(context, 70));
					params.topMargin = y - gvLocation[1]
							+ Util.dip2px(context, nDynamicMarginTop);
					params.leftMargin = x - Util.dip2px(context, 20);
					if (params.topMargin < 0) {
						params.topMargin = 0;
					}
					if (params.leftMargin < 0) {
						params.leftMargin = 0;
					}
					if (params.leftMargin > gridview.getWidth()
							- Util.dip2px(context, 70)) {
						params.leftMargin = gridview.getWidth()
								- Util.dip2px(context, 70);
					}
					ll.setLayoutParams(params);
					break;
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			ll.setVisibility(View.GONE);
			break;
		}
		return false;
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		// 震动
		Util.vibrate(context, nVibrateTime);
		// 获取屏幕坐标
		gridview.getLocationInWindow(gvLocation);
		// 清空列表
		listDynamicFace.clear();
		/**
		 * 获取可见的表情列表
		 */
		int start = parent.getFirstVisiblePosition();
		int end = parent.getLastVisiblePosition();
		for (int i = 0; i <= end - start; i++) {
			HashMap<String, String> map = new HashMap<String, String>();
			View v = parent.getChildAt(i);
			int location[] = new int[2];
			if (null == v) {
				continue;
			}
			// 获取该表情在窗口中的坐标
			v.getLocationInWindow(location);
			if (v == view) {
				gifview.setId(i);
			}
			map.put("x", location[0] + "");
			map.put("y", location[1] + "");
			map.put("imgName", parent.getItemAtPosition(i + start).toString());
			listDynamicFace.add(map);
		}

		// 获取图片名称
		String imgName = parent.getItemAtPosition(position).toString();
		int resId = getResources().getIdentifier(imgName,// 需要转换的资源名称
				"drawable", // 资源类型
				context.getPackageName());// R类所在的包名
		gifview.setMovieResource(resId);
		// 获取控件在窗口中的绝对位置，不包括最顶部的状态栏
		int location[] = new int[2];
		view.getLocationInWindow(location);
		LayoutParams params = new LayoutParams(Util.dip2px(context, 70),
				Util.dip2px(context, 70));
		params.topMargin = location[1] - gvLocation[1]
				+ Util.dip2px(context, nDynamicMarginTop);
		params.leftMargin = location[0] - Util.dip2px(context, 20);
		if (params.topMargin < 0) {
			params.topMargin = 0;
		}
		if (params.leftMargin < 0) {
			params.leftMargin = 0;
		}
		if (params.leftMargin > gridview.getWidth() - Util.dip2px(context, 70)) {
			params.leftMargin = gridview.getWidth() - Util.dip2px(context, 70);
		}
		ll.setLayoutParams(params);
		ll.setVisibility(View.VISIBLE);
		// 返回true可禁止gridview滚动
		return true;
	}
}
