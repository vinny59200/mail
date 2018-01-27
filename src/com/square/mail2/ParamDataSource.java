package com.square.mail2;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ParamDataSource {


  // Database fields
  private SQLiteDatabase database;
  private MySQLiteHelper dbHelper;
  private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
      MySQLiteHelper.COLUMN_PARAM };

  public ParamDataSource(Context context) {
    dbHelper = new MySQLiteHelper(context);
  }

  public void open() throws SQLException {
    database = dbHelper.getWritableDatabase();
  }

  public void close() {
    dbHelper.close();
  }

  public void updateParam(String[] params) {
	  int tmpId ;
	  int i=1;

	    Cursor cursor = database.query(MySQLiteHelper.TABLE_MAIL,
	        allColumns, null, null, null, null, null);

	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	    Param param = cursorToParam(cursor);
	    tmpId=(int) param.getId();
	    // make sure to close the cursor
	    
	    ContentValues values = new ContentValues();
	    values.put(MySQLiteHelper.COLUMN_PARAM, params[i-1]);
	    Log.e("vv", params[i-1]+" "+tmpId);
    	database.update(MySQLiteHelper.TABLE_MAIL, 
        values,"id="+tmpId,null);
    	cursor.moveToNext();
    	i=i+1;
    	}
    cursor.close();
    
  }

 

  public Param getParam(int id) {
	Param param = new Param();

    Cursor cursor = database.query(MySQLiteHelper.TABLE_MAIL,
        allColumns, MySQLiteHelper.COLUMN_ID + " = "+id, null, null, null, null);
    
    cursor.moveToNext();
    param = cursorToParam(cursor);
      
    // make sure to close the cursor
    cursor.close();
    return param;
  }

  private Param cursorToParam(Cursor cursor) {
    Param param = new Param();
    if(cursor.getString(1)!=null){

        param.setId(cursor.getLong(0));
        param.setParam(cursor.getString(1));

    }else{
    	ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_PARAM, "Fill the value");
        long insertId = database.insert(MySQLiteHelper.TABLE_MAIL, null,
            values);
        Cursor tmpCursor = database.query(MySQLiteHelper.TABLE_MAIL,
            allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
            null, null, null);
        cursor.moveToNext();
        param= cursorToParam(tmpCursor);
        cursor.close();
    }
    return param;
  }
} 
