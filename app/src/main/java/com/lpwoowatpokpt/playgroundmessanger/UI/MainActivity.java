package com.lpwoowatpokpt.playgroundmessanger.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lpwoowatpokpt.playgroundmessanger.Adapter.ViewPagerAdapter;
import com.lpwoowatpokpt.playgroundmessanger.R;

public class MainActivity extends AppCompatActivity {

    private TextView mProfileLabel, mUesrLabel, mNotificationLabel;
    private ViewPager viewPager;

    private ViewPagerAdapter viewPagerAdapter;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser == null){
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();

        mProfileLabel = findViewById(R.id.profileLabel);
        mUesrLabel = findViewById(R.id.userLabel);
        mNotificationLabel = findViewById(R.id.notificationsLabel);

        viewPager = findViewById(R.id.mainPager);
        viewPager.setOffscreenPageLimit(2);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);


        mProfileLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0);
            }
        });
        mUesrLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1);
            }
        });
        mNotificationLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(2);
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                
            }

            @Override
            public void onPageSelected(int position) {
                changeTabs(position);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    private void changeTabs(int position) {
        switch (position){
            case 0:
                mProfileLabel.setTextColor(getResources().getColor(R.color.textBarBright));
                mProfileLabel.setTextSize(22);

                mUesrLabel.setTextColor(getResources().getColor(R.color.textTabLight));
                mUesrLabel.setTextSize(16);

                mNotificationLabel.setTextColor(getResources().getColor(R.color.textTabLight));
                mNotificationLabel.setTextSize(16);
                break;
            case 1:
                mProfileLabel.setTextColor(getResources().getColor(R.color.textTabLight));
                mProfileLabel.setTextSize(16);

                mUesrLabel.setTextColor(getResources().getColor(R.color.textBarBright));
                mUesrLabel.setTextSize(22);

                mNotificationLabel.setTextColor(getResources().getColor(R.color.textTabLight));
                mNotificationLabel.setTextSize(16);
                break;
            case 2:
                mProfileLabel.setTextColor(getResources().getColor(R.color.textTabLight));
                mProfileLabel.setTextSize(16);

                mUesrLabel.setTextColor(getResources().getColor(R.color.textTabLight));
                mUesrLabel.setTextSize(16);

                mNotificationLabel.setTextColor(getResources().getColor(R.color.textBarBright));
                mNotificationLabel.setTextSize(22);
                break;
        }
    }
}
