package com.anker.ppblutoothkit.calculate;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;


import com.anker.ppblutoothkit.R;
import com.anker.ppblutoothkit.util.DataUtil;
import com.peng.ppscale.vo.PPBodyFatModel;

public class BodyDataDetailActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_body_data_detail);
        TextView textView = findViewById(R.id.data_detail);
        PPBodyFatModel bodyData = DataUtil.util().getBodyDataModel();
        if (bodyData != null) {
            textView.setText(bodyData.toString());
        }
    }
}
