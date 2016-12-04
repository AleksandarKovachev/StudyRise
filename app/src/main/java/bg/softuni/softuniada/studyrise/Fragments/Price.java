package bg.softuni.softuniada.studyrise.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import bg.softuni.softuniada.studyrise.Adapters.FinanceAdapter;
import bg.softuni.softuniada.studyrise.R;
import bg.softuni.softuniada.studyrise.SQLite.DBPref;

public class Price extends Fragment {

    private EditText editText;
    private Button button;
    private String value;
    private String programId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.viewpager_price_slide, container, false);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("Program", 0);
        programId = sharedPreferences.getString("program", null);

        editText = (EditText) rootView.findViewById(R.id.value_finance);
        button = (Button) rootView.findViewById(R.id.add_finance);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                value = editText.getText().toString();
                DBPref pref = new DBPref(getContext());
                pref.addRecord("profit_expense", FinanceAdapter.getType(), FinanceAdapter.getCategory(), value, Long.parseLong(programId));
                pref.close();

                getActivity().finish();
                getActivity().overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
            }
        });

        return rootView;
    }

}
