package bg.softuni.softuniada.studyrise.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.florent37.bubbletab.BubbleTab;

import bg.softuni.softuniada.studyrise.Adapters.ViewPagerAdapter;
import bg.softuni.softuniada.studyrise.R;

public class ProductivityFragment extends Fragment {

    private BubbleTab bubbleTab;
    private ViewPager mPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_productivity, container, false);

        bubbleTab = (BubbleTab) root.findViewById(R.id.tab_layout);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        mPager = (ViewPager) root.findViewById(R.id.view_pager);
        bubbleTab.setupWithViewPager(mPager);
        adapter.addFragment(new OverviewProductivityFragment(), "Productivity");
        adapter.addFragment(new ActivFragment(), "Activ");
        adapter.addFragment(new AchievementsFragment(), "Achievements");
        mPager.setAdapter(adapter);

        return root;
    }
}