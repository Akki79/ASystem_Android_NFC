package com.qioixiy.app.nfcStudentManagement.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.qioixiy.R;

public class LauncherActivity extends AppCompatActivity {

    Integer time = 200;
    Handler mDelayHandler = new Handler();

    private class CheckLoginRunnable implements Runnable {

        private Context context;
        public CheckLoginRunnable(Context context) {
            this.context = context;
        }

        @Override
        public void run() {
            LoginUtilActivity.checkLogin(context, new LoginUtilActivity.LoginCallback() {
                @Override
                public void onLogin() {
                    Intent intent = new Intent(LauncherActivity.this, ManagerMainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfcstudentmangement_luncher);

        mDelayHandler.postDelayed(new CheckLoginRunnable(this), time);

    }
}
