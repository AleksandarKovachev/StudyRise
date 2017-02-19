package bg.softuni.softuniada.studyrise.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import bg.softuni.softuniada.studyrise.Adapters.TodoListAdapter;
import bg.softuni.softuniada.studyrise.R;
import bg.softuni.softuniada.studyrise.Todo;

public class TodoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);

        SharedPreferences sharedPreferences = getSharedPreferences("Program", 0);
        int programId = Integer.parseInt(sharedPreferences.getString("program", null));

        List<Todo> list = new ArrayList<>();
        list.add(new Todo());
        list.add(new Todo());

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list_todo_recycler_view);
        TodoListAdapter adapter = new TodoListAdapter(this, list, programId);
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
    }

}
