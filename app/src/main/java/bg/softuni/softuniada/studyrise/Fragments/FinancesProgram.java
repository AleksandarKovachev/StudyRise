package bg.softuni.softuniada.studyrise.Fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import bg.softuni.softuniada.studyrise.Adapters.ViewPagerAdapter;
import bg.softuni.softuniada.studyrise.R;

public class FinancesProgram extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private int[] tabIcons = {
            R.drawable.ic_overview_finances,
            R.drawable.ic_profit,
            R.drawable.ic_expense
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_finances_program, container, false);

        viewPager = (ViewPager) root.findViewById(R.id.view_pager_finances);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) root.findViewById(R.id.tab_finances);
        tabLayout.setupWithViewPager(viewPager);

        setupTabIcons();

        return root;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new OverviewFinances(), "Общ преглед");
        adapter.addFragment(new Profit(), "Приходи");
        adapter.addFragment(new Expense(), "Разходи");
        viewPager.setAdapter(adapter);
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }
}
