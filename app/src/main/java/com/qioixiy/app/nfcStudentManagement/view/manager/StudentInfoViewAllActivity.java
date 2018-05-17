package com.qioixiy.app.nfcStudentManagement.view.manager;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;
import android.content.Intent;
import android.widget.Button;

import com.qioixiy.R;
import com.qioixiy.app.nfcStudentManagement.model.DataModel;
import com.qioixiy.app.nfcStudentManagement.model.DynInfo;
import com.qioixiy.app.nfcStudentManagement.model.LoginModel;
import com.qioixiy.app.nfcStudentManagement.model.NetDataModel;
import com.qioixiy.app.nfcStudentManagement.model.NfcTag;
import com.qioixiy.app.nfcStudentManagement.model.Student;
import com.qioixiy.app.nfcStudentManagement.view.student.StudentListInfoViewAdapter;
import com.qioixiy.test.dialog.CustomDialog;
import com.qioixiy.utils.Geocoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.qioixiy.utils.ByteArrayChange.ByteArrayToHexString;

public class StudentInfoViewAllActivity extends AppCompatActivity {
    private List<Student> studentList = null;
    private List<DynInfo> dynInfoList = null;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    List<Student> students = (List<Student>)msg.obj;
                    studentList = students;
                    List<String> items = new ArrayList<>();
                    for (Student student : students) {
                        items.add(student.getName());
                    }
                    StudentListInfoViewAdapter adapter = new StudentListInfoViewAdapter(items);
                    adapter.setOnItemClickListener(new StudentListInfoViewAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            showStudentInfoDialog(position);
                        }
                    });
                    recyclerView.setAdapter(adapter);
                    break;
                default:
                    break;
            }
        }
    };

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_info_view_all);

        initUI();
        initData();
    }

    private void initData() {
        new AsyncTask<String, Integer, String>() {
            @Override
            protected String doInBackground(String... strings) {
                List<Student> students = DataModel.getStudentList();
                dynInfoList = DataModel.getDynInfoList();

                Message msg = new Message();
                msg.what = 1;
                msg.obj = students;
                handler.sendMessage(msg);
                return null;
            }
        }.execute();
    }

    private void initUI() {
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        List<String> info = new ArrayList<String>();
        info.add("加载中");
        StudentListInfoViewAdapter adapter = new StudentListInfoViewAdapter(info);
        recyclerView.setAdapter(adapter);
    }

    private String getCheckStateFromDynInfo(int studentId) {
        String ret = "未知";
        for(DynInfo dynInfo : dynInfoList) {
            if (dynInfo.getStudentId() == studentId) {
                ret = dynInfo.getCheckState();
            }
        }

        return ret;
    }

    private String getLocationFromDynInfo(int studentId) {
        String ret = "未知";
        for(DynInfo dynInfo : dynInfoList) {
            if (dynInfo.getStudentId() == studentId) {
                ret = dynInfo.getLocation();
            }
        }

        return ret;
    }

    private void showStudentInfoDialog(final int position) {
        View customView = View.inflate(this, R.layout.dialog_student_manage_view_layout, null);

        TextView name = customView.findViewById(R.id.name);
        TextView number = customView.findViewById(R.id.number);
        TextView telphone = customView.findViewById(R.id.telphone);
        TextView email = customView.findViewById(R.id.email);
        TextView location = customView.findViewById(R.id.location);
        TextView checkState = customView.findViewById(R.id.checkState);

        Button btn = customView.findViewById(R.id.button_map_view);
        btn.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentInfoViewAllActivity.this, StoreMapActivity.class);

                String geo = dynInfoList.get(position).getGeo();
                String[] lan = geo.split(",");
                intent.putExtra("latx", Float.parseFloat(lan[0]));
                intent.putExtra("laty", Float.parseFloat(lan[1]));
                startActivity(intent);
            }
        });

        Student student = studentList.get(position);

        name.setText(student.getName());
        number.setText(student.getNumber());
        telphone.setText(student.getTelphone());
        email.setText(student.getEmail());
        location.setText(getLocationFromDynInfo(student.getId()));
        checkState.setText(getCheckStateFromDynInfo(student.getId()));

        final CustomDialog.Builder dialog = new CustomDialog.Builder(this);
        dialog.setTitle("学员信息编辑")
                .setContentView(customView)//设置自定义customView
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create().show();
    }
}
