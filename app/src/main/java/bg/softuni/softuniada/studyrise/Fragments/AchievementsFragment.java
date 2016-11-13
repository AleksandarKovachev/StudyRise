package bg.softuni.softuniada.studyrise.Fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

import bg.softuni.softuniada.studyrise.Achievement;
import bg.softuni.softuniada.studyrise.Adapters.AchievementAdapter;
import bg.softuni.softuniada.studyrise.R;

public class AchievementsFragment extends Fragment implements View.OnClickListener {

    private ListView listView;
    private AchievementAdapter adapter;
    private Button inputActiv;
    private ArrayList<Achievement> data;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_achievement_tab, container, false);

        data = new ArrayList<>();

        listView = (ListView) root.findViewById(R.id.list_achievement);
        adapter = new AchievementAdapter(getContext(), R.layout.activ_list_item, data);
        listView.setAdapter(adapter);

        inputActiv = (Button) root.findViewById(R.id.addNewAchievement);

        inputActiv.setOnClickListener(this);


        return root;
    }

    @Override
    public void onClick(View v) {
        LayoutInflater li = LayoutInflater.from(getContext());
        View dialogView = li.inflate(R.layout.dialog_input_activ_achievement, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());

        alertDialogBuilder.setView(dialogView);

        final EditText activInput, pointsInput;
        activInput = (EditText) dialogView.findViewById(R.id.inputActiv);
        pointsInput = (EditText) dialogView.findViewById(R.id.inputActivPoints);

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Добави",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Achievement achievement = new Achievement();
                                achievement.setTitle(activInput.getText().toString());
                                achievement.setPoints(pointsInput.getText().toString());
                                data.add(achievement);
                                adapter.notifyDataSetChanged();
                                listView.invalidate();
                            }
                        })
                .setNegativeButton("Отмени",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();


    }
}
