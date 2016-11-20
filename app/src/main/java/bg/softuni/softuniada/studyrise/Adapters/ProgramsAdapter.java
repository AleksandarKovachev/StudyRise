package bg.softuni.softuniada.studyrise.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.PopupMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import bg.softuni.softuniada.studyrise.Fragments.OverviewProductivityFragment;
import bg.softuni.softuniada.studyrise.Program;
import bg.softuni.softuniada.studyrise.R;
import bg.softuni.softuniada.studyrise.SQLite.DBPref;

public class ProgramsAdapter extends ArrayAdapter<Program> {

    private Context context;
    private int layoutId;
    private List<Program> data;
    private ListView listView;

    public ProgramsAdapter(Context context, int resource, List<Program> objects, ListView listView) {
        super(context, resource, objects);
        this.context = context;
        layoutId = resource;
        data = objects;
        this.listView = listView;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();

        View row = inflater.inflate(layoutId, parent, false);


        TextView name = (TextView) row.findViewById(R.id.programName);
        TextView date = (TextView) row.findViewById(R.id.programDate);
        ImageView menu = (ImageView) row.findViewById(R.id.program_menu);

        if (data.size() != 0)
            OverviewProductivityFragment.textView.setText("Добави нова програма");

        name.setText(data.get(position).getName());
        date.setText(data.get(position).getDate());

        menu.setTag(new Integer(position));
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v, Integer.parseInt(v.getTag().toString()));
            }
        });

        if (position % 2 == 0) {
            name.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
            date.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
        } else {
            name.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
            date.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
        }


        return row;
    }

    private void showPopupMenu(View view, int position) {
        // inflate menu
        PopupMenu popup = new PopupMenu(getContext(), view, Gravity.CENTER);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_edit_delete, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener(position));
        popup.show();
        System.out.println(position);
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
                    pref.deleteRecord("program", "programName", "date", program.getName(), program.getDate());
                    pref.close();
                    data.remove(position);
                    notifyDataSetChanged();
                    listView.invalidate();
                    return true;
                default:
            }
            return false;
        }
    }
}
