package com.immo2n.bytelover;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.immo2n.bytelover.DashboardTabs.MainTab;
import com.immo2n.bytelover.DashboardTabs.TasksTab;

public class DashboardAdapter extends FragmentStateAdapter {
    public DashboardAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if(position == 0) {
            return new MainTab();
        }
        else if (position == 1){
            return new TasksTab();
        }
        return new MainTab();
    }
    @Override
    public int getItemCount() {
        return 5; //Home and Courses
    }
}
