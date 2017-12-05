package com.example.administrator.manager;

import android.support.v7.app.ActionBar;
import android.content.pm.ActivityInfo;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.manager.view.HomeFragment;
import com.example.administrator.manager.view.ClassifyFragment;
import com.example.administrator.manager.view.MeFragment;
import com.example.administrator.manager.adapter.HomeTabAdapter;
import com.example.administrator.manager.view.viewpager.NoScrollViewPager;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private List<ImageView> imageViewList;
    private List<TextView> textViewList;
    private List<TextView> unReadMsgList;
    private List<Fragment> fragmentList;

    private int index = 0;//再次点击的id
    private int currentTabIndex; //当前显示的id
    private HomeTabAdapter homeTabAdapter;
    private NoScrollViewPager viewPager_main;

    private HomeFragment homeFragment;
    private ClassifyFragment classifyFragment;
    private MeFragment meFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

/*
        ActionBar actionBar = getActionBar();
        getSupportActionBar().hide();//隐藏ActionBar
*/
        ActionBar actionBar = getSupportActionBar();
        actionBar.setElevation(0);//去除ActionBar底下的阴影
        actionBar.setDisplayShowHomeEnabled(true);//设置是否显示应用程序的图标
        actionBar.setHomeButtonEnabled(true);//将应用程序图标设置为可点击图标

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//始终竖屏
        //找到每个view
        findView();



        homeTabAdapter = new HomeTabAdapter(getSupportFragmentManager(), fragmentList, viewPager_main);
        //设置缓存视图最多5页
        viewPager_main.setOffscreenPageLimit(5);
        viewPager_main.setAdapter(homeTabAdapter);

        //更新第一个view的颜色为红色
        updataUI(0);
    }
    //初始化各种view
    private void findView()
    {
        imageViewList = new ArrayList<ImageView>();
        imageViewList.add((ImageView) findViewById(R.id.home_iv));
        imageViewList.add((ImageView) findViewById(R.id.classify_iv));
        imageViewList.add((ImageView) findViewById(R.id.me_iv));

        textViewList = new ArrayList<TextView>();
        textViewList.add((TextView) findViewById(R.id.home_tv));
        textViewList.add((TextView) findViewById(R.id.classify_tv));
        textViewList.add((TextView) findViewById(R.id.me_tv));

        unReadMsgList = new ArrayList<TextView>();
        unReadMsgList.add((TextView) findViewById(R.id.unread_home));
        unReadMsgList.add((TextView) findViewById(R.id.unread_classify));
        unReadMsgList.add((TextView) findViewById(R.id.unread_me));

        fragmentList = new ArrayList<Fragment>();
        homeFragment = new HomeFragment();
        classifyFragment = new ClassifyFragment();
        meFragment = new MeFragment();

        fragmentList.add(homeFragment);
        fragmentList.add(classifyFragment);
        fragmentList.add(meFragment);

        viewPager_main = (NoScrollViewPager)findViewById(R.id.viewpager_main_v2);
    }
    //点击底部菜单触发
    public void onTabClick(View view)
    {
        switch (view.getId())
        {
            case R.id.home_menu_ll:
                index=0;
                break;
            case R.id.classify_menu_ll:
                index=1;
                break;
            case R.id.me_menu_ll:
                index=2;
                break;
        }
        updataUI(index);
    }
    //更新UI
    private void updataUI(int index){

        if (currentTabIndex != index && index != 3){
            TextView tv =  unReadMsgList.get(index);
            tv.setText("0");
            tv.setVisibility(View.INVISIBLE);
        }

        //选中的菜单图标为的值为false，未选中的为true
        imageViewList.get(currentTabIndex).setSelected(false);
        imageViewList.get(index).setSelected(true);

        //选中的菜单文字为红色，未选中的为灰色
        textViewList.get(currentTabIndex).setTextColor(this.getResources().getColor(R.color.colorDarkGrey));
        textViewList.get(index).setTextColor(this.getResources().getColor(R.color.barcolor));

        currentTabIndex = index;

        //点击事件之后，将fragment换到另一页
        homeTabAdapter.showFragmentWithID(index);
    }

    //搜索功能开始
    private SearchView mSearchView;
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflator = new MenuInflater(this);
        //状态R.menu.head_menu 对应的菜单，并添加到menu中
        inflator.inflate(R.menu.head_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
     /*
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.head_menu, menu);
        //找到ActionBar上所添加的UI组件的方法:
        mSearchView=(SearchView) menu.findItem(R.id.search).getActionView();
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String string) {
                Toast.makeText(MainActivity.this, "查询:"+string, Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String string) {
                return true;
            }
        });

        return true;
    }
    //搜索功能结束*/
}
