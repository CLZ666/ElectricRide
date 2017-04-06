package com.wanle.lequan.sharedbicycle.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.wanle.lequan.sharedbicycle.MainActivity;
import com.wanle.lequan.sharedbicycle.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * autor:Jerry
 * fuction:
 * Date: 2017/4/5.
 */

public class FitstBootEndFragment extends Fragment {
    public static FitstBootEndFragment newInstance() {

        Bundle args = new Bundle();

        FitstBootEndFragment fragment = new FitstBootEndFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first_boot_end, container, false);
        view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
       Button btn_start= (Button) view.findViewById(R.id.btn_start);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSharedPreferences("isFirst", MODE_PRIVATE).edit()
                        .putBoolean("isFirst",true) .commit();
                startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().finish();
            }
        });
        return view;
    }
}
