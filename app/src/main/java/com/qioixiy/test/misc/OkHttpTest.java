package com.qioixiy.test.misc;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.qioixiy.R;
import com.qioixiy.app.nfcStudentManagement.app.App;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttpTest extends Activity {
    private final String TAG = this.getClass().getSimpleName();

    private EditText et_username, et_code, et_identy;
    private Button btn_login, btn_changeImg;
    private ImageView img_identy;
    private Context context;
    private OkHttpClient okHttpClient;
    public String s;
    private static final int SUCCESS = 1;
    private static final int FALL = 2;
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) { //加载网络成功进行UI的更新,处理得到的图片资源
                case SUCCESS: //通过message，拿到字节数组
                    byte[] Picture = (byte[]) msg.obj;
                    //使用BitmapFactory工厂，把字节数组转化为bitmap
                    Bitmap bitmap = BitmapFactory.decodeByteArray(Picture, 0, Picture.length);
                    //通过imageview，设置图片
                    img_identy.setImageBitmap(bitmap);
                    break;
                //当加载网络失败执行的逻辑代码
                case FALL:
                    Toast.makeText(OkHttpTest.this, "网络出现了问题", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        context = getApplicationContext();
        ChangeImage();
        btn_changeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeImage();
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginServer();
            }
        });
    }

    private void LoginServer() {
        Log.i(TAG, "知道了session：" + s);
        OkHttpClient client = new OkHttpClient();
        FormBody body = new FormBody.Builder().add("userName", et_username.getText().toString()).add("password", et_code.getText().toString()).add("randCode", et_identy.getText().toString()).add("langCode", "zh-cn").build();
        Request request = new Request.Builder().addHeader("cookie", s).url(App.url_login).post(body).build();
        Call call2 = okHttpClient.newCall(request);
        call2.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("info_call2fail", e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    Log.i("info_call2success", response.body().string());
                }
                Headers headers = response.headers();
                Log.i("info_respons.headers", headers + "");
            }
        });
    }

    private void ChangeImage() {
        String s = "App.url_randCodeImage";
        Request request = new Request.Builder().url(s).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("info_callFailure", e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                byte[] byte_image = response.body().bytes(); //通过handler更新UI Message message = handler.obtainMessage(); message.obj = byte_image; message.what = SUCCESS; Log.i("info_handler","handler"); handler.sendMessage(message); //session
                Headers headers = response.headers();
                Log.d("info_headers", "header " + headers);
                List<String> cookies = headers.values("Set-Cookie");
                String session = cookies.get(0);
                Log.d("info_cookies", "onResponse-size: " + cookies);
                String s = session.substring(0, session.indexOf(";"));
                Log.i("info_s", "session is :" + s);
            }
        });
    }

    private void init() {
//        et_username = (EditText) findViewById(R.id.et_username);
//        et_code = (EditText) findViewById(R.id.et_code);
//        et_identy = (EditText) findViewById(R.id.et_identy);
//        btn_login = (Button) findViewById(R.id.btn_login);
//        img_identy = (ImageView) findViewById(R.id.img_identy);
//        btn_changeImg = (Button) findViewById(R.id.btn_changeImg);
        okHttpClient = new OkHttpClient();
    }
}
