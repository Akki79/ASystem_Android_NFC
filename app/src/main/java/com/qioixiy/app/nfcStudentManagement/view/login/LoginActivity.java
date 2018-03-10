package com.qioixiy.app.nfcStudentManagement.view.login;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.qioixiy.R;
import com.qioixiy.app.nfcStudentManagement.view.manager.ManagerMainActivity;
import com.qioixiy.app.nfcStudentManagement.view.student.StudentMainActivity;
import com.qioixiy.test.dialog.CustomDialog;
import com.qioixiy.utils.ConstString;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.qioixiy.utils.ConstString.getServerString;

public class LoginActivity extends AppCompatActivity {
    private final String TAG = getClass().getSimpleName();

    private final OkHttpClient client = new OkHttpClient();
    private EditText editAccount, editPwd;
    private boolean needCheckLogin = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfcstudentmangement_login);

        editAccount = (EditText) findViewById(R.id.account_edittext);
        editPwd = (EditText) findViewById(R.id.password_edittext);

        Button btnLogin = (Button) findViewById(R.id.custom_login_button);
        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (!needCheckLogin) {
                    startMainActivity("manager");
                    return;
                }
                String account = editAccount.getText().toString();
                String pwd = editPwd.getText().toString();
                if (account.equals("") || pwd.equals("")) {
                    //showLoginFailedDialog();
                    Toast.makeText(LoginActivity.this, "用户名或者密码为空", Toast.LENGTH_SHORT).show();
                } else {
                    new LoginAsyncTask().execute(account, pwd);
                }
            }
        });

        Spinner serverListSpinner = findViewById(R.id.serverListSpinner);
        serverListSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getItemAtPosition(position).toString();
                ConstString.setServerPrefix(selected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //未选中时候的操作
            }
        });

        CheckBox loginCheckBox = findViewById(R.id.loginCheckBox);
        loginCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                needCheckLogin = !isChecked;
            }
        });
    }

    protected void onResume() {
        super.onResume();
        needCheckLogin = true;
    }

    private void showLoginFailedDialog() {
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
    }

    private void startMainActivity(String userType) {
        if (userType.equals("manager")) {
            Intent intent = new Intent(LoginActivity.this, ManagerMainActivity.class);
            startActivity(intent);
        } else if (userType.equals("student")) {
            Intent intent = new Intent(LoginActivity.this, StudentMainActivity.class);
            startActivity(intent);
        }

        //finish();
    }

    private class LoginAsyncTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                RequestBody formBody = new FormBody.Builder()
                        .add("func", "login")
                        .add("account", params[0])
                        .add("pwd", params[1])
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

            try {
                JSONObject json = new JSONObject(result);
                JSONObject obj = json.getJSONObject("result");
                boolean result2 = obj.getBoolean("result");
                String userType = obj.getString("userType");

                if (result2) {
                    startMainActivity(userType);
                } else {
                    Toast.makeText(LoginActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
