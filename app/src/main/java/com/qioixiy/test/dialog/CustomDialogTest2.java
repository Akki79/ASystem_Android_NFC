package com.qioixiy.test.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.qioixiy.nfc.manager.R;

class MikyouCommonDialog {
    /**
 * @自定义封装的通用对话框:MikyouCommonDialog
 *
 * Created by mikyou on 16-7-21.
 * @param dialogTitle 对话框的标题
 * @param dialogMessage 对话框的内容
 * @param positiveText 表示是确定意图的按钮上text文本
 * @param negativeText 表示是取消意图的按钮上text文本
 * @param customeLayoutId 对话框自定义布局的id 若没有自定义布局　　默认传入0
 * @param context 上下文对象
 * @param dialogView 自定义布局的View对象,提供被外界访问的接口get和set方法,并且利用自定义的监听器将view对象回调出去
 * @param listener 监听器　用于将确定和取消意图的两个点击事件监听器，合成一个监听器，并传入一个标记变量区别确定和取消意图
 */
    private Context context;
    private int customeLayoutId;
    private String dialogTitle;
    private String dialogMessage;
    private String positiveText;
    private String negativeText;

    private View dialogView;
    private OnDialogListener listener;
    //带有自定义view的构造器
    public MikyouCommonDialog(Context context, int customeLayoutId, String dialogTitle, String positiveText, String negativeText) {
        this.context = context;
        this.customeLayoutId = customeLayoutId;
        this.dialogTitle = dialogTitle;
        this.positiveText = positiveText;
        this.negativeText = negativeText;
        this.dialogView=View.inflate(context,customeLayoutId,null);
    }
    //不带自定义view的构造器
    public MikyouCommonDialog(Context context, String dialogMessage,String dialogTitle, String positiveText, String negativeText) {
        this.context = context;
        this.dialogTitle = dialogTitle;
        this.dialogMessage = dialogMessage;
        this.positiveText = positiveText;
        this.negativeText = negativeText;
    }

    public View getDialogView() {
        return dialogView;
    }

    public void setDialogView(View dialogView) {
        this.dialogView = dialogView;
    }

    public void showDialog(){
        CustomDialog.Builder dialog=new CustomDialog.Builder(context);
        dialog.setTitle(dialogTitle);//设置标题
        //注意:dialogMessage和dialogView是互斥关系也就是dialogMessage存在dialogView就不存在,dialogView不存在dialogMessage就存在
        if (dialogMessage!=null){
            Toast.makeText(context, "sddsdsds", Toast.LENGTH_SHORT).show();
            dialog.setMessage(dialogMessage);//设置对话框内容
        }else{

            dialog.setContentView(dialogView);//设置对话框的自定义view对象
        }
        /**
         * 尽管有两个点击事件监听器,可以通过我们自定义的监听器设置一个标记变量,从而可以实现将两个点击事件合并成一个
         * 监听器OnDialogListener
         * */
        //确定意图传入positive标记值
        dialog.setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
                if (listener!=null){
                    listener.dialogListener("positive",dialogView,dialogInterface,which);
                }
            }
            //取消意图传入negative标记值
        }).setNegativeButton(negativeText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
                if (listener!=null){
                    listener.dialogListener("negative",dialogView,dialogInterface,which);
                }
            }
        }).create().show();
    }
    //注册监听器方法
    public MikyouCommonDialog setOnDiaLogListener(OnDialogListener listener){
        this.listener=listener;
        return this;//把当前对象返回,用于链式编程
    }
    //定义一个监听器接口
    public interface OnDialogListener{
        //customView　这个参数需要注意就是如果没有自定义view,那么它则为null
        public void dialogListener(String btnType, View customView, DialogInterface dialogInterface, int which);
    }
}

class MikyouCommonDialog2 {

    /**
     * @自定义封装的通用对话框:MikyouCommonDialog
     *
     * Created by mikyou on 16-7-21.
     * @param dialogTitle 对话框的标题
     * @param dialogMessage 对话框的内容
     * @param positiveText 表示是确定意图的按钮上text文本
     * @param negativeText 表示是取消意图的按钮上text文本
     * @param customeLayoutId 对话框自定义布局的id 若没有自定义布局　　默认传入0
     * @param context 上下文对象
     * @param dialogView 自定义布局的View对象,提供被外界访问的接口get和set方法,并且利用自定义的监听器将view对象回调出去
     * @param listener 监听器　用于将确定和取消意图的两个点击事件监听器，合成一个监听器，并传入一个标记变量区别确定和取消意图
     */
    private Context context;
    private int customeLayoutId;
    private String dialogTitle;
    private String dialogMessage;
    private String positiveText;
    private String negativeText;

