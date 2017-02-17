package bg.softuni.softuniada.studyrise.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionsMenu;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Calendar;

import bg.softuni.softuniada.studyrise.Achievement;
import bg.softuni.softuniada.studyrise.Activ;
import bg.softuni.softuniada.studyrise.Adapters.ViewPagerAdapter;
import bg.softuni.softuniada.studyrise.CalendarDialogBuilder;
import bg.softuni.softuniada.studyrise.DateChart;
import bg.softuni.softuniada.studyrise.DateType;
import bg.softuni.softuniada.studyrise.Fragments.AchievementsFragment;
import bg.softuni.softuniada.studyrise.Fragments.ActivFragment;
import bg.softuni.softuniada.studyrise.Fragments.OverviewProductivityFragment;
import bg.softuni.softuniada.studyrise.Program;
import bg.softuni.softuniada.studyrise.R;
import bg.softuni.softuniada.studyrise.SQLite.DBPref;

public class ProductivityActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ArrayList<Program> data;
    private ViewPagerAdapter adapter;
    private ViewPager viewPager;

    private int[] tabIcons = {
            R.drawable.ic_home,
            R.drawable.ic_activ,
            R.drawable.ic_achievement_tab
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setElevation(0);

        SharedPreferences sharedPreferences = getSharedPreferences("Program", 0);
        String programName = sharedPreferences.getString("program", null);

        data = new ArrayList<>();

        DBPref pref = new DBPref(this);
        Cursor c = pref.getVals("program", null);

        if (c.moveToFirst()) {
            do {
                Program program = new Program();
                program.setId(c.getLong(c.getColumnIndex("_id")));
                program.setName(c.getString(c.getColumnIndex("programName")));
                program.setDate(c.getString(c.getColumnIndex("date")));
                program.setProgram_type(c.getString(c.getColumnIndex("program_type")));

                data.add(program);
            } while (c.moveToNext());
        }

        c.close();
        pref.close();

        if (programName == null || data.size() == 0) {
            Fragment f;
            f = new OverviewProductivityFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container_body, f);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.commit();
        } else {
            setContentView(R.layout.fragment_productivity);

            viewPager = (ViewPager) findViewById(R.id.view_pager);

            setupViewPager(viewPager);

            tabLayout = (TabLayout) findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(viewPager);
            tabLayout.setOnTabSelectedListener(
                    new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {

                        @Override
                        public void onTabSelected(TabLayout.Tab tab) {
                            super.onTabSelected(tab);
                            int tabIconColor = ContextCompat.getColor(getApplicationContext(), R.color.colorAccent);
                            tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
                        }

                        @Override
                        public void onTabUnselected(TabLayout.Tab tab) {
                            super.onTabUnselected(tab);
                            int tabIconColor = ContextCompat.getColor(getApplicationContext(), R.color.white);
                            tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
                        }

                        @Override
                        public void onTabReselected(TabLayout.Tab tab) {
                            super.onTabReselected(tab);
                        }
                    }
            );
            setupTabIcons();

            final FloatingActionsMenu fabMenu = (FloatingActionsMenu) findViewById(R.id.right_labels);

            SharedPreferences id = getSharedPreferences("Program", 0);
            final String programId = id.getString("program", null);

            viewPager.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    fabMenu.collapse();
                    return false;
                }
            });

            fabMenu.getChildAt(3).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog("Activ", Long.parseLong(programId));
                    fabMenu.collapse();
                }
            });

            fabMenu.getChildAt(2).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog("Achievement", Long.parseLong(programId));
                    fabMenu.collapse();
                }
            });

            fabMenu.getChildAt(1).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fabMenu.collapse();
                    showCalendar();
                }
            });

            fabMenu.getChildAt(0).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), TodoActivity.class);
                    startActivity(intent);
                    fabMenu.collapse();
                }
            });
        }
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new OverviewProductivityFragment(), "Преглед");
        adapter.addFragment(new ActivFragment(), "Активи");
        adapter.addFragment(new AchievementsFragment(), "Награди");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_loged, menu);
        return true;
    }


    public void dialog(final String type, final long programId) {
        LayoutInflater li = LayoutInflater.from(this);
        View dialogView = li.inflate(R.layout.dialog_input_activ_achievement, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setView(dialogView);

        final EditText titleInput, pointsInput;
        titleInput = (EditText) dialogView.findViewById(R.id.inputActiv);
        pointsInput = (EditText) dialogView.findViewById(R.id.inputActivPoints);

        if (type.equals("Achievement")) {
            TextView textDialog = (TextView) dialogView.findViewById(R.id.textDialog);
            textDialog.setText("Въведи награда: ");

            TextView pointsDialog = (TextView) dialogView.findViewById(R.id.pointsDialog);
            pointsDialog.setText("Въведи точки за наградата: ");
        }

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Добави",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if (type.equals("Activ")) {
                                    Activ activ = new Activ();
                                    activ.setTitle(titleInput.getText().toString());
                                    activ.setPoints(pointsInput.getText().toString());

                                    EventBus.getDefault().post(activ);

                                    DBPref pref = new DBPref(getApplicationContext());
                                    pref.addRecord(programId, "activ", activ.getTitle(), activ.getPoints());
                                    pref.close();
                                } else {
                                    Achievement achievement = new Achievement();
                                    achievement.setTitle(titleInput.getText().toString());
                                    achievement.setPoints(pointsInput.getText().toString());

                                    EventBus.getDefault().post(achievement);

                                    DBPref pref = new DBPref(getApplicationContext());
                                    pref.addRecord(programId, "achievement", achievement.getTitle(), achievement.getPoints());
                                    pref.close();
                                }
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

        if (titleInput.getText().toString().isEmpty()) {
            alertDialog.getButton(
                    AlertDialog.BUTTON_POSITIVE).setEnabled(false);
            titleInput.setError("Въведете име на актива!");
        }

        titleInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                    alertDialog.getButton(
                            AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                    titleInput.setError("Въведете име на актива!");
                }
            }
        });

        pointsInput.addTextChangedListener(new TextWatcher() {
                                               @Override
                                               public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                                   if (TextUtils.isEmpty(s)) {
                                                       alertDialog.getButton(
                                                               AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                                                       pointsInput.setError("Въведете точки за актива!");
                                                   }
                                               }

                                               @Override
                                               public void onTextChanged(CharSequence s, int start, int before, int count) {

                                               }

                                               @Override
                                               public void afterTextChanged(Editable s) {
                                                   if (TextUtils.isEmpty(s)) {
                                                       alertDialog.getButton(
                                                               AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                                                       pointsInput.setError("Въведете точки за актива!");
                                                   } else if (!isParsable(s.toString())) {
                                                       pointsInput.setError("Само числа!");
                                                   } else if (isParsable(s.toString())) {
                                                       alertDialog.getButton(
                                                               AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                                                   }
                                               }
                                           }
        );

    }

    public static boolean isParsable(String input) {
        boolean parsable = true;
        try {
            Integer.parseInt(input);
        } catch (NumberFormatException e) {
            parsable = false;
        }
        return parsable;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe
    public void onEvent(Object object) {
    }

    public void showCalendar() {

        CalendarDialogBuilder calendar;

        calendar = new CalendarDialogBuilder(this, new CalendarDialogBuilder.OnDateSetListener() {
            @Override
            public void onDateSet(int Year, int Month, int Day, DateType type) {
                EventBus.getDefault().post(new DateChart(Year, Month, Day, type));
            }
        });
        calendar.showCalendar();
    }

}
