package com.main.database;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.main.utils.Configuration;
import com.main.listadecompras.R;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class DBConn extends SQLiteOpenHelper {

	private static String DB_PATH;
	private static String DB_NAME = "listadecompra";
	private SQLiteDatabase myDataBase;
	private final Context context;

	public DBConn(Context context) {
		/*
		 * O primeiro argumento é o contexto da aplicacao O segundo argumento é
		 * o nome do banco de dados O terceiro é um ponteiro para manipulação de
		 * dados, não precisaremos dele. O quarto é a versão do banco de dados
		 */
		super(context, DB_NAME, null, 1);
		this.context = context;
		DB_PATH = context.getDatabasePath(DB_NAME).getPath();
		System.out.println("Banco criado");
	}

	public SQLiteDatabase openDatabase() {
		File dbFile = context.getDatabasePath(DB_NAME);
		if (Configuration.updateDatabase || !dbFile.exists()) {
			try {
				createDataBase();
				copyDataBase(dbFile);
			} catch (IOException e) {
				throw new RuntimeException(
						context.getString(R.string.Erro_createDatabase), e);
			}
		}

		return SQLiteDatabase.openDatabase(dbFile.getPath(), null,
				SQLiteDatabase.OPEN_READWRITE);
	}

	public void createDataBase() throws IOException {
		boolean dbExist = checkDataBase();
		File dbFile = context.getDatabasePath(DB_NAME);
		if (Configuration.updateDatabase || !dbExist) {
			// created into
			// the default system path
			// of your application so we are gonna be able to overwrite that
			this.getReadableDatabase();

			try {
				copyDataBase(dbFile);
			} catch (IOException e) {
				e.printStackTrace();
				throw new Error(context.getString(R.string.Erro_copyDatabase));
			}
			this.close();
		}

	}

	private boolean checkDataBase() {
		SQLiteDatabase checkDB = null;
		try {
			String myPath = DB_PATH;
			checkDB = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READONLY);
			checkDB.close();
		} catch (SQLiteException e) {
		}
		return checkDB != null ? true : false;
	}

	private void copyDataBase(File dbFile) throws IOException {
		InputStream is = context.getAssets().open(DB_NAME);
		OutputStream os = new FileOutputStream(dbFile);

		byte[] buffer = new byte[1024];
		while (is.read(buffer) > 0) {
			os.write(buffer);
		}
		os.flush();
		os.close();
		is.close();
	}

	public void openDataBase() throws SQLException {
		String myPath = DB_PATH;
		myDataBase = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READWRITE);
	}

	@Override
	public synchronized void close() {
		if (myDataBase != null)
			myDataBase.close();

		super.close();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
