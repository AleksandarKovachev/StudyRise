package bg.softuni.softuniada.studyrise.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import bg.softuni.softuniada.studyrise.Fragments.OverviewProductivityFragment;
import bg.softuni.softuniada.studyrise.Program;
import bg.softuni.softuniada.studyrise.R;

public class ProgramsAdapter extends ArrayAdapter<Program> {

    private Context context;
    private int layoutId;
    private List<Program> data;

    public ProgramsAdapter(Context context, int resource, List<Program> objects) {
        super(context, resource, objects);
        this.context = context;
        layoutId = resource;
        data = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();

        View row = inflater.inflate(layoutId, parent, false);

        TextView name = (TextView) row.findViewById(R.id.programName);
        TextView date = (TextView) row.findViewById(R.id.programDate);

        if (data.size() != 0)
            OverviewProductivityFragment.textView.setText("Добави нова програма");

        name.setText(data.get(position).getName());
        date.setText(data.get(position).getDate());

        if (position % 2 == 0) {
            name.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
            date.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
        } else {
            name.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
            date.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
        }

        return row;
    }
}
