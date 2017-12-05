package com.example.administrator.manager.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import com.example.administrator.manager.MainActivity;
import com.example.administrator.manager.view.me.LoginActivity;
import com.example.administrator.manager.view.me.OrderActivity;

import com.example.administrator.manager.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    GridView gridView;
    Intent intent ;
    int[] imageIds = new int[]
            {
                    R.mipmap.tupian1,R.mipmap.tupian2,R.mipmap.tupian3,R.mipmap.tupian4,
                    R.mipmap.tupian5,R.mipmap.tupian6,R.mipmap.tupian7,R.mipmap.tupian8
            };
    String[] textView_gridView_home = new String[]
            {
                    "订单","记账本","三","四",
                    "五","六","七","八"
            };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //显示首页GridView中的内容
        gridView =(GridView)view.findViewById(R.id.submenu_gridview_home);
        //创建一个List对象，List对象的元素是Map
        List<Map<String,Object>> listItems = new ArrayList<Map<String,Object>>();
        for(int i=0;i<imageIds.length;i++)
        {
            Map<String,Object> listItem = new HashMap<String,Object>();
            listItem.put("image",imageIds[i]);
            listItem.put("text",textView_gridView_home[i]);
            listItems.add(listItem);
        }
        //创建一个SimpleAdapter
        SimpleAdapter simpleAdapter;
        simpleAdapter = new SimpleAdapter(getActivity(),listItems, R.layout.cell,
                new String[]{"image","text"},new int[]{R.id.image1,R.id.tv_gridview_home});

        gridView.setAdapter(simpleAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
        // TODO Auto-generated method stub
                switch(position){
                    case 0://点击图片0跳转
                    {
                        startActivity(new Intent(getActivity(),OrderActivity.class));
                    }
                    break;
                    case 1://点击图片1跳转
                    {
                        startActivity(new Intent(getActivity(),OrderActivity.class));
                    }
                    break;
                    case 2://点击图片2跳转
                    {
                        startActivity(new Intent(getActivity(),OrderActivity.class));
                    }
                    break;
                    case 3://点击图片3跳转
                    {
                        startActivity(new Intent(getActivity(),LoginActivity.class));
                    }
                    break;
                    case 4://点击图片4跳转
                    {
                        startActivity(new Intent(getActivity(),OrderActivity.class));
                    }
                    break;
                    case 5://点击图片5跳转
                    {
                        startActivity(new Intent(getActivity(),OrderActivity.class));
                    }
                    break;
                    case 6://点击图片6跳转
                    {
                        startActivity(new Intent(getActivity(),OrderActivity.class));
                    }
                    break;
                    case 7://点击图片7跳转
                    {
                        startActivity(new Intent(getActivity(),OrderActivity.class));
                    }
                    break;
                    case 8://点击图片8跳转
                    {
                        startActivity(new Intent(getActivity(), OrderActivity.class));
                    }
                }
            }
        });

        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_home, container, false);


        return view;
    }

}
