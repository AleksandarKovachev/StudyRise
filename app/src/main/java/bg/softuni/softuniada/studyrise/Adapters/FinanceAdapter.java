package bg.softuni.softuniada.studyrise.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import bg.softuni.softuniada.studyrise.Activities.ScreenSlidePagerActivity;
import bg.softuni.softuniada.studyrise.Finance;
import bg.softuni.softuniada.studyrise.Fragments.Price;
import bg.softuni.softuniada.studyrise.Fragments.Priohod;
import bg.softuni.softuniada.studyrise.Fragments.Razhod;
import bg.softuni.softuniada.studyrise.R;

public class FinanceAdapter extends RecyclerView.Adapter<FinanceAdapter.ViewHolder> {

    private List<Finance> listFinances;
    private Context context;

    private static String type;

    private static String category;

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
                    ScreenSlidePagerActivity.mPagerAdapter.addFragment(new Priohod());
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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            nameTextView = (TextView) itemView.findViewById(R.id.finance_name);
        }
    }
}
