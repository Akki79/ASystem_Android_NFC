package com.qioixiy.app.nfcStudentManagement.view.student;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
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
                        obj.put("StudentId", studentId);
                        obj.put("nfcTag", nfcTag);
                        obj.put("geo", geo);
                        obj.put("type", type);

                        NetDataModel.sendHttpRequest("dyn_info", "create", obj.toString());

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
}

