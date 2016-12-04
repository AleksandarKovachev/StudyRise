package bg.softuni.softuniada.studyrise.Activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import java.util.LinkedList;
import java.util.List;

import bg.softuni.softuniada.studyrise.Adapters.CustomViewPager;
import bg.softuni.softuniada.studyrise.Adapters.MyViewPageAdapter;
import bg.softuni.softuniada.studyrise.Animations.DepthPageTransformer;
import bg.softuni.softuniada.studyrise.Fragments.ScreenSlidePageFragment;
import bg.softuni.softuniada.studyrise.R;
import bg.softuni.softuniada.studyrise.SwipeDirection;

public class ScreenSlidePagerActivity extends FragmentActivity {

    private int NUM_PAGES = 3;

    public static CustomViewPager mPager;

    public static MyViewPageAdapter mPagerAdapter;

    private List list = new LinkedList<Fragment>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_slide);
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);

        mPager = (CustomViewPager) findViewById(R.id.pager);
        mPager.setPageTransformer(true, new DepthPageTransformer());
        mPager.setAllowedSwipeDirection(SwipeDirection.left);

        list.add(new ScreenSlidePageFragment());

        mPagerAdapter = new MyViewPageAdapter(getSupportFragmentManager(), list);
        mPager.setAdapter(mPagerAdapter);
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            super.onBackPressed();
            overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
        } else {
            NUM_PAGES--;
            mPagerAdapter.notifyDataSetChanged();
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

}
