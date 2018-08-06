package com.jinojino.happytogether.DB;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    // DBHelper 생성자로 관리할 DB 이름과 버전 정보를 받음
    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    // DB를 새로 생성할 때 호출되는 함수
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Room (id INTEGER PRIMARY KEY AUTOINCREMENT, roomId INTEGER, roomName TEXT, noise INTEGER, create_at INTEGER);");
        db.execSQL("Insert into Room Values(null, 1, '거실', 80, 1);");
        db.execSQL("Insert into Room Values(null, 1,'거실', 100, 2);");
        db.execSQL("Insert into Room Values(null, 1,'거실', 70, 3);");
        db.execSQL("Insert into Room Values(null, 1,'거실', 120, 4);");
        db.execSQL("Insert into Room Values(null, 1,'거실', 100, 5);");
        db.execSQL("Insert into Room Values(null, 1,'거실', 60, 6);");
        db.execSQL("Insert into Room Values(null, 1,'거실', 40, 7);");
        db.execSQL("Insert into Room Values(null, 1,'거실', 90, 8);");
        db.execSQL("Insert into Room Values(null, 2,'방1', 90, 1);");
        db.execSQL("Insert into Room Values(null, 2,'방1', 100, 2);");
        db.execSQL("Insert into Room Values(null, 2,'방1', 70, 3);");
        db.execSQL("Insert into Room Values(null, 2,'방1', 60, 4);");
        db.execSQL("Insert into Room Values(null, 2,'방1', 70, 5);");
        db.execSQL("Insert into Room Values(null, 2,'방1', 100, 6);");
        db.execSQL("Insert into Room Values(null, 2,'방1', 120, 7);");
        db.execSQL("Insert into Room Values(null, 2,'방1', 50, 8);");
        db.execSQL("Insert into Room Values(null, 2,'방1', 70, 9);");
        db.execSQL("Insert into Room Values(null, 2,'방1', 90, 10);");
        db.execSQL("Insert into Room Values(null, 3,'안방', 120, 7);");
        db.execSQL("Insert into Room Values(null, 3,'안방', 50, 8);");
        db.execSQL("Insert into Room Values(null, 3,'안방', 70, 9);");
        db.execSQL("Insert into Room Values(null, 3,'안방', 90, 10);");
    }

    // DB 업그레이드를 위해 버전이 변경될 때 호출되는 함수
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void delete() {
        SQLiteDatabase db = getWritableDatabase();
        // DB에 입력한 값으로 행 추가
        db.execSQL("DELETE FROM Room");
    }

    public void insert(String create_at, String roomName, int noise) {
        // 읽고 쓰기가 가능하게 DB 열기
        SQLiteDatabase db = getWritableDatabase();
        // DB에 입력한 값으로 행 추가
        db.execSQL("INSERT INTO Room VALUES(null, '" + roomName + "', " + noise + ", '" + create_at + "');");
    }

//    public void update(String item, int price) {
//        SQLiteDatabase db = getWritableDatabase();
//        // 입력한 항목과 일치하는 행의 가격 정보 수정
//        db.execSQL("UPDATE MONEYBOOK SET price=" + price + " WHERE item='" + item + "';");
//        db.close();
//    }

//    public void delete(String item) {
//        SQLiteDatabase db = getWritableDatabase();
//        // 입력한 항목과 일치하는 행 삭제
//        db.execSQL("DELETE FROM MONEYBOOK WHERE item='" + item + "';");
//        db.close();
//    }

    public ArrayList<Entry> getNoise(int roomId) {
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Entry> entries = new ArrayList<>();

        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT * FROM Room WHERE roomId=" + roomId +";", null);
        while (cursor.moveToNext()) {
            entries.add(new Entry(cursor.getInt(4), cursor.getInt(3)));
        }

        return entries;
    }

    public ArrayList<Entry> getAllNoise() {
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Entry> entries = new ArrayList<>();

        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT * FROM Room ", null);
        while (cursor.moveToNext()) {
            entries.add(new Entry(cursor.getInt(3), cursor.getInt(2)));
        }

        return entries;
    }
}

