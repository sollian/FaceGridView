package com.demo;

import com.sollian.facegridview.FaceGridView;
import com.sollian.facegridview.R;

import android.os.Bundle;
import android.app.Activity;

public class MainActivity extends Activity {
	private FaceGridView fgv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        fgv = (FaceGridView)findViewById(R.id.fgv);
        fgv.setAdapter(new FaceGridViewAdapter(getBaseContext()));
    }
}
