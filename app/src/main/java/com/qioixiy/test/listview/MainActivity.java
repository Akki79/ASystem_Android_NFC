package com.qioixiy.test.listview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.qioixiy.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private SlideListView listView;
    private List<String> list = new ArrayList<String>();
    private ListViewSlideAdapter listViewSlideAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_delete_edit);
        getData();
        initView();
    }

    private void initView() {
        listView = (SlideListView) findViewById(R.id.list);
        listViewSlideAdapter = new ListViewSlideAdapter(this, list);
        listView.setAdapter(listViewSlideAdapter);
        listViewSlideAdapter.setOnClickListenerEditOrDelete(new ListViewSlideAdapter.OnClickListenerEditOrDelete() {
            @Override
            public void OnClickListenerEdit(int position) {
                Toast.makeText(MainActivity.this, "edit position: " + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void OnClickListenerDelete(int position) {
                Toast.makeText(MainActivity.this, "delete position: " + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getData() {
        for (int i = 0; i < 20; i++) {
            list.add(new String("第" + i + "个item"));
        }
    }
}