package com.qioixiy.mvpdemo.view;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.qioixiy.mvpdemo.presenter.MvpPresenter;
import com.qioixiy.nfc.asystem_android_nfc.R;


/*
 * http://www.jcodecraeer.com/a/anzhuokaifa/2017/1020/8625.html?1508484926
 */
public class MainActivity extends BaseActivity implements MvpView  {
    TextView text;
    MvpPresenter presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mvp_demo);
        text = (TextView)findViewById(R.id.text);
        //初始化Presenter
        presenter = new MvpPresenter();
        presenter.attachView(this);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //断开View引用
        presenter.detachView();
    }
    @Override
    public void showData(String data) {
        text.setText(data);
    }
    // button 点击事件调用方法
    public void getData(View view){
        presenter.getData("normal");
    }
    // button 点击事件调用方法
    public void getDataForFailure(View view){
        presenter.getData("failure");
    }
    // button 点击事件调用方法
    public void getDataForError(View view){
        presenter.getData("error");
    }
}