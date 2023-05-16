package com.immo2n.bytelover.HomeTabs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.immo2n.bytelover.R;

public class CourseFragment extends Fragment {
    private RelativeLayout live_active, record_active, live_inactive, record_inactive;
    public CourseFragment() {}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View course_tab =  inflater.inflate(R.layout.home_tab_course, container, false);
        //Buttons
        live_active = course_tab.findViewById(R.id.live_active);
        live_inactive = course_tab.findViewById(R.id.live_inactive);
        record_active = course_tab.findViewById(R.id.record_active);
        record_inactive = course_tab.findViewById(R.id.record_inactive);
        live_inactive.setOnClickListener(v->{
            toggle_tab(true);
        });
        record_inactive.setOnClickListener(v-> {
            toggle_tab(false);
        });

        return course_tab;
    }
    private void toggle_tab(boolean is_live){
        if(is_live){
            record_active.setVisibility(View.GONE);
            record_inactive.setVisibility(View.VISIBLE);
            live_inactive.setVisibility(View.GONE);
            live_active.setVisibility(View.VISIBLE);
            return;
        }
        live_active.setVisibility(View.GONE);
        live_inactive.setVisibility(View.VISIBLE);
        record_inactive.setVisibility(View.GONE);
        record_active.setVisibility(View.VISIBLE);
    }
}