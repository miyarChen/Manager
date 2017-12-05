package com.example.administrator.manager.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.List;

/**
 * Created by Administrator on 2016/10/12 0012.
 */
public class HomeTabAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener {

    private List<Fragment> fragments;//主界面的fragment
    private int currentPageIndex = 0;//当前的页码
    private ViewPager viewPager; //主界面中间部分是由viewpager构成

    public HomeTabAdapter(FragmentManager fm) {
        super(fm);
    }

    public HomeTabAdapter(FragmentManager fm, List<Fragment> fragments,  ViewPager viewPager) {
        super(fm);
        this.fragments = fragments;
        this.viewPager = viewPager;
        this.viewPager.addOnPageChangeListener(this);

    }

    //底部按钮点击
    public void showFragmentWithID(int index){
        if (index != currentPageIndex)
            viewPager.setCurrentItem(index, false);

    }


    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        fragments.get(currentPageIndex).onPause(); // 调用切换前Fargment的onPause()
        if (fragments.get(position).isAdded()) {
            fragments.get(position).onResume(); // 调用切换后Fargment的onResume()
        }
        currentPageIndex = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
