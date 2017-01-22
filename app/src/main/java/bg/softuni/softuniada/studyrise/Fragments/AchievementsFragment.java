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

import bg.softuni.softuniada.studyrise.Achievement;
import bg.softuni.softuniada.studyrise.Adapters.AchievementAdapter;
import bg.softuni.softuniada.studyrise.R;
import bg.softuni.softuniada.studyrise.SQLite.DBPref;

public class AchievementsFragment extends Fragment {

    private ExpandableListView listView;
    private AchievementAdapter adapter;
    private ArrayList<Achievement> data;
    private long programId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_achievement_tab, container, false);

        programId = OverviewProductivityFragment.profile.getId();

        data = new ArrayList<>();

        DBPref pref = new DBPref(getContext());
        Cursor c = pref.getVals("achievement", programId + "");

        if (c.moveToFirst()) {
            do {
                Achievement achievement = new Achievement();
                achievement.setTitle(c.getString(c.getColumnIndex("achievement")));
                achievement.setPoints(c.getString(c.getColumnIndex("points")));
                data.add(achievement);
            } while (c.moveToNext());
        }

        c.close();
        pref.close();

        listView = (ExpandableListView) root.findViewById(R.id.list_achievement);
        adapter = new AchievementAdapter(getContext(), R.layout.activ_list_item, data, listView, programId + "");
        listView.setAdapter(adapter);
        listView.clearFocus();
        listView.animate();

        return root;
    }

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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Achievement achievement) {
        data.add(achievement);
        adapter.notifyDataSetChanged();
        listView.invalidate();
    }
}
