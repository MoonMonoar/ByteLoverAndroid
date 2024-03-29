package com.immo2n.bytelover.HomeTabs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.airbnb.lottie.LottieAnimationView;
import com.immo2n.bytelover.Home;
import com.immo2n.bytelover.R;
import com.immo2n.bytelover.Slides;

public class HomeFragment extends Fragment {
    private LottieAnimationView home_image_1, home_image_2;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View home_tab =  inflater.inflate(R.layout.home_tab_default, container, false);

        //Page components
        home_image_1 = home_tab.findViewById(R.id.home_image_1);
        home_image_2 = home_tab.findViewById(R.id.home_image_2);

        //Buttons
        home_tab.findViewById(R.id.goto_dash).setOnClickListener(v-> Home.goto_dash(getContext()));

        home_tab.findViewById(R.id.feature_expand).setOnClickListener(v-> {
            Intent slides = new Intent(getContext(), Slides.class);
            slides.putExtra("comeback", true);
            startActivity(slides);
        });
        return home_tab;
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        getParentFragmentManager().registerFragmentLifecycleCallbacks(new FragmentManager.FragmentLifecycleCallbacks() {
            @Override
            public void onFragmentPaused(@NonNull FragmentManager fm, @NonNull Fragment f) {
                super.onFragmentPaused(fm, f);
                home_image_1.pauseAnimation();
                home_image_2.pauseAnimation();
            }
            @Override
            public void onFragmentResumed(@NonNull FragmentManager fm, @NonNull Fragment f) {
                super.onFragmentResumed(fm, f);
                home_image_1.resumeAnimation();
                home_image_2.resumeAnimation();
            }
        }, true);
    }
}