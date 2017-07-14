package com.sollian.demo;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.sollian.facepreview.Util;

/**
 * 装载表情的gridview的adapter
 *
 * @author sollian
 */
class FaceGridViewAdapter extends BaseAdapter {
    /**
     * 显示的图片数组
     */
    private final String[] emc = {
            "emc0", "emc1", "emc2", "emc3", "emc4", "emc5",
            "emc6", "emc7", "emc8", "emc9", "emc10", "emc11", "emc12", "emc13",
            "emc14", "emc15", "emc16", "emc17", "emc18", "emc19", "emc20",
            "emc21", "emc22", "emc23", "emc24", "emc25", "emc26", "emc27",
            "emc28", "emc29", "emc30", "emc31", "emc32", "emc33", "emc34",
            "emc35", "emc36", "emc37", "emc38", "emc39", "emc40", "emc41",
            "emc42", "emc43", "emc44", "emc45", "emc46", "emc47", "emc48",
            "emc49", "emc50", "emc51", "emc52", "emc53", "emc54", "emc55",
            "emc56", "emc57", "emc58",
    };
    private final Context context;

    FaceGridViewAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return emc.length;
    }

    @Override
    public Object getItem(int position) {
        return emc[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = new ImageView(context);
            convertView.setLayoutParams(new GridView.LayoutParams(Util.dip2px(
                    context, 40), Util.dip2px(context, 40)));
            ((ImageView) convertView).setScaleType(ScaleType.FIT_XY);
        }
        ((ImageView) convertView).setImageResource(Util.getResourceId(context, emc[position]));
        return convertView;
    }
}