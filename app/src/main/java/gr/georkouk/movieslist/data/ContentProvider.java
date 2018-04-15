package gr.georkouk.movieslist.data;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

@SuppressWarnings("ConstantConditions")
public class ContentProvider extends android.content.ContentProvider{

    public static final String LOG_TAG = ContentProvider.class.getSimpleName();
    private static final int ITEMS = 100;
    private static final int ITEM_ID = 101;
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_ITEMS, ITEMS);
        uriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_ITEMS + "/#", ITEM_ID);
    }

    DbHelper dbHelper;

    @Override
    public boolean onCreate() {
        dbHelper = new DbHelper(getContext());

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor;

        int match = uriMatcher.match(uri);
        switch (match){
            case ITEMS:
                cursor = db.query(
                        MovieContract.MovieItem.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );

                break;
            case ITEM_ID:
                selection = MovieContract.MovieItem.COLUMN_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                cursor = db.query(
                        MovieContract.MovieItem.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );

                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = uriMatcher.match(uri);
        switch (match) {
            case ITEMS:
                return MovieContract.MovieItem.CONTENT_LIST_TYPE;
            case ITEM_ID:
                return MovieContract.MovieItem.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final int match = uriMatcher.match(uri);
        switch (match) {
            case ITEMS:
                assert contentValues != null;
                return insertItem(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        int rowsDeleted;

        final int match = uriMatcher.match(uri);
        switch (match) {
            case ITEMS:
                rowsDeleted = database.delete(
                        MovieContract.MovieItem.TABLE_NAME,
                        selection,
                        selectionArgs
                );

                break;
            case ITEM_ID:
                selection = MovieContract.MovieItem.COLUMN_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                rowsDeleted = database.delete(
                        MovieContract.MovieItem.TABLE_NAME,
                        selection,
                        selectionArgs
                );

                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues,
                      @Nullable String selection, @Nullable String[] selectionArgs) {

        final int match = uriMatcher.match(uri);
        switch (match) {
            case ITEMS:
                return updateItem(uri, contentValues, selection, selectionArgs);
            case ITEM_ID:
                selection = MovieContract.MovieItem._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                return updateItem(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private Uri insertItem(Uri uri, ContentValues values) {

        Integer movieId = values.getAsInteger(MovieContract.MovieItem.COLUMN_ID);
        if (movieId != null && movieId < 0) {
            throw new IllegalArgumentException("Item requires an id");
        }

        String title = values.getAsString(MovieContract.MovieItem.COLUMN_TITLE);
        if (title == null) {
            throw new IllegalArgumentException("Item requires a title");
        }

        String overview = values.getAsString(MovieContract.MovieItem.COLUMN_OVERVIEW);
        if (overview == null) {
            throw new IllegalArgumentException("Item requires a title");
        }

        String releasedate = values.getAsString(MovieContract.MovieItem.COLUMN_RELEASE_DATE);
        if (releasedate == null) {
            throw new IllegalArgumentException("Item requires a Release Date");
        }

        Double average = values.getAsDouble(MovieContract.MovieItem.COLUMN_VOTE_AVERAGE);
        if (average != null && average < 0) {
            throw new IllegalArgumentException("Item requires valid vote average");
        }

        String originalTitle = values.getAsString(MovieContract.MovieItem.COLUMN_ORIGINAL_TITLE);
        if (originalTitle == null) {
            throw new IllegalArgumentException("Item requires an original title");
        }

        String posterPath = values.getAsString(MovieContract.MovieItem.COLUMN_POSTER_PATH);
        if (posterPath == null) {
            throw new IllegalArgumentException("Item requires poster path");
        }

//        String posterBlob = values.getAsString(MovieContract.MovieItem.COLUMN_POSTER_BLOB);
//        if (posterBlob == null) {
//            throw new IllegalArgumentException("Item requires poster blob");
//        }

        String backdropPath = values.getAsString(MovieContract.MovieItem.COLUMN_BACKDROP_PATH);
        if (backdropPath == null) {
            throw new IllegalArgumentException("Item requires backdrop path");
        }

//        String backdropBlob = values.getAsString(MovieContract.MovieItem.COLUMN_BACKDROP_BLOB);
//        if (backdropBlob == null) {
//            throw new IllegalArgumentException("Item requires backdrop blob");
//        }

        SQLiteDatabase database = dbHelper.getWritableDatabase();

        long id = database.insert(MovieContract.MovieItem.TABLE_NAME, null, values);

        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }

    private int updateItem(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        if (values.containsKey(MovieContract.MovieItem.COLUMN_ID)) {
            Integer movieId = values.getAsInteger(MovieContract.MovieItem.COLUMN_ID);
            if (movieId != null && movieId < 0) {
                throw new IllegalArgumentException("Item requires an id");
            }
        }

        if (values.containsKey(MovieContract.MovieItem.COLUMN_TITLE)) {
            String title = values.getAsString(MovieContract.MovieItem.COLUMN_TITLE);
            if (title == null) {
                throw new IllegalArgumentException("Item requires a title");
            }
        }

        if (values.containsKey(MovieContract.MovieItem.COLUMN_OVERVIEW)) {
            String overview = values.getAsString(MovieContract.MovieItem.COLUMN_OVERVIEW);
            if (overview == null) {
                throw new IllegalArgumentException("Item requires a title");
            }
        }

        if (values.containsKey(MovieContract.MovieItem.COLUMN_RELEASE_DATE)) {
            String releasedate = values.getAsString(MovieContract.MovieItem.COLUMN_RELEASE_DATE);
            if (releasedate == null) {
                throw new IllegalArgumentException("Item requires a Release Date");
            }
        }

        if (values.containsKey(MovieContract.MovieItem.COLUMN_VOTE_AVERAGE)) {
            Double average = values.getAsDouble(MovieContract.MovieItem.COLUMN_VOTE_AVERAGE);
            if (average != null && average < 0) {
                throw new IllegalArgumentException("Item requires valid vote average");
            }
        }

        if (values.containsKey(MovieContract.MovieItem.COLUMN_ORIGINAL_TITLE)) {
            String originalTitle = values.getAsString(MovieContract.MovieItem.COLUMN_ORIGINAL_TITLE);
            if (originalTitle == null) {
                throw new IllegalArgumentException("Item requires an original title");
            }
        }

        if (values.containsKey(MovieContract.MovieItem.COLUMN_POSTER_PATH)) {
            String posterPath = values.getAsString(MovieContract.MovieItem.COLUMN_POSTER_PATH);
            if (posterPath == null) {
                throw new IllegalArgumentException("Item requires poster path");
            }
        }

        if (values.containsKey(MovieContract.MovieItem.COLUMN_POSTER_BLOB)) {
            String posterBlob = values.getAsString(MovieContract.MovieItem.COLUMN_POSTER_BLOB);
            if (posterBlob == null) {
                throw new IllegalArgumentException("Item requires poster blob");
            }
        }

        if (values.containsKey(MovieContract.MovieItem.COLUMN_BACKDROP_PATH)) {
            String backdropPath = values.getAsString(MovieContract.MovieItem.COLUMN_BACKDROP_PATH);
            if (backdropPath == null) {
                throw new IllegalArgumentException("Item requires backdrop path");
            }
        }

        if (values.containsKey(MovieContract.MovieItem.COLUMN_BACKDROP_BLOB)) {
            String backdropBlob = values.getAsString(MovieContract.MovieItem.COLUMN_BACKDROP_BLOB);
            if (backdropBlob == null) {
                throw new IllegalArgumentException("Item requires backdrop blob");
            }
        }

        if (values.size() == 0) {
            return 0;
        }

        SQLiteDatabase database = dbHelper.getWritableDatabase();

        int rowsUpdated = database.update(
                MovieContract.MovieItem.TABLE_NAME,
                values,
                selection,
                selectionArgs
        );

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }

}
