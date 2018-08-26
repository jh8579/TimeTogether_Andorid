package com.jinojino.happytogether.fragment;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jinojino.happytogether.R;
import com.jinojino.happytogether.http.PostThread;
import com.jinojino.happytogether.http.ServiceThread;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


public class FirstFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private int mParam1;
    private String mParam2;
    int num;
    String object;

    Timer mTimer;
    TextView tv;

    private FrameLayout frameLayout;

    public FirstFragment(){

    }

    // TODO: Rename and change types and number of parameters
    public static FirstFragment newInstance(int param1, String param2) {
        FirstFragment fragment = new FirstFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            num = getArguments().getInt(ARG_PARAM1);
            object = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_first, container, false);
        frameLayout = view.findViewById(R.id.first_frame_layout);

        object = "Clock";
        frameLayout.setBackgroundResource(R.mipmap.setting1);


        final SeekBar sb  = (SeekBar) view.findViewById(R.id.seekBar);
        final ImageView img = (ImageView)view.findViewById(R.id.imageView3);
        img.setImageAlpha(0);
        tv = (TextView)view.findViewById(R.id.textView);

        MainTimerTask timerTask = new MainTimerTask();
        mTimer = new Timer();
        mTimer.schedule(timerTask, 500, 1000);



        sb.getProgressDrawable().setColorFilter(Color.parseColor("#da4754"), PorterDuff.Mode.SRC_IN);
        sb.getThumb().setColorFilter(Color.parseColor("#da4754"), PorterDuff.Mode.SRC_IN);
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStopTrackingTouch(SeekBar seekBar) {


            }

            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                img.setImageAlpha(progress*2);
            }
        });

        Button postButton = (Button) view.findViewById(R.id.postButton);
        postButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                PostThread postThread = new PostThread(object, sb.getProgress(), true);
                postThread.start();
                try {
                    postThread.join();
                    Toast.makeText(getActivity(),"밝기/전원 정보가 업데이트 되었습니다!", Toast.LENGTH_SHORT).show();
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return view;
    }


    private Handler mHandler = new Handler();
    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            Date rightNow = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss");
            String dateString = formatter.format(rightNow);
            tv.setText(dateString);
        }
    };
    class MainTimerTask extends TimerTask {

        public void run() {
            mHandler.post(mUpdateTimeTask);
        }
    }

}
