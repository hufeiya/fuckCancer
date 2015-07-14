package com.hufeiya.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by hufeiya on 15-6-26.
 */
public class MydatabaseHelper extends SQLiteOpenHelper {
    public static final String CREATE_ARTICLE = "CREATE TABLE article (" +
            "  id integer  primary key autoincrement," +
            "  title varchar(100) NOT NULL," +
            "  type integer DEFAULT NULL," +
            "  image varchar(200) DEFAULT NULL," +
            "  columnx integer DEFAULT NULL" +
            ") ";
    public static final String CREATE_ARTICLEENTITY = "CREATE TABLE articleEntity (" +
            "  id integer primary key autoincrement," +
            "  image varchar(200) DEFAULT NULL," +
            "  details text" +
            ")";
    private Context mContext;

    public MydatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ARTICLE);
        db.execSQL(CREATE_ARTICLEENTITY);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
