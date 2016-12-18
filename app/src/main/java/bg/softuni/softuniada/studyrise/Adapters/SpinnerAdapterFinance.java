package bg.softuni.softuniada.studyrise.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import bg.softuni.softuniada.studyrise.R;

public class SpinnerAdapterFinance extends ArrayAdapter<String> {

    String[] type;

    public SpinnerAdapterFinance(Context context, int textViewResourceId,
                                 String[] objects) {
        super(context, textViewResourceId, objects);
        type = objects;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View row = inflater.inflate(R.layout.finance_row_spinner, parent, false);
        TextView label = (TextView) row.findViewById(R.id.typeOfChart);
        label.setText(type[position]);

        ImageView icon = (ImageView) row.findViewById(R.id.icon);

        if (type[position].equals("Дневен")) {
            icon.setImageResource(R.drawable.ic_day);
        } else if (type[position].equals("Седмичен")) {
            icon.setImageResource(R.drawable.ic_week);
        } else if (type[position].equals("Месечен")) {
            icon.setImageResource(R.drawable.ic_month);
        } else if (type[position].equals("Годишен")) {
            icon.setImageResource(R.drawable.ic_overview_spinner);
        }

        return row;
    }
}