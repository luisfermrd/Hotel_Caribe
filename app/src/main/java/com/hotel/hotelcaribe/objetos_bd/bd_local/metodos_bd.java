package com.hotel.hotelcaribe.objetos_bd.bd_local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class metodos_bd extends SQLiteOpenHelper {

    final String tabla ="CREATE TABLE servicios(id int, info text, n_personas text, nombre text, precio double, url text, uid text)";

    public metodos_bd(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(tabla);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS servicios");
        sqLiteDatabase.execSQL(tabla);
    }
}
