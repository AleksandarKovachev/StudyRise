package bg.softuni.softuniada.studyrise.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import bg.softuni.softuniada.studyrise.Adapters.ViewPagerAdapter;
import bg.softuni.softuniada.studyrise.Fragments.LoginFragment;
import bg.softuni.softuniada.studyrise.Fragments.Programs;
import bg.softuni.softuniada.studyrise.Fragments.QuestionsFragment;
import bg.softuni.softuniada.studyrise.Fragments.RegistrationFragment;
import bg.softuni.softuniada.studyrise.Navigation.FragmentDrawer;
import bg.softuni.softuniada.studyrise.R;
import bg.softuni.softuniada.studyrise.Services.PastDates;

import static bg.softuni.softuniada.studyrise.R.string.username;

public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {

    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private boolean log;
    private String programId;

    private static TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent mServiceIntent = new Intent(this, PastDates.class);
        startService(mServiceIntent);

//        String extra = getIntent().getStringExtra("LOGIN");
//
//        SharedPreferences pref = getSharedPreferences("MainActivity", 0);
//        String username = pref.getString("login", null);
//
//
//        if (extra == null && username == null) {
//            setContentView(R.layout.activity_login_register);
//
//            viewPager = (ViewPager) findViewById(R.id.viewpager);
//            setupViewPager(viewPager);
//
//            tabLayout = (TabLayout) findViewById(R.id.tabs);
//            tabLayout.setupWithViewPager(viewPager);
//        } else if (extra != null || username != null) {
        log = true;
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);

        SharedPreferences sharedPreferencesPoints = getSharedPreferences("ProfilePoints", 0);
        String points = sharedPreferencesPoints.getString("points", null);

        textView = (TextView) findViewById(R.id.menu_points);

        if (points != null)
            textView.setText(points);

        Fragment fragment = new Programs();

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            getFragmentManager().popBackStackImmediate();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.addToBackStack(null);
            fragmentManager.popBackStack();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();
            fragmentTransaction.addToBackStack(null);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.logout) {
            SharedPreferences pref = getSharedPreferences("MainActivity", 0);
            SharedPreferences.Editor editor = pref.edit();
            editor.clear();
            editor.commit();

            Intent i = new Intent(this, MainActivity.class);
            i.putExtra("LOGIN", username);
            startActivity(i);
        }
        return false;
    }

    private void displayView(int position) {

        Fragment fragment = null;
        String title = getString(R.string.app_name);
        Intent intent;
        switch (position) {
            case 0:
                intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);
                break;
            case 1:
                fragment = new QuestionsFragment();
                break;
            case 2:
                fragment = new Programs();
                break;
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            getFragmentManager().popBackStackImmediate();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.addToBackStack(null);
            fragmentManager.popBackStack();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();
            getSupportActionBar().setTitle(title);
        }
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new LoginFragment(), "Вход");
        adapter.addFragment(new RegistrationFragment(), "Регистрация");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (log)
            getMenuInflater().inflate(R.menu.menu_loged, menu);
        else
            getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public static void setText(String text) {
        textView.setText(text);
    }
}
