package bg.softuni.softuniada.studyrise;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

import bg.softuni.softuniada.studyrise.Animations.ViewAnimations;

public class CalendarDialogBuilder {

    public interface OnDateSetListener {
        void onDateSet(int Year, int Month, int Day, DateType type);
    }

    private OnDateSetListener dateSetlistener;
    private Context context;
    private Button year, month, week, daily;
    private AlertDialog.Builder alertBuilder;
    private DateType type;
    public int YEAR, MONTH, DAY;


    public CalendarDialogBuilder(Context context, OnDateSetListener listener) {
        this.context = context;
        this.dateSetlistener = listener;
        this.alertBuilder = returnDialog();
    }

    public AlertDialog.Builder returnDialog() {
        LayoutInflater inflater = LayoutInflater.from(context);
        LinearLayout dialog = (LinearLayout) inflater.inflate(R.layout.calendar_view, null, false);

        final TextView text = (TextView) dialog.findViewById(R.id.textDialog);

        final DatePicker datePicker = (DatePicker) dialog.findViewById(R.id.calendar);

        final DatePicker weekPicker = (DatePicker) dialog.findViewById(R.id.weekPicker);

        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        YEAR = calendar.get(Calendar.YEAR);
        MONTH = calendar.get(Calendar.MONTH) + 1;
        DAY = calendar.get(Calendar.DAY_OF_MONTH);
        type = DateType.DAY;

        String[] data = {"Януари", "Февруари", "Март", "Април", "Май", "Юни", "Юли", "Август", "Септември", "Октомври", "Ноември", "Декември"};
        final NumberPicker monthPicker = (NumberPicker) dialog.findViewById(R.id.monthPicker);
        monthPicker.setMinValue(0);
        monthPicker.setMaxValue(data.length - 1);
        monthPicker.setDisplayedValues(data);
        monthPicker.setValue(calendar.get(Calendar.MONTH));
        monthPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        final NumberPicker yearMonthPicker = (NumberPicker) dialog.findViewById(R.id.yearMonthPicker);
        yearMonthPicker.setMinValue(2000);
        yearMonthPicker.setMaxValue(2100);
        yearMonthPicker.setValue(calendar.get(Calendar.YEAR));
        yearMonthPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        final NumberPicker yearPicker = (NumberPicker) dialog.findViewById(R.id.yearPicker);
        yearPicker.setMinValue(2000);
        yearPicker.setMaxValue(2100);
        yearPicker.setValue(calendar.get(Calendar.YEAR));
        yearPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        type = DateType.DAY;
                        YEAR = year;
                        MONTH = ++monthOfYear;
                        DAY = dayOfMonth;
                    }
                }
        );

        year = (Button) dialog.findViewById(R.id.year);

        year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = DateType.YEAR;
                monthPicker.setVisibility(View.INVISIBLE);
                yearMonthPicker.setVisibility(View.INVISIBLE);
                datePicker.setVisibility(View.INVISIBLE);
                weekPicker.setVisibility(View.INVISIBLE);
                text.setVisibility(View.VISIBLE);

                text.setText("Избери година:");

                text.setVisibility(View.VISIBLE);
                ViewAnimations.fadeAnimateZoom(text);
                yearPicker.setVisibility(View.VISIBLE);
                ViewAnimations.fadeAnimateZoom(yearPicker);

                yearPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                        YEAR = newVal;
                    }
                });
            }
        });

        month = (Button) dialog.findViewById(R.id.month);

        month.setOnClickListener(new View.OnClickListener()

                                 {
                                     @Override
                                     public void onClick(View v) {
                                         type = DateType.MONTH;
                                         yearPicker.setVisibility(View.INVISIBLE);
                                         datePicker.setVisibility(View.INVISIBLE);
                                         weekPicker.setVisibility(View.INVISIBLE);
                                         text.setVisibility(View.VISIBLE);

                                         text.setText("Избери месец:");

                                         text.setVisibility(View.VISIBLE);
                                         ViewAnimations.fadeAnimateZoom(text);

                                         monthPicker.setVisibility(View.VISIBLE);
                                         ViewAnimations.fadeAnimateZoom(monthPicker);

                                         yearMonthPicker.setVisibility(View.VISIBLE);
                                         ViewAnimations.fadeAnimateZoom(yearMonthPicker);

                                         MONTH = calendar.get(Calendar.MONTH);
                                         YEAR = calendar.get(Calendar.YEAR);

                                         monthPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                                             @Override
                                             public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                                                 MONTH = newVal;
                                             }
                                         });

                                         yearMonthPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                                             @Override
                                             public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                                                 YEAR = newVal;
                                             }
                                         });
                                     }
                                 }
        );

        week = (Button) dialog.findViewById(R.id.week);

        week.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = DateType.WEEK;
                yearMonthPicker.setVisibility(View.INVISIBLE);
                monthPicker.setVisibility(View.INVISIBLE);
                yearPicker.setVisibility(View.INVISIBLE);
                datePicker.setVisibility(View.INVISIBLE);
                text.setVisibility(View.VISIBLE);

                text.setText("Избери ден на някоя седмица:");

                text.setVisibility(View.VISIBLE);
                ViewAnimations.fadeAnimateZoom(text);

                weekPicker.setVisibility(View.VISIBLE);
                weekPicker.setVisibility(View.VISIBLE);
                ViewAnimations.fadeAnimateZoom(weekPicker);

                YEAR = calendar.get(Calendar.YEAR);
                MONTH = calendar.get(Calendar.MONTH);
                DAY = calendar.get(Calendar.DAY_OF_MONTH);

                weekPicker.init(YEAR, MONTH, DAY, new DatePicker.OnDateChangedListener() {
                            @Override
                            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                type = DateType.WEEK;
                                YEAR = year;
                                MONTH = monthOfYear;
                                DAY = dayOfMonth;
                            }
                        }
                );
            }
        });

        daily = (Button) dialog.findViewById(R.id.daily);

        daily.setOnClickListener(new View.OnClickListener()

                                 {
                                     @Override
                                     public void onClick(View v) {
                                         type = DateType.DAY;
                                         yearMonthPicker.setVisibility(View.INVISIBLE);
                                         monthPicker.setVisibility(View.INVISIBLE);
                                         yearPicker.setVisibility(View.INVISIBLE);
                                         weekPicker.setVisibility(View.INVISIBLE);
                                         text.setVisibility(View.INVISIBLE);

                                         ViewAnimations.fadeAnimateZoom(datePicker);
                                     }
                                 }

        );

        AlertDialog.Builder aDBuilder = new AlertDialog.Builder(context)
                .setView(dialog)
                .setPositiveButton("Вземи", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dateSetlistener.onDateSet(YEAR, MONTH, DAY, type);
                    }
                })
                .setNegativeButton("Откажи", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dateSetlistener.onDateSet(YEAR, MONTH, DAY, DateType.DAILY);
                    }
                });

        return aDBuilder;
    }

    public void showCalendar() {
        alertBuilder.show();
    }
}