package com.example.hendrixassassins.uipages;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.hendrixassassins.game.Game;
import com.example.hendrixassassins.uipages.*;

import com.example.hendrixassassins.R;

public class HomeActivity extends AppCompatActivity {

    private TextView mTextMessage;

    private HomeFragment homeFragment;
    private NotificationsFragment notificationsFragment;
    private EmailFragment emailFragment;
    private Game game;
    private Context context;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    setFragment(homeFragment); // forward intent through fragment
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    setFragment(emailFragment);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    setFragment(notificationsFragment);
                    return true;
            }
            return false;
        }
    };


    @Override
    protected void onStop() {
        super.onStop();
        game.writeGameToFile(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String email = intent.getExtras().getString("email");
        context = getApplicationContext();
        setContentView(R.layout.activity_home);
        Log.e("AAA", "We've initialized");
        setupGame(email);
        initializeFragments();
        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.main_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        setFragment(homeFragment);
    }

    private void setupGame(String email) {
        game = new Game(email);
        Log.e("AAA", game.getGameFileName());
        game.readGameFromFile(context);
        Log.e("AAA", game.getPassword());
    }

    private void initializeFragments(){
        homeFragment = new HomeFragment();
        notificationsFragment = new NotificationsFragment();
        emailFragment = new EmailFragment();
    }

    private void setFragment(Fragment fragment){
//        HomeFragment.OnFragmentInteractionListener.class.cast()

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.commit();
    }
}
