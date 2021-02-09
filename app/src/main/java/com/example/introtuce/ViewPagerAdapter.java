package com.example.introtuce;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.google.android.material.tabs.TabLayout;


public class ViewPagerAdapter extends FragmentStateAdapter {
    private TabLayout tabs;
    public ViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle,TabLayout tabs) {
        super(fragmentManager, lifecycle);
        this.tabs=tabs;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch(position){
            case 0: return new ShowUserFrag();
            case 1: return new addUserFragment();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
