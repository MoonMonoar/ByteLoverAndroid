package com.immo2n.bytelover.HomeTabs;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.google.gson.reflect.TypeToken;
import com.immo2n.bytelover.Global;
import com.immo2n.bytelover.CoreClasses.Net;
import com.immo2n.bytelover.Objects.Course;
import com.immo2n.bytelover.R;
import com.immo2n.bytelover.Server;

import java.util.ArrayList;
import java.util.Map;

public class CourseFragment extends Fragment {
    private String tab_mode = "Live";
    private RelativeLayout live_active, record_active, live_inactive, record_inactive;
    private Global global;
    private TableLayout course_scroll;
    private ScrollView scrollView;
    private RelativeLayout load;
    private LottieAnimationView failed, loading, relax;
    private TextView error_message;
    private LayoutInflater main_inflater;
    private ViewGroup main_view_group;
    private Server server;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        global = new Global(getContext());
        server = new Server(global);
        main_inflater = inflater;
        main_view_group = container;
        // Inflate the layout for this fragment
        View course_tab =  inflater.inflate(R.layout.home_tab_course, container, false);
        course_scroll = course_tab.findViewById(R.id.course_scroll);
        scrollView = course_tab.findViewById(R.id.main_scroll);
        load = course_tab.findViewById(R.id.load);
        error_message = course_tab.findViewById(R.id.error_message);
        loading = course_tab.findViewById(R.id.loading);
        failed = course_tab.findViewById(R.id.failed);
        relax = course_tab.findViewById(R.id.no_course);
        //Buttons
        live_active = course_tab.findViewById(R.id.live_active);
        live_inactive = course_tab.findViewById(R.id.live_inactive);
        record_active = course_tab.findViewById(R.id.record_active);
        record_inactive = course_tab.findViewById(R.id.record_inactive);
        live_inactive.setOnClickListener(v-> toggle_tab(true));
        record_inactive.setOnClickListener(v-> toggle_tab(false));
        load_data(true);
        return course_tab;
    }
    private void toggle_tab(boolean is_live){
        load_data(is_live);
        if(is_live){
            record_active.setVisibility(View.GONE);
            record_inactive.setVisibility(View.VISIBLE);
            live_inactive.setVisibility(View.GONE);
            live_active.setVisibility(View.VISIBLE);
            return;
        }
        //Load recorded course data
        live_active.setVisibility(View.GONE);
        live_inactive.setVisibility(View.VISIBLE);
        record_inactive.setVisibility(View.GONE);
        record_active.setVisibility(View.VISIBLE);
    }
    private void load_data(boolean is_live){
        Handler course_load_handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg){
                String data = msg.obj.toString();
                if(data.equals("ERROR_NO_NET")){
                    error_message.setText(R.string.no_internet);
                    loading.setVisibility(View.GONE);
                    failed.playAnimation();
                    failed.setVisibility(View.VISIBLE);
                }
                else {
                    //Process the data
                    Map<String, Object> responseObject = global.jsonMap(data);
                    String status = (String)responseObject.get("status");
                    if(!tab_mode.equals(responseObject.get("type"))
                            || null == course_scroll
                            || load.getVisibility() != View.VISIBLE
                            || !load.isShown()){
                            return;
                    }
                    //Errors
                    if(null == status || status.isEmpty() || status.equals("busy")){
                        error_message.setText(R.string.sorry_server_is_busy_try_again);
                        loading.setVisibility(View.GONE);
                        loading.pauseAnimation();
                        failed.playAnimation();
                        failed.setVisibility(View.VISIBLE);
                        return;
                    }
                    if(status.equals("denied")){
                        error_message.setText(R.string.api_rejection_update_app);
                        loading.pauseAnimation();
                        loading.setVisibility(View.GONE);
                        failed.playAnimation();
                        failed.setVisibility(View.VISIBLE);
                        return;
                    }

                    //Process the data with the course object
                    ArrayList<Course> courseList = global.getGson().fromJson((String) responseObject.get("list"), new TypeToken<ArrayList<Course>>() {}.getType());
                    if(null == courseList){
                        //Error loading
                        return;
                    }
                    if(courseList.size() == 0){
                        //No courses for now!
                        failed.setVisibility(View.GONE);
                        failed.pauseAnimation();
                        loading.setVisibility(View.GONE);
                        loading.pauseAnimation();
                        relax.playAnimation();
                        relax.setVisibility(View.VISIBLE);
                        error_message.setText(R.string.no_courses_right_now);
                        return;
                    }
                    //Cleanup
                    course_scroll.removeAllViews();
                    for(Course course: courseList){
                        //Loop of every course
                        View child = main_inflater.inflate(R.layout.frag_course_child, main_view_group);
                        TextView title = child.findViewById(R.id.course_title),
                                short_des = child.findViewById(R.id.course_short_description),
                                code = child.findViewById(R.id.course_code),
                                level = child.findViewById(R.id.course_level),
                                batch = child.findViewById(R.id.course_batch),
                                slot = child.findViewById(R.id.course_slot),
                                duration = child.findViewById(R.id.course_duration),
                                date_start = child.findViewById(R.id.course_date_start),
                                date_end = child.findViewById(R.id.course_date_end),
                                course_lang = child.findViewById(R.id.course_language),
                                price = child.findViewById(R.id.course_price);
                        title.setText(course.getName());
                        short_des.setText(course.getShortDescription());
                        code.setText(String.format(": %s", course.getCourseCode()));
                        level.setText(String.format(": %s", course.getLevel()));
                        batch.setText(String.format(": %s", course.getBatch()));
                        slot.setText(String.format(": %s", course.getSlot()));
                        duration.setText(String.format(": %s", course.getDuration()));
                        date_start.setText(String.format(": %s", course.getStartDate()));
                        date_end.setText(String.format(": %s", course.getEndDate()));
                        course_lang.setText(String.format(": %s", course.getCourseLanguage()));
                        price.setText(String.format(" %s", course.getPrice()));
                        if(!course.getStatus().equals("Live")){
                            child.findViewById(R.id.live_tag).setVisibility(View.GONE);
                            child.findViewById(R.id.rc_tag).setVisibility(View.VISIBLE);
                        }
                        child.findViewById(R.id.info).setOnClickListener(v -> {
                            Intent open = new Intent(v.getContext(), com.immo2n.bytelover.Course.class);
                            open.putStringArrayListExtra("data", course.getAllData());
                            v.getContext().startActivity(open);
                        });
                        course_scroll.addView(child);
                    }
                    load.setVisibility(View.GONE);
                    scrollView.setVisibility(View.VISIBLE);
                }
            }
        };
        Net net = new Net(course_load_handler, global, true);
        scrollView.setVisibility(View.GONE);
        error_message.setText("");
        failed.setVisibility(View.GONE);
        failed.pauseAnimation();
        relax.setVisibility(View.GONE);
        relax.pauseAnimation();
        loading.playAnimation();
        loading.setVisibility(View.VISIBLE);
        load.setVisibility(View.VISIBLE);
        if(is_live){
            tab_mode = "Live";
            //Live data load
            net.post(server.courses_data_link, "hardware="+global.makeUrlSafe(global.getAndroidId(global.getContext())), null);
            return;
        }
        tab_mode = "Recorded";
        //Load recorded course data
        net.post(server.courses_data_link, "type=recorded&hardware="+server.getHardwareSignature(), null);
    }
}