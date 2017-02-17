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

        if (value[0].equals(DBConstants.PROGRAM_TABLE)) {
            contentValues.put("_id", id);
            contentValues.put("programName", value[1]);
            contentValues.put("date", value[2]);
            contentValues.put("program_type", value[3]);
        } else if (value[0].equals(DBConstants.ACTIV_TABLE)) {
            contentValues.put("activTitle", value[1]);
            contentValues.put("points", value[2]);
            contentValues.put("programId", id);
        } else if (value[0].equals(DBConstants.ACHIEVEMENT_TABLE)) {
            contentValues.put("achievement", value[1]);
            contentValues.put("points", value[2]);
            contentValues.put("programId", id);
        } else if (value[0].equals(DBConstants.PROFIT_EXPENSE_TABLE)) {
            contentValues.put("type", value[1]);
            contentValues.put("name", value[2]);
            contentValues.put("value", Double.parseDouble(value[3]));
            contentValues.put("programId", id);
            contentValues.put("date", value[4]);
        } else if (value[0].equals(DBConstants.FINANCE_TABLE)) {
            contentValues.put("type", value[1]);
            contentValues.put("name", value[2]);
        } else if (value[0].equals(DBConstants.HISTORY_TABLE)) {
            contentValues.put("type", value[1]);
            contentValues.put("name", value[2]);
            contentValues.put("date", value[3]);
            contentValues.put("points", value[4]);
            contentValues.put("programId", id);
        } else if (value[0].equals(DBConstants.TODO_TABLE)) {
            contentValues.put("priority", value[1]);
            contentValues.put("date", value[2]);
            contentValues.put("activId", value[3]);
            contentValues.put("achievementId", value[4]);
        }
        this.db.insert(value[0], null, contentValues);
    }

    public Cursor getVals(String... value) {
        if (value[0].equals(DBConstants.PROGRAM_TABLE))
            return this.db.query(value[0], new String[]{"_id", "programName", "date", "program_type"}, null, null, null, null, null);
        else if (value[0].equals(DBConstants.ACTIV_TABLE))
            return this.db.query(value[0], new String[]{"activTitle", "points"}, "programId=?", new String[]{value[1]}, null, null, null);
        else if (value[0].equals(DBConstants.ACHIEVEMENT_TABLE))
            return this.db.query(value[0], new String[]{"achievement", "points"}, "programId=?", new String[]{value[1]}, null, null, null);
        else if (value[0].equals(DBConstants.FINANCE_TABLE))
            return this.db.query(value[0], new String[]{"name"}, "type=?", new String[]{value[1]}, null, null, null);
        else if (value[0].equals(DBConstants.PROFIT_EXPENSE_TABLE) && value[1].isEmpty())
            return this.db.query(value[0], new String[]{"type", "name", "value", "date"}, null, null, null, null, null);
        else if (value[0].equals(DBConstants.PROFIT_EXPENSE_TABLE))
            return this.db.query(value[0], new String[]{"type", "name", "value", "date"}, "type=?", new String[]{value[1]}, null, null, "_id DESC");
        else if (value[0].equals(DBConstants.HISTORY_TABLE))
            return this.db.query(value[0], new String[]{"type", "name", "date", "points"}, "programId=?", new String[]{value[1]}, null, null, "_id DESC");
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