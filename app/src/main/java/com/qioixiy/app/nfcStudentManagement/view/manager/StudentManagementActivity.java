package com.qioixiy.app.nfcStudentManagement.view.manager;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.qioixiy.R;
import com.qioixiy.app.nfcStudentManagement.model.Student;
import com.qioixiy.test.dialog.CustomDialog;
import com.qioixiy.test.listview.*;

import org.json.JSONArray;
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

public class StudentManagementActivity extends AppCompatActivity {
    private String TAG = getClass().getSimpleName();

    private SlideListView listView;
    private List<String> list = new ArrayList<String>();
    private List<Student> mStudentList = new ArrayList<Student>();
    private ManagerStudentListViewSlideAdapter mListViewAdapter;

    private final OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_student);

        fetchData();
        updateListView();
    }

    private void updateListView() {
        listView = (SlideListView) findViewById(R.id.list);
        mListViewAdapter = new ManagerStudentListViewSlideAdapter(this, mStudentList);
        listView.setAdapter(mListViewAdapter);
        mListViewAdapter.setOnClickListenerEditOrDelete(new ManagerStudentListViewSlideAdapter.OnClickListenerEditOrDelete() {
            @Override
            public void OnClickListenerEdit(int position) {
                showEditDialog(position);
            }

            @Override
            public void OnClickListenerDelete(int position) {

                JSONArray array = new JSONArray();
                array.put(mStudentList.get(position).getId().toString());
                array.toString();
                new deleteStudentAsyncTask().execute(array.toString());
                mStudentList.remove(position);
                mListViewAdapter.notifyDataSetChanged();
                Toast.makeText(StudentManagementActivity.this,
                        "删除成功", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showEditDialog(final int position) {
        View customView = View.inflate(this, R.layout.dialog_student_manage_edit_layout, null);

        TextView name = customView.findViewById(R.id.name);
        TextView number = customView.findViewById(R.id.number);
        TextView telphone = customView.findViewById(R.id.telphone);
        TextView email = customView.findViewById(R.id.email);

        final Student student = mStudentList.get(position);
        name.setText(student.getName());
        number.setText(student.getNumber());
        telphone.setText(student.getTelphone());
        email.setText(student.getEmail());

        final CustomDialog.Builder dialog = new CustomDialog.Builder(this);
        dialog.setTitle("学员信息编辑")
                .setContentView(customView)//设置自定义customView
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();

                        TextView name = dialog.getContentView().findViewById(R.id.name);
                        TextView number = dialog.getContentView().findViewById(R.id.number);
                        TextView telphone = dialog.getContentView().findViewById(R.id.telphone);
                        TextView email = dialog.getContentView().findViewById(R.id.email);

                        student.setName(name.getText().toString());
                        student.setNumber(number.getText().toString());
                        student.setTelphone(telphone.getText().toString());
                        student.setEmail(email.getText().toString());

                        mListViewAdapter.notifyDataSetChanged();

                        try {
                            JSONObject json = new JSONObject();
                            json.put("id", student.getId());
                            json.put("name", student.getName());
                            json.put("number", student.getNumber());
                            json.put("telphone", student.getTelphone());
                            json.put("email", student.getEmail());

                            new updateStudentAsyncTask().execute(json.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Toast.makeText(StudentManagementActivity.this, "修改完成", Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                Toast.makeText(StudentManagementActivity.this, "取消", Toast.LENGTH_SHORT).show();
            }
        }).create().show();
    }

    private void fetchData() {
        new fetchStudentAsyncTask().execute();
    }

    private class fetchStudentAsyncTask extends AsyncTask<String, Integer, String> {

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
                JSONArray array = json.getJSONArray("items");

                for (int index = 0; index < array.length(); index++) {
                    JSONObject obj = array.getJSONObject(index);

                    int id = obj.getInt("id");
                    String name = obj.getString("name");
                    String number = obj.getString("number");
                    String telphone = obj.getString("telphone");
                    String email = obj.getString("email");

                    Student student = new Student();
                    student.setId(id);
                    student.setName(name);
                    student.setNumber(number);
                    student.setTelphone(telphone);
                    student.setEmail(email);

                    mStudentList.add(student);
                }

                updateListView();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class updateStudentAsyncTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                RequestBody formBody = new FormBody.Builder()
                        .add("func", "student")
                        .add("param1", "modify")
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

    private class deleteStudentAsyncTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                RequestBody formBody = new FormBody.Builder()
                        .add("func", "student")
                        .add("param1", "delete")
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
