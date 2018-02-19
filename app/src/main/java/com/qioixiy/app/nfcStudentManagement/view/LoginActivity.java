package com.qioixiy.app.nfcStudentManagement.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.qioixiy.R;
import com.qioixiy.test.dialog.CustomDialog;
import com.qioixiy.test.dialog.CustomDialogTest;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {


    private final OkHttpClient client = new OkHttpClient();
    private EditText editAccount,editPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfcstudentmangement_login);


        editAccount=(EditText)findViewById(R.id.account_edittext);
        editPwd=(EditText)findViewById(R.id.password_edittext);

        Button btnLogin=(Button)findViewById(R.id.custom_login_button);
        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                String account = editAccount.getText().toString();
                String pwd = editPwd.getText().toString();
                if (account.equals("") || pwd.equals("")) {
                    CustomDialog.Builder dialog = new CustomDialog.Builder(LoginActivity.this);
                    dialog.setTitle("登录提示")
                            .setMessage("输入不正确")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    Toast.makeText(LoginActivity.this, "确定", Toast.LENGTH_SHORT).show();
                                }
                            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).create().show();
                    return;
                }
                new AnsyTry().execute(account, pwd);
            }
        });
    }

    private class AnsyTry extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            try {
                RequestBody formBody = new FormBody.Builder()
                        .add("account", params[0])
                        .add("pwd", params[1])
                        .build();

                Request request = new Request.Builder()
                        .url("http://baidu.com")
                        .post(formBody)
                        .build();

                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    return response.body().string();
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

            //TODO 此处判断返回值

            Intent intent = new Intent();
            intent.setClass(LoginActivity.this, MainActivity.class);
            finish();
        }
    }
}
