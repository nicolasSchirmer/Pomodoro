package com.nschirmer.pomodoro.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.nschirmer.pomodoro.R;
import com.nschirmer.pomodoro.fragment.HistoryFragment;
import com.nschirmer.pomodoro.fragment.TimerFragment;
import com.nschirmer.pomodoro.util.Dictionary;
import com.nschirmer.pomodoro.util.FragmentHelper;
import com.nschirmer.pomodoro.util.Utils;

public class MainActivity extends AppCompatActivity {

    private FragmentHelper fragmentOpened;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // save in minutes the default task time
        Utils.saveMaxMilliTaskTime(this, Utils.minutesToMilliseconds(25));

        startFragment(new FragmentHelper(new TimerFragment(), Dictionary.FRAGMENT_TAG_TIMER));
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.bottommenu_timer:
                    startFragment(new FragmentHelper(new TimerFragment(), Dictionary.FRAGMENT_TAG_TIMER));
                    return true;
                case R.id.bottommenu_history:
                    startFragment(new FragmentHelper(new HistoryFragment(), Dictionary.FRAGMENT_TAG_HISTORY));
                    return true;
            }
            return false;
        }
    };


    private void startFragment(FragmentHelper fragmentHelper){
        if(fragmentOpened == null || !fragmentHelper.equals(fragmentOpened)) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.main_fragment_container, fragmentHelper.getFragment(), fragmentHelper.getTag());
            fragmentTransaction.commitAllowingStateLoss();

            fragmentOpened = fragmentHelper;
        }
    }

}
