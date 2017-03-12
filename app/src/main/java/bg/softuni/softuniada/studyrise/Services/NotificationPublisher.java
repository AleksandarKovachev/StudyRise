package bg.softuni.softuniada.studyrise.Services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.media.RingtoneManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import bg.softuni.softuniada.studyrise.Activities.ProductivityActivity;
import bg.softuni.softuniada.studyrise.NotificationConstants;
import bg.softuni.softuniada.studyrise.R;
import bg.softuni.softuniada.studyrise.SQLite.DBConstants;
import bg.softuni.softuniada.studyrise.SQLite.DBPref;

import static android.content.Context.NOTIFICATION_SERVICE;

public class NotificationPublisher extends BroadcastReceiver {

    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        callNotification();
    }

    private void callNotification() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.setFirstDayOfWeek(Calendar.MONDAY);

        String date = calendar.get(Calendar.DAY_OF_MONTH) + "." + (calendar.get(Calendar.MONTH) + 1) + "." + calendar.get(Calendar.YEAR);

        List<String> data = new ArrayList<>();
        DBPref pref = new DBPref(context);

        Cursor cursor = pref.getVals(DBConstants.TODO_ACTIV_TABLE, date);
        if (cursor.moveToFirst()) {
            do {
                data.add(cursor.getString(cursor.getColumnIndex("name")));
            } while (cursor.moveToNext());
        }

        cursor.close();
        pref.close();

        StringBuilder todoThings = new StringBuilder();

        for (String todo : data) {
            todoThings.append(todo + "\n");
        }

        if (data.size() != 0) {

            final String text = "Днес те очакват много нови възможности. " +
                    "Списък с нещата, които да направиш днес:\n" + todoThings.toString();

            Notification.Builder builder = new Notification.Builder(context);
            builder.setStyle(new Notification.BigTextStyle(builder)
                    .bigText(text)
                    .setBigContentTitle("Утрото е по-мъдро от вечерта!")
                    .setSummaryText("Списък"))
                    .setContentTitle("Утрото е по-мъдро от вечерта!")
                    .setContentText(text)
                    .setVibrate(new long[]{0, 1000, 200, 1000})
                    .setLights(Color.MAGENTA, 500, 500)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setSmallIcon(R.drawable.ic_todo_name);

            Intent resultIntent = new Intent(context, ProductivityActivity.class);
            PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            builder.setContentIntent(resultPendingIntent);

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(NotificationConstants.PRODUCTIVITY_NOTIFICATION, builder.build());
        }
    }
}
