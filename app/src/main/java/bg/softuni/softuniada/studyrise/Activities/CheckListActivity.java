package bg.softuni.softuniada.studyrise.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import bg.softuni.softuniada.studyrise.Activ;
import bg.softuni.softuniada.studyrise.Adapters.DialogListAdapter;
import bg.softuni.softuniada.studyrise.DatePickerDialog;
import bg.softuni.softuniada.studyrise.Model.LineItem;
import bg.softuni.softuniada.studyrise.R;
import bg.softuni.softuniada.studyrise.RecyclerItemClickListener;
import bg.softuni.softuniada.studyrise.RecylerAdapter.BaseViewAdapter;
import bg.softuni.softuniada.studyrise.RecylerAdapter.BindingViewHolder;
import bg.softuni.softuniada.studyrise.RecylerAdapter.MultiTypeAdapter;
import bg.softuni.softuniada.studyrise.SQLite.DBConstants;
import bg.softuni.softuniada.studyrise.SQLite.DBPref;
import bg.softuni.softuniada.studyrise.SwipeDragLayout;
import bg.softuni.softuniada.studyrise.Todo;
import bg.softuni.softuniada.studyrise.databinding.ChildStatusItemBinding;

public class CheckListActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private static final int VIEW_TYPE_ITEM = 1;
    private static final int VIEW_TYPE_TITLE = 2;
    private Context mContext;
    private int programId;
    private int position = -1;
    private long foreignId;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private MultiTypeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.acitivity_list);

        SharedPreferences sharedPreferences = getSharedPreferences("Program", 0);
        programId = Integer.parseInt(sharedPreferences.getString("program", null));

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_todo);
        swipeRefreshLayout.setOnRefreshListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.todo_recycler_view);

        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.todoFabButton);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogToAddItem();
            }
        });

        adapter = new MultiTypeAdapter(this);
        adapter.addViewTypeToLayoutMap(VIEW_TYPE_TITLE, R.layout.group_status_item);
        adapter.addViewTypeToLayoutMap(VIEW_TYPE_ITEM, R.layout.child_status_item);
        recyclerView.setAdapter(adapter);

        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.top = dip2px(mContext, 10);
            }
        });

        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);
                                        setData();
                                        swipeRefreshLayout.setRefreshing(false);
                                    }
                                }
        );

        adapter.setPresenter(new ItemPresenter());
        adapter.setDecorator(new ItemDecoration());
    }

    @Override
    public void onRefresh() {
        adapter.clear();
        setData();
        swipeRefreshLayout.setRefreshing(false);
    }

    private void setData(){

        adapter.addAll(takeData(), new MultiTypeAdapter.CustomMultiViewTyper() {

            @Override
            public int getViewType(Object item, int pos) {
                if (item instanceof LineItem) {
                    if (((LineItem) item).isTitle()) {
                        return VIEW_TYPE_TITLE;
                    } else {
                        return VIEW_TYPE_ITEM;
                    }
                }
                throw new RuntimeException("unExcepted item type");
            }
        });
    }

    private List<LineItem> takeData() {
        final List<Todo> data = new ArrayList<>();

        DBPref pref = new DBPref(this);
        Cursor c = pref.getVals(DBConstants.TODO_TABLE);

        if (c.moveToFirst()) {
            do {
                Todo todo = new Todo();
                todo.setName(c.getString(c.getColumnIndex("name")));
                todo.setDate(c.getString(c.getColumnIndex("date")));
                todo.setPriority(c.getString(c.getColumnIndex("priority")));
                data.add(todo);
            } while (c.moveToNext());
        }

        c.close();
        pref.close();

        List<LineItem> items = new ArrayList<>();

        for (Todo todo : data) {
            LineItem title = new LineItem(todo.getDate(), true);
            int index = containsElement(items, title);
            if (index != -1) {
                items.add(index + 1, new LineItem(todo.getName(), false));
            } else {
                items.add(title);
                items.add(new LineItem(todo.getName(), false));
            }
        }
        return items;
    }

    private int containsElement(List<LineItem> list, LineItem item) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getContent().equals(item.getContent())) {
                return i;
            }
        }
        return -1;
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public class ItemPresenter implements BaseViewAdapter.Presenter {

        public void onStarClick(LineItem item) {
            Toast.makeText(mContext, "star", Toast.LENGTH_SHORT).show();
        }

        public void onDeleteClick(LineItem item) {
            Toast.makeText(mContext, "delete", Toast.LENGTH_SHORT).show();
        }

    }

    public class ItemDecoration implements BaseViewAdapter.Decorator {

        @Override
        public void decorator(BindingViewHolder holder, final int position, int viewType) {
            if (viewType == VIEW_TYPE_ITEM) {
                ChildStatusItemBinding binding = (ChildStatusItemBinding) holder.getBinding();
                binding.swipLayout.addListener(new SwipeDragLayout.SwipeListener() {
                    @Override
                    public void onUpdate(SwipeDragLayout layout, float offset) {
                        Log.d("offset", "onUpdate() called with offset = [" + offset + "]");
                    }

                    @Override
                    public void onOpened(SwipeDragLayout layout) {
                        Toast.makeText(mContext, "onOpened", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onClosed(SwipeDragLayout layout) {
                        Toast.makeText(mContext, "onClosed", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onClick(SwipeDragLayout layout) {
                        Toast.makeText(mContext, takeData().get(position).getContent(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    private void showDialogToAddItem() {
        LayoutInflater li = LayoutInflater.from(this);
        View dialogView = li.inflate(R.layout.dialog_todo_list_item, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setView(dialogView);

        final EditText inputDate, inputTodo;

        inputDate = (EditText) dialogView.findViewById(R.id.input_date);
        inputTodo = (EditText) dialogView.findViewById(R.id.input_todo);

        final Spinner spinner = (Spinner) dialogView.findViewById(R.id.spinner_todo);
        ArrayAdapter<CharSequence> adapterSpinner = ArrayAdapter.createFromResource(this, R.array.todo_priority, android.R.layout.select_dialog_item);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_expandable_list_item_1);
        spinner.setAdapter(adapterSpinner);

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

                    calendar = new DatePickerDialog(CheckListActivity.this, new DatePickerDialog.OnDateSetListener() {
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

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Добави",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Todo todo = new Todo();
                                todo.setDate(inputDate.getText().toString());
                                todo.setName(inputTodo.getText().toString());
                                todo.setPriority(spinner.getSelectedItem().toString());

                                DBPref pref = new DBPref(getApplicationContext());
                                pref.addRecord(null, DBConstants.TODO_TABLE, todo.getName(), todo.getDate(), todo.getPriority());

                                Cursor cursor = pref.getVals(DBConstants.TODO_TABLE);
                                cursor.moveToLast();
                                long _id = cursor.getLong(cursor.getColumnIndex("_id"));

                                cursor.close();

                                pref.execSql("UPDATE " + DBConstants.ACTIV_TABLE + " SET todoId = " + _id + " WHERE _id = " + foreignId + ";");
                                pref.close();
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

        inputDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty() && !inputTodo.getText().toString().isEmpty()) {
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                }
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
                if (!s.toString().isEmpty() && !inputDate.getText().toString().isEmpty()) {
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                }
            }
        });
    }

    public void showDialog(final EditText inputTodo) {
        LayoutInflater li = LayoutInflater.from(this);
        View dialogView = li.inflate(R.layout.dialog_show_todo_list, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setView(dialogView);

        final List<Activ> data = new ArrayList<>();

        DBPref pref = new DBPref(this);
        Cursor c = pref.getVals("activ", programId + "");

        if (c.moveToFirst()) {
            do {
                Activ activ = new Activ();
                activ.setId(c.getLong(c.getColumnIndex("_id")));
                activ.setTitle(c.getString(c.getColumnIndex("activTitle")));
                activ.setPoints(c.getString(c.getColumnIndex("points")));
                data.add(activ);
            } while (c.moveToNext());
        }

        c.close();
        pref.close();

        RecyclerView recyclerView = (RecyclerView) dialogView.findViewById(R.id.dialog_recycler_view);
        DialogListAdapter adapter = new DialogListAdapter(this, data);
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Добави",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if (position != -1) {
                                    inputTodo.setText(data.get(position).getTitle());
                                    foreignId = data.get(position).getId();
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

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                position = pos;
            }
        }));
    }
}
