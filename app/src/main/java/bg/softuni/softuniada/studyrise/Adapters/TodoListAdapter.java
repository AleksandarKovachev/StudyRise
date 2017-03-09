package bg.softuni.softuniada.studyrise.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import bg.softuni.softuniada.studyrise.Activ;
import bg.softuni.softuniada.studyrise.DatePickerDialog;
import bg.softuni.softuniada.studyrise.R;
import bg.softuni.softuniada.studyrise.RecyclerItemClickListener;
import bg.softuni.softuniada.studyrise.SQLite.DBPref;
import bg.softuni.softuniada.studyrise.Todo;

public class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.ViewHolder> {

    private List<Todo> todoList;
    private Context context;
    private int programId;
    private int pos = -1;

    public TodoListAdapter(Context context, List<Todo> todoList, int programId) {
        this.todoList = todoList;
        this.context = context;
        this.programId = programId;
    }

    @Override
    public TodoListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View financeView = inflater.inflate(R.layout.dialog_todo_list_item, parent, false);

        TodoListAdapter.ViewHolder viewHolder = new TodoListAdapter.ViewHolder(financeView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TodoListAdapter.ViewHolder holder, final int position) {
        final EditText inputTodo = holder.inputTodo;
        final EditText inputDate = holder.inputDate;

        final String[] array = context.getResources()
                .getStringArray(R.array.todo_priority);

        final Spinner spinner = holder.spinner;
        spinner.setAdapter(new SpinnerAdapterFinance(context, R.layout.finance_row_spinner, array));

        inputTodo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDialog(inputTodo);
                }
            }
        });

        inputDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    DatePickerDialog calendar;

                    calendar = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(int Year, int Month, int Day) {
                            inputDate.setText(Day + "." + Month + "." + Year);
                            inputDate.invalidate();
                        }
                    });
                    calendar.showCalendar();
                }
            }
        });

        todoList.get(position).setPriority(spinner.getSelectedItem().toString());

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                todoList.get(position).setPriority(array[pos]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        inputTodo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()) {
                    todoList.get(position).setName(s.toString());
                }
            }
        });

        inputDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty())
                    todoList.get(position).setDate(s.toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public EditText inputDate, inputTodo;
        public Spinner spinner;

        public ViewHolder(View itemView) {
            super(itemView);

            inputDate = (EditText) itemView.findViewById(R.id.input_date);
            inputTodo = (EditText) itemView.findViewById(R.id.input_todo);
            spinner = (Spinner) itemView.findViewById(R.id.spinner_todo);
        }
    }

    public void showDialog(final EditText inputTodo) {
        LayoutInflater li = LayoutInflater.from(context);
        View dialogView = li.inflate(R.layout.dialog_show_todo_list, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        alertDialogBuilder.setView(dialogView);

        final List<Activ> data = new ArrayList<>();

        DBPref pref = new DBPref(context);
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

        RecyclerView recyclerView = (RecyclerView) dialogView.findViewById(R.id.dialog_recycler_view_todo);
        DialogListAdapter adapter = new DialogListAdapter(context, data);
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Добави",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if (pos != -1) {
                                    inputTodo.setText(data.get(pos).getTitle());
                                    inputTodo.invalidate();
                                }
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

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(context, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                pos = position;
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
            }
        }));
    }
}