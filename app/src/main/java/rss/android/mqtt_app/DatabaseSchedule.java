package rss.android.mqtt_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by AliFakih on 11-Dec-19.
 */

public class DatabaseSchedule extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 3;
    //   Database Name
    private static final String DATABASE_NAME = "studentSchedule";
    //  Contacts table name
    private static final String TABLE_CONTACTS = "schedule";
    //   Contacts Table Columns names
    private static final String Class_NAME = "ClassName";
    private static final String Course_Name = "CourseName";
    private static final String Course_Day = "CourseDay";
    private static final String Start_Time = "StartTime";
    private static final String End_Time = "EndTime";


    public DatabaseSchedule(Context c) {
        super(c, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "(" + Class_NAME + " TEXT," + Course_Name + " TEXT," +
                Course_Day + " TEXT," + Start_Time + " TEXT," + End_Time + " TEXT)";
        db.execSQL(CREATE_CONTACTS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS); // Create tables again
        onCreate(db);
    }

    void addRow(Session1 c) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Class_NAME, c.getClassName());
        values.put(Course_Name, c.getCourseName());
        values.put(Course_Day, c.getDay());
        values.put(Start_Time, c.getStartTime());
        values.put(End_Time, c.getEndTime());
        Long l=db.insert(TABLE_CONTACTS, null, values);
        Toast.makeText(this.c, l+"", Toast.LENGTH_LONG).show();
        db.close();


    }

    Context c;

    public void getContext(Context c) {
        this.c = c;
    }

    //format of time hh:mm:ss
    Boolean CheckTime(String day, String CurrentTime) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query="SELECT * FROM "+TABLE_CONTACTS +" where CourseDay='" + day + "'";
        Cursor cursor = db.rawQuery(query, null);

     //   Toast.makeText(c, "nb Cl: " + cursor.getCount(), Toast.LENGTH_LONG).show();

        if (cursor.moveToFirst()) {
            do {
                String StartTime = cursor.getString(3);
                String EndTime = cursor.getString(4);

                try {
                    //    String string1 = "20:11:13";
                    Date time1 = new SimpleDateFormat("HH:mm:ss").parse(StartTime);
                    Calendar calendar1 = Calendar.getInstance();
                    calendar1.setTime(time1);
                    calendar1.add(Calendar.DATE, 1);


                    //     String string2 = "14:49:00";
                    Date time2 = new SimpleDateFormat("HH:mm:ss").parse(EndTime);
                    Calendar calendar2 = Calendar.getInstance();
                    calendar2.setTime(time2);
                    calendar2.add(Calendar.DATE, 1);

                    //     String someRandomTime = "01:00:00";
                    Date d = new SimpleDateFormat("HH:mm:ss").parse(CurrentTime);
                    Calendar calendar3 = Calendar.getInstance();
                    calendar3.setTime(d);
                    calendar3.add(Calendar.DATE, 1);

                    Date x = calendar3.getTime();
                    if (x.after(calendar1.getTime()) && x.before(calendar2.getTime())) {
                        //checkes whether the current time is between 14:49:00 and 20:11:13.
                        return true;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }


            } while (cursor.moveToNext());
        }
        db.close(); // return contact
        return false;
    }


}
