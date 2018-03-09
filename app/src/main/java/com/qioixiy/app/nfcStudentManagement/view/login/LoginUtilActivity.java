package com.qioixiy.app.nfcStudentManagement.view.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class LoginUtilActivity extends Activity {
    private int REQUEST_CODE_LOGIN = 1;
    static LoginCallback mCallback;

    public interface LoginCallback {
        void onLogin();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, REQUEST_CODE_LOGIN);
    }

    //此处检查当前的登录状态
    public static void checkLogin(Context context, LoginCallback callback) {
        boolean isLogin = false;
        if (isLogin) {
            callback.onLogin();
        } else {
            mCallback = callback;
            Intent intent = new Intent(context, LoginActivity.class);
            context.startActivity(intent);
        }
    }

    //此处检查当前的登录状态
    public static void checkLogin(Context context, LoginCallback logged, LoginCallback callback) {
        boolean isLogin = false;
        if (isLogin) {
            logged.onLogin();
        } else {
            mCallback = callback;
            Intent intent = new Intent(context, LoginActivity.class);
            context.startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        finish();
        if (requestCode == REQUEST_CODE_LOGIN && resultCode == RESULT_OK && mCallback != null) {
            mCallback.onLogin();
        }
        mCallback = null;
    }
}

