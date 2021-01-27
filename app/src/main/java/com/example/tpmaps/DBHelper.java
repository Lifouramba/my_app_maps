package com.example.tpmaps;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {
        super(context, "Userdata.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase MyDB) {
        MyDB.execSQL("create Table Users(numphone TEXT primary key, adresse1 TEXT,adresse2 TEXT,adresse3 TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int i, int i1) {
        MyDB.execSQL("drop Table if exists Users");
    }



    public Boolean insertuserdata(String numphone, String adresse1, String adresse2, String adresse3){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("numphone", numphone);
        contentValues.put("adresse1", adresse1);
        contentValues.put("adresse2", adresse2);
        contentValues.put("adresse3", adresse3);

        long result= MyDB.insert("Users", null, contentValues);
        if (result==-1){
            return false;
        }else {
            return true;
        }
    }

    public Boolean updateuserdata(String numphone, String adresse1, String adresse2, String adresse3){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("adresse1", adresse1);
        contentValues.put("adresse2", adresse2);
        contentValues.put("adresse3", adresse3);

        Cursor cursor = MyDB.rawQuery("Select * from Users where numphone = ?", new String[] {numphone});
        if (cursor.getCount()>0){
            long result=MyDB.update("Users", contentValues, "numphone=?", new String[] {numphone});
            if (result==-1){
                return false;
            }else {
                return true;
            }

        }else{
            return false;
        }

    }

    public Boolean deletedata(String numphone){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from Users where numphone = ?", new String[] {numphone});
        if (cursor.getCount()>0){
            long result=MyDB.delete("users", "numphone=?", new String[] {numphone});
            if (result==-1){
                return false;
            }else {
                return true;
            }

        }else{
            return false;
        }

    }

    public Cursor getdata(){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from Users where numphone = ?", null);

        return cursor;
    }
}
