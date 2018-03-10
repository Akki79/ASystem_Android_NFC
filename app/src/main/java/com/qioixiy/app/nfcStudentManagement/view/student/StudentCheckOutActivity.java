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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.qioixiy.R;
import com.qioixiy.app.nfcStudentManagement.model.NetDataModel;

import org.json.JSONException;
import org.json.JSONObject;

import static com.qioixiy.utils.ByteArrayChange.ByteArrayToHexString;

public class StudentCheckOutActivity extends StudentCheckInActivity {

    private final String TAG = getClass().getSimpleName();

    @Override
    protected String getType() {
        return "check_out";
    }
}

