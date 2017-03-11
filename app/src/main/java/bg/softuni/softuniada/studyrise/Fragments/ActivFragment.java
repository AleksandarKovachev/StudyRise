package bg.softuni.softuniada.studyrise.Fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;

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

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.setFirstDayOfWeek(Calendar.MONDAY);

        String date = calendar.get(Calendar.DAY_OF_MONTH) + "." + (calendar.get(Calendar.MONTH) + 1) + "." + calendar.get(Calendar.YEAR);

        DBPref pref = new DBPref(getContext());

        Cursor cursor = pref.getVals(DBConstants.TODO_ACTIV_TABLE, date);
        if (cursor.moveToFirst()) {
            do {
                TodoActiv activ = new TodoActiv();
                activ.setTitle(cursor.getString(cursor.getColumnIndex("name")));
                activ.setPoints(cursor.getString(cursor.getColumnIndex("points")));
                activ.setDate(cursor.getString(cursor.getColumnIndex("date")));
                activ.setPriority(cursor.getString(cursor.getColumnIndex("priority")));
                data.add(activ);
            } while (cursor.moveToNext());
        }

        Cursor c = pref.getVals("activ", programId + "");

        if (c.moveToFirst()) {
            do {
                String title = c.getString(c.getColumnIndex("activTitle"));
                if (!containsElement(title)) {
                    TodoActiv activ = new TodoActiv();
                    activ.setTitle(title);
                    activ.setPoints(c.getString(c.getColumnIndex("points")));
                    data.add(activ);
                }
            } while (c.moveToNext());
        }

        c.close();
        pref.close();

        ImageView imageView = (ImageView) root.findViewById(R.id.todo_done);

        listView = (ExpandableListView) root.findViewById(R.id.list_activ);
        adapter = new ActivAdapter(getContext(), R.layout.activ_list_item, data, listView, programId + "", imageView);
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
