package com.qioixiy.app.nfcStudentManagement.view;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;

import com.qioixiy.R;

public class AddNFCActivity extends AppCompatActivity {
    private NfcAdapter mNfcAdapter;
    private PendingIntent mPendingIntent;
    private Intent mIntent;
    private final int READER_FLAGS = NfcAdapter.FLAG_READER_NFC_A | NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK;
    private NfcAdapter.ReaderCallback mReaderCallback = new NfcAdapter.ReaderCallback() {
        @Override
        public void onTagDiscovered(Tag tag) {
            System.out.println(tag.toString());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_nfcstudentmangement_add_nfc);
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

}

