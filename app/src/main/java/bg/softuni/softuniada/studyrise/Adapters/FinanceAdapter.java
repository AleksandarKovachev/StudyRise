package bg.softuni.softuniada.studyrise.Adapters;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import bg.softuni.softuniada.studyrise.Activities.ScreenSlidePagerActivity;
import bg.softuni.softuniada.studyrise.Finance;
import bg.softuni.softuniada.studyrise.Fragments.Price;
import bg.softuni.softuniada.studyrise.Fragments.Prihod;
import bg.softuni.softuniada.studyrise.Fragments.Razhod;
import bg.softuni.softuniada.studyrise.Helper.ItemTouchHelperAdapter;
import bg.softuni.softuniada.studyrise.Helper.ItemTouchHelperViewHolder;
import bg.softuni.softuniada.studyrise.R;
import bg.softuni.softuniada.studyrise.SQLite.DBPref;

public class FinanceAdapter extends RecyclerView.Adapter<FinanceAdapter.ViewHolder> implements ItemTouchHelperAdapter {

    private List<Finance> listFinances;
    private Context context;

    private static String type;

    private static String category;

    private View view;

    public static String getCategory() {
        return category;
    }

    public static String getType() {
        return type;
    }

    public FinanceAdapter(Context context, List<Finance> listFinances) {
        this.listFinances = listFinances;
        this.context = context;
    }

    private Context getContext() {
        return context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View financeView = inflater.inflate(R.layout.finance_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(financeView);
        view = parent;
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Finance finance = listFinances.get(position);

        final TextView textView = holder.nameTextView;
        textView.setText(finance.getName());

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textView.getText().toString().equals("Разход")) {
                    type = textView.getText().toString();
                    ScreenSlidePagerActivity.mPagerAdapter.addFragment(new Razhod());
                    ScreenSlidePagerActivity.mPager.setCurrentItem(ScreenSlidePagerActivity.mPager.getCurrentItem() + 1);
                } else if (textView.getText().toString().equals("Приход")) {
                    type = textView.getText().toString();
                    ScreenSlidePagerActivity.mPagerAdapter.addFragment(new Prihod());
                    ScreenSlidePagerActivity.mPager.setCurrentItem(ScreenSlidePagerActivity.mPager.getCurrentItem() + 1);
                } else {
                    category = textView.getText().toString();
                    ScreenSlidePagerActivity.mPagerAdapter.addFragment(new Price());
                    ScreenSlidePagerActivity.mPager.setCurrentItem(ScreenSlidePagerActivity.mPager.getCurrentItem() + 1);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listFinances.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {
        public TextView nameTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            nameTextView = (TextView) itemView.findViewById(R.id.finance_name);
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(itemView.getResources().getColor(R.color.colorAccentDark));
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
    }

    @Override
    public void onItemDismiss(int position) {
        final Finance finance = listFinances.get(position);
        DBPref pref = new DBPref(context);
        pref.deleteRecord("finance", "type", "name", getType(), finance.getName());
        pref.close();
        listFinances.remove(position);
        notifyItemRemoved(position);
        Snackbar.make(view, "Изтрит артикул.", Snackbar.LENGTH_LONG).
                setAction("ВЪРНИ", new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        DBPref pref = new DBPref(getContext());
                        pref.addRecord(null, "finance", getType(), finance.getName());
                        pref.close();
                        listFinances.add(finance);
                        notifyDataSetChanged();
                    }
                }).show();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        Finance finance = listFinances.remove(fromPosition);
        listFinances.add(toPosition > fromPosition ? toPosition - 1 : toPosition, finance);
        notifyItemMoved(fromPosition, toPosition);
    }
}
