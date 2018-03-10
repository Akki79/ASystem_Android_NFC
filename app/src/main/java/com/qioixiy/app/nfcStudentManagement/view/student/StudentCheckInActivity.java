package com.qioixiy.app.nfcStudentManagement.view.student;

import android.Manifest;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.qioixiy.R;
import com.qioixiy.app.nfcStudentManagement.model.DynInfo;
import com.qioixiy.app.nfcStudentManagement.model.LoginModel;
import com.qioixiy.app.nfcStudentManagement.model.NetDataModel;

import org.json.JSONException;
import org.json.JSONObject;

import static com.qioixiy.utils.ByteArrayChange.ByteArrayToHexString;

public class StudentCheckInActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();
    private LocationManager locationManager;
    private String locationProvider;
    private Location location;

    private final int MSG_UPDATE_NFC_TAG = 0x01;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_UPDATE_NFC_TAG:
                    TextView textView = findViewById(R.id.nfc_tag);
                    textView.setText((String)msg.obj);

                    Integer studentId = LoginModel.instance().getUserId();
                    String nfcTag = (String)msg.obj;
                    String geo = "";
                    String type = getType();


                    JSONObject obj = new JSONObject();
                    try {
                        geo += location.getLatitude() + "," + location.getLongitude();
                        obj.put("studentId", studentId);
                        obj.put("nfcTag", nfcTag);
                        obj.put("geo", geo);
                        obj.put("type", type);

                        Log.i(TAG, obj.toString());

                        NetDataModel.sendHttpRequest(new NetDataModel.OnHttpRequestReturn() {
                            @Override
                            public void onReturn(String response) {

                            }
                        }, "dyn_info", "create", obj.toString());

                        showToast("签到成功");

                        finish();
                    } catch (JSONException e) {
                        showToast("签到失败");
                    }
                    break;
            }
        }
    };

    private NfcAdapter mNfcAdapter;
    private PendingIntent mPendingIntent;
    private Intent mIntent;
    private final int READER_FLAGS = NfcAdapter.FLAG_READER_NFC_A | NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK;
    private NfcAdapter.ReaderCallback mReaderCallback = new NfcAdapter.ReaderCallback() {
        @Override
        public void onTagDiscovered(Tag tag) {
            Log.i(TAG, tag.toString() + "id:" + ByteArrayToHexString(tag.getId()));

            Message msg = new Message();
            msg.what = MSG_UPDATE_NFC_TAG;
            msg.obj = ByteArrayToHexString(tag.getId());
            handler.sendMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_student_check);

        // 要申请的权限 数组 可以同时申请多个权限
        String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION};

        if (Build.VERSION.SDK_INT >= 23) {
            //如果超过6.0才需要动态权限，否则不需要动态权限
            //如果同时申请多个权限，可以for循环遍历
            int check = ContextCompat.checkSelfPermission(this,permissions[0]);
            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
            if (check == PackageManager.PERMISSION_GRANTED) {
                //写入你需要权限才能使用的方法
                //run();
            } else {
                //手动去请求用户打开权限(可以在数组中添加多个权限) 1 为请求码 一般设置为final静态变量
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            }
        } else {
            //写入你需要权限才能使用的方法
            //run();
        }

        locationManager = (LocationManager) getSystemService(getApplicationContext().LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);//低精度，如果设置为高精度，依然获取不了location。
        criteria.setAltitudeRequired(false);//不要求海拔
        criteria.setBearingRequired(false);//不要求方位
        criteria.setCostAllowed(true);//允许有花费
        criteria.setPowerRequirement(Criteria.POWER_LOW);//低功耗

        //从可用的位置提供器中，匹配以上标准的最佳提供器
        locationProvider = locationManager.getBestProvider(criteria, true);
        if (ActivityCompat.checkSelfPermission(getApplicationContext(),
            Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "onCreate: 没有权限 ");
            return;
        }
        Location location = locationManager.getLastKnownLocation(locationProvider);
        Log.d(TAG, "onCreate: " + (location == null) + "..");
        if (location != null) {
            Log.d(TAG, "onCreate: location");
            //不为空,显示地理位置经纬度
            showLocation(location);
        }
        //监视地理位置变化
        locationManager.requestLocationUpdates(locationProvider, 0, 0, locationListener);
    }

    /**
     * LocationListern监听器
     * 参数：地理位置提供器、监听位置变化的时间间隔、位置变化的距离间隔、LocationListener监听器
     */

    LocationListener locationListener = new LocationListener() {

        @Override
        public void onStatusChanged(String provider, int status, Bundle arg2) {

        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.d(TAG, "onProviderEnabled: " + provider + ".." + Thread.currentThread().getName());
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.d(TAG, "onProviderDisabled: " + provider + ".." + Thread.currentThread().getName());
        }

        @Override
        public void onLocationChanged(Location location) {
            Log.d(TAG, "onLocationChanged: " + ".." + Thread.currentThread().getName());
            //如果位置发生变化,重新显示
            showLocation(location);
        }
    };

    private void showLocation(Location location) {
        Log.d(TAG,"location：" + location.getLatitude() + "," + location.getLongitude());
        this.location = location;
    }

    public void showToast(String msg) {
        Toast.makeText(StudentCheckInActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initNfc();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onResume() {
        super.onResume();
        registerNfc();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onPause() {
        super.onPause();
        unRegisterNfc();
    }

    private void initNfc() {
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        mIntent = new Intent(NfcAdapter.ACTION_TECH_DISCOVERED);
        mIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        mPendingIntent = PendingIntent.getActivity(this, 0, mIntent, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void registerNfc() {
        Bundle bundle = new Bundle();
        mNfcAdapter.enableReaderMode(this, mReaderCallback, READER_FLAGS, bundle);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void unRegisterNfc() {
        mNfcAdapter.disableReaderMode(this);
    }

    protected String getType() {
        return "check_in";
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //回调，判断用户到底点击是还是否。
        //如果同时申请多个权限，可以for循环遍历
        if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //写入你需要权限才能使用的方法
            //run();
        } else {
            // 没有获取 到权限，从新请求，或者关闭app
            Toast.makeText(this,"需要获得XXX权限",Toast.LENGTH_SHORT).show();
        }
    }

    private int GPS_REQUEST_CODE = 10;

    /**
     * 检测GPS是否打开
     *
     * @return
     */
    private boolean checkGPSIsOpen() {
        boolean isOpen;
        LocationManager locationManager = (LocationManager) this
                .getSystemService(Context.LOCATION_SERVICE);
        isOpen = locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER);
        return isOpen;
    }

    /**
     * 跳转GPS设置
     */
    private void openGPSSettings() {
        if (checkGPSIsOpen()) {
            //initLocation(); //自己写的定位方法
        } else {
            //没有打开则弹出对话框
            new AlertDialog.Builder(this)
                    .setTitle(R.string.notifyTitle)
                    .setMessage(R.string.gpsNotifyMsg)
                    // 拒绝, 退出应用
                    .setNegativeButton(R.string.cancel,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            })

                    .setPositiveButton(R.string.setting,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //跳转GPS设置界面
                                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    startActivityForResult(intent, GPS_REQUEST_CODE);
                                }
                            })

                    .setCancelable(false)
                    .show();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GPS_REQUEST_CODE) {
            //做需要做的事情，比如再次检测是否打开GPS了 或者定位
            openGPSSettings();
        }
    }
}

