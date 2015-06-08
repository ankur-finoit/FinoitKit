package com.finoit.databasehelper;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * String Selection Builder Common Class used for ContentProvider Purposes
 * @author Ankur Parashar
 *
 */
public class SelectionBuilder {
	
  private Map<String, String> mProjectionMap = new HashMap<String, String>();
  private StringBuilder mSelection = new StringBuilder();
  private ArrayList<String> mSelectionArgs = new ArrayList<String>();
  private String mTable = null;

  private void assertTable(){
	  
	  if (this.mTable != null)
     	 return;
	  throw new IllegalStateException("Table not specified");
  }

  private void mapColumns(String[] paramArrayOfString)
  {
	  for (int i = 0; i < paramArrayOfString.length; i++)
	  {
		  String str = (String)this.mProjectionMap.get(paramArrayOfString[i]);
      	if (str == null)
      		continue;
      	paramArrayOfString[i] = str;
	  }
  }

  public int delete(SQLiteDatabase paramSQLiteDatabase)
  {
    assertTable();
    return paramSQLiteDatabase.delete(this.mTable, getSelection(), getSelectionArgs());
  }

  public String getSelection()
  {
    return this.mSelection.toString();
  }

  public String[] getSelectionArgs()
  {
	  return (String[])this.mSelectionArgs.toArray(new String[this.mSelectionArgs.size()]);
  }

  public SelectionBuilder map(String paramString1, String paramString2)
  {
	  this.mProjectionMap.put(paramString1, paramString2 + " AS " + paramString1);
   	 return this;
  }

  public SelectionBuilder mapToTable(String paramString1, String paramString2)
  {
	  this.mProjectionMap.put(paramString1, paramString2 + "." + paramString1);
	  return this;
  }
  
  public Cursor query(boolean disticnt, SQLiteDatabase paramSQLiteDatabase, String[] paramArrayOfString, String paramString)
  {
	  return query(disticnt, paramSQLiteDatabase, paramArrayOfString, null, null, paramString, null);
  }

  public Cursor query(SQLiteDatabase paramSQLiteDatabase, String[] paramArrayOfString, String paramString)
  {
	  return query(false, paramSQLiteDatabase, paramArrayOfString, null, null, paramString, null);
  }

  public Cursor query(boolean distinct, SQLiteDatabase paramSQLiteDatabase, String[] paramArrayOfString, String paramString1, String paramString2, String paramString3, String paramString4)
  {
	  assertTable();
	  if (paramArrayOfString != null)
		  mapColumns(paramArrayOfString);
	  if(!distinct)
		  	return paramSQLiteDatabase.query(this.mTable, paramArrayOfString, getSelection(), getSelectionArgs(), paramString1, paramString2, paramString3, paramString4);
	  else
		  	return paramSQLiteDatabase.query( distinct, this.mTable, paramArrayOfString, getSelection(), getSelectionArgs(), paramString1, paramString2, paramString3, paramString4);
	    
  }

  public SelectionBuilder reset()
  {
	  this.mTable = null;
	  this.mSelection.setLength(0);
	  this.mSelectionArgs.clear();
	  return this;
  }

  public SelectionBuilder table(String paramString)
  {
    this.mTable = paramString;
    return this;
  }

  public String toString()
  {
    return "SelectionBuilder[table=" + this.mTable + ", selection=" + getSelection() + ", selectionArgs=" + Arrays.toString(getSelectionArgs()) + "]";
  }

  public int update(SQLiteDatabase paramSQLiteDatabase, ContentValues paramContentValues)
  {
	  assertTable();
	  return paramSQLiteDatabase.update(this.mTable, paramContentValues, getSelection(), getSelectionArgs());
  }

  public SelectionBuilder where(String paramString, String[] paramArrayOfString)
  {
	  if (!TextUtils.isEmpty(paramString))
	  {
		  if (this.mSelection.length() > 0)
			  this.mSelection.append(" AND ");
		  this.mSelection.append("(").append(paramString).append(")");
		  if (paramArrayOfString != null)
			  	this.mSelectionArgs.addAll(Arrays.asList(paramArrayOfString));
     }
     else{
    	if ((paramArrayOfString != null) && (paramArrayOfString.length > 0))
    		 throw new IllegalStateException("Valid selection required when including arguments");
     }
	  
     return this;
   
  }
  
}