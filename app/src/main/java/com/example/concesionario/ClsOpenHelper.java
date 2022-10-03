package com.example.concesionario;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class ClsOpenHelper extends SQLiteOpenHelper {

    public ClsOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("Create table TblVehiculo(placa text primary key, marca text not null,modelo text not null, valor integer not null, activo text not null default 'si')");

        db.execSQL("Create table TblFactura(codigo text primary key, fecha text not null, placa text not null, activo text not null default'si', constraint pk_factura foreign key (placa) references TblVehiculo(placa))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table Tblvehiculo");
        db.execSQL("drop table TblFactura");
        onCreate(db);
    }

    }
