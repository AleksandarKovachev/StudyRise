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

import bg.softuni.softuniada.studyrise.Activ;
import bg.softuni.softuniada.studyrise.R;
import bg.softuni.softuniada.studyrise.SQLite.DBPref;

public class ActivAdapter extends ArrayAdapter<Activ> {

    private Context context;
    private int layoutId;
    private List<Activ> data;
    private ListView listView;

    public ActivAdapter(Context context, int resource, List<Activ> objects, ListView listView) {
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

        TextView title = (TextView) row.findViewById(R.id.activTitle);
        TextView points = (TextView) row.findViewById(R.id.activPoints);
        ImageView menu = (ImageView) row.findViewById(R.id.activ_menu);

        title.setText(data.get(position).getTitle());
        points.setText(data.get(position).getPoints());

        menu.setTag(new Integer(position));
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v, Integer.parseInt(v.getTag().toString()));
            }
        });

        if (position % 2 == 0) {
            title.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
            points.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
        } else {
            title.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
            points.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
        }

        return row;
    }

    private void showPopupMenu(View view, int position) {
        // inflate menu
        PopupMenu popup = new PopupMenu(getContext(), view, Gravity.CENTER);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_edit_delete, popup.getMenu());
        popup.setOnMenuItemClickListener(new ActivAdapter.MyMenuItemClickListener(position));
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
                    Activ activ = (Activ) listView.getItemAtPosition(position);
                    DBPref pref = new DBPref(getContext());
                    pref.deleteRecord("activ", "activTitle", "points", activ.getTitle(), activ.getPoints());
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
