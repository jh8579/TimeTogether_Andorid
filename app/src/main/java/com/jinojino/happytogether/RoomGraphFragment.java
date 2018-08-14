package com.jinojino.happytogether;

import android.database.Cursor;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
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

import static com.github.mikephil.charting.components.Legend.LegendPosition.BELOW_CHART_LEFT;


public class RoomGraphFragment extends Fragment {

    LineChart lineChart;
    ArrayList<String> labels;

    private String room_name;
    private DBHelper dbHelper;
    private Cursor mCursor;

    public RoomGraphFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DBHelper(getActivity().getApplicationContext(), "RoomNoise.db", null, 1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_room_graph, container, false);

        // DB에서 Data 전송시
        //ArrayList<Entry> entries = dbHelper.getNoise(id);

        // 서버에서 Data 전송시
        ArrayList<Entry> entries1 = new ArrayList<>();
        ArrayList<Entry> entries2 = new ArrayList<>();
        ArrayList<Entry> entries3 = new ArrayList<>();

        ArrayList<Entry> entries[] = new ArrayList[10];

        labels = new ArrayList<String>();
        HttpThread thread = new HttpThread();

        thread.start();
        try {
            thread.join();
        } catch(Exception e) {
            e.printStackTrace();
        }

        int count = 0;
        try{
            JSONArray jarray = new JSONArray(thread.getResult());

            JSONArray countArray = jarray.getJSONArray(0);
            JSONObject countObject = countArray.getJSONObject(0);
            count = countObject.getInt("count");
            entries = new ArrayList[count];
            for(int i=0; i<count; i++){
                entries[i] = new ArrayList<>();
            }

            for(int j=1; j<=count; j++){
                JSONArray roomArray = jarray.getJSONArray(j);
                for(int i=0; i < roomArray.length(); i++){
                    JSONObject noiseObject = roomArray.getJSONObject(i);  // JSONObject 추출
                    int id = noiseObject.getInt("roomId");
                    int noise = noiseObject.getInt("noise");
                    String time = noiseObject.getString("time");
                    entries[j-1].add(new Entry(i , noise));
                    if(id == 1){
                        labels.add(time);
                    }
                    Log.d("id", String.valueOf(id));
                }
            }
        } catch(JSONException e){
            e.printStackTrace();
        }

        lineChart = (LineChart)view.findViewById(R.id.roomNoiseChart);

        IAxisValueFormatter formatter = new IAxisValueFormatter() {
            String[] array = labels.toArray(new String[labels.size()]);

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return array[(int) value];
            }

        };

        XAxis xAxis = lineChart.getXAxis();
        YAxis lyAxis = lineChart.getAxisLeft();
        YAxis ryAxis = lineChart.getAxisRight();

        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelCount(3);
        xAxis.setTextColor(Color.parseColor("#80d87d9d"));
        xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
        xAxis.setValueFormatter(formatter);

        lyAxis.setDrawGridLines(true);
        lyAxis.setGridColor(Color.parseColor("#80d87d9d"));
        lyAxis.enableGridDashedLine(10, 10, 10);
        lyAxis.setDrawAxisLine(true);
        lyAxis.setAxisLineColor(Color.parseColor("#d87d9d"));
        lyAxis.enableAxisLineDashedLine(10, 10, 10);
        lyAxis.setTextColor(Color.parseColor("#d87d9d"));

        ryAxis.setDrawGridLines(false);
        ryAxis.setDrawAxisLine(false);
        ryAxis.setGridColor(Color.parseColor("#d87d9d"));
        ryAxis.setTextColor(Color.parseColor("#d87d9d"));

        String name[] = {"Living room", "Kitchen",  "Room 1", "Room 2", "Bathroom"};
        String colorData[] = {"#A9B9DF", "#F4AA9B", "#FCDD7D", "#58FAD0", "#6E6E6E"};

        LineDataSet data[] = new LineDataSet[count];
        for(int i=0; i<count; i++){
            data[i] = new LineDataSet(entries[i], name[i]);
            data[i].setColor(Color.parseColor(colorData[i]));
            data[i].setCircleColor(Color.parseColor(colorData[i]));
            data[i].setFillColor(Color.parseColor(colorData[i]));
            data[i].setMode(LineDataSet.Mode.CUBIC_BEZIER); //선 둥글게 만들기
            data[i].setDrawFilled(true); //그래프 밑부분 색칠
        }

        LineData lineData = new LineData();
       for(int i=0; i<count; i++){
           lineData.addDataSet(data[i]);
       }

        Legend legend = lineChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setTextColor(Color.parseColor("#d87d9d"));
        legend.setTextSize(10);
        legend.setFormToTextSpace(10);

        lineChart.setData(lineData);
        lineChart.getDescription().setEnabled(false);
        lineChart.setBorderColor(Color.parseColor("#FFFFFF"));
        lineChart.setBackgroundColor(Color.parseColor("#FFFFFF"));
        lineChart.setDrawGridBackground(false);
        //lineChart.setGridBackgroundColor(Color.parseColor("#FFFFFF"));
        lineChart.animateY(1500);

        return view;
    }
}
