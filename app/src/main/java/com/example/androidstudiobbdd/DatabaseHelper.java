package com.example.androidstudiobbdd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static String DATABASE_NAME = "androidadvancesqlite";
    public static String COMMENT_TABLE = "comments";

    private ArrayList<Comment> cartList = new ArrayList<Comment>();
    Context c;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 33);
        c = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE if not exists comments(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "title TEXT,"
                + "description TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int
            newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + COMMENT_TABLE);
        onCreate(db);
    }

    public void addComment(Comment comment) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", comment.getTitle());
        contentValues.put("description", comment.getDescription());
        db.insert(COMMENT_TABLE, null, contentValues);
        db.close();
    }


    public void removeComment(String title) {
        try {
            String[] args = {title};

            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("delete from " + COMMENT_TABLE + " where title=?", args);
            db.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public ArrayList<Comment> getComments() {
        cartList.clear();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + COMMENT_TABLE, null);

        if (cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                do {
                    Comment comment = new Comment();
                    comment.setTitle(cursor.getString(cursor.getColumnIndexOrThrow("title")));
                    comment.setDescription(cursor.getString(cursor.getColumnIndexOrThrow("description")));
                    cartList.add(comment);
                } while (cursor.moveToNext());
            }
        }

        cursor.close();
        db.close();
        return cartList;
    }
}