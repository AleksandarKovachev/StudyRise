package bg.softuni.softuniada.studyrise.Adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
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

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.List;

import bg.softuni.softuniada.studyrise.Finance;
import bg.softuni.softuniada.studyrise.R;
import bg.softuni.softuniada.studyrise.SQLite.DBPref;

public class ProfitExpenseAdapter extends RecyclerView.Adapter<ProfitExpenseAdapter.ViewHolder> {

    private List<Finance> listFinances;
    private Context context;
    private RecyclerView recyclerView;

    public ProfitExpenseAdapter(Context context, List<Finance> listFinances, RecyclerView recyclerView) {
        this.listFinances = listFinances;
        this.context = context;
        this.recyclerView = recyclerView;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View financeView = inflater.inflate(R.layout.profit_expense_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(financeView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        YoYo.with(Techniques.FadeInUp).playOn(holder.cardView);
        final Finance finance = listFinances.get(position);

        holder.nameTextView.setText(finance.getName());
        holder.valueTextView.setText(finance.getValue() + "");
        holder.dateTextView.setText(finance.getDate());
        holder.typeTextView.setText(finance.getType());

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
        return listFinances.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView valueTextView;
        public TextView dateTextView;
        public TextView typeTextView;
        public ImageView menu;
        public CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);

            nameTextView = (TextView) itemView.findViewById(R.id.name_finance);
            valueTextView = (TextView) itemView.findViewById(R.id.value_finance);
            dateTextView = (TextView) itemView.findViewById(R.id.date);
            menu = (ImageView) itemView.findViewById(R.id.finance_menu);
            typeTextView = (TextView) itemView.findViewById(R.id.finance_type);
            cardView = (CardView) itemView.findViewById(R.id.finance_card);
        }
    }

    private void showPopupMenu(View view, int position) {
        PopupMenu popup = new PopupMenu(context, view, Gravity.CENTER);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_edit_delete, popup.getMenu());
        popup.setOnMenuItemClickListener(new ProfitExpenseAdapter.MyMenuItemClickListener(position));
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
                    Finance finance = listFinances.get(position);
                    DBPref pref = new DBPref(context);
                    pref.deleteRecord("profit_expense", "type", "name", finance.getType(), finance.getName());
                    pref.close();
                    listFinances.remove(position);
                    notifyDataSetChanged();
                    recyclerView.invalidate();
                    return true;
                default:
            }
            return false;
        }
    }
}