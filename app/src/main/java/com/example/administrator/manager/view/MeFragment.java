package com.example.administrator.manager.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.administrator.manager.R;
import com.example.administrator.manager.view.me.OrderActivity;
import com.example.administrator.manager.view.me.PersonalInformationActivity;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MeFragment extends Fragment {

    RelativeLayout rlOrder ;
    RelativeLayout personal_information_rl;
    Intent intent;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        // Inflate the layout for this fragment
        /*rlOrder = (RelativeLayout) view.findViewById(R.id.rl_order);
        rlOrder.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(getActivity(),OrderActivity.class));
            }
        }
        );*/
        //点击头像进入个人信息界面
        personal_information_rl = (RelativeLayout) view.findViewById(R.id.personal_information_rl);
        personal_information_rl.setOnClickListener(new View.OnClickListener()
                                   {
                                       @Override
                                       public void onClick(View v)
                                       {
                                           startActivity(new Intent(getActivity(), PersonalInformationActivity.class));
                                       }
                                   }
        );
        return view;
    }

}
