package bg.softuni.softuniada.studyrise.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class MyViewPageAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments = new ArrayList<Fragment>();
    FragmentManager fm;

    public List<Fragment> getFragments() {
        return fragments;
    }

    public void setFragments(List<Fragment> fragments) {
        this.fragments = fragments;
    }

    public MyViewPageAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fm = fm;
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public void addFragment(Fragment fragment) {
        this.fragments.add(fragment);
        notifyDataSetChanged();
    }

    public void removeFragments() {
        try {
            List<Fragment> newList = new ArrayList<Fragment>();
            Fragment general = fragments.get(0);
            newList.add(general);
            this.fragments.clear();
            this.fragments = new ArrayList<Fragment>();
            this.fragments = newList;
            notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeItemFromFrament(ViewGroup container, int position, Object object) {
        try {
            try {
                container.removeViewAt(position);
                ((ViewPager) container).setCurrentItem(position - 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}