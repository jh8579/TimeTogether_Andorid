package com.jinojino.happytogether;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import com.jinojino.happytogether.DB.DBHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;


public class RoomGraphFragment extends Fragment {

    LineChart lineChart;
    ArrayList<String> labels;

    private String room_name;
    private DBHelper dbHelper;
    private Cursor mCursor;
    int id;
    String title;

    public RoomGraphFragment() {

    }

    public static RoomGraphFragment newInstance() {
        RoomGraphFragment fragment = new RoomGraphFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DBHelper(getActivity().getApplicationContext(), "RoomNoise.db", null, 1);

        if (getArguments() != null) {
            id = getArguments().getInt("id");
            title = getArguments().getString("title");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_room_graph, container, false);

        // DB에서 Data 전송시
        //ArrayList<Entry> entries = dbHelper.getNoise(id);

        // 서버에서 Data 전송시
        ArrayList<Entry> entries = new ArrayList<>();
        labels = new ArrayList<String>();
        HttpThread thread = new HttpThread(id);

        thread.start();
        try {
            thread.join();
        } catch(Exception e) {
            e.printStackTrace();
        }

        try{
            JSONArray jarray = new JSONArray(thread.getResult());
            for(int i=0; i < jarray.length(); i++){
                JSONObject jObject = jarray.getJSONObject(i);  // JSONObject 추출
                int id = jObject.getInt("id");
                int noise = jObject.getInt("noise");
                String time = jObject.getString("time");
                entries.add(new Entry(i , noise));
                labels.add(time);
            }
        } catch(JSONException e){
            e.printStackTrace();
        }


        lineChart = (LineChart)view.findViewById(R.id.roomNoiseChart);
        LineDataSet dataset = new LineDataSet(entries, title);


        Log.d("DB 확인", String.valueOf(entries.size()));

        IAxisValueFormatter formatter = new IAxisValueFormatter() {
            String[] array = labels.toArray(new String[labels.size()]);

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return array[(int) value];
            }

        };

        XAxis xAxis = lineChart.getXAxis();
        YAxis yAxis = lineChart.getAxisLeft();
        YAxis y = lineChart.getAxisRight();

        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
        xAxis.setValueFormatter(formatter);

        yAxis.setDrawGridLines(false);
        yAxis.setDrawAxisLine(false);

        y.setGridColor(Color.parseColor("#FFFFFF"));

        LineData data = new LineData(dataset);
        dataset.setColors(Color.parseColor("#FFFFFF"));
        dataset.setFillColor(Color.parseColor("#ffe6e6"));
        dataset.setMode(LineDataSet.Mode.CUBIC_BEZIER); //선 둥글게 만들기
        dataset.setDrawFilled(true); //그래프 밑부분 색칠


        lineChart.setData(data);
        lineChart.setBorderColor(Color.parseColor("#FFFFFF"));
        lineChart.setBorderColor(Color.parseColor("#FFFFFF"));
        lineChart.setGridBackgroundColor(Color.parseColor("#FFFFFF"));
        lineChart.setDrawGridBackground(false);
        lineChart.setBackgroundColor(Color.parseColor("#ff8080"));
        lineChart.animateY(3000);

        // Inflate the layout for this fragment
        return view;
    }
}
