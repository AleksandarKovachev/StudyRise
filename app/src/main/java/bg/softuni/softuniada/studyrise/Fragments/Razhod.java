package bg.softuni.softuniada.studyrise.Fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import bg.softuni.softuniada.studyrise.Adapters.FinanceAdapter;
import bg.softuni.softuniada.studyrise.Finance;
import bg.softuni.softuniada.studyrise.R;
import bg.softuni.softuniada.studyrise.SQLite.DBPref;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class Razhod extends Fragment {

    private RecyclerView recyclerView;

    private ArrayList<Finance> listFinances;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_screen_slide_page, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.finance_recycler_view);

        listFinances = new ArrayList<>();


        DBPref pref = new DBPref(getContext());
        Cursor c = pref.getVals("finance", "Разход");

        if (c.moveToFirst()) {
            do {
                Finance finance = new Finance();
                finance.setName(c.getString(c.getColumnIndex("name")));
                listFinances.add(finance);
            } while (c.moveToNext());
        }

        c.close();
        pref.close();
        FinanceAdapter financeAdapter = new FinanceAdapter(getContext(), listFinances);
        recyclerView.setAdapter(financeAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new SlideInUpAnimator()
        );

        return rootView;
    }

}
