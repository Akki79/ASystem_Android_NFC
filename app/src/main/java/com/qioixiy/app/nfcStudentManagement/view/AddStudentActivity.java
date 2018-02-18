package com.qioixiy.app.nfcStudentManagement.view;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.qioixiy.R;
import com.qioixiy.app.nfcStudentManagement.presenter.AddStudentPresenter;

public class AddStudentActivity extends AppCompatActivity implements MvpView {

    AddStudentPresenter mAddStudentPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_nfcstudentmangement_add_student);

        mAddStudentPresenter = new AddStudentPresenter();
    }

    private void onClickedOk(View view) {

    }

    private void onClickedCannel(View view) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showToast(String msg) {

    }

    @Override
    public void showErr() {

    }

    @Override
    public Context getContext() {
        return null;
    }

    @Override
    public void showData(String data) {

    }
}

