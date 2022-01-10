package com.mobileapllication.db;

//books table
public class Book {

    public static final String TABLE_NAME = "tbl_book";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_IMAGE = "image";
    public static final String COLUMN_AUTHOR = "author";
    public static final String COLUMN_WISHLIST_ID = "wishListId";
    public static final String COLUMN_TIMESTAMP = "timestamp";
    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " TEXT,"
                    + COLUMN_NAME + " TEXT,"
                    + COLUMN_IMAGE + " TEXT,"
                    + COLUMN_AUTHOR + " TEXT,"
                    + COLUMN_WISHLIST_ID + " INTEGER,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";
    private String id;
    private String name;
    private String image;
    private String author;
    private String timestamp;
    private int wishListId;

    public Book() {
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getWishListId() {
        return wishListId;
    }

    public void setWishListId(int wishListId) {
        this.wishListId = wishListId;
    }
}
