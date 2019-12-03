package com.example.aravind.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import org.json.JSONObject;

import java.util.ArrayList;


public class DatabaseHelper extends SQLiteOpenHelper {
    private static String DB_NAME = "MairakDB.sqlite";
    private static int DB_VERSION = 1;
    private static DatabaseHelper sInstance;






    public static synchronized DatabaseHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }




    public DatabaseHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE IF NOT EXISTS ProductList (id INTEGER PRIMARY KEY ,product_name TEXT , price TEXT,description TEXT,special_price TEXT,discount_price TEXT,images TEXT)");
    //    db.execSQL("CREATE TABLE IF NOT EXISTS cartList (id INTEGER PRIMARY KEY ,product_id TEXT,product_desc TEXT , user_id TEXT,qty TEXT,price TEXT,lat TEXT,lon TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS AddressBook (id INTEGER PRIMARY KEY ,name TEXT , address1 TEXT,address2 TEXT,state TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS NotificationList (id INTEGER PRIMARY KEY ,title TEXT , message TEXT,read Text)");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS ProductList");
        db.execSQL("DROP TABLE IF EXISTS AddressBook");
        db.execSQL("DROP TABLE IF EXISTS NotificationList");
      //  db.execSQL("DROP TABLE IF EXISTS cartList");

    }



    public void addToProductList(String id,String prodName, String price,String desc,String sPrice,String dPrice,String img){
        SQLiteDatabase db = getWritableDatabase();

        String INSERT_QUERY = "INSERT INTO ProductList VALUES (?,?,?,?,?,?,?);";
        final SQLiteStatement statement = db.compileStatement(INSERT_QUERY);
        db.beginTransaction();
        try {
                statement.clearBindings();
                statement.bindString(1,id);
                statement.bindString(2, prodName);
                statement.bindString(3, price);
                statement.bindString(4, desc);
                statement.bindString(5, sPrice);
                statement.bindString(6, dPrice);
                statement.bindString(7, img);


            // rest of bindings
                statement.execute(); //or executeInsert() if id is needed
            db.setTransactionSuccessful();
        }  catch (Exception e){
            e.printStackTrace();

        } finally {
            db.endTransaction();
        }


    }

    public ArrayList getProd(){
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM ProductList ";
        ArrayList <JSONObject> hash = new ArrayList<>();
     //   System.out.println("Query = " + query);
        Cursor cursor = db.rawQuery(query, new String[] {});
        if (cursor.moveToFirst()) {
            do {
                try {
                    JSONObject json = new JSONObject();
                    json.put("id", cursor.getString(cursor.getColumnIndex("id")));
                    json.put("product_name", cursor.getString(cursor.getColumnIndex("product_name")));
                    json.put("price", cursor.getString(cursor.getColumnIndex("price")));
                    json.put("description", cursor.getString(cursor.getColumnIndex("description")));
                    json.put("special_price", cursor.getString(cursor.getColumnIndex("special_price")));
                    json.put("discount_price", cursor.getString(cursor.getColumnIndex("discount_price")));
                    json.put("images", "null");



                    hash.add(json);


                }catch(Exception e){
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return hash;
    }




    public void addToCartList(String id,String product_id,String product_desc, String user_id,String qty,String price,String lat,String lon){
        SQLiteDatabase db = getWritableDatabase();

        String INSERT_QUERY = "INSERT INTO ProductList VALUES (null,?,?,?,?,?,?);";
        final SQLiteStatement statement = db.compileStatement(INSERT_QUERY);
        db.beginTransaction();
        try {
            statement.clearBindings();
            statement.bindString(1, product_id);
            statement.bindString(2, product_desc);
            statement.bindString(3, user_id);
            statement.bindString(4, qty);
            statement.bindString(5, price);
            statement.bindString(6, lat);
            statement.bindString(7, lon);


            // rest of bindings
            statement.execute(); //or executeInsert() if id is needed
            db.setTransactionSuccessful();
        }  catch (Exception e){
            e.printStackTrace();

        } finally {
            db.endTransaction();
        }


    }


    public ArrayList getCart(){
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM cartList ";
        ArrayList <JSONObject> hash = new ArrayList<>();
   //     System.out.println("Query = " + query);
        Cursor cursor = db.rawQuery(query, new String[] {});
        if (cursor.moveToFirst()) {
            do {
                try {
                    JSONObject json = new JSONObject();
                    json.put("product_name", cursor.getString(cursor.getColumnIndex("product_name")));
                    json.put("price", cursor.getString(cursor.getColumnIndex("price")));
                    json.put("description", cursor.getString(cursor.getColumnIndex("description")));
                    json.put("special_price", cursor.getString(cursor.getColumnIndex("special_price")));
                    json.put("discount_price", cursor.getString(cursor.getColumnIndex("discount_price")));
                    json.put("images", "null");



                    hash.add(json);


                }catch(Exception e){
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return hash;
    }



  /*  public void deleteFromCart(){
        SQLiteDatabase db = getWritableDatabase();

        db.execSQL("DELETE FROM cartList");
    }
*/

    public void addToAddressBook(String name,String address1, String address2,String state){
        SQLiteDatabase db = getWritableDatabase();

        String INSERT_QUERY = "INSERT INTO AddressBook VALUES (null,?,?,?,?);";
        final SQLiteStatement statement = db.compileStatement(INSERT_QUERY);
        db.beginTransaction();
        try {
            statement.clearBindings();
            statement.bindString(1, name);
            statement.bindString(2, address1);
            statement.bindString(3, address2);
            statement.bindString(4, state);


            // rest of bindings
            statement.execute(); //or executeInsert() if id is needed
            db.setTransactionSuccessful();
        }  catch (Exception e){
            e.printStackTrace();

        } finally {
            db.endTransaction();
        }


    }

    public void updateNotification(String id, String status){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("read",status);

        db.update("NotificationList", contentValues, "id = ?",new String[] { id });





    }


    public void deleteNotification(){
        SQLiteDatabase db = getWritableDatabase();

        db.execSQL("DELETE FROM NotificationList");
    }





    public void addToNotificationList(String title,String Message ){
        SQLiteDatabase db = getWritableDatabase();

        String INSERT_QUERY = "INSERT INTO NotificationList VALUES (null,?,?,?);";
        final SQLiteStatement statement = db.compileStatement(INSERT_QUERY);
        db.beginTransaction();
        try {
            statement.clearBindings();
            statement.bindString(1, title);
            statement.bindString(2, Message);
            statement.bindString(3, "u");




            // rest of bindings
            statement.execute(); //or executeInsert() if id is needed
            db.setTransactionSuccessful();
        }  catch (Exception e){
            e.printStackTrace();

        } finally {
            db.endTransaction();
        }


    }


    public ArrayList getNotificationList(){
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM NotificationList ";
        ArrayList <JSONObject> hash = new ArrayList<>();
        //     System.out.println("Query = " + query);
        Cursor cursor = db.rawQuery(query, new String[] {});
        if (cursor.moveToFirst()) {
            do {
                try {
                    JSONObject json = new JSONObject();
                    json.put("id", cursor.getString(cursor.getColumnIndex("id")));
                    json.put("title", cursor.getString(cursor.getColumnIndex("title")));
                    json.put("message", cursor.getString(cursor.getColumnIndex("message")));
                    json.put("read", cursor.getString(cursor.getColumnIndex("read")));


                    hash.add(json);


                }catch(Exception e){
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return hash;
    }


    public long getAddressCOunt(){
        String countQuery = "SELECT  * FROM " + "AddressBook";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }


    public int getNotifiCount(){
        String countQuery = "select  * from NotificationList where read='u';";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }


    public void deleteFromNotificationList(String id){
        SQLiteDatabase db = getWritableDatabase();

        db.execSQL("DELETE FROM NotificationList WHERE id="+id);
    }

    public void deleteFromProductList(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM ProductList");
    }


    @Override
    public synchronized void close() {
        SQLiteDatabase db= getWritableDatabase();
        if (db != null)
            db.close();
        super.close();
    }





}
