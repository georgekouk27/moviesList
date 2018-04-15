package gr.georkouk.movieslist.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;


public class MovieContract {

    public static final String CONTENT_AUTHORITY = "gr.georkouk.movieslist";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_ITEMS = "favorites";

    public static final class MovieItem implements BaseColumns {

        public final static String TABLE_NAME = "favorites";

        public final static String COLUMN_ID = "id";

        public final static String COLUMN_TITLE = "title";

        public final static String COLUMN_OVERVIEW = "overview";

        public final static String COLUMN_RELEASE_DATE = "releasedate";

        public final static String COLUMN_VOTE_AVERAGE = "voteaverage";

        public final static String COLUMN_POSTER_PATH = "posterpath";

        public final static String COLUMN_BACKDROP_PATH = "backdroppath";

        public final static String COLUMN_POSTER_BLOB = "posterblob";

        public final static String COLUMN_BACKDROP_BLOB = "backdropblob";

        public final static String COLUMN_ORIGINAL_TITLE = "originaltitle";


        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_ITEMS);
        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ITEMS;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ITEMS;
    }

}
