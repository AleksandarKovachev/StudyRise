package bg.softuni.softuniada.studyrise.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.List;

import bg.softuni.softuniada.studyrise.Activities.FinanceActivity;
import bg.softuni.softuniada.studyrise.Activities.ProductivityActivity;
import bg.softuni.softuniada.studyrise.Fragments.Programs;
import bg.softuni.softuniada.studyrise.Profile;
import bg.softuni.softuniada.studyrise.Program;
import bg.softuni.softuniada.studyrise.R;
import bg.softuni.softuniada.studyrise.SQLite.DBPref;

public class ProgramsAdapter extends RecyclerView.Adapter<ProgramsAdapter.ProgramViewHolder> {

    private Context context;
    private List<Program> data;
    private RecyclerView recyclerView;
    private FragmentTransaction fragmentTransaction;

    public ProgramsAdapter(Context context, List<Program> objects, RecyclerView recyclerView, FragmentTransaction fragmentTransaction) {
        this.context = context;
        data = objects;
        this.recyclerView = recyclerView;
        this.fragmentTransaction = fragmentTransaction;
    }

    @Override
    public ProgramViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.program_list_item, parent, false);
        return new ProgramViewHolder(itemView);
    }

    public class ProgramViewHolder extends RecyclerView.ViewHolder {
        public TextView name, date, type;
        public ImageView menu;
        public CardView cardView;

        public ProgramViewHolder(View view) {
            super(view);

            name = (TextView) view.findViewById(R.id.programName);
            date = (TextView) view.findViewById(R.id.programDate);
            type = (TextView) view.findViewById(R.id.programType);

            cardView = (CardView) view.findViewById(R.id.program_card);

            menu = (ImageView) view.findViewById(R.id.program_menu);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onBindViewHolder(final ProgramViewHolder holder, final int position) {
        YoYo.with(Techniques.FadeInUp).playOn(holder.cardView);

        if (data.size() != 0)
            Programs.textView.setText("Добави нова програма");

        holder.name.setText(data.get(position).getName());
        holder.date.setText(data.get(position).getDate());
        holder.type.setText(data.get(position).getProgram_type());

        holder.menu.setTag(new Integer(position));
        holder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v, Integer.parseInt(v.getTag().toString()));
            }
        });

        if (position % 2 == 0) {
            holder.name.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
        } else {
            holder.name.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View v) {
                                                   if (holder.type.getText().equals("Продуктивност")) {
                                                       Program program = data.get(position);

                                                       Profile profile = new Profile();

                                                       profile.setId(program.getId());

                                                       profile.setPersonalPoints("0", context, "");
                                                       profile.setDailyGoals(0 + "");

                                                       SharedPreferences preferences = context.getSharedPreferences("Program", 0);
                                                       SharedPreferences.Editor editor = preferences.edit();
                                                       editor.putString("program", program.getId() + "");
                                                       editor.commit();

                                                       String[] array = context.getResources().getStringArray(R.array.programs);

                                                       if (program.getProgram_type().equals(array[1])) {
                                                           Intent intent = new Intent(context, ProductivityActivity.class);
                                                           context.startActivity(intent);
                                                       } else {
                                                           Intent intent = new Intent(context, FinanceActivity.class);
                                                           context.startActivity(intent);
                                                       }
                                                   }
                                               }
                                           }
        );
    }

    private void showPopupMenu(View view, int position) {
        // inflate menu
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
                    dialogChangeItem(data.get(position));
                    return true;
                case R.id.delete:
                    Program program = data.get(position);
                    DBPref pref = new DBPref(context);
                    pref.deleteRecord("program", "programName", "date", program.getName(), program.getDate());
                    pref.close();
                    data.remove(position);
                    notifyDataSetChanged();
                    recyclerView.invalidate();
                    return true;
                default:
            }
            return false;
        }

    }

    private void dialogChangeItem(final Program item) {
        LayoutInflater li = LayoutInflater.from(context);
        View dialogView = li.inflate(R.layout.dialog_change_program, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        alertDialogBuilder.setView(dialogView);

        final EditText title;
        title = (EditText) dialogView.findViewById(R.id.changeProgramName);

        title.setText(item.getName());

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Промени",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                DBPref pref = new DBPref(context);
                                pref.changeItem("program", "programName", title.getText().toString(), "date", item.getDate(), "programName", item.getName());
                                pref.close();
                                data.get(data.indexOf(item)).setName(title.getText().toString());
                                notifyDataSetChanged();
                                recyclerView.invalidate();
                            }
                        })
                .setNegativeButton("Отмени",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
