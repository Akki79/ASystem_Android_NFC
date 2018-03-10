package com.qioixiy.app.nfcStudentManagement.model;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.qioixiy.utils.ConstString.getServerString;

public class NetDataModel {
    private static String TAG = NetDataModel.class.getSimpleName();

    private static final OkHttpClient client = new OkHttpClient();

    public interface OnHttpRequestReturn {
        void onReturn(String response);
    }

    public static void sendHttpRequest(String... params) {
        sendHttpRequest(null, params);
    }

    public static void sendHttpRequest(OnHttpRequestReturn listener, String... params) {

        class CommonAsyncTask extends AsyncTask<String, Integer, String> {
            private String TAG = getClass().getSimpleName();
            private OnHttpRequestReturn listener = null;

            CommonAsyncTask(OnHttpRequestReturn listener) {
                this.listener = listener;
            }

            @Override
            protected String doInBackground(String... params) {
                try {
                    FormBody.Builder builder = new FormBody.Builder()
                            .add("func", params[0]);

                    for (int i = 1; i < params.length; i++) {
                        builder.add("param" + i, params[i]);
                    }

                    RequestBody formBody = builder.build();

                    Request request = new Request.Builder()
                            .url(getServerString())
                            .post(formBody)
                            .build();

                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        String rep = response.body().string();
                        Log.i(TAG, "response:" + rep);

                        if (listener != null) {
                            listener.onReturn(rep);
                        }

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

        new CommonAsyncTask(listener).execute(params);
    }

    public static String sendHttpRequestSync(String... params) {

        FormBody.Builder builder = new FormBody.Builder()
                .add("func", params[0]);

        for (int i = 1; i < params.length; i++) {
            builder.add("param" + i, params[i]);
        }

        RequestBody formBody = builder.build();

        Request request = new Request.Builder()
                .url(getServerString())
                .post(formBody)
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String rep = response.body().string();
                Log.i(TAG, "response:" + rep);
                return rep;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }
}