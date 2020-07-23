package com.example.bookspace.fragment.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.bookspace.R;
import com.example.bookspace.adapter.ModePagerAdapter;
import com.google.android.material.tabs.TabLayout;


public class HomeFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tabLayout = view.findViewById(R.id.modeTabLayout);
        viewPager = view.findViewById(R.id.modeViewPager);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ModePagerAdapter modePagerAdapter = new ModePagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(modePagerAdapter);
        viewPager.setOffscreenPageLimit(modePagerAdapter.getCount() - 1);
        tabLayout.setupWithViewPager(viewPager, true);
    }
}
