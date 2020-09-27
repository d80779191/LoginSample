package com.example.loginexample.viewmodel;

import android.app.Application;

import com.example.loginexample.base.ApiService;
import com.example.loginexample.base.RetrofitClient;
import com.example.loginexample.module.LoginResponse;
import com.example.loginexample.module.UserLoginDTO;
import com.google.gson.Gson;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginTestViewModel extends AndroidViewModel {
    private Retrofit retrofit;
    private ApiService apiService;

    public LoginTestViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<String> postLogin(UserLoginDTO userLoginDTO) {
        retrofit = RetrofitClient.getInstance();
        apiService = retrofit.create(ApiService.class);
        final MutableLiveData<String> liveData = new MutableLiveData<>();
        final Observer<Response<String>> observer = new Observer<Response<String>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Response<String> response) {
                LoginResponse loginResponse = new LoginResponse();
                loginResponse.setStatusCode(response.code());
                Gson gson = new Gson();
                liveData.setValue(gson.toJson(response));
            }

            @Override
            public void onError(Throwable e) {
                LoginResponse response = new LoginResponse();
                response.setStatusCode(((HttpException) e).code());
                Gson gson = new Gson();
                liveData.setValue(gson.toJson(response));
            }

            @Override
            public void onComplete() {

            }
        };
        Observable<Response<String>> observable = apiService.login(userLoginDTO);
        observable.subscribe(observer);
        return liveData;
    }
}
