package bg.softuni.softuniada.studyrise.Fragments;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import bg.softuni.softuniada.studyrise.Adapters.ProgramsAdapter;
import bg.softuni.softuniada.studyrise.Program;
import bg.softuni.softuniada.studyrise.R;
import bg.softuni.softuniada.studyrise.SQLite.DBPref;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class Programs extends Fragment implements View.OnClickListener {

    private RecyclerView recyclerView;
    private ProgramsAdapter adapter;
    private ArrayList<Program> data;
    public static TextView textView;
    private Button addProgram;
    private boolean hasAProgram = false;
    private boolean hasAType = false;
    private String programType;
    private String[] array;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_add_program, container, false);

        array = getResources().getStringArray(R.array.programs);

        data = new ArrayList<>();

        DBPref pref = new DBPref(getContext());
        Cursor c = pref.getVals("program", null);

        if (c.moveToFirst()) {
            do {
                Program program = new Program();

                program.setId((c.getLong(c.getColumnIndex("_id"))));
                program.setName(c.getString(c.getColumnIndex("programName")));
                program.setDate(c.getString(c.getColumnIndex("date")));
                program.setProgram_type(c.getString(c.getColumnIndex("program_type")));

                data.add(program);
            } while (c.moveToNext());
        }

        c.close();
        pref.close();

        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();

        recyclerView = (RecyclerView) root.findViewById(R.id.list_programs);
        adapter = new ProgramsAdapter(getContext(), data, recyclerView, fragmentTransaction);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new SlideInUpAnimator());
        recyclerView.setNestedScrollingEnabled(false);

        textView = (TextView) root.findViewById(R.id.textAddProgram);

        addProgram = (Button) root.findViewById(R.id.addProgramForProductivity);
        addProgram.setOnClickListener(this);

        return root;

    }

    @Override
    public void onClick(View v) {
        LayoutInflater li = LayoutInflater.from(getContext());
        View dialogView = li.inflate(R.layout.dialog_add_program, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());

        alertDialogBuilder.setView(dialogView);

        final EditText addProgram;
        addProgram = (EditText) dialogView.findViewById(R.id.nameOfProgram);

        Spinner spinner = (Spinner) dialogView.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapterSpinner = ArrayAdapter.createFromResource(getContext(), R.array.programs, android.R.layout.select_dialog_item);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_expandable_list_item_1);
        spinner.setAdapter(adapterSpinner);

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Добави",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                Date date = new Date();
                                Program program = new Program();
                                program.setId(data.size());
                                program.setName(addProgram.getText().toString());
                                program.setDate(dateFormat.format(date));
                                program.setProgram_type(programType);

                                DBPref pref = new DBPref(getContext());
                                pref.addRecord((long) data.size(), "program", program.getName(), program.getDate(), program.getProgram_type());
                                pref.close();

                                data.add(program);

                                adapter.notifyDataSetChanged();
                                recyclerView.invalidate();
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


        addProgram.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                hasAProgram = false;
                hasAType = false;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    addProgram.setError("Въведете име на програмата!");
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                } else if (!(s.toString().isEmpty()) && (hasAType == true)) {
                    hasAProgram = true;
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                } else if (!(s.toString().isEmpty())) {
                    hasAProgram = true;
                }
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    hasAType = false;
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                } else if (position != 0 && hasAProgram) {
                    hasAType = true;
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                    programType = array[position].toString();
                } else if (position != 0) {
                    hasAType = true;
                    programType = array[position].toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                hasAType = false;
            }
        });
    }
}
