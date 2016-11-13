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

import bg.softuni.softuniada.studyrise.Achievement;
import bg.softuni.softuniada.studyrise.R;

public class AchievementAdapter extends ArrayAdapter<Achievement> {

    private Context context;
    private int layoutId;
    private List<Achievement> data;

    public AchievementAdapter(Context context, int resource, List<Achievement> objects) {
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

        TextView title = (TextView) row.findViewById(R.id.activTitle);
        TextView points = (TextView) row.findViewById(R.id.activPoints);

        title.setText(data.get(position).getTitle());
        points.setText(data.get(position).getPoints());

        if (position % 2 == 0) {
            title.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
            points.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
        } else {
            title.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
            points.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
        }

        return row;
    }
}
