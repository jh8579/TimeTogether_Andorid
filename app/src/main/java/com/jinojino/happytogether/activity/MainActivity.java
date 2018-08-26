package com.jinojino.happytogether.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.jinojino.happytogether.DB.DBHelper;
import com.jinojino.happytogether.R;
import com.jinojino.happytogether.fragment.MainFragment;
import com.jinojino.happytogether.fragment.RoomGraphFragment;
import com.jinojino.happytogether.service.GetNoiseService;

public class MainActivity extends AppCompatActivity {

    private DBHelper dbHelper;
    private Cursor mCursor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // splash에서 메인으로 전환
        setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        final ConstraintLayout mylayout = (ConstraintLayout) findViewById(R.id.container);

        Button startButton = (Button) findViewById(R.id.startButton);
        Button endButton = (Button) findViewById(R.id.endButton);

        startButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Toast.makeText(getApplicationContext(),"Service 시작", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this,GetNoiseService.class);
                startService(intent);
            }
        });
        endButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Toast.makeText(getApplicationContext(),"Service 끝",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this,GetNoiseService.class);
                stopService(intent);
            }
        });


        BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
                = new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        mylayout.setBackgroundResource(R.mipmap.background_setting);
                        setTitle("층간소음");
                        replaceFragment(new MainFragment());
                        return true;

                    case R.id.navigation_graph:
                        mylayout.setBackgroundResource(R.mipmap.background_main);
                        setTitle("층간소음 그래프");
                        replaceFragment(new RoomGraphFragment());
                        return true;


                }
                return false;
            }
        };

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        mylayout.setBackgroundResource(R.mipmap.background_setting);
        fragmentTransaction.add(R.id.main_fragment, new MainFragment()).commit();
    }

    public void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_fragment, fragment).commit();
    }
}
