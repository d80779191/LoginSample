package com.example.loginexample.base;

import com.example.loginexample.module.UserDataResponse;
import com.example.loginexample.module.UserLoginDTO;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.POST;

public interface ApiService {

    @POST
    Observable<Response<String>> login(UserLoginDTO userLoginDTO);
}
