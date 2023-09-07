package com.immo2n.bytelover.DashboardTabs;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.immo2n.bytelover.CoreClasses.Net;
import com.immo2n.bytelover.Dashboard;
import com.immo2n.bytelover.Global;
import com.immo2n.bytelover.HelperClasses.RoundImagePicasso;
import com.immo2n.bytelover.Objects.ClassObj;
import com.immo2n.bytelover.Objects.User;
import com.immo2n.bytelover.R;
import com.immo2n.bytelover.Server;
import com.squareup.picasso.Picasso;

import java.text.MessageFormat;
import java.util.List;

public class MainTab extends Fragment {
    private Global global;
    private View tab;
    private RelativeLayout margin_body;
    private Server server;
    private View user_info, no_class;
    private LayoutInflater inflater_all;
    private ViewGroup container_all;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        global = new Global(getContext());
        server = new Server(global);
        Handler net_handler = main_tab_net_handler();
        String user_token = global.getUserToken();
        inflater_all = inflater;
        container_all = container;

        //Views
        tab =  inflater.inflate(R.layout.dash_tab_main, container, false);
        View load =  inflater.inflate(R.layout.dash_comp_load, container, false);
        no_class =  inflater.inflate(R.layout.dash_comp_no_class, container, false);

        //DYNAMIC - NET - FETCH USER WITH TOKEN

        Net user_sync = new Net(net_handler, global, true);
        user_sync.post(server.getUser_data_link(), "token="+user_token+"&hardware="
                +server.getHardwareSignature(), 101);

        //Components
        user_info = tab.findViewById(R.id.ll2);
        margin_body = tab.findViewById(R.id.margin_view);
        TextView home_button = user_info.findViewById(R.id.button_home);

        //Click events
        home_button.setOnClickListener(v-> Dashboard.goto_home(global.getContext()));

        //FETCH - CLASS TODAY
        Net today_class_check = new Net(net_handler, global, false);
        today_class_check.post(server.getToday_class_link(), "token="+user_token+"&hardware="
                +server.getHardwareSignature(), 102);

        return tab;
    }

    //NET HANDLER -- FOR ALL REQUESTS
    private Handler main_tab_net_handler(){
        return new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                String data = msg.obj.toString();
                switch (msg.what) {
                    case 101:
                        //USER INFO
                        try {
                            Gson gson = new Gson();
                            User user = gson.fromJson(data, User.class);
                            TextView user_name = user_info.findViewById(R.id.user_name),
                                    user_degree = user_info.findViewById(R.id.user_degree);
                            ImageView user_image = user_info.findViewById(R.id.user_image);
                            tab.post(()-> {
                                user_name.setText(user.getFullname());
                                user_degree.setText(!user.getDegree().isEmpty() ? user.getDegree() : "Independent learner");
                                Picasso.get()
                                        .load(server.getUser_photo_end_point() + user.getImage())
                                        .placeholder(R.drawable.user_dp)
                                        .resize(250, 250)
                                        .centerCrop()
                                        .transform(new RoundImagePicasso())
                                        .into(user_image);
                            });
                        } catch (Exception e) {
                            if (data.equals("UNDER_MAINTENANCE")) {
                                Dashboard.goto_error(global.getContext(), "maintenance");
                                return;
                            }
                            Dashboard.goto_error(global.getContext(), "unknown");
                        }
                        break;
                    case 102:
                        //Todays classes
                        try {
                            Gson gson = new Gson();
                            ClassObj[] classes = gson.fromJson(data, ClassObj[].class);
                            int s = 1;
                            if(classes.length == 0){
                                //NO class
                                tab.post(() -> {
                                    margin_body.removeAllViews();
                                    margin_body.addView(no_class);
                                });
                                return;
                            }
                            for(ClassObj classObj: classes){
                                View frag_class = inflater_all.inflate(R.layout.frag_class_today, container_all, false);
                                TextView serial = frag_class.findViewById(R.id.class_serial),
                                        title = frag_class.findViewById(R.id.class_title),
                                        time = frag_class.findViewById(R.id.class_time),
                                        ends_in = frag_class.findViewById(R.id.class_duration),
                                        class_status = frag_class.findViewById(R.id.class_status),
                                        not_live_message = frag_class.findViewById(R.id.not_live_message),
                                        course = frag_class.findViewById(R.id.course_name);
                                LinearLayout files = frag_class.findViewById(R.id.class_files),
                                            live_button = frag_class.findViewById(R.id.live_button);
                                if(classObj.getStatus().equals("Live now") && !classObj.getClass_link().equals("None")){
                                    not_live_message.setVisibility(View.GONE);
                                    live_button.setOnClickListener(v-> Dashboard.openGoogleMeet(classObj.getClass_link(), global.getContext()));
                                    live_button.setVisibility(View.VISIBLE);
                                }
                                else if(classObj.getStatus().equals("Record available")){
                                    live_button.setOnClickListener(v-> Dashboard.play_class_video(classObj, global.getContext()));
                                    not_live_message.setVisibility(View.GONE);
                                    TextView icon = frag_class.findViewById(R.id.live_icon),
                                            text = frag_class.findViewById(R.id.live_text);
                                    icon.setText("\uf144");
                                    text.setText(R.string.watch_class);
                                    live_button.setVisibility(View.VISIBLE);
                                }
                                serial.setText(MessageFormat.format("{0}. Today at {1}", s, classObj.getStart_time()));
                                title.setText(classObj.getTitle());
                                time.setText(MessageFormat.format("Class time: {0}", classObj.getStart_time()));
                                ends_in.setText(MessageFormat.format("Ends in: {0}", classObj.getDuration()));
                                course.setText(MessageFormat.format("Course: {0}", classObj.getCourse_name()));
                                class_status.setText(classObj.getStatus());
                                //Files
                                List<ClassObj.FileItem> list = classObj.getFile_list();
                                if(list.size() > 0){
                                    files.removeAllViews();
                                    for(ClassObj.FileItem item:list){
                                        View file_child = inflater_all.inflate(R.layout.frag_classfile_child, container_all, false);
                                        TextView name = file_child.findViewById(R.id.file_name),
                                                ext = file_child.findViewById(R.id.file_ext);
                                        ext.setText(global.getExtension(item.getName()));
                                        name.setText(MessageFormat.format("({0}) {1}", item.getSize(), item.getName()));
                                        files.addView(file_child);
                                    }
                                }
                                tab.post(() -> margin_body.addView(frag_class));
                                s++;
                            }
                        }
                        catch (Exception e) {
                            if(data.equals("UNDER_MAINTENANCE")) {
                                Dashboard.goto_error(global.getContext(), "maintenance");
                                return;
                            }
                            if(data.equals("none_today")){
                                Toast.makeText(global.getContext(), "No class today", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if(data.equals("none_booked")){
                                Toast.makeText(global.getContext(), "No course booked", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            Dashboard.goto_error(global.getContext(), "unknown");
                        }
                        break;
                }
            }
        };
    }
}
