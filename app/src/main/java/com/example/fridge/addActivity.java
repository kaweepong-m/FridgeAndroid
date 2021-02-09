package com.example.fridge;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.Calendar;

public class addActivity extends AppCompatActivity {
    Button button1,button2,button3,button4;
    EditText editText1,editText2;
    String dueDateText,foodTypeText,foodPriceText,foodIDText;
    String[] foodType = {"เนื้อ","ผัก/ผลไม้","อาหารสำเร็จรูป","อื่นๆ"};
    String[] checkPriceList = {"สุกรชำแหละ เนื้อสัน สันใน","สุกรชำแหละ เนื้อสามชั้น","สุกรชำแหละ เนื้อสัน สันนอก","สุกรชำแหละ เนื้อแดง สะโพก","ไก่สดชำแหละ เนื้ออก (เนื้อล้วน)","ไก่สดชำแหละ ปีก ทั้งปีก (ปีกเต็ม)","ไก่สดชำแหละ น่อง สะโพก","ไข่เป็ด ใหญ่",
    "ไข่ไก่ เบอร์ 0","ผักกาดขาวปลี คัด","กะหล่ำปลี คัด","กะหล่ำดอก คัด","ผักชี คัด","ผักบุ้งไทย","ผักคะน้า คัด","พริกสดชี้ฟ้า","ผักกวางตุ้ง คัด","แตงกวา คัด","ถั่วฝักยาว คัด","มะเขือเทศสีดา คัด","หน่อไม้ฝรั่ง คัด"};
    String[] checkPriceID = {"P11001","P11005","P11002","P11003","P11012","P11014","P11013","P11020","P11025","P13010","P13012","P13014","P13034","P13042","P13002","P13037",
    "P13006","P13025","P13023","P13018","P13031"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        this.button1 = findViewById(R.id.button4);
        this.button2 = findViewById(R.id.button5);
        this.button3 = findViewById(R.id.button6);
        this.button4 = findViewById(R.id.button7);
        this.editText1 = findViewById(R.id.editTextTextPersonName);
        this.editText2 = findViewById(R.id.editTextTextPersonName2);

    }

    public void save(View view){
        if(editText2.length() != 0 && editText1.length() != 0 && dueDateText != null && foodPriceText != null && isNumeric(editText2.getText().toString())){
            DBhelper dBhelper = new DBhelper(this);
            SQLiteDatabase sqLiteDatabase = dBhelper.getWritableDatabase();
            ContentValues c = new ContentValues();
            c.put("quantity",editText2.getText().toString());
            if (foodPriceText==null){
                c.put("canCheckPrice","False");
                c.put("foodName",editText1.getText().toString());
            }
            else {
                c.put("canCheckPrice",foodIDText);
                c.put("foodName",foodPriceText);
            }
            c.put("foodEXP",dueDateText);
            c.put("foodType",foodTypeText);
            sqLiteDatabase.insert("foodlist",null,c);

            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        }
        else {
            Toast.makeText(this,  "กรุณาใส่ข้อมูลให้ถูกต้อง" , Toast.LENGTH_LONG).show();
        }
    }

    public void duedate(View view){
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(addActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                dueDateText = i2+"/"+(i1+1)+"/"+i;
                button1.setText(dueDateText);
            }
        },year,month,day);
        datePickerDialog.show();
    }

    public void typeSelect(View view){
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle("เลือกประเภทอาหาร");
        adb.setItems(foodType, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                foodTypeText = foodType[i];
                button2.setText(foodTypeText);
            }
        });
        adb.show();
    }

    public void cancheckprice(View view){
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle("เลือกประเภทอาหาร");
        adb.setItems(checkPriceList, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                foodIDText = checkPriceID[i];
                foodPriceText = checkPriceList[i];
                editText1.setEnabled(false);
                editText1.setText(foodPriceText);
            }
        });
        adb.show();
    }

    public static boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

}