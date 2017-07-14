package com.sollian.demo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.GridView;

import com.sollian.facepreview.EmotionPreview;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GridView vGrid = (GridView) findViewById(R.id.gridview);
        vGrid.setAdapter(new FaceGridViewAdapter(this));

        EmotionPreview emotionPreview = new EmotionPreview(this);

        vGrid.setOnItemLongClickListener(emotionPreview);
        vGrid.setOnTouchListener(emotionPreview);
    }
}
