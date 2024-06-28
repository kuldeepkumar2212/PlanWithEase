package com.example.travelapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.example.travelapp.Adapter.Adapter;
import com.example.travelapp.Data.AppData;
import com.example.travelapp.Database.RoomDB;
import com.example.travelapp.constants.myconstants;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<String> titles;
    List<Integer> images;
    Adapter adapter;
    RoomDB database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        recyclerView = findViewById(R.id.recyclerview);

        addAllTitles();
        addAllImages();
        persistAppdata();
        database = RoomDB.getInstance(this);
        System.out.println("-------->"+database.mainDao().getALlSelected(false).get(0).getItemname());

        adapter = new com.example.travelapp.Adapter.Adapter(this,titles,images,MainActivity.this);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2,RecyclerView.VERTICAL,false);

        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);

    }

    private static final int TIME_INTERVAL = 2000;
    private long mBackPressed;

    @Override
    public void onBackPressed() {
       if(mBackPressed+TIME_INTERVAL > System.currentTimeMillis()){
           super.onBackPressed();
           return;
       }
       else{
           Toast.makeText(this,"Double Click to exit",Toast.LENGTH_SHORT).show();
       }
        mBackPressed= System.currentTimeMillis();
    }

    private void persistAppdata(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        database = RoomDB.getInstance(this);
        AppData appData = new AppData(database);
        int last = prefs.getInt(AppData.LAST_VERSION,0);
        if(!prefs.getBoolean(myconstants.FIRST_TIME_CAMEL_CASE,false)){
            appData.persistAlldata();
            editor.putBoolean(myconstants.FIRST_TIME_CAMEL_CASE,true);
            editor.commit();
        } else if (last<AppData.NEW_VERSION) {
            database.mainDao().deleteAllsystemItems(myconstants.SYSTEM_SMALL);
            appData.persistAlldata();
            editor.putInt(AppData.LAST_VERSION,AppData.NEW_VERSION);
            editor.commit();
        }
    }

    private void  addAllTitles(){
        titles = new ArrayList<>();
        titles.add(myconstants.BASIC_NEEDS_CAMEL_CASE);
        titles.add(myconstants.CLOTHING_CAMEL_CASE);
        titles.add(myconstants.PERSONAL_CARE_CAMEL_CASE);
        titles.add(myconstants.BABY_NEEDS_CAMEL_CASE);
        titles.add(myconstants.HEALTH_CAMEL_CASE);
        titles.add(myconstants.TECHNOLOGY_CAMEL_CASE);
        titles.add(myconstants.FOOD_CAMEL_CASE);
        titles.add(myconstants.BEACH_SUPPLIES_CAMEL_CASE);
        titles.add(myconstants.CAR_SUPPLIES_CAMEL_CASE);
        titles.add(myconstants.NEEDS_CAMEL_CASE);
        titles.add(myconstants.MY_LIST_CAMEL_CASE);
        titles.add(myconstants.MY_SELECTIONS_CAMEL_CASE);
    }

    private void addAllImages(){
        images = new ArrayList<>();
        images.add(R.drawable.p1);
        images.add(R.drawable.p2);
        images.add(R.drawable.p3);
        images.add(R.drawable.p4);
        images.add(R.drawable.p5);
        images.add(R.drawable.p6);
        images.add(R.drawable.p7);
        images.add(R.drawable.p8);
        images.add(R.drawable.p9);
        images.add(R.drawable.p10);
        images.add(R.drawable.p11);
        images.add(R.drawable.p12);
    }
}