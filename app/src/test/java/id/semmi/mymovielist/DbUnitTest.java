package id.semmi.mymovielist;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import org.junit.Test;

import id.semmi.mymovielist.persist.MovieContract;
import id.semmi.mymovielist.persist.MovieDbHelper;

/**
 * Created by Semmiverian on 6/1/16.
 */
public class DbUnitTest extends AndroidTestCase {
    @Test
    public void testOpeningDatabase(){
        SQLiteDatabase database = new MovieDbHelper(mContext).getWritableDatabase();
        assertTrue("Error to connect to database",database.isOpen());
        database.close();
    }

    @Test
    public void testInsertingData(){
        MovieDbHelper helper = new MovieDbHelper(mContext);
        SQLiteDatabase database = helper.getWritableDatabase();
        assertTrue("Database is not open",database.isOpen());
        ContentValues test = insertDummyData();
        long row_id;
        row_id = database.insert(MovieContract.MovieEntry.Table_Name,null,test);
        assertTrue("Error while inserting new data",row_id != -1);
        database.close();

    }


    public ContentValues insertDummyData(){
        ContentValues test = new ContentValues();
        test.put(MovieContract.MovieEntry.COLUMN_NAME,"Iron Man");
        test.put(MovieContract.MovieEntry.COLUMN_DATE,"12/12/12");
        test.put(MovieContract.MovieEntry.COLUMN_RATING,4.5);
        test.put(MovieContract.MovieEntry.COLUMN_DESCRIPTION,"Lorem Ipsum");
        test.put(MovieContract.MovieEntry.COLUMN_IMAGE,"/dummy");
        return test;
    }
}
