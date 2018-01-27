package com.square.mail2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

  public static final String TABLE_MAIL = "mail";
  public static final String COLUMN_ID = "id";
  public static final String COLUMN_PARAM = "param";

  private static final String DATABASE_NAME = "mail.db";
  private static final int DATABASE_VERSION = 1;

  // Database creation sql statement
  private static final String DATABASE_CREATE = "create table "
	      + TABLE_MAIL + "(" + COLUMN_ID
	      + " integer primary key autoincrement, " + COLUMN_PARAM
	      + " text not null);";
  
  private static final String DATABASE_INIT = "insert into "
	      + TABLE_MAIL + "("+COLUMN_ID+","+COLUMN_PARAM+") VALUES(1,'protocolo.dominio.tld');";
  private static final String DATABASE_INIT2 = "insert into "
	      + TABLE_MAIL + "("+COLUMN_ID+","+COLUMN_PARAM+") VALUES(2,'nombre.appelido@dominio.tld');";
  private static final String DATABASE_INIT3 = "insert into "
	      + TABLE_MAIL + "("+COLUMN_ID+","+COLUMN_PARAM+") VALUES(3,'pass');";
  private static final String DATABASE_INIT4 = "insert into "
	      + TABLE_MAIL + "("+COLUMN_ID+","+COLUMN_PARAM+") VALUES(4,'true');";
  private static final String DATABASE_INIT5 = "insert into "
	      + TABLE_MAIL + "("+COLUMN_ID+","+COLUMN_PARAM+") VALUES(5,'false');";
  private static final String DATABASE_INIT6 = "insert into "
	      + TABLE_MAIL + "("+COLUMN_ID+","+COLUMN_PARAM+") VALUES(6,'http://webmail.dominio.tld');";

  public MySQLiteHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase database) {
    database.execSQL(DATABASE_CREATE);
    database.execSQL(DATABASE_INIT);
    database.execSQL(DATABASE_INIT2);
    database.execSQL(DATABASE_INIT3);
    database.execSQL(DATABASE_INIT4);
    database.execSQL(DATABASE_INIT5);
    database.execSQL(DATABASE_INIT6);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    Log.w(MySQLiteHelper.class.getName(),
        "Upgrading database from version " + oldVersion + " to "
            + newVersion + ", which will destroy all old data");
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_MAIL);
    onCreate(db);
  }

} 