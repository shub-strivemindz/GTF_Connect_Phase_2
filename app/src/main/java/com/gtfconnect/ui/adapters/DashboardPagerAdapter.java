package com.gtfconnect.ui.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.gtfconnect.ui.fragments.ChannelFragment;
import com.gtfconnect.ui.fragments.GroupFragment;
import com.gtfconnect.ui.fragments.MentorFragment;
import com.gtfconnect.ui.fragments.RecentFragment;

public class DashboardPagerAdapter extends FragmentStateAdapter {

    private int tabCount = 0;


    public DashboardPagerAdapter(@NonNull FragmentActivity fragmentActivity, int tabCount) {
        super(fragmentActivity);
        this.tabCount = tabCount;

    }
    @NonNull @Override public Fragment createFragment(int position) {

        return checkViewType(position);
    }



    @Override public int getItemCount() {
        return tabCount;
    }


    private Fragment checkViewType(int position)
    {
        //--------------------------------------------------------------For Expense Approval -------------------------------------------------------------------

        Fragment fragment = null;

            switch (position) {
                case 1:
                    fragment =  ChannelFragment.newInstance();
                    break;
                case 2:
                    fragment =  GroupFragment.newInstance();
                    break;
                case 3:
                    fragment =  MentorFragment.newInstance();
                    break;
                case 0:
                default:
                    fragment =  RecentFragment.newInstance();
                    break;
            }

            return fragment;
    }
}