package bg.softuni.softuniada.studyrise.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import bg.softuni.softuniada.studyrise.Activities.ScreenSlidePagerActivity;
import bg.softuni.softuniada.studyrise.R;

public class OverviewFinances extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.finance_overview_fragment, container, false);

        Button button = (Button) root.findViewById(R.id.financesButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ScreenSlidePagerActivity.class);
                startActivity(intent);
            }
        });

        return root;
    }
}
