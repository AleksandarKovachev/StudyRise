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
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import bg.softuni.softuniada.studyrise.Adapters.ProgramsAdapter;
import bg.softuni.softuniada.studyrise.Program;
import bg.softuni.softuniada.studyrise.R;

public class OverviewProductivityFragment extends Fragment implements View.OnClickListener {

    private Button addProgram;
    private ListView listView;
    private ProgramsAdapter adapter;
    private ArrayList<Program> data;
    public static TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add_program, container, false);

        data = new ArrayList<Program>();

        listView = (ListView) root.findViewById(R.id.list_programs);
        adapter = new ProgramsAdapter(getContext(), R.layout.program_list_item, data);
        listView.setAdapter(adapter);

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

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Добави",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                //get current date time with Date()
                                Date date = new Date();
                                Program program = new Program();
                                program.setName(addProgram.getText().toString());
                                program.setDate(dateFormat.format(date));
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

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }
}