package com.immo2n.bytelover.HomeTabs;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.immo2n.bytelover.Dashboard;
import com.immo2n.bytelover.Global;
import com.immo2n.bytelover.Login;
import com.immo2n.bytelover.Net;
import com.immo2n.bytelover.Objects.Course;
import com.immo2n.bytelover.R;
import com.immo2n.bytelover.Server;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CourseFragment extends Fragment {
    private String tab_mode = "Live";
    private RelativeLayout live_active, record_active, live_inactive, record_inactive;
    private Global global;
    private TableLayout course_scroll;
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
        Handler course_load_handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg){
                String data = msg.obj.toString();
                if(data.equals("ERROR_NO_NET")){
                    Toast.makeText(global.getContext(), "No internet!", Toast.LENGTH_SHORT).show();
                }
                else {
                    //Process the data
                    Map<String, Object> responseObject = global.jsonMap(data);
                    String status = (String)responseObject.get("status");
                    if(!tab_mode.equals(responseObject.get("type"))
                            || null == course_scroll
                            || course_scroll.getVisibility() != View.VISIBLE
                            || !course_scroll.isShown()){
                            return;
                    }
                    //Errors
                    if(null == status || status.isEmpty() || status.equals("busy")){
                        Toast.makeText(global.getContext(), "Sorry, server is busy! Try again.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(status.equals("denied")){
                        Toast.makeText(global.getContext(), "API rejection. Update app.", Toast.LENGTH_SHORT).show();
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

                    }
                    //Cleanup
                    course_scroll.removeAllViews();
                    for(Course course: courseList){
                        //Loop of every course
                        View child = main_inflater.inflate(R.layout.course_child, main_view_group);
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
                        price.setText(String.format("%s", course.getPrice()));
                        course_scroll.addView(child);
                    }
                }
            }
        };
        Net net = new Net(course_load_handler, global);

        if(is_live){
            tab_mode = "Live";
            //Live data load
            net.post(server.courses_data_link, "hardware="+global.makeUrlSafe(global.getAndroidId(global.getContext())));
            return;
        }
        tab_mode = "Recorded";
        //Load recorded course data
        net.post(server.courses_data_link, "type=recorded&hardware="+global.makeUrlSafe(global.getAndroidId(global.getContext())));
    }
}