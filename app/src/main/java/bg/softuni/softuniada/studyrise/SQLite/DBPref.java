package bg.softuni.softuniada.studyrise.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.provider.Settings;
import android.util.Log;

public class DBPref extends DBHelper {

    public DBPref(Context context) {
        super(context);
    }

    public void addRecord(Long id, String... value) {
        ContentValues contentValues = new ContentValues();

        if (value[0].equals("program")) {
            contentValues.put("_id", id);
            contentValues.put("programName", value[1]);
            contentValues.put("date", value[2]);
            contentValues.put("program_type", value[3]);
        } else if (value[0].equals("activ")) {
            contentValues.put("activTitle", value[1]);
            contentValues.put("points", value[2]);
            contentValues.put("programId", id);
        } else if (value[0].equals("achievement")) {
            contentValues.put("achievement", value[1]);
            contentValues.put("points", value[2]);
            contentValues.put("programId", id);
        } else if (value[0].equals("profit_expense")) {
            contentValues.put("type", value[1]);
            contentValues.put("name", value[2]);
            contentValues.put("value", Double.parseDouble(value[3]));
            contentValues.put("programId", id);
            contentValues.put("date", value[4]);
        } else if (value[0].equals("finance")) {
            contentValues.put("type", value[1]);
            contentValues.put("name", value[2]);
        } else if (value[0].equals("history")) {
            contentValues.put("type", value[1]);
            contentValues.put("name", value[2]);
            contentValues.put("date", value[3]);
            contentValues.put("points", value[4]);
            contentValues.put("programId", id);
        }
        this.db.insert(value[0], null, contentValues);
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
        else if (database.equals("profit_expense") && id.isEmpty())
            return this.db.query(database, new String[]{"type", "name", "value", "date"}, null, null, null, null, null);
        else if (database.equals("profit_expense"))
            return this.db.query(database, new String[]{"type", "name", "value", "date"}, "type=?", new String[]{id}, null, null, "_id DESC");
        else if (database.equals("history"))
            return this.db.query(database, new String[]{"type", "name", "date", "points"}, "programId=?", new String[]{id}, null, null, "_id DESC");
        else
            return null;
    }

    public void deleteRecord(String table, String... value) {
        db.execSQL("delete from  " + table +
                " where " + value[0] + "=\'" + value[2] +
                "\' and " + value[1] + " = \'" + value[3] + "\'");
    }

    public void changeItem(String table, String... value) {
        ContentValues newValues = new ContentValues();
        newValues.put(value[0], value[1]);
        newValues.put(value[2], value[3]);

        db.update(table, newValues, String.format("%s = ?", value[4]), new String[]{value[5]});
    }
}