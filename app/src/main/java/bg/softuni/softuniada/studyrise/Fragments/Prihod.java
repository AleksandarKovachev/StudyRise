package bg.softuni.softuniada.studyrise.Fragments;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import bg.softuni.softuniada.studyrise.Adapters.FinanceAdapter;
import bg.softuni.softuniada.studyrise.Finance;
import bg.softuni.softuniada.studyrise.Helper.SimpleItemTouchHelperCallback;
import bg.softuni.softuniada.studyrise.R;
import bg.softuni.softuniada.studyrise.SQLite.DBPref;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class Prihod extends Fragment implements View.OnClickListener {

    private RecyclerView recyclerView;

    private ArrayList<Finance> listFinances;

    private FinanceAdapter financeAdapter;

    private ItemTouchHelper itemTouchHelper;

    private Button button;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_screen_slide_page, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.finance_recycler_view);

        button = (Button) rootView.findViewById(R.id.add_items_finance);
        button.setVisibility(View.VISIBLE);
        button.setOnClickListener(this);

        listFinances = new ArrayList<>();

        DBPref pref = new DBPref(getContext());
        Cursor c = pref.getVals("finance", "Приход");

        if (c.moveToFirst()) {
            do {
                Finance finance = new Finance();
                finance.setName(c.getString(c.getColumnIndex("name")));
                listFinances.add(finance);
            } while (c.moveToNext());
        }

        c.close();
        pref.close();

        financeAdapter = new FinanceAdapter(getContext(), listFinances);
        recyclerView.setAdapter(financeAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new SlideInUpAnimator());

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(financeAdapter);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        LayoutInflater li = LayoutInflater.from(getContext());
        View dialogView = li.inflate(R.layout.dialog_add_finance_item, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());

        alertDialogBuilder.setView(dialogView);

        final EditText addFinanceItem;
        addFinanceItem = (EditText) dialogView.findViewById(R.id.finance_item_name);

        TextView textView = (TextView) dialogView.findViewById(R.id.dialog_text_finance);
        textView.setText("Добавете нов вид приход:");

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Добави",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Finance finance = new Finance();
                                finance.setType("Приход");
                                finance.setName(addFinanceItem.getText().toString());

                                DBPref pref = new DBPref(getContext());
                                pref.addRecord("finance", finance.getType(), finance.getName(), null, null);
                                pref.close();

                                listFinances.add(finance);

                                financeAdapter.notifyDataSetChanged();
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

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);


        addFinanceItem.addTextChangedListener(new TextWatcher() {
                                                  @Override
                                                  public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                                  }

                                                  @Override
                                                  public void onTextChanged(CharSequence s, int start, int before, int count) {

                                                  }

                                                  @Override
                                                  public void afterTextChanged(Editable s) {
                                                      if (s.toString().isEmpty()) {
                                                          addFinanceItem.setError("Въведете име на програмата!");
                                                          alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                                                      } else
                                                          alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                                                  }
                                              }
        );
    }
}
