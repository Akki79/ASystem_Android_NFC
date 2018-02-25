package com.qioixiy.app.nfcStudentManagement.view;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.qioixiy.R;
import com.qioixiy.app.nfcStudentManagement.model.Student;
import com.qioixiy.app.nfcStudentManagement.presenter.AddStudentPresenter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.qioixiy.utils.ConstString.getServerString;

public class AddStudentActivity extends AppCompatActivity implements MvpView {

    private String TAG = getClass().getSimpleName();

    private final OkHttpClient client = new OkHttpClient();

    AddStudentPresenter mAddStudentPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_nfcstudentmangement_add_student);

        mAddStudentPresenter = new AddStudentPresenter();

        Button register = findViewById(R.id.nfcstudentmangement_add_student_register_button);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView name = findViewById(R.id.nfcstudentmangement_add_student_textview_user);
                TextView number = findViewById(R.id.nfcstudentmangement_add_student_textview_number);
                TextView telphone = findViewById(R.id.nfcstudentmangement_add_student_textview_telphone);
                TextView email = findViewById(R.id.nfcstudentmangement_add_student_textview_email);

                if (name.getText().toString().equals("")) {
                    showToast("用户名不能为空");
                    return;
                }
                if (number.getText().toString().equals("")) {
                    showToast("编号不能为空");
                    return;
                }
                if (telphone.getText().toString().equals("")) {
                    showToast("联系方式不能为空");
                    return;
                }
                if (email.getText().toString().equals("")) {
                    showToast("邮箱不能为空");
                    return;
                }

                try {
                    JSONObject obj = new JSONObject();
                    obj.put("name", name.getText().toString());
                    obj.put("number", number.getText().toString());
                    obj.put("telphone", telphone.getText().toString());
                    obj.put("email", email.getText().toString());

                    new createStudentAsyncTask().execute(obj.toString());

                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        Button cancel = findViewById(R.id.nfcstudentmangement_add_student_cannel_button);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(AddStudentActivity.this, msg, Toast.LENGTH_SHORT).show();

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

    private class createStudentAsyncTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                RequestBody formBody = new FormBody.Builder()
                        .add("func", "student")
                        .add("param1", "create")
                        .add("param2", params[0])
                        .build();

                Request request = new Request.Builder()
                        .url(getServerString())
                        .post(formBody)
                        .build();

                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    String rep = response.body().string();
                    Log.i(TAG, "response:" + rep);
                    return rep;
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
        }
    }
}

