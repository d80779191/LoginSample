package com.example.loginexample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.loginexample.base.BaseActivity;
import com.example.loginexample.module.LoginResponse;
import com.example.loginexample.module.UserLoginDTO;
import com.example.loginexample.user.UserDataActivity;
import com.example.loginexample.viewmodel.LoginTestViewModel;
import com.google.gson.Gson;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

public class MainActivity extends BaseActivity {
    private EditText editTextAccount;
    private EditText editTextPassword;
    private Button buttonLoginSubmit;
    private LoginTestViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewModel = new ViewModelProvider(MainActivity.this).get(LoginTestViewModel.class);
        editTextAccount = findViewById(R.id.login_account);
        editTextPassword = findViewById(R.id.login_password);
        buttonLoginSubmit = findViewById(R.id.login_submit);

        buttonLoginSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String account = editTextAccount.getText().toString();
                String password = editTextPassword.getText().toString();
                if (account.replace(" ", "").isEmpty()) {
                    Toast.makeText(MainActivity.this, "請輸入帳號", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.replace(" ", "").isEmpty()) {
                    Toast.makeText(MainActivity.this, "請輸入密碼", Toast.LENGTH_SHORT).show();
                    return;
                }
                UserLoginDTO userLoginDTO = new UserLoginDTO();
                userLoginDTO.setAccount(account);
                userLoginDTO.setPassword(password);
                login(userLoginDTO);
            }
        });
    }

    private void login(UserLoginDTO userLoginDTO) {
        viewModel.postLogin(userLoginDTO).observe(MainActivity.this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Gson gson = new Gson();
                LoginResponse response = gson.fromJson(s, LoginResponse.class);
                if (response.getStatusCode() < 400) {
                    startActivity(new Intent(MainActivity.this, UserDataActivity.class));
                    finish();
                } else if (response.getStatusCode() >= 400 && response.getStatusCode() < 500){
                    Toast.makeText(MainActivity.this, "帳號或密碼錯誤", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "錯誤，請稍後再試", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}