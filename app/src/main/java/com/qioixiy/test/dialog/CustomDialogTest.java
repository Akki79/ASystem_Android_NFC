package com.qioixiy.test.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.qioixiy.nfc.manager.R;

public class CustomDialogTest extends AppCompatActivity {

    public void dialog1(View view) {//弹出第一个对话框
        CustomDialog.Builder dialog = new CustomDialog.Builder(this);
        dialog.setTitle("测试一")
                .setMessage("普通文本提示信息对话框")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        Toast.makeText(CustomDialogTest.this, "确定", Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                Toast.makeText(CustomDialogTest.this, "取消", Toast.LENGTH_SHORT).show();
            }
        }).create().show();
    }

    public void dialog2(View view) {//弹出第二个对话框
        CustomDialog.Builder dialog = new CustomDialog.Builder(this);
        dialog.setTitle("测试二")
                .setMessage("密码错误")
                .setPositiveButton("重新输入", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        Toast.makeText(CustomDialogTest.this, "重新输入", Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                Toast.makeText(CustomDialogTest.this, "取消", Toast.LENGTH_SHORT).show();
            }
        }).create().show();
    }

    public void dialog3(View view) {//弹出第三个对话框
        View customView = View.inflate(this, R.layout.dialog_normal_layout, null);
        CustomDialog.Builder dialog = new CustomDialog.Builder(this);
        dialog.setTitle("测试三带有自定义View的布局")
                .setContentView(customView)//设置自定义customView
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        Toast.makeText(CustomDialogTest.this, "确定", Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                Toast.makeText(CustomDialogTest.this, "取消", Toast.LENGTH_SHORT).show();
            }
        }).create().show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_normal_test);
        initView();
    }

    private void initView() {
    }
}