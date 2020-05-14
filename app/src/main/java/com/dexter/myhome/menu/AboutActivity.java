package com.dexter.myhome.menu;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.dexter.myhome.R;
import com.dexter.myhome.adapter.ViewPageAdapter;
import com.dexter.myhome.menu.fragment.Details;
import com.dexter.myhome.menu.fragment.Developers;
import com.google.android.material.tabs.TabLayout;

public class AboutActivity extends AppCompatActivity {

    private ViewPager aboutViewPager;
    private TabLayout aboutTabs;
    private ViewPageAdapter aboutAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        aboutViewPager = findViewById(R.id.about_view_pager);

        aboutAdapter = new ViewPageAdapter(getSupportFragmentManager());
        aboutAdapter.addFragment(new Developers(), "DEVELOPERS");
        aboutAdapter.addFragment(new Details(), "DETAILS");

        aboutViewPager.setAdapter(aboutAdapter);

        aboutTabs = findViewById(R.id.about_tabs);
        aboutTabs.setupWithViewPager(aboutViewPager);
    }
}
