package com.tahlilgargroup.androidchatlibrary;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.microsoft.appcenter.analytics.Analytics;
import com.tahlilgargroup.commonlibrary.CommonClass;

import java.util.ArrayList;
import java.util.List;

import static com.tahlilgargroup.commonlibrary.CommonClass.DeviceProperty;


public class DB_Creator extends SQLiteOpenHelper {

    private final static String DB_NAME = "ChatBD";//CommonClass.DB_NAME;//"DriverMessage";
    private final static int DB_VERSION = 1;


    public static final String tbl_Message = "tbl_Message";


    public static String MsgID = "MsgID";
    public static String MsgType = "MsgType";
    public static String MsgPos = "MsgPos";
    public static String MsgContent = "MsgContent";
    public static String LocalPath = "LocalPath";
    public static String HostPath = "HostPath";
    public static String DriverID = "DriverID";
    public static String OperatorID = "OperatorID";
    public static String MsgDate = "MsgDate";
    public static String MsgTime = "MsgTime";
    public static String isSend = "isSend";
    public static String IsSeen = "IsSeen";
    public static String Image = "Image";
    public static String ID = "ID";
    public static String IsEdited="IsEdited";




    public DB_Creator(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override //
    public void onCreate(SQLiteDatabase db) {
        CreateTables(db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        try{

            //اگر ورژن جدید نصب شد کل جدول ها پاک شن و از اول زده بشن

            // query to obtain the names of all tables in your database
            @SuppressLint("Recycle") Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
            List<String> tables = new ArrayList<>();

            // iterate over the result set, adding every table name to a list
            while (c.moveToNext()) {
                tables.add(c.getString(0));
            }

            // call DROP TABLE on every table name
            for (String table : tables) {
                if (table.contains("tbl_")) {
                    String dropQuery = "DROP TABLE IF EXISTS " + table;
                    db.execSQL(dropQuery);
                }
            }


            CreateTables(db);
        } catch (Exception e) {
            Analytics.trackEvent("onUpgrade "  + "_" + CommonClass.GetCurrentMDate() + "_"+DeviceProperty+"_"  + e.getMessage());

        }


    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);

        try {

            // query to obtain the names of all tables in your database
            @SuppressLint("Recycle") Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
            List<String> tables = new ArrayList<>();

            // iterate over the result set, adding every table name to a list
            while (c.moveToNext()) {
                tables.add(c.getString(0));
            }

            // call DROP TABLE on every table name
            for (String table : tables) {
                if (table.contains("tbl_")) {
                    String dropQuery = "DROP TABLE IF EXISTS " + table;
                    db.execSQL(dropQuery);
                }
            }


            CreateTables(db);
        }catch (Exception e) {
            Analytics.trackEvent("onDowngrade "  + "_" + CommonClass.GetCurrentMDate() + "_"+DeviceProperty+"_"  + e.getMessage());

        }


    }


    private void CreateTables(SQLiteDatabase db)
    {
        try {

            //ایجاد جدول ها

            db.execSQL("CREATE TABLE IF NOT EXISTS " + tbl_Message + " (" +
                    " ID  INTEGER PRIMARY KEY AUTOINCREMENT ," +
                    " MsgID   TEXT , " +
                    " MsgType   INTEGER, " +
                    " MsgPos   INTEGER, " +
                    " MsgContent   TEXT, " +
                    " LocalPath TEXT, " +
                    " HostPath TEXT, " +
                    " DriverID TEXT, " +
                    " OperatorID TEXT, " +
                    " MsgDate TEXT," +
                    " MsgTime TEXT," +
                    " isSend INTEGER," +
                    "IsSeen INTEGER," +
                    "IsEdited INTEGER," +
                    " Image BLOB);");


        }catch (Exception e) {
            Analytics.trackEvent("CreateTables "  + "_" + CommonClass.GetCurrentMDate() + "_"+DeviceProperty+"_"  + e.getMessage());

        }
    }

    /*   public static void DropTable(SQLiteDatabase db,String TableName)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TableName);
    }*/
}
