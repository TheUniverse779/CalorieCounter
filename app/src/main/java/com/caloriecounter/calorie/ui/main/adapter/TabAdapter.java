package com.caloriecounter.calorie.ui.main.adapter;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.caloriecounter.calorie.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class TabAdapter extends FragmentPagerAdapter {
    private Context context;
    private final List<Fragment> fragmentList = new ArrayList<>();
    private final List<String> fragmentTitleList = new ArrayList<>();

    public TabAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }


    public void addFragment(Fragment fragment, String title) {
        fragmentList.add(fragment);
        fragmentTitleList.add(title);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    public String getPageTitle(int position) {
        return fragmentTitleList.get(position);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }


    public void setOnSelectView(TabLayout tabLayout, int position) {
        TabLayout.Tab tab = tabLayout.getTabAt(position);
        View selected = tab.getCustomView();
        TextView iv_text = selected.findViewById(R.id.tvTitle);
        View indicator = selected.findViewById(R.id.indicator);
        indicator.setVisibility(View.VISIBLE);
        iv_text.setTextColor(Color.parseColor("#FFFFFF"));
        Typeface typeface = ResourcesCompat.getFont(context, R.font.svn_avo_bold);
        iv_text.setTypeface(typeface);


    }

    public void setUnSelectView(TabLayout tabLayout, int position) {
        TabLayout.Tab tab = tabLayout.getTabAt(position);
        View selected = tab.getCustomView();
        TextView iv_text = selected.findViewById(R.id.tvTitle);
        View indicator = selected.findViewById(R.id.indicator);
        indicator.setVisibility(View.INVISIBLE);
        iv_text.setTextColor(Color.parseColor("#80FFFFFF"));
        Typeface typeface = ResourcesCompat.getFont(context, R.font.svn_avo);
        iv_text.setTypeface(typeface);
    }
}