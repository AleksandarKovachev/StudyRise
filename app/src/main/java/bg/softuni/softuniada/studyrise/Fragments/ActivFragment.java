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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;

import java.util.ArrayList;

import bg.softuni.softuniada.studyrise.Activ;
import bg.softuni.softuniada.studyrise.Adapters.ActivAdapter;
import bg.softuni.softuniada.studyrise.FragmentLifecycle;
import bg.softuni.softuniada.studyrise.R;
import bg.softuni.softuniada.studyrise.SQLite.DBPref;

public class ActivFragment extends Fragment implements View.OnClickListener, FragmentLifecycle {

    private ExpandableListView listView;
    private ActivAdapter adapter;
    private Button inputActiv;
    private ArrayList<Activ> data;
    private long programId;

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

        inputActiv = (Button) root.findViewById(R.id.addNewActiv);

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
                                Activ activ = new Activ();
                                activ.setTitle(activInput.getText().toString());
                                activ.setPoints(pointsInput.getText().toString());
                                data.add(activ);

                                DBPref pref = new DBPref(getContext());
                                pref.addRecord(programId, "activ", activ.getTitle(), activ.getPoints());
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

        if (activInput.getText().toString().isEmpty()) {
            alertDialog.getButton(
                    AlertDialog.BUTTON_POSITIVE).setEnabled(false);
            activInput.setError("Въведете име на актива!");
        }

        activInput.addTextChangedListener(new TextWatcher() {
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
                    activInput.setError("Въведете име на актива!");
                }
            }
        });

        pointsInput.addTextChangedListener(new TextWatcher() {
                                               @Override
                                               public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                                   if (TextUtils.isEmpty(s)) {
                                                       alertDialog.getButton(
                                                               AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                                                       pointsInput.setError("Въведете точки за актива!");
                                                   }
                                               }

                                               @Override
                                               public void onTextChanged(CharSequence s, int start, int before, int count) {

                                               }

                                               @Override
                                               public void afterTextChanged(Editable s) {
                                                   if (TextUtils.isEmpty(s)) {
                                                       alertDialog.getButton(
                                                               AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                                                       pointsInput.setError("Въведете точки за актива!");
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
