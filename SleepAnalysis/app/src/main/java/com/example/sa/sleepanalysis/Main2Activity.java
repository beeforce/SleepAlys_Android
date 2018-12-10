package com.example.sa.sleepanalysis;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.sa.sleepanalysis.adapter.SectionPageAdapter;
import com.example.sa.sleepanalysis.fragement.alarmClockFragment;
import com.example.sa.sleepanalysis.fragement.graphFragment;
import com.example.sa.sleepanalysis.fragement.historyFragment;
import com.example.sa.sleepanalysis.fragement.profileFragment;
import com.example.sa.sleepanalysis.fragement.sleepTimeFragment;

import java.lang.reflect.Field;

public class Main2Activity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private ViewPager viewPager;
    private MenuItem prevMenuItem;
    private alarmClockFragment alarmClockFragment;
    private sleepTimeFragment sleepTimeFragment;
    private graphFragment graphFragment;
    private historyFragment historyFragment;
    private profileFragment profileFragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        initBottomNavigation();
        initViewPager();
        setupViewPager();


    }

    private void initBottomNavigation() {

        bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);
        for (int i = 0; i < menuView.getChildCount(); i++) {
            final View iconView = menuView.getChildAt(i).findViewById(android.support.design.R.id.icon);
            final ViewGroup.LayoutParams layoutParams = iconView.getLayoutParams();
            final DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            // set your height here
            layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 32, displayMetrics);
            // set your width here
            layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 32, displayMetrics);
            iconView.setLayoutParams(layoutParams);
        }
        BottomNavigationViewHelper.removeShiftMode(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_upcoming:
                                viewPager.setCurrentItem(0);
                                return true;

                            case R.id.action_listening:
                                viewPager.setCurrentItem(1);
                                return true;

                            case R.id.action_schedule:
                                viewPager.setCurrentItem(2);
                                return true;
                            case R.id.action_static:
                                viewPager.setCurrentItem(3);
                                return true;
                            case R.id.action_setting:
                                viewPager.setCurrentItem(4);
                                return true;


                        }
                        return true;
                    }
                });
    }

    private void initViewPager() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        if (getSupportActionBar() != null)
                        break;
                    case 1:
                        if (getSupportActionBar() != null)
                        break;
                    case 2:
                        if (getSupportActionBar() != null)
                        break;
                    case 3:
                        if (getSupportActionBar() != null)
                        break;
                    case 4:
                        if (getSupportActionBar() != null)
//                            getSupportActionBar().setTitle(R.string.action_bar_setting);
                        break;

                }
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }

                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottomNavigationView.getMenu().getItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    private void setupViewPager() {
        alarmClockFragment = new alarmClockFragment();
        sleepTimeFragment = new sleepTimeFragment();
        graphFragment = new graphFragment();
        historyFragment = new historyFragment();
        profileFragment = new profileFragment();

        SectionPageAdapter adapter = new SectionPageAdapter(getSupportFragmentManager());

        adapter.addFragment(sleepTimeFragment);
        adapter.addFragment(alarmClockFragment);
        adapter.addFragment(graphFragment);
        adapter.addFragment(historyFragment);
        adapter.addFragment(profileFragment);


        viewPager.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/M/yyyy hh:mm:ss");
//        String current = simpleDateFormat.format(new Date());
//
//        MySharedPreference.putPref(MySharedPreference.LAST_ACTIVE_TIME, current, getApplicationContext());
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        Boolean isActiveOverHour = Validator.isActiveOverHour(getApplicationContext());
//
//        if (isActiveOverHour){
//            MySharedPreference.clearPref(getApplicationContext());
//            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(intent);
//        }
//    }
}
class BottomNavigationViewHelper {

    @SuppressLint("RestrictedApi")
    static void removeShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                item.setShifting(false);
//                item.setShiftingMode(false);
                // set once again checked value, so view will be updated
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException e) {
            Log.e("ERROR NO SUCH FIELD", "Unable to get shift mode field");
        } catch (IllegalAccessException e) {
            Log.e("ERROR ILLEGAL ALG", "Unable to change value of shift mode");
        }
    }
}