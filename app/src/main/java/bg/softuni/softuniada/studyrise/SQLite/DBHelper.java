package bg.softuni.softuniada.studyrise.SQLite;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    static final String DB_NAME = "studyrise";
    static final int CURRENT_VERSION = 1;
    protected SQLiteDatabase db;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, CURRENT_VERSION);
        open();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table program ("
                + "_id integer primary key autoincrement, "
                + "programName text not null, "
                + "date text not null);");

        db.execSQL("create table activ ("
                + "_id integer primary key autoincrement, "
                + "activTitle text not null, " + "points text not null, "
                + "programId integer, "
                + "FOREIGN KEY(programId) REFERENCES program(_id) "
                + "ON DELETE CASCADE);");

        db.execSQL("create table achievement ("
                + "_id integer primary key autoincrement, "
                + "achievement text not null, "
                + "points text not null, "
                + "programId integer, "
                + "FOREIGN KEY(programId) REFERENCES program(_id) "
                + "ON DELETE CASCADE);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + "program");
        db.execSQL("DROP TABLE IF EXISTS " + "activ");
        db.execSQL("DROP TABLE IF EXISTS " + "achievement");

        onCreate(db);
    }

    public void open() throws SQLException {
        db = getWritableDatabase();
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if(!db.isReadOnly()) {
            db.execSQL("PRAGMA foreign_keys=ON;");
            db.setForeignKeyConstraintsEnabled(true);
        }
    }

    public void close() {
        db.close();
    }
}