    private View dialogView;
    private OnDialogListener listener;
    //带有自定义view的构造器
    public MikyouCommonDialog2(Context context, int customeLayoutId, String dialogTitle, String positiveText, String negativeText) {
        this.context = context;
        this.customeLayoutId = customeLayoutId;
        this.dialogTitle = dialogTitle;
        this.positiveText = positiveText;
        this.negativeText = negativeText;
        this.dialogView=View.inflate(context,customeLayoutId,null);
    }
    //不带自定义view的构造器
    public MikyouCommonDialog2(Context context, String dialogMessage, String dialogTitle, String positiveText, String negativeText) {
        this.context = context;
        this.dialogTitle = dialogTitle;
        this.dialogMessage = dialogMessage;
        this.positiveText = positiveText;
        this.negativeText = negativeText;
    }

    public View getDialogView() {
        return dialogView;
    }

    public void setDialogView(View dialogView) {
        this.dialogView = dialogView;
    }

    public void showDialog(){
        CustomDialog.Builder dialog=new CustomDialog.Builder(context);
        dialog.setTitle(dialogTitle);//设置标题
        //注意:dialogMessage和dialogView是互斥关系也就是dialogMessage存在dialogView就不存在,dialogView不存在dialogMessage就存在
        if (dialogMessage!=null){
            dialog.setMessage(dialogMessage);//设置对话框内容
        }else{
            dialog.setContentView(dialogView);//设置对话框的自定义view对象
        }
        dialog.setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
                if (listener!=null){
                    listener.dialogPositiveListener(dialogView,dialogInterface,which);
                }
            }
        }).setNegativeButton(negativeText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
                if (listener!=null){
                    listener.dialogNegativeListener(dialogView,dialogInterface,which);
                }
            }
        }).create().show();
    }
    //注册监听器方法
    public MikyouCommonDialog2 setOnDiaLogListener(OnDialogListener listener){
        this.listener=listener;
        return this;//把当前对象返回,用于链式编程
    }
    //定义一个监听器接口
    public interface OnDialogListener{
        //customView　这个参数需要注意就是如果没有自定义view,那么它则为null
        public void dialogPositiveListener(View customView, DialogInterface dialogInterface, int which);
        public void dialogNegativeListener(View customView, DialogInterface dialogInterface, int which);
    }
}

public class CustomDialogTest2 extends AppCompatActivity {

    public void dialog1(View view) {//弹出第一个对话框
        new MikyouCommonDialog(this, "普通文本提示信息对话框", "测试一", "确定", "取消")
                .setOnDiaLogListener(new MikyouCommonDialog.OnDialogListener() {
                    @Override
                    public void dialogListener(String btnType, View customView, DialogInterface dialogInterface, int which) {
                        switch (btnType) {
                            case "positive":
                                Toast.makeText(CustomDialogTest2.this, "确定", Toast.LENGTH_SHORT).show();
                                break;
                            case "negative":
                                Toast.makeText(CustomDialogTest2.this, "取消", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                }).showDialog();
    }

    public void dialog2(View view) {//弹出第二个对话框
        new MikyouCommonDialog(this, "密码错误", "测试二", "重新输入", "取消")
                .setOnDiaLogListener(new MikyouCommonDialog.OnDialogListener() {
                    @Override
                    public void dialogListener(String btnType, View customView, DialogInterface dialogInterface, int which) {
                        switch (btnType) {
                            case "positive":
                                Toast.makeText(CustomDialogTest2.this, "重新输入", Toast.LENGTH_SHORT).show();
                                break;
                            case "negative":
                                Toast.makeText(CustomDialogTest2.this, "取消", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                }).showDialog();
    }

    public void dialog3(View view) {//弹出第三个对话框
        new MikyouCommonDialog(this, R.layout.dialog_normal_layout, "测试三带有自定义View的布局", "确定", "取消")
                .setOnDiaLogListener(new MikyouCommonDialog.OnDialogListener() {
                    @Override
                    public void dialogListener(String btnType, View customView, DialogInterface dialogInterface, int which) {
                        switch (btnType) {
                            case "positive":
                                Toast.makeText(CustomDialogTest2.this, "确定", Toast.LENGTH_SHORT).show();
                                break;
                            case "negative":
                                Toast.makeText(CustomDialogTest2.this, "取消", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                }).showDialog();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_normal_test);
        initView();
    }

    private void initView() {
    }

    private void popInputPwdDialog() {
        new MikyouCommonDialog2(this,R.layout.dialog_normal_layout,"输入安全密码","确定","取消")
                .setOnDiaLogListener(new MikyouCommonDialog2.OnDialogListener() {
                    @Override
                    public void dialogPositiveListener(View customView, DialogInterface dialogInterface, int which) {//点击确定按钮的操作
                        EditText eTPwd=(EditText) customView.findViewById(R.id.message);
                        //checkAndSavePwd("input",eTPwd.getText().toString(),"");
                    }
                    @Override
                    public void dialogNegativeListener(View customView, DialogInterface dialogInterface, int which) {//点击取消按钮的操作
                        //
                    }
                }).showDialog();
    }
}