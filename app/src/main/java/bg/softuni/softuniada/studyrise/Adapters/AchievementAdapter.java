package bg.softuni.softuniada.studyrise.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.PopupMenu;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import bg.softuni.softuniada.studyrise.Achievement;
import bg.softuni.softuniada.studyrise.R;
import bg.softuni.softuniada.studyrise.SQLite.DBPref;

import static bg.softuni.softuniada.studyrise.Fragments.OverviewProductivityFragment.profile;

public class AchievementAdapter extends BaseExpandableListAdapter {

    private Context context;
    private int layoutId;
    private List<Achievement> data;
    private String number;
    private String finalNumber;
    private ExpandableListView listView;

    public AchievementAdapter(Context context, int resource, List<Achievement> objects, ExpandableListView listView) {
        this.context = context;
        layoutId = resource;
        data = objects;
        this.listView = listView;
    }


    private void showPopupMenu(View view, int position) {
        // inflate menu
        PopupMenu popup = new PopupMenu(context, view, Gravity.CENTER);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_edit_delete, popup.getMenu());
        popup.setOnMenuItemClickListener(new AchievementAdapter.MyMenuItemClickListener(position));
        popup.show();
    }

    @Override
    public int getGroupCount() {
        return this.data.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.data.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return data.get(groupPosition).getPoints();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();

        View row = inflater.inflate(layoutId, parent, false);

        TextView title = (TextView) row.findViewById(R.id.activTitle);
        TextView points = (TextView) row.findViewById(R.id.activPoints);
        ImageView menu = (ImageView) row.findViewById(R.id.finance_menu);

        title.setText(data.get(groupPosition).getTitle());
        points.setText(data.get(groupPosition).getPoints());

        menu.setTag(new Integer(groupPosition));
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v, Integer.parseInt(v.getTag().toString()));
            }
        });

        if (groupPosition % 2 == 0) {
            title.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
            points.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
        } else {
            title.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
            points.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
        }

        return row;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        final int gp, chp;

        gp = groupPosition;
        chp = childPosition;

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.chield_list_item, null);
        }

        final Button runPoints = (Button) convertView.findViewById(R.id.runPoints);
        final EditText num = (EditText) convertView.findViewById(R.id.quantity);

        num.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                num.setText("");
                return false;
            }
        });

        final TextView finalTextView;

        finalTextView = (TextView) convertView.findViewById(R.id.finalPoints);

        number = "1";

        num.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()) {
                    number = s.toString();
                    finalTextView.setText(" = " + ((Integer.parseInt(number.toString())) * (Integer.parseInt(getChild(gp, chp).toString()))) + "");
                }
            }
        });

        if (number == null || number.isEmpty()) {
            finalTextView.setText(" = " + ((Integer.parseInt(getChild(gp, chp).toString()))) + "");
        } else {
            finalTextView.setText(" = " + ((Integer.parseInt(number.toString())) * (Integer.parseInt(getChild(gp, chp).toString()))) + "");
        }

        runPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (number == null)
                    number = "1";
                finalNumber = (Integer.parseInt(number.toString())) * (Integer.parseInt(getChild(gp, chp).toString())) + "";
                profile.setPersonalPoints(finalNumber, context, "achievement");

            }
        });

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
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
                    Toast.makeText(context, "Промени", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.delete:
                    Achievement achievement = (Achievement) listView.getItemAtPosition(position);
                    DBPref pref = new DBPref(context);
                    pref.deleteRecord("achievement", "achievement", "points", achievement.getTitle(), achievement.getPoints());
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
