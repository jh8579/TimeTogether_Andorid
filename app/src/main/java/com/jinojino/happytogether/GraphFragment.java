package com.jinojino.happytogether;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class GraphFragment extends Fragment{
    String[] roomList = {"거실", "방1", "안방"};
    int [] roomId = {1, 2, 3};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_graph, container, false);
        ListView listView = (ListView) view.findViewById(R.id.roomListView);
        ArrayAdapter listAdapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1, roomList);
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                adapterView.getItemAtPosition(pos);
                RoomGraphFragment newFragment = new RoomGraphFragment();
                Bundle args = new Bundle();
                args.putInt("id", roomId[pos]);
                args.putString("title", roomList[pos]);
                newFragment.setArguments(args);
                ((Main2Activity)getActivity()).replaceFragment(newFragment);
            }
        });
        return view;
    }
}
