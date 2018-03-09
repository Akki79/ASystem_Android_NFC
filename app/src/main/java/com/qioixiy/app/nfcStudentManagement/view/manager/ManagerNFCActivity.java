package com.qioixiy.app.nfcStudentManagement.view.manager;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.qioixiy.R;
import com.qioixiy.app.nfcStudentManagement.model.NfcTag;
import com.qioixiy.test.dialog.CustomDialog;
import com.qioixiy.test.listview.SlideListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.qioixiy.utils.ConstString.getServerString;

public class ManagerNFCActivity extends AppCompatActivity {
    private String TAG = getClass().getSimpleName();

    private SlideListView listView;
    private List<String> list = new ArrayList<String>();
    private List<NfcTag> mNfcList = new ArrayList<NfcTag>();
    private ManagerNFCListViewSlideAdapter mListViewAdapter;

    private final OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_nfc);

        fetchData();
        updateListView();
    }

    private void updateListView() {
        listView = (SlideListView) findViewById(R.id.list);
        mListViewAdapter = new ManagerNFCListViewSlideAdapter(this, mNfcList);
        listView.setAdapter(mListViewAdapter);
        mListViewAdapter.setOnClickListenerEditOrDelete(new ManagerNFCListViewSlideAdapter.OnClickListenerEditOrDelete() {
            @Override
            public void OnClickListenerEdit(int position) {
                showEditDialog(position);
            }

            @Override
            public void OnClickListenerDelete(int position) {

                JSONArray array = new JSONArray();
                array.put(mNfcList.get(position).getId().toString());
                array.toString();
                new deleteStudentAsyncTask().execute(array.toString());
                mNfcList.remove(position);
                mListViewAdapter.notifyDataSetChanged();
                Toast.makeText(ManagerNFCActivity.this,
                        "删除成功", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showEditDialog(final int position) {
        View customView = View.inflate(this, R.layout.dialog_nfc_manage_edit_layout, null);

        TextView name = customView.findViewById(R.id.name);
        TextView number = customView.findViewById(R.id.number);

        final NfcTag nfcTag = mNfcList.get(position);
        name.setText(nfcTag.getDefine());
        number.setText(nfcTag.getTag());

        final CustomDialog.Builder dialog = new CustomDialog.Builder(this);
        dialog.setTitle("NFC信息编辑")
                .setContentView(customView)//设置自定义customView
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();

                        TextView tag = dialog.getContentView().findViewById(R.id.number);
                        TextView define = dialog.getContentView().findViewById(R.id.name);

                        nfcTag.setTag(tag.getText().toString());
                        nfcTag.setDefine(define.getText().toString());

                        mListViewAdapter.notifyDataSetChanged();

                        try {
                            JSONObject json = new JSONObject();
                            json.put("id", nfcTag.getId());
                            json.put("tag", nfcTag.getTag());
                            json.put("define", nfcTag.getDefine());

                            new updateStudentAsyncTask().execute(json.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Toast.makeText(ManagerNFCActivity.this, "修改完成", Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                Toast.makeText(ManagerNFCActivity.this, "取消", Toast.LENGTH_SHORT).show();
            }
        }).create().show();
    }

    private void fetchData() {
        new fetchNFCAsyncTask().execute();
    }

    private class fetchNFCAsyncTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                RequestBody formBody = new FormBody.Builder()
                        .add("func", "nfc")
                        .add("param1", "viewall")
                        .build();

                Request request = new Request.Builder()
                        .url(getServerString())
                        .post(formBody)
                        .build();

                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    String rep = response.body().string();
                    Log.i(TAG, "response:" + rep);
                    return rep;
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            try {
                JSONObject json = new JSONObject(result);
                JSONArray array = json.getJSONArray("items");

                for (int index = 0; index < array.length(); index++) {
                    JSONObject obj = array.getJSONObject(index);

                    int id = obj.getInt("id");
                    String tag = obj.getString("tag");
                    String define = obj.getString("define");

                    NfcTag nfcTag = new NfcTag();
                    nfcTag.setId(id);
                    nfcTag.setTag(tag);
                    nfcTag.setDefine(define);

                    mNfcList.add(nfcTag);
                }

                updateListView();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class updateStudentAsyncTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                RequestBody formBody = new FormBody.Builder()
                        .add("func", "nfc")
                        .add("param1", "modify")
                        .add("param2", params[0])
                        .build();

                Request request = new Request.Builder()
                        .url(getServerString())
                        .post(formBody)
                        .build();

                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    String rep = response.body().string();
                    Log.i(TAG, "response:" + rep);
                    return rep;
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
        }
    }

    private class deleteStudentAsyncTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                RequestBody formBody = new FormBody.Builder()
                        .add("func", "nfc")
                        .add("param1", "delete")
                        .add("param2", params[0])
                        .build();

                Request request = new Request.Builder()
                        .url(getServerString())
                        .post(formBody)
                        .build();

                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    String rep = response.body().string();
                    Log.i(TAG, "response:" + rep);
                    return rep;
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
        }
    }
}
