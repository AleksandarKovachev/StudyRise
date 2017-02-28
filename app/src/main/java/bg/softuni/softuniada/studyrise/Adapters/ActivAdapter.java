package bg.softuni.softuniada.studyrise.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import bg.softuni.softuniada.studyrise.Activ;
import bg.softuni.softuniada.studyrise.Points;
import bg.softuni.softuniada.studyrise.R;
import bg.softuni.softuniada.studyrise.SQLite.DBPref;
import bg.softuni.softuniada.studyrise.TodoActiv;

import static bg.softuni.softuniada.studyrise.Fragments.OverviewProductivityFragment.profile;

public class ActivAdapter extends BaseExpandableListAdapter {

    private Context context;
    private int layoutId;
    private List<TodoActiv> data;
    private ExpandableListView listView;
    private String number;
    private String finalNumber;
    private String programId;
    private Calendar calendar;

    public ActivAdapter(Context context, int resource, List<TodoActiv> objects, ExpandableListView listView, Calendar calendar, String programId) {
        this.context = context;
        layoutId = resource;
        data = objects;
        this.listView = listView;
        this.programId = programId;
        this.calendar = calendar;
    }

    private void showPopupMenu(View view, int position) {
        PopupMenu popup = new PopupMenu(context, view, Gravity.CENTER);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_edit_delete, popup.getMenu());
        popup.setOnMenuItemClickListener(new ActivAdapter.MyMenuItemClickListener(position));
        popup.show();
    }

    @Override
    public int getGroupCount() {
        return this.data.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.data.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return data.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();

        View row = inflater.inflate(layoutId, parent, false);

        String[] array = context.getResources()
                .getStringArray(R.array.todo_priority);

        TextView title = (TextView) row.findViewById(R.id.activTitle);
        TextView points = (TextView) row.findViewById(R.id.activPoints);
        ImageView menu = (ImageView) row.findViewById(R.id.activ_menu);
        final ImageView priority = (ImageView) row.findViewById(R.id.activ_priority);

        if (data.get(groupPosition).getDate() != null) {
            String[] date = data.get(groupPosition).getDate().split("\\.");

            int day = Integer.parseInt(date[0]);
            int month = Integer.parseInt(date[1]);
            int year = Integer.parseInt(date[2]);

            int priorityNumber = 0;

            if (day == (calendar.get(Calendar.DAY_OF_MONTH)) && month == (calendar.get(Calendar.MONTH) + 1) && year == (calendar.get(Calendar.YEAR))) {
                priorityNumber = getPriorityPosition(data.get(groupPosition).getPriority(), array);
                Drawable drawable = null;
                switch (priorityNumber) {
                    case 0:
                        drawable = context.getResources().getDrawable(R.drawable.ic_priority_one);
                        break;
                    case 1:
                        drawable = context.getResources().getDrawable(R.drawable.ic_priority_two);
                        break;
                    case 2:
                        drawable = context.getResources().getDrawable(R.drawable.ic_priority_three);
                        break;
                    case 3:
                        drawable = context.getResources().getDrawable(R.drawable.ic_priority_four);
                        break;
                    case 4:
                        drawable = context.getResources().getDrawable(R.drawable.ic_priority_five);
                        break;
                }
                priority.setImageDrawable(drawable);
                priority.setVisibility(View.VISIBLE);
            }
            final String priorityValue = array[priorityNumber];
            priority.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    displayPopupWindow(priority, priorityValue);
                }
            });
        }

        title.setText(data.get(groupPosition).getTitle());
        points.setText(data.get(groupPosition).getPoints());

        menu.setTag(new Integer(groupPosition));
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v, Integer.parseInt(v.getTag().toString()));
            }
        });

        if (groupPosition % 2 == 0) {
            title.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
            points.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
        } else {
            title.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
            points.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
        }
        return row;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        final View view = convertView;
        final int gp, chp;
        gp = groupPosition;
        chp = childPosition;

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.chield_list_item, null);
        }

        final Activ activ = (Activ) getChild(gp, chp);

        Button runPoints = (Button) convertView.findViewById(R.id.runPoints);

        final EditText num;
        final TextView finalTextView;
        num = (EditText) convertView.findViewById(R.id.quantity);

        num.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                num.setText("");
                return false;
            }
        });

        finalTextView = (TextView) convertView.findViewById(R.id.finalPoints);

        final String points = 1 + "";
        number = points;

        num.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()) {
                    number = s.toString();
                    finalTextView.setText(" = " + ((Integer.parseInt(number.toString())) * (Integer.parseInt(activ.getPoints().toString()))) + "");
                }
            }
        });


        if (num.getText().toString() == null || num.getText().toString().isEmpty()) {
            number = points;
            finalTextView.setText(" = " + ((Integer.parseInt(activ.getPoints().toString()))) + "");
        } else {
            number = num.getText().toString();
            finalTextView.setText(" = " + ((Integer.parseInt(number.toString())) * (Integer.parseInt(activ.getPoints().toString()))) + "");
        }

        runPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (num.getText().toString() != null && !num.getText().toString().isEmpty())
                    number = num.getText().toString();
                else
                    number = points;
                Snackbar.make(view, "ИЗПЪЛНИ СЕ: " + activ.getTitle(), Snackbar.LENGTH_LONG).show();
                finalNumber = (Integer.parseInt(number.toString())) * (Integer.parseInt(activ.getPoints().toString())) + "";
                profile.setPersonalPoints(finalNumber, context, "activ");
                EventBus.getDefault().post(new Points(Integer.parseInt(finalNumber)));

                DBPref pref = new DBPref(context);
                String datePattern = "HH:mm:ss EEE dd MMM yyyy";
                SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern);
                String date = dateFormat.format(new Date(System.currentTimeMillis()));
                pref.addRecord(Long.parseLong(programId), "history", "Activ", activ.getTitle(), date, finalNumber);
                pref.close();
            }
        });

        return convertView;
    }

    private void displayPopupWindow(View anchorView, String text) {
        PopupWindow popup = new PopupWindow();
        LayoutInflater li = LayoutInflater.from(context);
        View layout = li.inflate(R.layout.popup_content, null);
        TextView textView = (TextView) layout.findViewById(R.id.popup_text);
        textView.setText(text);
        popup.setContentView(layout);
        popup.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popup.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        popup.setOutsideTouchable(true);
        popup.setFocusable(true);
        popup.setBackgroundDrawable(new BitmapDrawable());
        popup.showAsDropDown(anchorView);
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
        int position;

        public MyMenuItemClickListener(int position) {
            this.position = position;
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_edit:
                    dialogChangeItem((Activ) listView.getItemAtPosition(position));
                    return true;
                case R.id.delete:
                    Activ activ = (Activ) listView.getItemAtPosition(position);
                    DBPref pref = new DBPref(context);
                    pref.deleteRecord("activ", "activTitle", "points", activ.getTitle(), activ.getPoints());
                    pref.close();
                    data.remove(position);
                    notifyDataSetChanged();
                    listView.invalidate();
                    return true;
                default:
            }
            return false;
        }
    }

    private void dialogChangeItem(final Activ item) {
        LayoutInflater li = LayoutInflater.from(context);
        View dialogView = li.inflate(R.layout.dialog_change_item, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        alertDialogBuilder.setView(dialogView);

        final EditText title, points;
        title = (EditText) dialogView.findViewById(R.id.changeTitle);
        points = (EditText) dialogView.findViewById(R.id.changePoints);

        title.setText(item.getTitle());
        points.setText(item.getPoints());

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Промени",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                DBPref pref = new DBPref(context);
                                pref.changeItem("activ", "activTitle", title.getText().toString(), "points", points.getText().toString(), "activTitle", item.getTitle());
                                pref.close();
                                data.get(data.indexOf(item)).setTitle(title.getText().toString());
                                data.get(data.indexOf(item)).setPoints(points.getText().toString());
                                notifyDataSetChanged();
                                listView.invalidate();
                            }
                        })
                .setNegativeButton("Отмени",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private int getPriorityPosition(String priority, String[] array) {
        return Arrays.asList(array).indexOf(priority);
    }
}
