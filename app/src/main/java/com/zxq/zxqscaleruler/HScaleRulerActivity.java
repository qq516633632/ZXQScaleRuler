package com.zxq.zxqscaleruler;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.zxq.scalruerview.HorizontalScaleRulerView;

public class HScaleRulerActivity extends AppCompatActivity {
    private HorizontalScaleRulerView app_ver_view;
    private TextView lab_text_view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hscale_ruler);
        app_ver_view=findViewById(R.id.app_ver_view);
        lab_text_view=findViewById(R.id.lab_text_view);
        app_ver_view.setValueChangeListener(new HorizontalScaleRulerView.OnValueChangeListener() {
            @Override
            public void onValueChange(float value) {
                lab_text_view.setText("体重："+value+"kg");
            }
        });
    }
}
