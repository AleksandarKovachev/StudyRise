package bg.softuni.softuniada.studyrise.Adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import bg.softuni.softuniada.studyrise.Activ;
import bg.softuni.softuniada.studyrise.R;

public class DialogListAdapter extends RecyclerView.Adapter<DialogListAdapter.ViewHolder> {

    private List<Activ> data;
    private Context context;

    public DialogListAdapter(Context context, List<Activ> data) {
        this.data = data;
        this.context = context;
    }

    @Override
    public DialogListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View financeView = inflater.inflate(R.layout.dialog_item_list, parent, false);

        DialogListAdapter.ViewHolder viewHolder = new DialogListAdapter.ViewHolder(financeView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final DialogListAdapter.ViewHolder holder, int position) {
        Activ activ = data.get(position);

        TextView title = holder.title;
        TextView points = holder.points;

        CardView cardView = holder.cardView;

        title.setText(activ.getTitle());
        points.setText(activ.getPoints());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, points;
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.dialog_list_title);
            points = (TextView) itemView.findViewById(R.id.dialog_list_points);
            cardView = (CardView) itemView.findViewById(R.id.todo_item_card);
        }
    }
}