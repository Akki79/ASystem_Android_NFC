package com.qioixiy.test.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.qioixiy.nfc.manager.R;

import studios.codelight.smartloginlibrary.SmartLoginCallbacks;
import studios.codelight.smartloginlibrary.users.SmartUser;
import studios.codelight.smartloginlibrary.util.SmartLoginException;

public class LoginActivity extends AppCompatActivity implements SmartLoginCallbacks {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    public void onLoginSuccess(SmartUser user) {

    }

    @Override
    public void onLoginFailure(SmartLoginException e) {

    }

    @Override
    public SmartUser doCustomLogin() {
        SmartUser user = new SmartUser();
        //user.setEmail(emailEditText.getText().toString());
        return user;
    }

    @Override
    public SmartUser doCustomSignup() {
        return null;
    }
}
