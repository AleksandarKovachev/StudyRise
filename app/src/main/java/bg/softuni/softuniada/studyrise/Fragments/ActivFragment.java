package bg.softuni.softuniada.studyrise.Fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import bg.softuni.softuniada.studyrise.Adapters.ActivAdapter;
import bg.softuni.softuniada.studyrise.R;
import bg.softuni.softuniada.studyrise.SQLite.DBConstants;
import bg.softuni.softuniada.studyrise.SQLite.DBPref;
import bg.softuni.softuniada.studyrise.TodoActiv;

public class ActivFragment extends Fragment {

    private ExpandableListView listView;
    private ActivAdapter adapter;
    private ArrayList<TodoActiv> data;
    private long programId;

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_activ_tab, container, false);

        programId = OverviewProductivityFragment.profile.getId();

        data = new ArrayList<>();

        DBPref pref = new DBPref(getContext());

        Cursor cursor = pref.getRawQuery("SELECT activTitle, points, todoId, todo.name, todo.date, todo.priority FROM " + DBConstants.ACTIV_TABLE +
                " JOIN " + DBConstants.TODO_TABLE + " WHERE activ.todoId = todo._id", null);
        if (cursor.moveToFirst()) {
            do {
                TodoActiv activ = new TodoActiv();
                activ.setTitle(cursor.getString(cursor.getColumnIndex("activTitle")));
                activ.setPoints(cursor.getString(cursor.getColumnIndex("points")));
                activ.setName(cursor.getString(cursor.getColumnIndex("name")));
                activ.setDate(cursor.getString(cursor.getColumnIndex("date")));
                activ.setPriority(cursor.getString(cursor.getColumnIndex("priority")));
                data.add(activ);
            } while (cursor.moveToNext());
        }

        Cursor c = pref.getVals("activ", programId + "");

        if (c.moveToFirst()) {
            do {
                if (!containsElement(c.getString(c.getColumnIndex("activTitle")))) {
                    TodoActiv activ = new TodoActiv();
                    activ.setTitle(c.getString(c.getColumnIndex("activTitle")));
                    activ.setPoints(c.getString(c.getColumnIndex("points")));
                    activ.setTodoId(c.getLong(c.getColumnIndex("todoId")));
                    data.add(activ);
                }
            } while (c.moveToNext());
        }

        c.close();
        pref.close();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.setFirstDayOfWeek(Calendar.MONDAY);

        listView = (ExpandableListView) root.findViewById(R.id.list_activ);
        adapter = new ActivAdapter(getContext(), R.layout.activ_list_item, data, listView, calendar, programId + "");
        listView.setAdapter(adapter);
        listView.clearFocus();
        listView.animate();

        return root;
    }

    private boolean containsElement(String title) {
        for (TodoActiv activ : data) {
            if (activ.getTitle().equals(title)) {
                return true;
            }
        }
        return false;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(TodoActiv activ) {
        data.add(activ);
        adapter.notifyDataSetChanged();
        listView.invalidate();
    }
}
