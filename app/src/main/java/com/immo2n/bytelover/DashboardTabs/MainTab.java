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
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.immo2n.bytelover.Dashboard;
import com.immo2n.bytelover.Global;
import com.immo2n.bytelover.HelperClasses.RoundImagePicasso;
import com.immo2n.bytelover.CoreClasses.Net;
import com.immo2n.bytelover.Objects.ClassObj;
import com.immo2n.bytelover.Objects.User;
import com.immo2n.bytelover.R;
import com.immo2n.bytelover.Server;
import com.squareup.picasso.Picasso;

import java.io.Reader;
import java.util.List;

public class MainTab extends Fragment {
    private Global global;
    private Server server;
    private View user_info;
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

        //Views
        View tab =  inflater.inflate(R.layout.dash_scroll_container, container, false);
        View load =  inflater.inflate(R.layout.dash_comp_load, container, false);
        View no_class =  inflater.inflate(R.layout.dash_comp_no_class, container, false);

        user_info = inflater.inflate(R.layout.dash_main_tab_user, container, false);

        //DYNAMIC - NET - FETCH USER WITH TOKEN

        Net user_sync = new Net(net_handler, global, true);
        user_sync.post(server.getUser_data_link(), "token="+user_token+"&hardware="
                +server.getHardwareSignature(), 101);

        //Components
        RelativeLayout main_body = tab.findViewById(R.id.scroll_body);
        ScrollView main_scroll = tab.findViewById(R.id.main_scroll);
        TableLayout main_table = tab.findViewById(R.id.main_table);
        TextView home_button = user_info.findViewById(R.id.button_home);

        //Click events
        home_button.setOnClickListener(v-> Dashboard.goto_home(global.getContext()));

        Net today_class_check = new Net(net_handler, global, false);
        today_class_check.post(server.getToday_class_link(), "token="+user_token+"&hardware="
                +server.getHardwareSignature(), 102);

        main_body.addView(user_info);
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
                            user_name.setText(user.getFullname());
                            user_degree.setText(!user.getDegree().isEmpty() ? user.getDegree() : "Independent learner");
                            Picasso.get()
                                    .load(server.getUser_photo_end_point() + user.getImage())
                                    .placeholder(R.drawable.user_dp)
                                    .resize(250, 250)
                                    .centerCrop()
                                    .transform(new RoundImagePicasso())
                                    .into(user_image);
                        } catch (Exception e) {
                            Log.d("User-fetch-error", e.toString());
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
                            Log.d("ERROR-MOON-200", e.toString());
                            Dashboard.goto_error(global.getContext(), "unknown");
                        }
                        break;
                }
            }
        };
    }
}
