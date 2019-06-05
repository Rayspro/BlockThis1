package com.example.admin.blockthis1;

import android.content.ContentValues;
import android.content.Context;
import android.content.SyncStats;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

class myDbAdapter {
    private static final String normal ="0";
    private static final String boss ="1";
    private static final String friend ="2";
    private static final String family ="3";
    myDbHelper myhelper;
    public myDbAdapter(Context context)
    {
        myhelper = new myDbHelper(context);
    }

    public long insertData(String name,String phone_no, String profile,String count,
                           String time_limit,String call_limit, String first_call,String exceeded, String rejected)
    {
        SQLiteDatabase dbb = myhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        String newString = "";

        for(int i=0 ; i<phone_no.length();i++){
                    char c = phone_no.charAt(i);
                    if((int)c != 32) {
                        newString += phone_no.charAt(i);
                    }
                }

        String[] columns = {myDbHelper.UID,myDbHelper.Name,myDbHelper.PHONE_NO,
                myDbHelper.PROFILE_STATUS,myDbHelper.COUNT,myDbHelper.TIME_LIMIT,
                myDbHelper.EXCEEDED,myDbHelper.CALL_LIMIT,myDbHelper.FIRST_CALL,myDbHelper.REJECTED};
        Cursor cursor = dbb.query(myDbHelper.TABLE_NAME,columns,null,null,null,null,null);
        while (cursor.moveToNext())
        {
            String  phone = cursor.getString(cursor.getColumnIndex(myDbHelper.PHONE_NO));
            if(phone.equals(phone_no)){
                return 0;
            }
        }
        contentValues.put(myDbHelper.Name,name);
        contentValues.put(myDbHelper.PHONE_NO, newString);
        contentValues.put(myDbHelper.PROFILE_STATUS, profile);
        contentValues.put(myDbHelper.COUNT,count);
       contentValues.put(myDbHelper.TIME_LIMIT,time_limit);
       contentValues.put(myDbHelper.EXCEEDED,exceeded);
       contentValues.put(myDbHelper.CALL_LIMIT,call_limit);
       contentValues.put(myDbHelper.FIRST_CALL,first_call);
       contentValues.put(myDbHelper.REJECTED,rejected);
        long id = dbb.insert(myDbHelper.TABLE_NAME, null , contentValues);
        System.out.println("Inserted with ID"+String.valueOf(id));
        //cursor.close();
        return id;
    }

