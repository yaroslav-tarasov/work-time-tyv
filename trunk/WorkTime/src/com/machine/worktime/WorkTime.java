package com.machine.worktime;
  
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Convenience definitions for NotePadProvider
 */
public final class WorkTime {
	
    public static final String AUTHORITY = "com.machine.worktime.WorkTime";
    
    // This class cannot be instantiated
    private WorkTime() {}	
    
    /**
     * Notes table
     */
    public static final class Notes implements BaseColumns {
        // This class cannot be instantiated
        private Notes() {}

        /**
         * The content:// style URL for this table
         */
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/notes");

        /**
         * The MIME type of {@link #CONTENT_URI} providing a directory of notes.
         */
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.google.note";
        
        /**
         * The MIME type of a {@link #CONTENT_URI} sub-directory of a single note.
         */
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.google.note";
 
        /**
         * The title of the note
         * <P>Type: TEXT</P>
         */
        public static final String TITLE = "title";
         
        /**
         * The default sort order for this table
         */
        public static final String DEFAULT_SORT_ORDER = "stamp DESC";
        
        /**
         * The note itself
         * <P>Type: TEXT</P>
         */
        public static final String TYPE = "type";

        /**
         * The timestamp for when the note was created
         * <P>Type: INTEGER (long)</P>
         */
        public static final String TIME_STAMP = "stamp";

    }
}