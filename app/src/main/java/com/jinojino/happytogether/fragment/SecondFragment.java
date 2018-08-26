package com.jinojino.happytogether.fragment;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.jinojino.happytogether.R;
import com.jinojino.happytogether.http.PostThread;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


public class SecondFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private int mParam1;
    private String mParam2;
    int num;
    String object;

    MediaPlayer mMediaPlayer;

    private FrameLayout frameLayout;

    public SecondFragment(){

    }

    // TODO: Rename and change types and number of parameters
    public static SecondFragment newInstance(int param1, String param2) {
        SecondFragment fragment = new SecondFragment();
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
        View view = inflater.inflate(R.layout.fragment_second, container, false);
        frameLayout = view.findViewById(R.id.second_frame_layout);

        object="Mood";
        frameLayout.setBackgroundResource(R.mipmap.setting2);


        final SeekBar sb  = (SeekBar) view.findViewById(R.id.seekBar2);
        final Switch sw = (Switch)view.findViewById(R.id.switch2);
        final ImageView img = (ImageView)view.findViewById(R.id.imageView2);


        sb.getProgressDrawable().setColorFilter(Color.parseColor("#da4754"), PorterDuff.Mode.SRC_IN);
        sb.getThumb().setColorFilter(Color.parseColor("#da4754"), PorterDuff.Mode.SRC_IN);
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                if(seekBar.getProgress()<33){
                    img.setImageResource(R.drawable.mood_img1);
                }else if(seekBar.getProgress()>=33 && seekBar.getProgress()<66){
                    img.setImageResource(R.drawable.mood_img2);
                }else{
                    img.setImageResource(R.drawable.mood_img3);
                }
            }
        });

        sw.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(mMediaPlayer != null){
                    isPlaying();
                }else{
                    mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.test);
                    isPlaying();
                }

            }
        });

        Button postButton = (Button) view.findViewById(R.id.postButton2);
        postButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                PostThread postThread = new PostThread(object, sb.getProgress(), sw.isChecked());
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
    private void isPlaying(){
        if(!mMediaPlayer.isPlaying()){
            mMediaPlayer.start();
        }else{
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

}
