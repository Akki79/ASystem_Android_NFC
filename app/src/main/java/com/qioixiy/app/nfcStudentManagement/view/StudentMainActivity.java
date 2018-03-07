package com.qioixiy.app.nfcStudentManagement.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.qioixiy.R;

import java.util.ArrayList;
import java.util.List;

public class StudentMainActivity extends AppCompatActivity {
    public class Item {
        private String name;
        private int imageId;

        public Item(String name, int imageId) {
            this.name = name;
            this.imageId = imageId;
        }

        public String getName() {
            return name;
        }

        public int getImageId() {
            return imageId;
        }
    }

    public class MyListViewAdapter extends ArrayAdapter {
        private final int resourceId;

        public MyListViewAdapter(Context context, int textViewResourceId, List<Item> objects) {
            super(context, textViewResourceId, objects);
            resourceId = textViewResourceId;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Item item = (Item) getItem(position); // 获取当前项的Fruit实例
            View view = LayoutInflater.from(getContext()).inflate(resourceId, null);//实例化一个对象
            TextView name = (TextView) view.findViewById(R.id.name);//获取该布局内的文本视图
            name.setText(item.getName());//为文本视图设置文本内容
            //ImageView imageView = (ImageView) view.findViewById(R.id.image);//获取该布局内的图片视图
            //imageView.setImageResource(item.getImageId());//为图片视图设置图片资源

            return view;
        }
    }

    private List<Item> items = new ArrayList<Item>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main);

        initFruits(); // 初始化水果数据
        MyListViewAdapter adapter = new MyListViewAdapter(StudentMainActivity.this, R.layout.item_student, items);
        ListView listView = (ListView) findViewById(R.id.student_list_view);
        listView.setAdapter(adapter);
    }

    private void initFruits() {
        Item item1 = new Item("上传动态信息", R.drawable.avator);
        items.add(item1);
        Item item2 = new Item("查看动态信息", R.drawable.avator);
        items.add(item2);
    }
}
