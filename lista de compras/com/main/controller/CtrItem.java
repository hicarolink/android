package com.main.controller;

import java.util.ArrayList;

import com.main.database.DBConn;
import com.main.model.Item;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CtrItem {

	private static final String table = "item";
	private static final String[] fields = { "_id", "name", "price", "quantity" };

	public static void add(Item i, Context context) {
		DBConn conn = new DBConn(context);
		SQLiteDatabase db = conn.openDatabase();
		ContentValues values = new ContentValues();
		values.put(fields[0], i.getId());
		values.put(fields[1], i.getName());
		values.put(fields[2], i.getPrice());
		values.put(fields[3], i.getQuantity());
		db.insert(table, null, values);
		db.close();
	}

	public static Item getItem(Cursor cursor) {
		Item item = new Item();

		item.setId(cursor.getInt(0));
		item.setName(cursor.getString(1));
		item.setPrice(cursor.getString(2));
		item.setQuantity(cursor.getString(3));

		return item;
	}

	public static Item getItemById(Context context, int idItem) {
		Item item = null;

		DBConn conn = new DBConn(context);
		SQLiteDatabase db = conn.openDatabase();

		Cursor cursor = db
				.query(table, fields, "_id = ?",
						new String[] { String.valueOf(idItem) }, null, null,
						null, null);

		if (cursor != null) {
			cursor.moveToLast();
			if (cursor.getCount() > 0) {
				item = getItem(cursor);
			}
		}
		cursor.close();
		db.close();

		return item;
	}

	public static ArrayList<Item> getAllItem(Context context) {
		Item item = null;
		ArrayList<Item> list = new ArrayList<Item>();
		DBConn conn = new DBConn(context);
		SQLiteDatabase db = conn.openDatabase();

		Cursor cursor = db
				.query(table, fields, null,
						null, null, null,
						null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			item = new Item();
			item = getItem(cursor);
			list.add(item);
			cursor.moveToNext();
		}
		cursor.close();
		db.close();

		return list;
	}

	public static void cleanTable(Context context) {
		DBConn conn = new DBConn(context);
		SQLiteDatabase db = conn.openDatabase();
		db.delete(table, null, null);
		db.close();
	}

}
