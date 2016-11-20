package bg.softuni.softuniada.studyrise.Fragments;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import bg.softuni.softuniada.studyrise.Adapters.ViewPagerAdapter;
import bg.softuni.softuniada.studyrise.Program;
import bg.softuni.softuniada.studyrise.R;
import bg.softuni.softuniada.studyrise.SQLite.DBPref;

public class ProductivityFragment extends Fragment {

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("ProgramProductivity", 0);
        String programName = sharedPreferences.getString("program", null);

        data = new ArrayList<>();

        DBPref pref = new DBPref(getContext());
        Cursor c = pref.getVals("program", null);

        if (c.moveToFirst()) {
            do {
                Program program = new Program();
                program.setId(c.getLong(c.getColumnIndex("_id")));
                program.setName(c.getString(c.getColumnIndex("programName")));
                program.setDate(c.getString(c.getColumnIndex("date")));

                data.add(program);
            } while (c.moveToNext());
        }

        c.close();
        pref.close();

        if (programName == null || data.size() == 0) {
            Fragment f;
            f = new OverviewProductivityFragment();
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container_body, f);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.commit();
        } else {
            View root = inflater.inflate(R.layout.fragment_productivity, container, false);

            viewPager = (ViewPager) root.findViewById(R.id.view_pager);

            setupViewPager(viewPager);

            tabLayout = (TabLayout) root.findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(viewPager);
            setupTabIcons();

            viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                @Override
                public void onPageSelected(int position) {
                    FragmentLifecycle fragmentToShow = (FragmentLifecycle) adapter.getItem(position);
                    fragmentToShow.onResumeFragment();
                }
            });
            return root;
        }
        return null;
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new OverviewProductivityFragment(), "Преглед");
        adapter.addFragment(new ActivFragment(), "Активи");
        adapter.addFragment(new AchievementsFragment(), "Награди");
        viewPager.setAdapter(adapter);
    }
}