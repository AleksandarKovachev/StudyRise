package bg.softuni.softuniada.studyrise;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.widget.CalendarView;
import android.widget.LinearLayout;

import java.util.Date;

public class CalendarDialogBuilder {

    public interface OnDateSetListener {

        void onDateSet(int Year, int Month, int Day);
    }

    private OnDateSetListener dateSetlistener;
    private Context context;
    private long initialDate;
    private CalendarView mCv;
    private AlertDialog.Builder alertBuilder;

    public int YEAR, MONTH, DAY;


    public CalendarDialogBuilder(Context ctx, OnDateSetListener listener, long initialDate) {
        this.context = ctx;
        this.dateSetlistener = listener;
        this.initialDate = initialDate;
        this.alertBuilder = returnDialog();
    }

    public AlertDialog.Builder returnDialog() {
        LayoutInflater inflater = LayoutInflater.from(context);
        LinearLayout dialog = (LinearLayout) inflater.inflate(R.layout.calendar_view, null, false);

        mCv = (CalendarView) dialog.findViewById(R.id.calendar);

        mCv.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                YEAR = year;
                MONTH = month;
                DAY = dayOfMonth;
            }
        });

        configCalendarView();

        AlertDialog.Builder aDBuilder = new AlertDialog.Builder(context)
                .setView(dialog)
                .setPositiveButton("Вземи", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dateSetlistener.onDateSet(YEAR, MONTH, DAY);
                    }
                })
                .setNegativeButton("Откажи", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dateSetlistener.onDateSet(0, 0, 0);
                    }
                });
        return aDBuilder;
    }

    public void showCalendar() {
        alertBuilder.show();
    }

    private void configCalendarView() {

        mCv.setFirstDayOfWeek(2);

        if (initialDate != 0) {
            mCv.setDate(initialDate, true, false);

            Date dateToSet = new Date(initialDate);
            YEAR = dateToSet.getYear();
            MONTH = dateToSet.getMonth();
            DAY = dateToSet.getDay();

        }
        mCv.setBackgroundColor(context.getResources().getColor(R.color.holo_orange));
    }

}