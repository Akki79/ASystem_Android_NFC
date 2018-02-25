package com.qioixiy.app.nfcStudentManagement.view;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.qioixiy.R;
import com.qioixiy.test.listview.*;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.qioixiy.utils.ConstString.getServerString;

public class ManagerStudentActivity extends AppCompatActivity {
    private String TAG = getClass().getSimpleName();

    private SlideListView listView;
    private List<String> list = new ArrayList<String>();
    private ListViewSlideAdapter listViewSlideAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_student);

        initData();
        initView();
    }

    private void initView() {
        listView = (SlideListView) findViewById(R.id.list);
        listViewSlideAdapter = new ListViewSlideAdapter(this, list);
        listView.setAdapter(listViewSlideAdapter);
        listViewSlideAdapter.setOnClickListenerEditOrDelete(new ListViewSlideAdapter.OnClickListenerEditOrDelete() {
            @Override
            public void OnClickListenerEdit(int position) {
                Toast.makeText(ManagerStudentActivity.this, "edit position: " + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void OnClickListenerDelete(int position) {
                Toast.makeText(ManagerStudentActivity.this, "delete position: " + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initData() {
        new getStudentAsyncTask().execute();

        for (int i = 0; i < 20; i++) {
            list.add(new String("第" + i + "个item"));
        }
    }

    private final OkHttpClient client = new OkHttpClient();
    private class getStudentAsyncTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                RequestBody formBody = new FormBody.Builder()
                        .add("func", "student")
                        .add("param1", "viewall")
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
                boolean res = json.getBoolean("result");
                if (res) {
                    Intent intent = new Intent(ManagerStudentActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(ManagerStudentActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
