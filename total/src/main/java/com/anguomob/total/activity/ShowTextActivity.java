package com.anguomob.total.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import com.anguomob.total.R;
import com.anguomob.total.utils.StatusBarUtil;
import com.anguomob.total.utils.ToolbarUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShowTextActivity extends AppCompatActivity {

    Toolbar mToolbar;
    TextView mTvAstContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_text);


        mToolbar = findViewById(R.id.toolbar);
        mTvAstContent = findViewById(R.id.tv_ast_content);
        ButterKnife.bind(this);
        String text = getIntent().getStringExtra("text");
        String sub_text = getIntent().getStringExtra("sub_text");
        int toobar_bg = getIntent().getIntExtra("toobar_bg_id", -1);

        ToolbarUtils.setToobar(sub_text, mToolbar, this);
        if (toobar_bg != -1) {
            StatusBarUtil.initStatusBar(this, false, toobar_bg);
            mToolbar.setBackground(getResources().getDrawable(toobar_bg));
        } else {
            mToolbar.setBackgroundColor(getResources().getColor(R.color.color_main));
        }
        if (!TextUtils.isEmpty(text)) {
            mTvAstContent.setText(text);
        }

    }
}
