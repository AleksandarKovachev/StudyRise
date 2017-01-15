package bg.softuni.softuniada.studyrise.Activities;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

import java.util.ArrayList;

import bg.softuni.softuniada.studyrise.Adapters.ViewPagerAdapter;
import bg.softuni.softuniada.studyrise.FragmentLifecycle;
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
            R.drawable.ic_todo,
            R.drawable.ic_achievements
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
            setupTabIcons();

            viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                @Override
                public void onPageSelected(int position) {
                    FragmentLifecycle fragmentToShow = (FragmentLifecycle) adapter.getItem(position);
                    fragmentToShow.onResumeFragment();
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

}
