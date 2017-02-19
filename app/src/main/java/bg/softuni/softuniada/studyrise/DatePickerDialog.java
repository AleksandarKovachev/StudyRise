package bg.softuni.softuniada.studyrise;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.widget.DatePicker;
import android.widget.LinearLayout;

import java.util.Calendar;
import java.util.Date;

public class DatePickerDialog {

    public interface OnDateSetListener {
        void onDateSet(int Year, int Month, int Day);
    }

    private OnDateSetListener dateSetlistener;
    private Context context;
    private AlertDialog.Builder alertBuilder;
    public int YEAR, MONTH, DAY;


    public DatePickerDialog(Context context, OnDateSetListener listener) {
        this.context = context;
        this.dateSetlistener = listener;
        this.alertBuilder = returnDialog();
    }

    public AlertDialog.Builder returnDialog() {
        LayoutInflater inflater = LayoutInflater.from(context);
        LinearLayout dialog = (LinearLayout) inflater.inflate(R.layout.date_picker, null, false);

        final DatePicker datePicker = (DatePicker) dialog.findViewById(R.id.date_picker);

        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        YEAR = calendar.get(Calendar.YEAR);
        MONTH = calendar.get(Calendar.MONTH) + 1;
        DAY = calendar.get(Calendar.DAY_OF_MONTH);

        datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new android.widget.DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        YEAR = year;
                        MONTH = ++monthOfYear;
                        DAY = dayOfMonth;
                    }
                }
        );


        AlertDialog.Builder aDBuilder = new AlertDialog.Builder(context)
                .setView(dialog)
                .setPositiveButton("Вземи", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dateSetlistener.onDateSet(YEAR, MONTH, DAY);
                    }
                })
                .setNegativeButton("Откажи", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dateSetlistener.onDateSet(YEAR, MONTH, DAY);
                    }
                });

        return aDBuilder;
    }

    public void showCalendar() {
        alertBuilder.show();
    }
}
