package com.mobileapllication.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBManager extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "books_db";

    private static DBManager instance;

    private DBManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized DBManager getInstance(Context context) {
        if (instance == null) {
            instance = new DBManager(context);
        }
        return instance;
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create tables
        db.execSQL(WishList.CREATE_TABLE);
        db.execSQL(Book.CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //do nothing
    }


    public void insertWishList(WishList wishList) {
        ContentValues values = new ContentValues();
        values.put(WishList.COLUMN_NAME, wishList.getName());
        values.put(WishList.COLUMN_IMAGE, wishList.getImage());
        values.put(WishList.COLUMN_USER_ID, wishList.getUserId());

        SQLiteDatabase db = this.getWritableDatabase();
        // insert row
        db.insert(WishList.TABLE_NAME, null, values);
    }


    public List<WishList> getWishListsByUser(String userId) {
        List<WishList> wishLists = new ArrayList<>();

        // Select by user Query
        String selectQuery = "SELECT  * FROM " + WishList.TABLE_NAME
                + " WHERE " + WishList.COLUMN_USER_ID + " = ? "
                + " ORDER BY " + WishList.COLUMN_TIMESTAMP + " DESC";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, new String[]{userId});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                WishList wishList = new WishList();
                wishList.setId(cursor.getInt(cursor.getColumnIndex(WishList.COLUMN_ID)));
                wishList.setName(cursor.getString(cursor.getColumnIndex(WishList.COLUMN_NAME)));
                wishList.setImage(cursor.getInt(cursor.getColumnIndex(WishList.COLUMN_IMAGE)));
                wishList.setUserId(cursor.getString(cursor.getColumnIndex(WishList.COLUMN_USER_ID)));
                wishList.setTimestamp(cursor.getString(cursor.getColumnIndex(WishList.COLUMN_TIMESTAMP)));

                wishLists.add(wishList);
            } while (cursor.moveToNext());
        }

        // return rows
        return wishLists;
    }
    public WishList getWishListById(int wishListId) {

        // Select by id Query
        String selectQuery = "SELECT  * FROM " + WishList.TABLE_NAME
                + " WHERE " + WishList.COLUMN_ID + " = ? "
                + " ORDER BY " + WishList.COLUMN_TIMESTAMP + " DESC";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(wishListId)});

        // fetching row
        if (cursor.moveToFirst()) {
                WishList wishList = new WishList();
                wishList.setId(cursor.getInt(cursor.getColumnIndex(WishList.COLUMN_ID)));
                wishList.setName(cursor.getString(cursor.getColumnIndex(WishList.COLUMN_NAME)));
                wishList.setImage(cursor.getInt(cursor.getColumnIndex(WishList.COLUMN_IMAGE)));
                wishList.setUserId(cursor.getString(cursor.getColumnIndex(WishList.COLUMN_USER_ID)));
                wishList.setTimestamp(cursor.getString(cursor.getColumnIndex(WishList.COLUMN_TIMESTAMP)));

            return wishList;
        }

        return null;
    }


    public void insertBookInWishList(Book book, int wishListId) {
        // get writable database to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Book.COLUMN_ID, book.getId());
        values.put(Book.COLUMN_NAME, book.getName());
        values.put(Book.COLUMN_IMAGE, book.getImage());
        values.put(Book.COLUMN_AUTHOR, book.getAuthor());
        values.put(Book.COLUMN_WISHLIST_ID, wishListId);

        // insert row
        db.insert(Book.TABLE_NAME, null, values);
    }

    public void removeBookFromWishList(String bookId, int wishlistId) {
        SQLiteDatabase db = this.getWritableDatabase();

        // delete row
        db.delete(Book.TABLE_NAME, Book.COLUMN_ID + " = ? AND " + Book.COLUMN_WISHLIST_ID + " = ?",
                new String[]{bookId, String.valueOf(wishlistId)});
    }

    public List<Book> getBooksInWishList(int wishListId) {
        List<Book> books = new ArrayList<>();

        // Select books by wishlist query
        String selectQuery = "SELECT  * FROM " + Book.TABLE_NAME +
                " WHERE " + Book.COLUMN_WISHLIST_ID + " = ? " +
                " ORDER BY " + Book.COLUMN_TIMESTAMP + " DESC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(wishListId)});

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Book book = new Book();
                book.setId(cursor.getString(cursor.getColumnIndex(Book.COLUMN_ID)));
                book.setName(cursor.getString(cursor.getColumnIndex(Book.COLUMN_NAME)));
                book.setImage(cursor.getString(cursor.getColumnIndex(Book.COLUMN_IMAGE)));
                book.setAuthor(cursor.getString(cursor.getColumnIndex(Book.COLUMN_AUTHOR)));
                book.setWishListId(cursor.getInt(cursor.getColumnIndex(Book.COLUMN_WISHLIST_ID)));
                book.setTimestamp(cursor.getString(cursor.getColumnIndex(Book.COLUMN_TIMESTAMP)));

                books.add(book);
            } while (cursor.moveToNext());
        }

        // return rows
        return books;
    }
}