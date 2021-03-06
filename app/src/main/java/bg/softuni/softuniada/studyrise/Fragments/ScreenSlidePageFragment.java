package bg.softuni.softuniada.studyrise.Fragments;

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

public class ScreenSlidePageFragment extends Fragment {

    private RecyclerView recyclerView;

    private ArrayList<Finance> listFinances;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_screen_slide_page, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.finance_recycler_view);

        listFinances = new ArrayList<>();

        Finance finance = new Finance();
        finance.setName("Приход");
        listFinances.add(finance);
        Finance f = new Finance();
        f.setName("Разход");
        listFinances.add(f);

        FinanceAdapter financeAdapter = new FinanceAdapter(getContext(), listFinances);
        recyclerView.setAdapter(financeAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        return rootView;
    }
}
