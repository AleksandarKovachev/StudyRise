package bg.softuni.softuniada.studyrise.Fragments;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import bg.softuni.softuniada.studyrise.Achievement;
import bg.softuni.softuniada.studyrise.Adapters.AchievementAdapter;
import bg.softuni.softuniada.studyrise.R;
import bg.softuni.softuniada.studyrise.SQLite.DBPref;

import static bg.softuni.softuniada.studyrise.Fragments.OverviewProductivityFragment.profile;

public class AchievementsFragment extends Fragment implements View.OnClickListener, FragmentLifecycle {

    private ListView listView;
    private AchievementAdapter adapter;
    private Button inputActiv;
    private ArrayList<Achievement> data;
    private long programId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_achievement_tab, container, false);

        programId = OverviewProductivityFragment.profile.getId();

        data = new ArrayList<>();

        DBPref pref = new DBPref(getContext());
        Cursor c = pref.getVals("achievement", programId + "");

        if (c.moveToFirst()) {
            do {
                Achievement achievement = new Achievement();
                achievement.setTitle(c.getString(c.getColumnIndex("achievement")));
                achievement.setPoints(c.getString(c.getColumnIndex("points")));
                data.add(achievement);
            } while (c.moveToNext());
        }

        c.close();
        pref.close();

        listView = (ListView) root.findViewById(R.id.list_achievement);
        adapter = new AchievementAdapter(getContext(), R.layout.activ_list_item, data, listView);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                profile.setPersonalPoints(data.get(position).getPoints(), getContext(), "achievement");

            }
        });

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

        final EditText achievementInput, pointsInput;
        achievementInput = (EditText) dialogView.findViewById(R.id.inputActiv);
        pointsInput = (EditText) dialogView.findViewById(R.id.inputActivPoints);

        TextView textDialog = (TextView) dialogView.findViewById(R.id.textDialog);
        textDialog.setText("Въведи награда: ");

        TextView pointsDialog = (TextView) dialogView.findViewById(R.id.pointsDialog);
        pointsDialog.setText("Въведи точки за наградата: ");

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Добави",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Achievement achievement = new Achievement();
                                achievement.setTitle(achievementInput.getText().toString());
                                achievement.setPoints(pointsInput.getText().toString());
                                data.add(achievement);

                                DBPref pref = new DBPref(getContext());
                                pref.addRecord("achievement", achievement.getTitle(), achievement.getPoints(), programId);
                                pref.close();

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

        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);

        if (achievementInput.getText().toString().isEmpty()) {
            achievementInput.setError("Въведете име на наградата!");
            alertDialog.getButton(
                    AlertDialog.BUTTON_POSITIVE).setEnabled(false);
        }

        achievementInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    achievementInput.setError("Въведете име на наградата!");
                    alertDialog.getButton(
                            AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                }
            }
        });

        pointsInput.addTextChangedListener(new TextWatcher() {
                                               @Override
                                               public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                               }

                                               @Override
                                               public void onTextChanged(CharSequence s, int start, int before, int count) {

                                               }

                                               @Override
                                               public void afterTextChanged(Editable s) {
                                                   if (TextUtils.isEmpty(s)) {
                                                       alertDialog.getButton(
                                                               AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                                                       pointsInput.setError("Въведете точки за награта!");
                                                   } else if (!isParsable(s.toString())) {
                                                       pointsInput.setError("Само числа!");
                                                   } else if (isParsable(s.toString())) {
                                                       alertDialog.getButton(
                                                               AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                                                   }
                                               }
                                           }
        );
    }

    public static boolean isParsable(String input) {
        boolean parsable = true;
        try {
            Integer.parseInt(input);
        } catch (NumberFormatException e) {
            parsable = false;
        }
        return parsable;
    }

    @Override
    public void onResumeFragment() {
    }
}
