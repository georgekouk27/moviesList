package gr.georkouk.movieslist.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "movies.db";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_INVENTORY_TABLE = "CREATE TABLE " + MovieContract.MovieItem.TABLE_NAME + " (" +
                MovieContract.MovieItem.COLUMN_ID + " INTEGER PRIMARY KEY, " +
                MovieContract.MovieItem.COLUMN_TITLE + " TEXT DEFAULT ''," +
                MovieContract.MovieItem.COLUMN_OVERVIEW + " TEXT DEFAULT ''," +
                MovieContract.MovieItem.COLUMN_VOTE_AVERAGE + " REAL DEFAULT 0, " +
                MovieContract.MovieItem.COLUMN_RELEASE_DATE + " TEXT DEFAULT ''," +
                MovieContract.MovieItem.COLUMN_ORIGINAL_TITLE + " TEXT DEFAULT ''," +
                MovieContract.MovieItem.COLUMN_BACKDROP_PATH + " TEXT DEFAULT ''," +
                MovieContract.MovieItem.COLUMN_POSTER_PATH + " TEXT DEFAULT ''" + ")";

        db.execSQL(CREATE_INVENTORY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

}
