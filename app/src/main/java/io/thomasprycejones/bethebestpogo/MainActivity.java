package io.thomasprycejones.bethebestpogo;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.google.firebase.analytics.FirebaseAnalytics;

import io.thomasprycejones.bethebestpogo.Fragment.AdsFragment;
import io.thomasprycejones.bethebestpogo.Fragment.HomeFragment;
import io.thomasprycejones.bethebestpogo.Fragment.NewsFragment;
import io.thomasprycejones.bethebestpogo.Fragment.PokedexFragment;
import io.thomasprycejones.bethebestpogo.Fragment.TipsFragment;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private PrefManager prefManager;
    private static final int REQUEST_CODE_LOCATION = 2;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefManager = new PrefManager(this);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setMinimumSessionDuration(3000);

        //Cosas para fragment one
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //TABS INITIALIZERS
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        //UI Color
        String team = prefManager.isWhatTeam();
        setTeamsAndColors(team);



        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Request missing location permission.
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_LOCATION);
        } else {
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Set up the tabs
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new HomeFragment(), getString(R.string.home_fragment));
        adapter.addFragment(new PokedexFragment(), getString(R.string.pokedex_fragment));
        adapter.addFragment(new TipsFragment(), getString(R.string.tips_fragment));
        adapter.addFragment(new NewsFragment(), getString(R.string.news_fragment));
        adapter.addFragment(new AdsFragment(), getString(R.string.ads_fragment));
        viewPager.setAdapter(adapter);
    }

    //Set up the UI colors
    private void setTeamsAndColors(String team){
        switch (team) {
            case "Valor":
                int myColor_valor = getResources().getColor(R.color.Valor);
                toolbar.setBackgroundColor(myColor_valor);
                viewPager.setBackgroundColor(myColor_valor);
                tabLayout.setBackgroundColor(myColor_valor);
                changeStatusBarColor(getResources().getColor(R.color.ValorStatusBar));
                break;
            case "Mystic":
                int myColor_mystic = getResources().getColor(R.color.Mystic);
                toolbar.setBackgroundColor(myColor_mystic);
                viewPager.setBackgroundColor(myColor_mystic);
                tabLayout.setBackgroundColor(myColor_mystic);
                changeStatusBarColor(getResources().getColor(R.color.MysticStatusBar));
                break;
            case "Instinct":
                int myColor_instinct = getResources().getColor(R.color.Instinct);
                toolbar.setBackgroundColor(myColor_instinct);
                viewPager.setBackgroundColor(myColor_instinct);
                tabLayout.setBackgroundColor(myColor_instinct);
                changeStatusBarColor(getResources().getColor(R.color.InstinctStatusBar));
                break;
            default:
                toolbar.setBackgroundColor(Color.GRAY);
                viewPager.setBackgroundColor(Color.GRAY);
                tabLayout.setBackgroundColor(Color.GRAY);
                break;
        }
    }

    /**
     * Making notification bar transparent
     */
    private void changeStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
    }



    //*** Logic behind fragment switching***
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }


    }
}

