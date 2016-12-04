package bg.softuni.softuniada.studyrise.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import bg.softuni.softuniada.studyrise.Finance;
import bg.softuni.softuniada.studyrise.R;

public class ProfitAdapter extends RecyclerView.Adapter<ProfitAdapter.ViewHolder> {

    private List<Finance> listFinances;
    private Context context;

    public ProfitAdapter(Context context, List<Finance> listFinances) {
        this.listFinances = listFinances;
        this.context = context;
    }

    private Context getContext() {
        return context;
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
        final Finance finance = listFinances.get(position);

        holder.nameTextView.setText(finance.getName());
        holder.valueTextView.setText(finance.getValue() + "");


    }

    @Override
    public int getItemCount() {
        return listFinances.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView valueTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            nameTextView = (TextView) itemView.findViewById(R.id.name_finance);
            valueTextView = (TextView) itemView.findViewById(R.id.value_finance);
        }
    }
}