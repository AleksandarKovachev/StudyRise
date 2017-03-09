package bg.softuni.softuniada.studyrise.SQLite;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DBHelper extends SQLiteOpenHelper {

    static final String DB_NAME = "studyrise";
    static final int CURRENT_VERSION = 5;
    protected SQLiteDatabase db;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, CURRENT_VERSION);
        open();
    }

    private final String programSql = "create table " + DBConstants.PROGRAM_TABLE + " ("
            + "_id integer primary key autoincrement, "
            + "programName text not null, "
            + "date text not null, "
            + "program_type text not null);";

    private final String activSql = "create table " + DBConstants.ACTIV_TABLE + " ("
            + "_id integer primary key autoincrement, "
            + "activTitle text not null, "
            + "points text not null, "
            + "programId integer, "
            + "todoId integer default null,"
            + "FOREIGN KEY(programId) REFERENCES program(_id) "
            + "ON DELETE CASCADE);";

    private final String achievementSql = "create table " + DBConstants.ACHIEVEMENT_TABLE + " ("
            + "_id integer primary key autoincrement, "
            + "achievement text not null, "
            + "points text not null, "
            + "programId integer, "
            + "FOREIGN KEY(programId) REFERENCES program(_id) "
            + "ON DELETE CASCADE);";

    private final String financeSql = "create table " + DBConstants.FINANCE_TABLE + " ("
            + "_id integer primary key autoincrement, "
            + "type text not null, "
            + "name text not null);";

    private final String profitExpenseSql = "create table " + DBConstants.PROFIT_EXPENSE_TABLE + " ("
            + "_id integer primary key autoincrement, "
            + "type text not null, "
            + "name text not null, "
            + "value real not null, "
            + "programId integer, "
            + "date text not null, "
            + "FOREIGN KEY(programId) REFERENCES program(_id) "
            + "ON DELETE CASCADE);";

    private final String historySql = "create table " + DBConstants.HISTORY_TABLE + " ("
            + "_id integer primary key autoincrement, "
            + "type text not null, "
            + "name text not null, "
            + "date text not null, "
            + "programId integer, "
            + "points text not null, "
            + "FOREIGN KEY(programId) REFERENCES program(_id) "
            + "ON DELETE CASCADE);";

    private final String todoActivSql = "create table " + DBConstants.TODO_ACTIV_TABLE + " (" +
            "_id integer primary key autoincrement," +
            "name text not null," +
            "points text not null," +
            "priority text not null," +
            "date text not null);";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(programSql);

        db.execSQL(activSql);

        db.execSQL(achievementSql);

        db.execSQL(financeSql);

        db.execSQL("INSERT INTO finance VALUES(null, 'Разход', 'Храна');");
        db.execSQL("INSERT INTO finance VALUES(null, 'Разход', 'Транспорт');");
        db.execSQL("INSERT INTO finance VALUES(null, 'Разход', 'Интернет');");
        db.execSQL("INSERT INTO finance VALUES(null, 'Приход', 'Родители');");
        db.execSQL("INSERT INTO finance VALUES(null, 'Приход', 'Стипендия');");
        db.execSQL("INSERT INTO finance VALUES(null, 'Приход', 'Заплата');");

        db.execSQL(profitExpenseSql);

        db.execSQL(historySql);

        db.execSQL(todoActivSql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL(financeSql);

            db.execSQL("INSERT INTO finance VALUES(null, 'Разход', 'Храна');");
            db.execSQL("INSERT INTO finance VALUES(null, 'Разход', 'Транспорт');");
            db.execSQL("INSERT INTO finance VALUES(null, 'Разход', 'Интернет');");
            db.execSQL("INSERT INTO finance VALUES(null, 'Приход', 'Родители');");
            db.execSQL("INSERT INTO finance VALUES(null, 'Приход', 'Стипендия');");
            db.execSQL("INSERT INTO finance VALUES(null, 'Приход', 'Заплата');");

            db.execSQL(profitExpenseSql);
        }

        if (oldVersion < 3) {
            String datePattern = "HH:mm:ss EEE dd MMM yyyy";
            SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern);
            String date = dateFormat.format(new Date(System.currentTimeMillis()));
            db.execSQL("ALTER TABLE " + DBConstants.PROFIT_EXPENSE_TABLE + " ADD COLUMN date text DEFAULT ('" + date + "');");
        }

        if (oldVersion < 4) {
            db.execSQL(historySql);
        }

        if (oldVersion < 5) {
            db.execSQL(todoActivSql);
        }
    }

    public void open() throws SQLException {
        db = getWritableDatabase();
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            db.execSQL("PRAGMA foreign_keys=ON;");
            db.setForeignKeyConstraintsEnabled(true);
        }
    }

    public void close() {
        db.close();
    }
}