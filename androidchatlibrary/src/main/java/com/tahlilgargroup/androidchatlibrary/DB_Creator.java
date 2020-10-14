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

    private final static String DB_NAME = CommonClass.DB_NAME;//"DriverMessage";
    private final static int DB_VERSION = 1;

    public static final String tbl_User = "tbl_User";
    public static final String tbl_City = "tbl_City";
    public static final String tbl_Ostan = "tbl_Ostan";
    public static final String tbl_Location = "tbl_Location";
    public static final String tbl_SurveyTitle = "tbl_SurveyTitle";
    public static final String tbl_SystemSetting="tbl_SystemSetting";
    public static final String tbl_StopReason = "tbl_StopReason";
    public static final String tbl_UnDeliverReason = "tbl_UnDeliverReason";
    public static final String tbl_Message = "tbl_Message";
    public static final String tbl_Customers = "tbl_Customers";
    public static final String tbl_LastOpration = "tbl_LastOpration";
    public static final String tbl_CostTitle = "tbl_CostTitle";


    public static String CostCode = "Code";
    public static String CostOnvan = "Onvan";


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


    public static String PersonCode = "PersonCode";
    public static String KalaCode = "KalaCode";
    public static String KalaOnvan="KalaOnvan";
    public static String Number="Number";
    public static String FirstNumber="FirstNumber";
    public static String KalaFie="Fie";
    public static String SelectedNumber="SelectedNumber";



    public static String DistributionID="ID";
    public static String VizitorID="VizitorID";
    public static String CustomerID="CustomerID";
    public static String CustomerAddrID="CustomerAddrID";
    public static String SabtDate="SabtDate";
    public static String SabtType="SabtType";
    public static String DistributionDate="DistributionDate";
    public static String DistributionTime="DistributionTime";
    public static String C_Sharh="Sharh";
    public static String CauseUnDistributionCode="CauseUnDistributionCode";
    public static String HavaleID="HavaleID";
    public static String IsCancel="IsCancel";
    public static String WhyCancel="WhyCancel";
    public static String CancelDate="CancelDate";
    public static String Priority="Priority";
    public static String MasterID="MasterID";
    public static String NumOfStar="NumOfStar";
    public static String ArriveDate="ArriveDate";
    public static String ArriveStatus="ArriveStatus";
    public static String DoReturnDate="DoReturnDate";
    public static String DoReturnStatus="DoReturnStatus";
    public static String EndDistributionDate="EndDistributionDate";
    public static String EndDistributionStatus="EndDistributionStatus";
    public static String TasviehArriveDate="TasviehArriveDate";
    public static String TasviehArriveStatus="TasviehArriveStatus";
    public static String EndTasviehDate="EndTasviehDate";
    public static String EndTasviehStatus="EndTasviehStatus";
    public static String C_Code="Code";
    public static String C_Name="Name";
    public static String C_Family="Family";
    public static String C_NameFamily="NameFamily";
    public static String SaghfEtebar="SaghfEtebar";
    public static String SaghfEtebarForFactor="SaghfEtebarForFactor";
    public static String IsInListSiah="IsInListSiah";
    public static String PersonMandeHesab="PersonMandeHesab";
    public static String C_Address="Address";
    public static String C_Tel1="Tel1";
    public static String C_Tel2="Tel2";
    public static String C_Tel3="Tel3";
    public static String C_Mobile="Mobile";
    public static String C_Fax="Fax";
    public static String C_OstanCode="OstanCode";
    public static String C_OstanName="OstanName";
    public static String C_CityID="CityID";
    public static String C_CityName="CityName";
    public static String C_Area="Area";
    public static String C_Sector="Sector";
    public static String C_MainStreet="MainStreet";
    public static String C_SecondStreet="SecondStreet";
    public static String C_Alley="Alley";
    public static String C_Pelak="Pelak";
    public static String C_VahedNum="VahedNum";
    public static String C_PostCode="PostCode";
    public static String C_Lat="Lat";
    public static String C_Lng="Lng";
    public static String SumDeliveryTV1="SumDeliveryTV1";
    public static String SumDeliveryTV2Ex="SumDeliveryTV2Ex";
    public static String SumReturnTV1="SumReturnTV1";
    public static String SumReturnTV2Ex="SumReturnTV2Ex";
    public static String SumMandeTV1="SumMandeTV1";
    public static String SumMandeTV2Ex="SumMandeTV2Ex";
    public static String SumMabKol="SumMabKol";






    public static String Code = "Code";
    public static String VisitorID="ID";
    public static String Name = "Name";
    public static String Family = "Family";
    public static String NameFamily = "NameFamily";
    public static String Tel = "Tel";
    public static String Mobile = "Mobile";
    public static String Address = "Address";
    public static String UserName = "eShop_User";
    public static String Password = "eShop_Pass";
    public static String eShop_IsActive = "eShop_IsActive";
    public static String BeLogin = "BeLogin";
    public static String LoginDate="LoginDate";


    public static String StopReasonCode="Code";
    public static String StopReasonOnvan="Onvan";
    public static String StopReasonType="Type";



    public static String CityCode = "Code";
    public static String CityOnvan = "Onvan";
    public static String OCode = "OCode";
    public static String City_ID="ID";

    public static String OstanCode="Code";
    public static String OstanOnvan="Onvan";


    public static String SurveyTitleCode="Code";
    public static String SurveyTitleOnvan="Onvan";
    public static String SurveyTitlePType="PType";
    public static String SurveyTitleIsActive="IsActive";


    public static String KCode="KCode";
    public static String SystemType="SystemType";
    public static String FormName="FormName";
    public static String ObjectName="ObjectName";
    public static String ObjectType="ObjectType";
    public static String SettingType="SettingType";
    public static String Value="Value";
    public static String DepCode="DepCode";


    public static String UnDeliverCode="Code";
    public static String UnDeliverOnvan="Onvan";

    public static String lat = "lat";
    public static String lng = "lng";
    public static String LocDate="Date";





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

            db.execSQL("CREATE TABLE IF NOT EXISTS " + tbl_User + " (" +
                    " Code   TEXT  , " +
                    " ID   INTEGER  , " +
                    " Name   TEXT  , " +
                    " Family   TEXT  , " +
                    " NameFamily   TEXT  , " +
                    " Tel   TEXT  , " +
                    " Mobile   TEXT  , " +
                    " Address   TEXT  , " +
                    " eShop_User   TEXT  , " +
                    " eShop_Pass   TEXT  , " +
                    " eShop_IsActive   INTEGER  , " +
                    " LoginDate   TEXT  , " +
                    " BeLogin   INTEGER);");


            db.execSQL("CREATE TABLE IF NOT EXISTS " + tbl_LastOpration + " (" +
                    " ID   INTEGER  , " +
                    " VizitorID   INTEGER  , " +
                    " CustomerID   INTEGER  , " +
                    " CustomerAddrID   INTEGER  , " +
                    " SabtDate   TEXT  , " +
                    " SabtType   INTEGER  , " +
                    " DistributionDate   TEXT  , " +
                    " DistributionTime   TEXT  , " +
                    " Sharh   TEXT  , " +
                    " CauseUnDistributionCode   INTEGER  , " +
                    " HavaleID   INTEGER  , " +
                    " IsCancel   INTEGER  , " +
                    " WhyCancel   TEXT  , " +
                    " CancelDate   TEXT  , " +
                    " Priority   INTEGER  , " +
                    " MasterID   INTEGER  , " +
                    " NumOfStar   REAL  , " +
                    " ArriveDate   TEXT  , " +
                    " ArriveStatus  INTEGER  , " +
                    " DoReturnDate   TEXT  , " +
                    " DoReturnStatus   INTEGER  , " +
                    " EndDistributionDate   TEXT  , " +
                    " EndDistributionStatus   INTEGER  , " +
                    " TasviehArriveDate   TEXT  , " +
                    " TasviehArriveStatus   INTEGER  , " +
                    " EndTasviehDate   TEXT  , " +
                    " EndTasviehStatus   INTEGER  , " +
                    " Code   TEXT  , " +
                    " Name   TEXT  , " +
                    " Family   TEXT  , " +
                    " NameFamily   TEXT  , " +
                    " SaghfEtebar   REAL  , " +
                    " SaghfEtebarForFactor   REAL  , " +
                    " IsInListSiah   INTEGER  , " +
                    " PersonMandeHesab   REAL  , " +
                    " Address   TEXT  , " +
                    " Tel1   TEXT  , " +
                    " Tel2   TEXT  , " +
                    " Tel3   TEXT  , " +
                    " Mobile   TEXT  , " +
                    " Fax   TEXT  , " +
                    " OstanCode   INTEGER  , " +
                    " OstanName   TEXT  , " +
                    " CityID   INTEGER  , " +
                    " CityName   TEXT  , " +
                    " Area   TEXT  , " +
                    " Sector   TEXT  , " +
                    " MainStreet   TEXT  , " +
                    " SecondStreet   TEXT  , " +
                    " Alley   TEXT  , " +
                    " Pelak   TEXT  , " +
                    " VahedNum   INTEGER  , " +
                    " PostCode   TEXT  , " +
                    " Lat   REAL  , " +
                    " Lng   REAL  , " +
                    " SumDeliveryTV1   REAL  , " +
                    " SumDeliveryTV2Ex   REAL  , " +
                    " SumReturnTV1   REAL  , " +
                    " SumReturnTV2Ex   REAL  , " +
                    " SumMandeTV1   REAL  , " +
                    " SumMandeTV2Ex   REAL  , " +

                    " PersonCode   TEXT  , " +
                    " KalaCode   TEXT  , " +
                    " KalaOnvan   TEXT  , " +
                    " Number   REAL  , " +
                    " FirstNumber   REAL  , " +
                    " Fie   REAL  , " +
                    " SelectedNumber   REAL  , " +

                    " SumMabKol   REAL);");


            db.execSQL("CREATE TABLE IF NOT EXISTS " + tbl_Customers + " (" +
                    " ID   INTEGER  , " +
                    " VizitorID   INTEGER  , " +
                    " CustomerID   INTEGER  , " +
                    " CustomerAddrID   INTEGER  , " +
                    " SabtDate   TEXT  , " +
                    " SabtType   INTEGER  , " +
                    " DistributionDate   TEXT  , " +
                    " DistributionTime   TEXT  , " +
                    " Sharh   TEXT  , " +
                    " CauseUnDistributionCode   INTEGER  , " +
                    " HavaleID   INTEGER  , " +
                    " IsCancel   INTEGER  , " +
                    " WhyCancel   TEXT  , " +
                    " CancelDate   TEXT  , " +
                    " Priority   INTEGER  , " +
                    " MasterID   INTEGER  , " +
                    " NumOfStar   REAL  , " +
                    " ArriveDate   TEXT  , " +
                    " ArriveStatus  INTEGER  , " +
                    " DoReturnDate   TEXT  , " +
                    " DoReturnStatus   INTEGER  , " +
                    " EndDistributionDate   TEXT  , " +
                    " EndDistributionStatus   INTEGER  , " +
                    " TasviehArriveDate   TEXT  , " +
                    " TasviehArriveStatus   INTEGER  , " +
                    " EndTasviehDate   TEXT  , " +
                    " EndTasviehStatus   INTEGER  , " +
                    " Code   TEXT  , " +
                    " Name   TEXT  , " +
                    " Family   TEXT  , " +
                    " NameFamily   TEXT  , " +
                    " SaghfEtebar   REAL  , " +
                    " SaghfEtebarForFactor   REAL  , " +
                    " IsInListSiah   INTEGER  , " +
                    " PersonMandeHesab   REAL  , " +
                    " Address   TEXT  , " +
                    " Tel1   TEXT  , " +
                    " Tel2   TEXT  , " +
                    " Tel3   TEXT  , " +
                    " Mobile   TEXT  , " +
                    " Fax   TEXT  , " +
                    " OstanCode   INTEGER  , " +
                    " OstanName   TEXT  , " +
                    " CityID   INTEGER  , " +
                    " CityName   TEXT  , " +
                    " Area   TEXT  , " +
                    " Sector   TEXT  , " +
                    " MainStreet   TEXT  , " +
                    " SecondStreet   TEXT  , " +
                    " Alley   TEXT  , " +
                    " Pelak   TEXT  , " +
                    " VahedNum   INTEGER  , " +
                    " PostCode   TEXT  , " +
                    " Lat   REAL  , " +
                    " Lng   REAL  , " +
                    " SumDeliveryTV1   REAL  , " +
                    " SumDeliveryTV2Ex   REAL  , " +
                    " SumReturnTV1   REAL  , " +
                    " SumReturnTV2Ex   REAL  , " +
                    " SumMandeTV1   REAL  , " +
                    " SumMandeTV2Ex   REAL  , " +
                    " SumMabKol   REAL);");

            db.execSQL("CREATE TABLE IF NOT EXISTS " + tbl_Location + " (" +
                    " Date TEXT , "+
                    " lat   REAL  , " +
                    " lng   REAL);");


            db.execSQL("CREATE TABLE IF NOT EXISTS " + tbl_CostTitle + " (" +
                    " Code TEXT , "+
                    " Onvan   TEXT);");

            db.execSQL("CREATE TABLE IF NOT EXISTS " + tbl_City + " (" +
                    " Code  TEXT ," +
                    " Onvan   TEXT , " +
                    " ID   INTEGER , " +
                    " OCode INTEGER);");

            db.execSQL("CREATE TABLE IF NOT EXISTS " + tbl_Ostan + " (" +
                    " Code  INTEGER ," +
                    " Onvan TEXT);");


            db.execSQL("CREATE TABLE IF NOT EXISTS " + tbl_SurveyTitle + " (" +
                    " Code   INTEGER  , " +
                    " Onvan   TEXT  , " +
                    " PType   INTEGER  , " +
                    " IsActive   INTEGER);");

            db.execSQL("CREATE TABLE IF NOT EXISTS " + tbl_SystemSetting + " (" +
                    " KCode   INTEGER  , " +
                    " SystemType   INTEGER  , " +
                    " FormName   TEXT  , " +
                    " ObjectName   TEXT  , " +
                    " ObjectType   TEXT  , " +
                    " SettingType   INTEGER  , " +
                    " Value   TEXT  , " +
                    " DepCode   INTEGER);");


            db.execSQL("CREATE TABLE IF NOT EXISTS " + tbl_StopReason + " (" +
                    " Code  INTEGER ," +
                    " Onvan   TEXT , " +
                    " Type INTEGER);");

            db.execSQL("CREATE TABLE IF NOT EXISTS " + tbl_UnDeliverReason + " (" +
                    " Code  INTEGER ," +
                    " Onvan TEXT);");
        }catch (Exception e) {
            Analytics.trackEvent("CreateTables "  + "_" + CommonClass.GetCurrentMDate() + "_"+DeviceProperty+"_"  + e.getMessage());

        }
    }

    /*   public static void DropTable(SQLiteDatabase db,String TableName)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TableName);
    }*/
}