    public String blaclList()
    {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] columns = {myDbHelper.UID,myDbHelper.Name,myDbHelper.PHONE_NO,myDbHelper.PROFILE_STATUS};
        Cursor cursor = db.query(myDbHelper.TABLE_NAME,columns,null,null,null,null,null);
        StringBuffer BlackList= new StringBuffer();
        while (cursor.moveToNext())
        {
            String  phone_no =cursor.getString(cursor.getColumnIndex(myDbHelper.PHONE_NO));
            String type = cursor.getString(cursor.getColumnIndex(myDbHelper.PROFILE_STATUS));
            //BlackList.append(phone_no + "," + type + "\n");
            BlackList.append(phone_no + ",");
        }
        //cursor.close();
        return BlackList.toString();

    }
    public String getCount(String phone)
    {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] whereArgs ={phone};
        String[] columns = {myDbHelper.UID,myDbHelper.Name,myDbHelper.PHONE_NO,myDbHelper.PROFILE_STATUS,myDbHelper.COUNT};
        Cursor cursor = db.query(myDbHelper.TABLE_NAME,columns,null,null,null,null,null);
        StringBuffer countList= new StringBuffer();
        while (cursor.moveToNext())
        {
            if((cursor.getString(cursor.getColumnIndex(myDbHelper.PHONE_NO))).equals(phone)){
                String count = cursor.getString(cursor.getColumnIndex(myDbHelper.COUNT));
                countList.append(count);
                System.out.println("count  in 'getCount'"+count);
                return count;
            }
        }
        //cursor.close();
        return null;
    }
    public String getCallLimit(String phone)
    {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] whereArgs ={phone};
        String[] columns = {myDbHelper.PHONE_NO,myDbHelper.CALL_LIMIT};
        Cursor cursor = db.query(myDbHelper.TABLE_NAME,columns,null,null,null,null,null);
        StringBuffer strCallLimit = new StringBuffer();
        while (cursor.moveToNext())
        {
            if((cursor.getString(cursor.getColumnIndex(myDbHelper.PHONE_NO))).equals(phone)){
                String callLimit = cursor.getString(cursor.getColumnIndex(myDbHelper.CALL_LIMIT));
                strCallLimit.append(callLimit);
                return callLimit;
            }
        }
        //cursor.close();
        return strCallLimit.toString();
    }
    public String valueOfFirstCallForIncomingNumber(String phone){
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] whereArgs ={phone};
        String[] columns = {myDbHelper.PHONE_NO,myDbHelper.FIRST_CALL};
        Cursor cursor = db.query(myDbHelper.TABLE_NAME,columns,null,null,null,null,null);
        StringBuffer strFirstCall = new StringBuffer();
        while (cursor.moveToNext())
        {
            if((cursor.getString(cursor.getColumnIndex(myDbHelper.PHONE_NO))).equals(phone)){
                String firstCall = cursor.getString(cursor.getColumnIndex(myDbHelper.FIRST_CALL));
                strFirstCall.append(firstCall);
                System.out.println("first call in 'valueOfFirstCallForIncomingNumber'"+firstCall);
                return firstCall;
            }
        }
        //cursor.close();
        return strFirstCall.toString();
    }

    public String valueOfTimeLimitForIncomingNumber(String phone){
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] whereArgs ={phone};
        String[] columns = {myDbHelper.PHONE_NO,myDbHelper.TIME_LIMIT};
        Cursor cursor = db.query(myDbHelper.TABLE_NAME,columns,null,null,null,null,null);
        StringBuffer strTimeLimit = new StringBuffer();
        while (cursor.moveToNext())
        {
            if((cursor.getString(cursor.getColumnIndex(myDbHelper.PHONE_NO))).equals(phone)){
                String timeLimit = cursor.getString(cursor.getColumnIndex(myDbHelper.TIME_LIMIT));
                strTimeLimit.append(timeLimit);
                return timeLimit;
            }
        }
        //cursor.close();
        return strTimeLimit.toString();
    }
    public int update(String phone, String count){
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] whereArgs ={phone};
        ContentValues contentValues = new ContentValues();
        contentValues.put(myDbHelper.COUNT,count);
        int id =db.update(myDbHelper.TABLE_NAME,contentValues, myDbHelper.PHONE_NO+" = ?",whereArgs);
        System.out.println("Updated Count:"+count);
        return id;

    }
    public int checkExceededValue(String phone){
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] whereArgs ={phone};
        System.out.println("phone number in checkExceededValue() :"+phone);
        String[] columns = {myDbHelper.UID,myDbHelper.PHONE_NO,myDbHelper.EXCEEDED};
        Cursor cursor = db.query(myDbHelper.TABLE_NAME,columns,null,null,null,null,null);
        if(((cursor != null) && (cursor.getCount() > 0))){
            System.out.print("Cursor is null");
        }
        else{
            System.out.println("Cursor is not empty");
        }
        while (cursor.moveToNext())
        {
            if((cursor.getString(cursor.getColumnIndex(myDbHelper.PHONE_NO))).equals(phone)){
                String exceeded = cursor.getString(cursor.getColumnIndex(myDbHelper.EXCEEDED));
                return Integer.parseInt(exceeded);
            }
        }
        return 0;

    }

    public int updateForLimitExceeded(String phone){
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] whereArgs = {phone};
        ContentValues contentValues = new ContentValues();
        contentValues.put(myDbHelper.COUNT,"0");
        contentValues.put(myDbHelper.EXCEEDED,"0");
        contentValues.put(myDbHelper.FIRST_CALL,"0");
        int id = db.update(myDbHelper.TABLE_NAME,contentValues,myDbHelper.PHONE_NO+"=?",whereArgs);
        System.out.println("Updated Count,Exceeded, first call becoz of LIMIT EXCEEDEd WITH ID :"+id);
        return id;
    }
    public int updateToggleExceeded(String phone,String exceeded){
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] whereArgs = {phone};
        ContentValues contentValues = new ContentValues();
        contentValues.put(myDbHelper.EXCEEDED,exceeded);
        int id = db.update(myDbHelper.TABLE_NAME,contentValues,myDbHelper.PHONE_NO+" = ?",whereArgs);
        System.out.println("Excedded toggled to 1");
        return id;
    }
    public int updateWhenFirstCall(String phone, String first_call){
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] whereArgs ={phone};
        ContentValues contentValues = new ContentValues();
        contentValues.put(myDbHelper.FIRST_CALL,first_call);
        int id =db.update(myDbHelper.TABLE_NAME,contentValues, myDbHelper.PHONE_NO+" = ?",whereArgs);
        System.out.println("Updated FIRST_CALL:"+first_call);
        return id;

    }
    public int updateForFirstCallAfterExceed(String phone, String first_call){
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] whereArgs ={phone};
        ContentValues contentValues = new ContentValues();
        contentValues.put(myDbHelper.FIRST_CALL,first_call);
        int id =db.update(myDbHelper.TABLE_NAME,contentValues, myDbHelper.PHONE_NO+" = ?",whereArgs);
        System.out.println("Updated FIRST_CALL:"+contentValues.get(myDbHelper.FIRST_CALL));
        return id;

    }
    public int updateTimeByProfile(String profile,String timeLimit,String callLimit){
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] whereArgs ={profile};
        ContentValues contentValues = new ContentValues();
        contentValues.put(myDbHelper.TIME_LIMIT,timeLimit);
        contentValues.put(myDbHelper.CALL_LIMIT,callLimit);
        int id =db.update(myDbHelper.TABLE_NAME,contentValues, myDbHelper.PROFILE_STATUS+" = ?",whereArgs);
        System.out.println("Updated time limit:"+timeLimit +" ----->"+"call Limit"+callLimit);
        return id;

    }

    public String getData()
    {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] columns = {myDbHelper.UID,myDbHelper.Name,myDbHelper.PHONE_NO,myDbHelper.PROFILE_STATUS,
                myDbHelper.COUNT,myDbHelper.TIME_LIMIT,myDbHelper.EXCEEDED,myDbHelper.CALL_LIMIT,myDbHelper.FIRST_CALL};
        Cursor cursor = db.query(myDbHelper.TABLE_NAME,columns,null,null,null,null,null);
        StringBuffer buffer= new StringBuffer();
        while (cursor.moveToNext())
        {
            String name = cursor.getString(cursor.getColumnIndex(myDbHelper.Name));
            String  phone_no =cursor.getString(cursor.getColumnIndex(myDbHelper.PHONE_NO));
            String profile = cursor.getString(cursor.getColumnIndex(myDbHelper.PROFILE_STATUS));
            buffer.append(name + "," + phone_no + ","+profile+ "\n");
        }
        //cursor.close();
        return buffer.toString();
    }
    public String getRejectedCalls(){
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] columns = {myDbHelper.UID,myDbHelper.Name,myDbHelper.PHONE_NO,myDbHelper.PROFILE_STATUS,
                myDbHelper.COUNT,myDbHelper.TIME_LIMIT,myDbHelper.EXCEEDED,myDbHelper.CALL_LIMIT,myDbHelper.FIRST_CALL,myDbHelper.REJECTED};

        Cursor cursor = db.query(myDbHelper.TABLE_NAME,columns,null,null,null,null,null);

        StringBuffer buffer= new StringBuffer();
        while (cursor.moveToNext())
        {
            String name = cursor.getString(cursor.getColumnIndex(myDbHelper.Name));
            String  phone_no =cursor.getString(cursor.getColumnIndex(myDbHelper.PHONE_NO));
            String rejected = cursor.getString(cursor.getColumnIndex(myDbHelper.REJECTED));
            buffer.append(name + "," + phone_no + ","+rejected+"\n");
        }
        return buffer.toString();
    }

    public int updateRejectedCallValue(String phone,String rejected){
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] whereArgs ={phone};
        ContentValues contentValues = new ContentValues();
        contentValues.put(myDbHelper.REJECTED,rejected);
        int id =db.update(myDbHelper.TABLE_NAME,contentValues, myDbHelper.PHONE_NO+" = ?",whereArgs);
        return id;
    }
    public int getRejectedCount(String phone){
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] whereArgs ={phone};
        String[] columns = {myDbHelper.PHONE_NO,myDbHelper.REJECTED};
        Cursor cursor = db.query(myDbHelper.TABLE_NAME,columns,null,null,null,null,null);
        while (cursor.moveToNext())
        {
            if((cursor.getString(cursor.getColumnIndex(myDbHelper.PHONE_NO))).equals(phone)){
                String rejected = cursor.getString(cursor.getColumnIndex(myDbHelper.REJECTED));
               int rejectedCount = Integer.parseInt(rejected);
               return rejectedCount;
            }
        }
        return 0;
    }
    public String getDataIf(String type)
    {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] whereArgs ={type};
        String[] columns = {myDbHelper.UID,myDbHelper.Name,myDbHelper.PHONE_NO,myDbHelper.PROFILE_STATUS,myDbHelper.COUNT};
        Cursor cursor = db.query(myDbHelper.TABLE_NAME,columns,null,null,null,null,null);
        StringBuffer buffer= new StringBuffer();
        while (cursor.moveToNext())
        {
            if((cursor.getString(cursor.getColumnIndex(myDbHelper.PROFILE_STATUS))).equals(type)){
                String name = cursor.getString(cursor.getColumnIndex(myDbHelper.Name));
                String  phone_no =cursor.getString(cursor.getColumnIndex(myDbHelper.PHONE_NO));
                buffer.append(name + "," + phone_no + "\n");
            }
        }
        //cursor.close();
        return buffer.toString();
    }
    public int ifBossProfile(String phone){
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] whereArgs ={phone};
        String[] columns = {myDbHelper.UID,myDbHelper.Name,myDbHelper.PHONE_NO,myDbHelper.PROFILE_STATUS,myDbHelper.COUNT};
        Cursor cursor = db.query(myDbHelper.TABLE_NAME,columns,null,null,null,null,null);
        while (cursor.moveToNext())
        {
            if((cursor.getString(cursor.getColumnIndex(myDbHelper.PHONE_NO))).equals(phone)){
                String profile = cursor.getString(cursor.getColumnIndex(myDbHelper.PROFILE_STATUS));
                if(profile.equals("1")){
                    return 1;
                }
                else {
                    return 0;
                }
            }
        }
        return 0;
    }
    public  int delete(String phone_no)
    {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] whereArgs ={phone_no};

        int count =db.delete(myDbHelper.TABLE_NAME ,myDbHelper.PHONE_NO+" = ?",whereArgs);
        return  count;
    }

    static class myDbHelper extends SQLiteOpenHelper
    {
        private static final String DATABASE_NAME = "DB_BlockList";    // Database Name
        private static final String TABLE_NAME = "Table_BlackList";   // Table Name
        private static final int DATABASE_Version = 3;   // Database Version
        private static final String UID="_id";     // Column I (Primary Key)
        private static final String PHONE_NO = "phone_no";    //Column II
        private static final String Name = "name";    //Column III
        private static final String PROFILE_STATUS = "profile_status";
        private static final String COUNT = "count";  //column IV
        private static final String TIME_LIMIT = "time_limit"; //column V
        private static final String CALL_LIMIT = "call_limit"; //column VI
        private static final String FIRST_CALL = "first_call"; //column V
        private static final String EXCEEDED = "exceeded"; //column VI
        private static final String REJECTED = "rejected";
        private static final String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+
                " ("+UID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+ Name+" VARCHAR(225),"+ PHONE_NO+
                " VARCHAR(225),"+ PROFILE_STATUS+" VARCHAR(225),"+ COUNT+" VARCHAR(225),"+ TIME_LIMIT+
                " VARCHAR(225),"+ EXCEEDED+" VARCHAR(225),"+ CALL_LIMIT+ " VARCHAR(225),"+ FIRST_CALL+
                " VARCHAR(225),"+ REJECTED+" VARCHAR(225));";

        private static final String CREATE_TABLE_NEW = "CREATE TABLE "+TABLE_NAME+
                " ("+UID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+ Name+" VARCHAR(225),"+ PHONE_NO+
                " VARCHAR(225),"+PROFILE_STATUS+" VARCHAR(225),"+COUNT+" VARCHAR(225),"+TIME_LIMIT+
                " VARCHAR(225),"+CALL_LIMIT+ " VARCHAR(225),"+FIRST_CALL+" VARCHAR(225),"+ REJECTED+" VARCHAR(225));";

        private static final String DROP_TABLE ="DROP TABLE IF EXISTS "+TABLE_NAME;

        private Context context;

        public myDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_Version);
            this.context=context;
        }
        public void onCreate(SQLiteDatabase db) {

            try {
                db.execSQL(CREATE_TABLE);
                System.out.println("in DB onCreate()");
            } catch (Exception e) {
            }
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                System.out.println("NewVersion :"+newVersion);
                System.out.println("OldVersion :"+oldVersion);
                if(newVersion > oldVersion) {
                    db.execSQL(DROP_TABLE);
                    System.out.println("table dropped");
                }
                System.out.println("in DB onUpgrade()");
                onCreate(db);
            }catch (Exception e) {
                Toast.makeText(context,e.toString(),Toast.LENGTH_SHORT).show();}
        }
    }
}
