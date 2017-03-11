package bg.softuni.softuniada.studyrise.Services;

import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.Nullable;

import java.util.Calendar;
import java.util.Date;

import bg.softuni.softuniada.studyrise.SQLite.DBConstants;
import bg.softuni.softuniada.studyrise.SQLite.DBPref;

public class PastDates extends IntentService {

    public PastDates() {
        this(PastDates.class.getName());
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public PastDates(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        DBPref pref = new DBPref(this);
        Cursor c = pref.getVals(DBConstants.TODO_ACTIV_TABLE, "");

        if (c.moveToFirst()) {
            do {
                if (isPastDate(c.getString(c.getColumnIndex("date")))) {
                    pref.deleteRecord(DBConstants.TODO_ACTIV_TABLE, "name", "date", c.getString(c.getColumnIndex("name")), c.getString(c.getColumnIndex("date")));
                }
            } while (c.moveToNext());
        }

        c.close();
        pref.close();
    }

    private boolean isPastDate(String date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.setFirstDayOfWeek(Calendar.MONDAY);

        String[] dates = date.split("\\.");

        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);

        if (Integer.parseInt(dates[0]) < day && Integer.parseInt(dates[1]) <= month && Integer.parseInt(dates[2]) <= year) {
            return true;
        }
        return false;
    }

}
