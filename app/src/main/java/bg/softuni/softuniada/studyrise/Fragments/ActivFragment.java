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

import bg.softuni.softuniada.studyrise.Activ;
import bg.softuni.softuniada.studyrise.Adapters.ActivAdapter;
import bg.softuni.softuniada.studyrise.R;
import bg.softuni.softuniada.studyrise.SQLite.DBPref;

public class ActivFragment extends Fragment {

    private ExpandableListView listView;
    private ActivAdapter adapter;
    private ArrayList<Activ> data;
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
        Cursor c = pref.getVals("activ", programId + "");

        if (c.moveToFirst()) {
            do {
                Activ activ = new Activ();
                activ.setTitle(c.getString(c.getColumnIndex("activTitle")));
                activ.setPoints(c.getString(c.getColumnIndex("points")));
                data.add(activ);
            } while (c.moveToNext());
        }

        c.close();
        pref.close();


        listView = (ExpandableListView) root.findViewById(R.id.list_activ);
        adapter = new ActivAdapter(getContext(), R.layout.activ_list_item, data, listView, programId + "");
        listView.setAdapter(adapter);
        listView.clearFocus();
        listView.animate();

        return root;
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Activ activ) {
        data.add(activ);
        adapter.notifyDataSetChanged();
        listView.invalidate();
    }
}
