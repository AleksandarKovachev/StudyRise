package bg.softuni.softuniada.studyrise.Fragments;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import bg.softuni.softuniada.studyrise.Adapters.ProgramsAdapter;
import bg.softuni.softuniada.studyrise.Program;
import bg.softuni.softuniada.studyrise.R;
import bg.softuni.softuniada.studyrise.SQLite.DBPref;

public class OverviewProductivityFragment extends Fragment implements View.OnClickListener {

    private Button addProgram;
    private ListView listView;
    private ProgramsAdapter adapter;
    private ArrayList<Program> data;
    public static TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("ProgramProductivity", 0);
        String programName = sharedPreferences.getString("program", null);

        if (programName == null) {

            View root = inflater.inflate(R.layout.fragment_add_program, container, false);

            data = new ArrayList<>();

            DBPref pref = new DBPref(getContext());
            Cursor c = pref.getVals("program");

            if (c.moveToFirst()) {
                do {
                    Program program = new Program();
                    program.setName(c.getString(c.getColumnIndex("programName")));
                    program.setDate(c.getString(c.getColumnIndex("date")));

                    data.add(program);
                } while (c.moveToNext());
            }

            c.close();
            pref.close();

            listView = (ListView) root.findViewById(R.id.list_programs);
            adapter = new ProgramsAdapter(getContext(), R.layout.program_list_item, data);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Program program = (Program) listView.getItemAtPosition(position);

                    SharedPreferences preferences = getContext().getSharedPreferences("ProgramProductivity", 0);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("program", program.getName().toString());
                    editor.commit();

                    Fragment f;
                    f = new ProductivityFragment();
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.container_body,f);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft.commit();
                }
            });

            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    showPopupMenu(listView, position);
                    return false;

                }
            });

            textView = (TextView) root.findViewById(R.id.textAddProgram);

            addProgram = (Button) root.findViewById(R.id.addProgramForProductivity);
            addProgram.setOnClickListener(this);

            return root;
        } else {
            View root = inflater.inflate(R.layout.fragment_overview_program, container, false);

            TextView textView = (TextView) root.findViewById(R.id.programNameOverview);

            textView.setText(programName);

            Button button = (Button) root.findViewById(R.id.back);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences sharedPreferences = getContext().getSharedPreferences("ProgramProductivity", 0);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.commit();

                    Fragment f;
                    f = new ProductivityFragment();
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.container_body,f);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft.commit();
                }
            });

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
                                program.setName(addProgram.getText().toString());
                                program.setDate(dateFormat.format(date));
                                data.add(program);

                                DBPref pref = new DBPref(getContext());
                                pref.addRecord("program", program.getName(), program.getDate());
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

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    private void showPopupMenu(View view, int position) {
        // inflate menu
        PopupMenu popup = new PopupMenu(getContext(), view, Gravity.CENTER);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_edit_delete, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener(position));
        popup.show();
    }

    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
        int position;

        public MyMenuItemClickListener(int position) {
            this.position = position;
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_edit:
                    Toast.makeText(getContext(), "Промени", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.delete:
                    Program program = (Program) listView.getItemAtPosition(position);
                    DBPref pref = new DBPref(getContext());
                    pref.deleteRecord("program", program.getName(), program.getDate());
                    pref.close();
                    data.remove(position);
                    adapter.notifyDataSetChanged();
                    listView.invalidate();
                    return true;
                default:
            }
            return false;
        }
    }
}