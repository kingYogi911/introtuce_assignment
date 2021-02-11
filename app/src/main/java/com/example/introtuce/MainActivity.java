package com.example.introtuce;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends FragmentActivity {
    Toolbar toolbar;
    TabLayout tabs;
    ViewPagerAdapter adapter;
    ViewPager2 viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        setContentView(R.layout.activity_main);
        toolbar=findViewById(R.id.toolbar);
        viewPager=findViewById(R.id.pager);
        tabs=findViewById(R.id.tabs);
        adapter=new ViewPagerAdapter(getSupportFragmentManager(),getLifecycle(),tabs);
        viewPager.setAdapter(adapter);
        String[] title ={"Users","Enroll"};
        new TabLayoutMediator(tabs,viewPager,(tab, position) -> tab.setText(title[position])).attach();
    }
}