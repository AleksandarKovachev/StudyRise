package bg.softuni.softuniada.studyrise.Fragments;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import bg.softuni.softuniada.studyrise.Activities.MainActivity;
import bg.softuni.softuniada.studyrise.Adapters.HistoryAdapter;
import bg.softuni.softuniada.studyrise.FragmentLifecycle;
import bg.softuni.softuniada.studyrise.History;
import bg.softuni.softuniada.studyrise.Profile;
import bg.softuni.softuniada.studyrise.R;
import bg.softuni.softuniada.studyrise.SQLite.DBPref;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class OverviewProductivityFragment extends Fragment implements FragmentLifecycle {

    public static Profile profile;
    private String programId;
    private boolean inOverview = false;

    private RecyclerView recyclerView;
    private HistoryAdapter adapter;
    private List<History> list;

    private TextView profilePoints;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_overview_program, container, false);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("Program", 0);
        programId = sharedPreferences.getString("program", null);

        inOverview = true;

        profilePoints = (TextView) root.findViewById(R.id.pointsProfile);

        SharedPreferences sharedPreferencesPoints = getContext().getSharedPreferences("ProfilePoints", 0);
        String points = sharedPreferencesPoints.getString("points", null);

        if (profile == null) {
            profile = new Profile();
            profile.setId(Long.parseLong(programId));
            profile.setPersonalPoints("0", getContext(), "");
        }

        profile.setId(Long.parseLong(programId));

        if (points != null) {
            profilePoints.setText(points.toString());
            MainActivity.setText(points.toString());
            profile.setPersonalPoints(points, getContext(), "");
        } else {

            if (profile.getPersonalPoints() == null) {
                profilePoints.setText("" + profile.getId());
                profile.setPersonalPoints("0", getContext(), "");
                MainActivity.setText("" + profile.getId());
            } else
                profilePoints.setText(profile.getPersonalPoints());
        }

        if (profile.getPersonalPoints().toString().length() > 2) {
            profilePoints.setTextSize(40);
        } else if (profile.getPersonalPoints().toString().length() > 3) {
            profilePoints.setTextSize(25);
        }

        recyclerView = (RecyclerView) root.findViewById(R.id.history_recycler_view);
        list = new ArrayList<>();

        DBPref pref = new DBPref(getContext());
        Cursor c = pref.getVals("history", programId);

        if (c.moveToFirst()) {
            do {
                History history = new History();
                history.setType(c.getString(c.getColumnIndex("type")));
                history.setName(c.getString(c.getColumnIndex("name")));
                history.setDate(c.getString(c.getColumnIndex("date")));
                history.setPoints(c.getString(c.getColumnIndex("points")));
                list.add(history);
            } while (c.moveToNext());
        }
        c.close();
        pref.close();

        adapter = new HistoryAdapter(getContext(), list, recyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new SlideInUpAnimator());

        return root;
    }

    @Override
    public void onResumeFragment() {
        if (profilePoints != null)
            if (profile.getPersonalPoints() == null) {
                profilePoints.setText("0");
                profile.setPersonalPoints("0", getContext(), "init");
            } else
                profilePoints.setText(profile.getPersonalPoints());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        if (inOverview)
            inflater.inflate(R.menu.menu_program, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.programs) {
            inOverview = false;
            SharedPreferences sharedPreferences = getContext().getSharedPreferences("Program", 0);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.commit();

            Fragment f;
            f = new Programs();
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container_body, f);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.commit();
        }

        return super.onOptionsItemSelected(item);
    }

    public OverviewProductivityFragment() {
        setHasOptionsMenu(true);
    }
}