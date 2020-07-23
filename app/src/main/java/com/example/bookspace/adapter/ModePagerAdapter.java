package com.example.bookspace.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.bookspace.fragment.home.DonateFragment;
import com.example.bookspace.fragment.home.ExchangeFragment;
import com.example.bookspace.fragment.home.ExchangeWithCashFragment;
import com.example.bookspace.fragment.home.RentFragment;
import com.example.bookspace.fragment.home.SellFragment;

public class ModePagerAdapter extends FragmentStatePagerAdapter {

    public ModePagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return DonateFragment.newInstance();
            case 1:
                return RentFragment.newInstance();
            case 2:
                return new ExchangeFragment();
            case 3:
                return ExchangeWithCashFragment.newInstance();
            case 4:
                return SellFragment.newInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Donate";
            case 1:
                return "Rent";
            case 2:
                return "Exchange";
            case 3:
                return "Exchange/Cash";
            case 4:
                return "Sell";
        }
        return null;
    }
}
