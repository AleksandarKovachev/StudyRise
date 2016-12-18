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

import bg.softuni.softuniada.studyrise.Adapters.ProfitExpenseAdapter;
import bg.softuni.softuniada.studyrise.Finance;
import bg.softuni.softuniada.studyrise.R;
import bg.softuni.softuniada.studyrise.SQLite.DBPref;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class Profit extends Fragment {

    public static RecyclerView recyclerView;

    private ArrayList<Finance> listFinances;

    public static ProfitExpenseAdapter profitAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.profit_fragment, container, false);

        recyclerView = (RecyclerView) root.findViewById(R.id.profit_recycler_view);

        listFinances = new ArrayList<>();

        DBPref pref = new DBPref(getContext());
        Cursor c = pref.getVals("profit_expense", "Приход");

        if (c.moveToFirst()) {
            do {
                Finance finance = new Finance();
                finance.setType(c.getString(c.getColumnIndex("type")));
                finance.setName(c.getString(c.getColumnIndex("name")));
                finance.setValue(c.getDouble(c.getColumnIndex("value")));
                finance.setDate(c.getString(c.getColumnIndex("date")));
                listFinances.add(finance);
            } while (c.moveToNext());
        }

        c.close();
        pref.close();

        profitAdapter = new ProfitExpenseAdapter(getContext(), listFinances, recyclerView);
        recyclerView.setAdapter(profitAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new SlideInUpAnimator());

        return root;
    }
}
