package com.example.fridge;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DBhelper extends SQLiteOpenHelper {

    public static final int VERSION = 1;
    public DBhelper(@Nullable Context context) {
        super(context, "Food.db", null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE 'foodlist' ('id' INTEGER PRIMARY KEY AUTOINCREMENT,'quantity' INTEGER,'canCheckPrice' INTEGER,'foodName' TEXT,'foodEXP' TEXT,'foodType' TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public ArrayList<Food> getFoodArray(){
        ArrayList<Food> foodArrayList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        int id,quantity;
        String foodName,foodEXP,foodType,canCheckPrice;

        Cursor c = sqLiteDatabase.rawQuery("SELECT * FROM foodlist",null);
        while (c.moveToNext()){
            id = c.getInt(c.getColumnIndex("id"));
            quantity = c.getInt(c.getColumnIndex("quantity"));
            canCheckPrice = c.getString(c.getColumnIndex("canCheckPrice"));
            foodName = c.getString(c.getColumnIndex("foodName"));
            foodEXP = c.getString(c.getColumnIndex("foodEXP"));
            foodType = c.getString(c.getColumnIndex("foodType"));
            foodArrayList.add(new Food(id,quantity,canCheckPrice,foodName,foodEXP,foodType));
        }
        return foodArrayList;
    }
}
