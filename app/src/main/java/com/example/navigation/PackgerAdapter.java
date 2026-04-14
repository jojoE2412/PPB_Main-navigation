package com.example.navigation;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;


public class PackgerAdapter extends FragmentStateAdapter {

    int numTab;

    public PackgerAdapter (@NonNull FragmentActivity fm, int numTab) {
        super(fm);
        this.numTab = numTab;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new MainFragment();
            case 1:
                return new SecondFragment();
            default:
                return new MainFragment();
        }
    }

    @Override
    public int getItemCount() {
        return numTab;
    }
}

