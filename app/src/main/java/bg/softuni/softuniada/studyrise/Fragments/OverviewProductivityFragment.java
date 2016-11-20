package bg.softuni.softuniada.studyrise.Fragments;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import bg.softuni.softuniada.studyrise.Adapters.ProgramsAdapter;
import bg.softuni.softuniada.studyrise.Profile;
import bg.softuni.softuniada.studyrise.Program;
import bg.softuni.softuniada.studyrise.R;
import bg.softuni.softuniada.studyrise.SQLite.DBPref;

public class OverviewProductivityFragment extends Fragment implements View.OnClickListener, FragmentLifecycle {

    private Button addProgram;
    private ListView listView;
    private ProgramsAdapter adapter;
    private ArrayList<Program> data;
    public static TextView textView;
    public static Profile profile;
    private Program program;
    private String programId;
    private boolean inOverview = false;

    private TextView profilePoints;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("ProgramProductivity", 0);
        programId = sharedPreferences.getString("program", null);

        if (programId == null) {

            View root = inflater.inflate(R.layout.fragment_add_program, container, false);

            data = new ArrayList<>();

            DBPref pref = new DBPref(getContext());
            Cursor c = pref.getVals("program", null);

            if (c.moveToFirst()) {
                do {
                    Program program = new Program();

                    program.setId((c.getLong(c.getColumnIndex("_id"))));
                    program.setName(c.getString(c.getColumnIndex("programName")));
                    program.setDate(c.getString(c.getColumnIndex("date")));

                    data.add(program);
                } while (c.moveToNext());
            }

            c.close();
            pref.close();

            listView = (ListView) root.findViewById(R.id.list_programs);
            adapter = new ProgramsAdapter(getContext(), R.layout.program_list_item, data, listView);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    program = (Program) listView.getItemAtPosition(position);

                    profile = new Profile();

                    profile.setId(program.getId());

                    profile.setPersonalPoints("0", getContext(), "");
                    profile.setDailyGoals(0 + "");

                    SharedPreferences preferences = getContext().getSharedPreferences("ProgramProductivity", 0);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("program", program.getId() + "");
                    editor.commit();


                    Fragment f;
                    f = new ProductivityFragment();
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.container_body, f);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft.commit();
                }
            });

            textView = (TextView) root.findViewById(R.id.textAddProgram);

            addProgram = (Button) root.findViewById(R.id.addProgramForProductivity);
            addProgram.setOnClickListener(this);

            return root;
        } else {
            View root = inflater.inflate(R.layout.fragment_overview_program, container, false);

            inOverview = true;

            profilePoints = (TextView) root.findViewById(R.id.pointsProfile);

            SharedPreferences sharedPreferencesPoints = getContext().getSharedPreferences("ProfilePoints", 0);
            String points = sharedPreferencesPoints.getString("points", null);

            if (profile == null) {
                profile = new Profile();
                profile.setId(Long.parseLong(programId));
                profile.setPersonalPoints("0", getContext(), "");
            }

            if (points != null) {
                profilePoints.setText(points.toString());
                profile.setPersonalPoints(points, getContext(), "");
            } else {

                if (profile.getPersonalPoints() == null) {
                    profilePoints.setText("" + profile.getId());
                    profile.setPersonalPoints("0", getContext(), "");
                } else
                    profilePoints.setText(profile.getPersonalPoints());
            }

            if (profile.getPersonalPoints().toString().length() > 2) {
                profilePoints.setTextSize(40);
            } else if (profile.getPersonalPoints().toString().length() > 3) {
                profilePoints.setTextSize(25);
            }

            return root;
        }
    }

    @Override
    public void onClick(View v) {
        LayoutInflater li = LayoutInflater.from(getContext());
        View dialogView = li.inflate(R.layout.dialog_add_program, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());

        alertDialogBuilder.setView(dialogView);

        final EditText addProgram;
        addProgram = (EditText) dialogView.findViewById(R.id.nameOfProgram);

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Добави",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                //get current date time with Date()
                                Date date = new Date();
                                Program program = new Program();
                                program.setId(data.size());
                                program.setName(addProgram.getText().toString());
                                program.setDate(dateFormat.format(date));

                                DBPref pref = new DBPref(getContext());
                                pref.addRecord("program", program.getName(), program.getDate(), (long) data.size());
                                pref.close();

                                data.add(program);

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

        if (addProgram.getText().toString().isEmpty()) {
            addProgram.setError("Въведете име на програмата!");
            alertDialog.getButton(
                    AlertDialog.BUTTON_POSITIVE).setEnabled(false);
        }

        addProgram.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    addProgram.setError("Въведете име на програмата!");
                    alertDialog.getButton(
                            AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                } else
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
            }
        });

    }

    @Override
    public void onResumeFragment() {
        if (profilePoints != null)
            if (profile.getPersonalPoints() == null) {
                profilePoints.setText("0");
                profile.setPersonalPoints("0", getContext(), "init");
            } else
                profilePoints.setText(profile.getPersonalPoints());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        if (inOverview)
            inflater.inflate(R.menu.menu_program, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.programs) {
            inOverview = false;
            SharedPreferences sharedPreferences = getContext().getSharedPreferences("ProgramProductivity", 0);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.commit();

            Fragment f;
            f = new ProductivityFragment();
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container_body, f);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.commit();
        }

        return super.onOptionsItemSelected(item);
    }

    public OverviewProductivityFragment() {

        setHasOptionsMenu(true);
    }
}