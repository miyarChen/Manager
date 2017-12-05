package com.example.administrator.manager.view.me;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;

import com.example.administrator.manager.R;
import com.example.administrator.manager.view.MeFragment;
import com.example.administrator.manager.view.me.PicturesActivity;
import com.example.administrator.manager.view.me.PersonalInformationActivity;

public class PersonalInformationActivity extends AppCompatActivity {

    LinearLayout ll_head_portrait;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_information);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setElevation(0);//去除ActionBar底部阴影
        actionBar.setDisplayHomeAsUpEnabled(true);//ActionBar显示返回箭头按钮

        ll_head_portrait = (LinearLayout) findViewById(R.id.ll_head_portrait);
        ll_head_portrait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PersonalInformationActivity.this,PicturesActivity.class);
                startActivity(intent);

            }
        });
    }

    @Override
    //"个人信息"界面上的ActionBar返回按钮实现，关闭当前Activity
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }




}
