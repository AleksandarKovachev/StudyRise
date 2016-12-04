package bg.softuni.softuniada.studyrise.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import bg.softuni.softuniada.studyrise.Adapters.ProfitAdapter;
import bg.softuni.softuniada.studyrise.Finance;
import bg.softuni.softuniada.studyrise.R;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class Expense extends Fragment {

    private RecyclerView recyclerView;

    private ArrayList<Finance> listFinances;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.expense_fragment, container, false);

        recyclerView = (RecyclerView) root.findViewById(R.id.expense_recycler_view);

        listFinances = new ArrayList<>();

        ProfitAdapter profitAdapter = new ProfitAdapter(getContext(), listFinances);
        recyclerView.setAdapter(profitAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new SlideInUpAnimator());

        return root;
    }
}
