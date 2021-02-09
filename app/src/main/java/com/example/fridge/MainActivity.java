package com.example.fridge;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    TextView title2 ;
    ArrayList <Food> foodArrayList;
    RecyclerView rcc1;
    FridgeAdapter fridgeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.title2 = findViewById(R.id.title2);
        this.rcc1 = findViewById(R.id.rcc1);

        DBhelper dBhelper = new DBhelper(this);
        SQLiteDatabase sqLiteDatabase = dBhelper.getReadableDatabase();
        sqLiteDatabase.close();
        Date date = new Date();
        CharSequence charSequence = DateFormat.format("dd/MM/yyyy",date.getTime());
        title2.setText("Today : " + charSequence);

        foodArrayList = new ArrayList<>();
        foodArrayList = dBhelper.getFoodArray();


        fridgeAdapter = new FridgeAdapter(foodArrayList,this);

        rcc1.setAdapter(fridgeAdapter);
        rcc1.setLayoutManager(new LinearLayoutManager(this ));
    }
    public void clk (View view){
        Intent intent = new Intent(this,addActivity.class);
        startActivity(intent);
    }

    public void sort (View view){
        Collections.reverse(foodArrayList);
        fridgeAdapter.notifyDataSetChanged();
    }
}