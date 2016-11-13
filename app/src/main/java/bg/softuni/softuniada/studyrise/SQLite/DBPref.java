package bg.softuni.softuniada.studyrise.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class DBPref extends DBHelper {

    public DBPref(Context context) {
        super(context);
    }

    public void addRecord(String database, String value1, String value2) {
        ContentValues contentValues = new ContentValues();

        if (database.equals("program")) {
            contentValues.put("programName", value1);
            contentValues.put("date", value2);
        } else if (database.equals("activ")) {
            contentValues.put("activTitle", value1);
            contentValues.put("points", value2);
        } else if (database.equals("achievement")) {
            contentValues.put("achievement", value1);
            contentValues.put("points", value2);
        }

        this.db.insert(database, null, contentValues);
    }

    public Cursor getVals(String database) {
        if (database.equals("program"))
            return this.db.query(database, new String[]{"programName", "date"}, null, null, null, null, null);
        else if (database.equals("activ"))
            return this.db.query(database, new String[]{"activTitle", "points"}, null, null, null, null, null);
        else if (database.equals("achievement"))
            return this.db.query(database, new String[]{"achievement", "points"}, null, null, null, null, null);
        else
            return null;
    }

    public void deleteRecord(String database, String value1, String value2) {
        db.execSQL("delete from  " + database +
                " where programName=\'" + value1 +
                "\' and date = \'" + value2 + "\'");
    }
}