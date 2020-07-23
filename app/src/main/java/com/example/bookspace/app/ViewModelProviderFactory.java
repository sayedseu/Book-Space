package com.example.bookspace.app;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.example.bookspace.model.local.source.BookDataSource;
import com.example.bookspace.model.remote.ApiInterface;
import com.example.bookspace.view_model.SharedViewModel;
import com.example.bookspace.view_model.auth.LoginViewModel;
import com.example.bookspace.view_model.auth.OtpViewModel;
import com.example.bookspace.view_model.auth.ResetPasswordViewModel;
import com.example.bookspace.view_model.auth.SignupViewModel;
import com.example.bookspace.view_model.category.CategoryDetailsViewModel;
import com.example.bookspace.view_model.home.DonateViewModel;
import com.example.bookspace.view_model.home.ExchangeViewModel;
import com.example.bookspace.view_model.home.ExchangeWithCashViewModel;
import com.example.bookspace.view_model.home.RentViewModel;
import com.example.bookspace.view_model.home.SellViewModel;
import com.example.bookspace.view_model.interested.BookDetailsViewModel;
import com.example.bookspace.view_model.interested.InterestedViewModel;
import com.example.bookspace.view_model.profile.UpdateProfileViewModel;
import com.example.bookspace.view_model.search.SearchViewModel;
import com.example.bookspace.view_model.upload.ReviewViewModel;

public class ViewModelProviderFactory implements androidx.lifecycle.ViewModelProvider.Factory {
    private final BookDataSource dataSource;
    private final ApiInterface apiInterface;

    ViewModelProviderFactory(BookDataSource dataSource, ApiInterface apiInterface) {
        this.dataSource = dataSource;
        this.apiInterface = apiInterface;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(SearchViewModel.class)) {
            return (T) new SearchViewModel(apiInterface);
        }
        if (modelClass.isAssignableFrom(LoginViewModel.class)) {
            return (T) new LoginViewModel(apiInterface);
        }
        if (modelClass.isAssignableFrom(SignupViewModel.class)) {
            return (T) new SignupViewModel(apiInterface);
        }
        if (modelClass.isAssignableFrom(OtpViewModel.class)) {
            return (T) new OtpViewModel(apiInterface);
        }
        if (modelClass.isAssignableFrom(SharedViewModel.class)) {
            return (T) new SharedViewModel();
        }
        if (modelClass.isAssignableFrom(ResetPasswordViewModel.class)) {
            return (T) new ResetPasswordViewModel(apiInterface);
        }
        if (modelClass.isAssignableFrom(ReviewViewModel.class)) {
            return (T) new ReviewViewModel(apiInterface);
        }
        if (modelClass.isAssignableFrom(SellViewModel.class)) {
            return (T) new SellViewModel(apiInterface);
        }
        if (modelClass.isAssignableFrom(DonateViewModel.class)) {
            return (T) new DonateViewModel(apiInterface);
        }
        if (modelClass.isAssignableFrom(RentViewModel.class)) {
            return (T) new RentViewModel(apiInterface);
        }
        if (modelClass.isAssignableFrom(ExchangeViewModel.class)) {
            return (T) new ExchangeViewModel(apiInterface);
        }
        if (modelClass.isAssignableFrom(ExchangeWithCashViewModel.class)) {
            return (T) new ExchangeWithCashViewModel(apiInterface);
        }
        if (modelClass.isAssignableFrom(CategoryDetailsViewModel.class)) {
            return (T) new CategoryDetailsViewModel(apiInterface);
        }
        if (modelClass.isAssignableFrom(UpdateProfileViewModel.class)) {
            return (T) new UpdateProfileViewModel(apiInterface);
        }
        if (modelClass.isAssignableFrom(BookDetailsViewModel.class)) {
            return (T) new BookDetailsViewModel(apiInterface);
        }
        if (modelClass.isAssignableFrom(InterestedViewModel.class)) {
            return (T) new InterestedViewModel(apiInterface);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
