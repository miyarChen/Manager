package com.example.administrator.manager.view.me;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.administrator.manager.R;

public class PicturesActivity extends AppCompatActivity {

    GridView gv_system_pictures;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pictures);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);//ActionBar显示返回箭头按钮
        actionBar.setTitle("图片");//设置actionBar上的标题为"图片"
        actionBar.setElevation(0);

        gv_system_pictures = (GridView) findViewById(R.id.gv_system_pictures);
    }
    @Override
    //"图片"界面上的ActionBar返回按钮实现，关闭当前Activity
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

}
