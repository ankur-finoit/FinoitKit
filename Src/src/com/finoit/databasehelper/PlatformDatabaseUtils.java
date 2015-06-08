package com.finoit.databasehelper;

import android.content.ContentValues;
import android.database.sqlite.SQLiteOpenHelper;

public class PlatformDatabaseUtils {
	
  /*public static final int CONFLICT_ABORT = 2;
  public static final int CONFLICT_FAIL = 3;
  public static final int CONFLICT_IGNORE = 4;
  public static final int CONFLICT_NONE = 0;
  public static final int CONFLICT_REPLACE = 5;
  public static final int CONFLICT_ROLLBACK = 1;
  private static final String[] CONFLICT_VALUES;*/
  
  SQLiteOpenHelper mDatabase;

  /*static
  {
    String[] arrayOfString = new String[6];
    arrayOfString[0] = "";
    arrayOfString[1] = " OR ROLLBACK ";
    arrayOfString[2] = " OR ABORT ";
    arrayOfString[3] = " OR FAIL ";
    arrayOfString[4] = " OR IGNORE ";
    arrayOfString[5] = " OR REPLACE ";
    CONFLICT_VALUES = arrayOfString;
  }
  */

  public PlatformDatabaseUtils(SQLiteOpenHelper paramSQLiteOpenHelper)
  {
    this.mDatabase = paramSQLiteOpenHelper;
  }

  private long insert(String paramString1, String paramString2, ContentValues paramContentValues, int paramInt)
  {
    return this.mDatabase.getWritableDatabase().insertWithOnConflict(paramString1, paramString2, paramContentValues, paramInt);
  }

  private int update(String paramString1, ContentValues paramContentValues, String paramString2, String[] paramArrayOfString, int paramInt)
  {
    return this.mDatabase.getWritableDatabase().updateWithOnConflict(paramString1, paramContentValues, paramString2, paramArrayOfString, paramInt);
  }

  public void execSQL(String paramString)
  {
    this.mDatabase.getWritableDatabase().execSQL(paramString);
  }

  public long insertWithOnConflict(String paramString1, String paramString2, ContentValues paramContentValues, int paramInt)
  {
	  long l = insert(paramString1, paramString2, paramContentValues, paramInt);
	  return l;
  }

  public int update(String paramString1, ContentValues paramContentValues, String paramString2, String[] paramArrayOfString)
  {
    return this.mDatabase.getWritableDatabase().update(paramString1, paramContentValues, paramString2, paramArrayOfString);
  }

  public int updateWithOnConflict(String paramString1, ContentValues paramContentValues, String paramString2, String[] paramArrayOfString, int paramInt)
  {
    int i = update(paramString1, paramContentValues, paramString2, paramArrayOfString, paramInt);
    return i;
  }
  
}