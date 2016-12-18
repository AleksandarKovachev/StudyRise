package bg.softuni.softuniada.studyrise.Adapters;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import bg.softuni.softuniada.studyrise.History;
import bg.softuni.softuniada.studyrise.R;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private List<History> list;
    private Context context;
    private RecyclerView recyclerView;

    public HistoryAdapter(Context context, List<History> list, RecyclerView recyclerView) {
        this.list = list;
        this.context = context;
        this.recyclerView = recyclerView;
    }

    private Context getContext() {
        return context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.history_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final History history = list.get(position);

        holder.historyName.setText(history.getName());
        holder.historyDate.setText(history.getDate());
        holder.historyPoints.setText(history.getPoints());

        holder.menu.setTag(new Integer(position));
        holder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v, Integer.parseInt(v.getTag().toString()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView historyName;
        public TextView historyDate;
        public TextView historyPoints;
        public ImageView menu;

        public ViewHolder(View itemView) {
            super(itemView);

            historyName = (TextView) itemView.findViewById(R.id.history_name);
            historyDate = (TextView) itemView.findViewById(R.id.history_date);
            historyPoints = (TextView) itemView.findViewById(R.id.history_points);
            menu = (ImageView) itemView.findViewById(R.id.history_menu);
        }
    }

    private void showPopupMenu(View view, int position) {
        PopupMenu popup = new PopupMenu(context, view, Gravity.CENTER);
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
                    Toast.makeText(context, "Промени", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.delete:
//                    History history = list.get(position);
//                    DBPref pref = new DBPref(context);
//                    pref.deleteRecord("profit_expense", "type", "name", history.getType(), finance.getName());
//                    pref.close();
//                    list.remove(position);
//                    notifyDataSetChanged();
//                    recyclerView.invalidate();
                    return true;
                default:
            }
            return false;
        }
    }
}
