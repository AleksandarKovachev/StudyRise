package bg.softuni.softuniada.studyrise.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import bg.softuni.softuniada.studyrise.R;

public class FinancesProgram extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_finances_program, container, false);

        return root;
    }
}
