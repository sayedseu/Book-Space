package com.example.bookspace.model.remote;

import com.example.bookspace.model.model_class.remote.BookDetails;
import com.example.bookspace.model.model_class.remote.Interested;
import com.example.bookspace.model.model_class.remote.User;

import java.util.List;

import io.reactivex.Flowable;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiInterface {
    @POST(value = "auth/reg")
    Flowable<Long> registerUser(@Body User user);

    @POST(value = "auth/check/{number}/{email}")
    Flowable<Integer> checkDuplicateUser(@Path(value = "number") String number, @Path(value = "email") String email);

    @POST(value = "otp/send/{number}")
    Flowable<Integer> sendOtp(@Path(value = "number") String number);

    @POST(value = "otp/verify/{number}/{code}")
    Flowable<Integer> verifyOtp(@Path(value = "number") String number, @Path(value = "code") String code);

    @PUT(value = "auth/update/{number}/{newPassword}")
    Flowable<User> updatePassword(@Path(value = "number") String number, @Path(value = "newPassword") String newPassword);

    @PUT(value = "auth/update")
    Flowable<User> updateProfile(@Body User user);

    @POST(value = "auth/{username}/{password}")
    Flowable<User> authenticate(@Path(value = "username") String username, @Path(value = "password") String password);

    @POST(value = "books/new")
    Flowable<Long> insertBook(@Body BookDetails bookDetails);

    @GET(value = "books")
    Flowable<List<BookDetails>> getAllBook();

    @GET(value = "books/mode/{mode}")
    Flowable<List<BookDetails>> getAllBookByMode(@Path(value = "mode") String mode);

    @GET(value = "books/category/{category}")
    Flowable<List<BookDetails>> getAllBookByCategory(@Path(value = "category") String category);

    @POST(value = "interest/new")
    Flowable<Long> insertInterestedBook(@Body Interested interested);

    @GET(value = "interest/{userid}")
    Flowable<List<Interested>> getInterestedBook(@Path(value = "userid") long userid);

    @DELETE(value = "interest/{id}")
    Flowable<Integer> deleteInterestedBook(@Path(value = "id") long id);
}
