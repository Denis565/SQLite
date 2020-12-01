package com.example.sqlite

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.lang.Exception

class DatabaseHelper (context:Context):SQLiteOpenHelper(context,"people.db",null,5) {

    val TABLE_NAME="people_table"
    val COL2="NAME"
    val COL3="EMAIL"
    val COL1="ID"

    private  val DROP_USER_TABLE="DROP TABLE IF EXISTS $TABLE_NAME"

    val db = this.writableDatabase
    val contextValues = ContentValues()


    //Создание БД
    override fun onCreate(db: SQLiteDatabase?) {
        val createTable="CREATE TABLE "+ TABLE_NAME+" (ID INTEGER PRIMARY KEY AUTOINCREMENT,"+
                " NAME TEXT, EMAIL TEXT)"
        db!!.execSQL(createTable)
    }

    //Обновление БД
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL(DROP_USER_TABLE)
        onCreate(db)
    }

    //Добавление поьзователя
    fun addData(name:String?, email:String?):Boolean{
        try {
            contextValues.put(COL2, name)
            contextValues.put(COL3, email)
            val result = db.insert(TABLE_NAME, null, contextValues)
            return result != -1L
        }catch (e:Exception){
            Log.e("ADD",e.message.toString())
        }
        return false
    }

    //Читать поля из БД
    fun showData():Cursor?{
        try {
            val db = this.writableDatabase
            return db.rawQuery("SELECT * FROM $TABLE_NAME",null)
        }catch (e:Exception){
            Log.d("EROR",e.message.toString())
        }
        return null
    }

    //Удаление записи
    fun remove(id: Int):Boolean{
        val result=db.delete(TABLE_NAME, "$COL1=$id", null);
        return result!=-1
    }

    //Обновление записи
    fun update(name: String?,email: String?,id:Int):Boolean{
        contextValues.put(COL2, name)
        contextValues.put(COL3, email)
        val result= db.update(TABLE_NAME,contextValues, "$COL1=$id",null)

        return result != -1
    }
}