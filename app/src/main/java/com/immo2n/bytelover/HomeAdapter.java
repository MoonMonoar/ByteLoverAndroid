package com.immo2n.bytelover;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.immo2n.bytelover.HomeTabs.CourseFragment;
import com.immo2n.bytelover.HomeTabs.HomeFragment;

public class HomeAdapter extends FragmentStateAdapter {
    private final Global global;
    public HomeAdapter(@NonNull FragmentActivity fragmentActivity, Global global_obj) {
        super(fragmentActivity);
        global = global_obj;
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if(position == 0) {
            return new HomeFragment(global);
        }
        return new CourseFragment(global);
    }
    @Override
    public int getItemCount() {
        return 2; //Home and Courses
    }
}