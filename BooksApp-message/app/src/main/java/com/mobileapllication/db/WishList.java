package com.mobileapllication.db;

import androidx.annotation.DrawableRes;

import com.mobileapllication.R;

import java.util.ArrayList;
import java.util.List;

/**
 * wishlist table
 */
public class WishList {
    public static final String TABLE_NAME = "tbl_wishlist";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_IMAGE = "image";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_TIMESTAMP = "timestamp";
    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_NAME + " TEXT,"
                    + COLUMN_IMAGE + " INT,"
                    + COLUMN_USER_ID + " TEXT,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";
    private int id;
    private String name;
    @DrawableRes
    private int image;
    private String userId;
    private String timestamp;

    public WishList() {
    }

    public WishList(String name, @DrawableRes int image, String userId) {
        this.name = name;
        this.image = image;
        this.userId = userId;
    }

    public static List<WishList> generateDefaultWishLists(String userId) {
        List<WishList> wishLists = new ArrayList<>();
        wishLists.add(new WishList("Read", R.drawable.optimizedread,userId));
        wishLists.add(new WishList("Want to Read", R.drawable.optimizedwant,userId));
        wishLists.add(new WishList("Fiction", R.drawable.optimizedread,userId));
        wishLists.add(new WishList("Non-Fiction", R.drawable.optimizedwant,userId));
        return wishLists;
    }

    @DrawableRes
    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

}