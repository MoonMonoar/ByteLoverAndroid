package com.immo2n.bytelover.DashboardTabs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;

import androidx.fragment.app.Fragment;

import com.immo2n.bytelover.R;

public class TasksTab extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Views
        View tab =  inflater.inflate(R.layout.dash_tab_main, container, false);
        View load =  inflater.inflate(R.layout.dash_comp_load, container, false);

        //Components
        RelativeLayout main_body = tab.findViewById(R.id.scroll_body);
        ScrollView main_scroll = tab.findViewById(R.id.main_scroll);
        TableLayout main_table = tab.findViewById(R.id.main_table);



        //Main tab


        main_body.addView(load);
        return tab;
    }
}
