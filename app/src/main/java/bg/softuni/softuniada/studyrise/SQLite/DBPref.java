package bg.softuni.softuniada.studyrise.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class DBPref extends DBHelper {

    public DBPref(Context context) {
        super(context);
    }

    public void addRecord(String database, String value1, String value2, String value3, Long id) {
        ContentValues contentValues = new ContentValues();

        if (database.equals("program")) {
            contentValues.put("_id", id);
            contentValues.put("programName", value1);
            contentValues.put("date", value2);
            contentValues.put("program_type", value3);
        } else if (database.equals("activ")) {
            contentValues.put("activTitle", value1);
            contentValues.put("points", value2);
            contentValues.put("programId", id);
        } else if (database.equals("achievement")) {
            contentValues.put("achievement", value1);
            contentValues.put("points", value2);
            contentValues.put("programId", id);
        } else if (database.equals("profit_expense")) {
            contentValues.put("type", value1);
            contentValues.put("name", value2);
            contentValues.put("value", Double.parseDouble(value3));
            contentValues.put("programId", id);
        }
        this.db.insert(database, null, contentValues);
    }

    public Cursor getVals(String database, String id) {
        if (database.equals("program"))
            return this.db.query(database, new String[]{"_id", "programName", "date", "program_type"}, null, null, null, null, null);
        else if (database.equals("activ"))
            return this.db.query(database, new String[]{"activTitle", "points"}, "programId=?", new String[]{id}, null, null, null);
        else if (database.equals("achievement"))
            return this.db.query(database, new String[]{"achievement", "points"}, "programId=?", new String[]{id}, null, null, null);
        else if (database.equals("finance"))
            return this.db.query(database, new String[]{"name"}, "type=?", new String[]{id}, null, null, null);
        else if (database.equals("profit_expense"))
            return this.db.query(database, new String[]{"type", "name", "value"}, "type=?", new String[]{id}, null, null, null);
        else
            return null;
    }

    public void deleteRecord(String database, String row1, String row2, String value1, String value2) {
        db.execSQL("delete from  " + database +
                " where " + row1 + "=\'" + value1 +
                "\' and " + row2 + " = \'" + value2 + "\'");
    }
}