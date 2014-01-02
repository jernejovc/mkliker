package com.jernejovc.mkliker.db;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import com.jernejovc.mkliker.net.Server;
import com.jernejovc.mkliker.util.ServerList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class KlikerDatabase extends SQLiteOpenHelper
{
	private final Context myContext;

	//The Android's default system path of your application database.
	
	private String DB_PATH;

	private static String DB_NAME = "mkliker_sqlite";
	private static int DB_VERSION = 1;
	private SQLiteDatabase m_database;

	/**
	 * Constructor
	 * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
	 * @param context
	 */
	public KlikerDatabase(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
		this.myContext = context;
		try {
			createDatabase();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	

	/**
	 * Creates a empty database on the system and rewrites it with your own database.
	 * */
	public void createDatabase() throws IOException{
		boolean dbExist = checkDatabase();
		if(dbExist){} else{
			//By calling this method and empty database will be created into the default system path
			//of your application so we are gonna be able to overwrite that database with our database.
			this.getReadableDatabase();
			try {
				copyDatabase();
			} catch (IOException e) {
				throw new Error("Error copying database");
			}
		}
	}

	/**
	 * Check if the database already exist to avoid re-copying the file each time you open the application.
	 * @return true if it exists, false if it doesn't
	 */
	private boolean checkDatabase(){
		SQLiteDatabase checkDB = null;
		try{
			String myPath = DB_PATH + DB_NAME;
			checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
		}catch(SQLiteException e){
			//database does't exist yet.
		}

		if(checkDB != null){
			checkDB.close();
		}

		return checkDB != null ? true : false;
	}

	/**
	 * Copies your database from your local assets-folder to the just created empty database in the
	 * system folder, from where it can be accessed and handled.
	 * This is done by transfering bytestream.
	 * */
	private void copyDatabase() throws IOException{
		//Open your local db as the input stream
		InputStream myInput = myContext.getAssets().open(DB_NAME);

		// Path to the just created empty db
		String outFileName = DB_PATH + DB_NAME;
		File out  = new File(outFileName);
		if(out.exists())
			out.delete();
		
		//Open the empty db as the output stream
		OutputStream myOutput = new FileOutputStream(outFileName);

		//transfer bytes from the inputfile to the outputfile
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer))>0){
			myOutput.write(buffer, 0, length);
		}

		//Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();

	}

	public void openDatabase() throws SQLException{
		//Open the database
		String myPath = DB_PATH + DB_NAME;
		try {
			copyDatabase();
			checkDatabase();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		m_database = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
	}

	@Override
	public synchronized void close() {
		if(m_database != null)
			m_database.close();
		super.close();
	}

	  @Override
	  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	    Log.w(KlikerDatabase.class.getName(),
	        "Upgrading database from version " + oldVersion + " to "
	            + newVersion + ", which will destroy all old data");
	    //TODO
	  }

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		
	}
	
	public void addServer(Server s) {
		ContentValues values = new ContentValues();
		values.put("name", s.getName());
		values.put("url", s.getUrl().toString());
		values.put("isdefault", s.getIsDefault());
		long ret = m_database.insert("servers", null, values);
		if(ret == -1) {
			Log.d("com.jernejovc.mKliker", "Cannot add server to database!");
		} else {
		
			// Delete default flag on old default server
			if(s.getIsDefault()) {
				values = new ContentValues();
				values.put("isdefault", 0);
				m_database.update("servers", values, "isdefault = 1 and _id != ?" , new String [] {String.valueOf(ret)});
			}
		}
	}
	
	public ArrayList<Server> getServerList() {
		ServerList list = new ServerList();
		Cursor cursor = m_database.rawQuery("Select _id, name, url, isdefault from servers", null);
		
		while(cursor.moveToNext()) {
//			int id = cursor.getInt(0);
			String name = cursor.getString(1);
			String url = cursor.getString(2);
			boolean def = cursor.getInt(3) == 1;
			
			Server s = new Server(name, url, "", def);
			list.add(s);
		}
		
		return list.serverList();
	}
	
	public void updateServer(Server server) {
		//TODO
	}
	
	public void removeServer(Server server) {
		//TODO
	}
}