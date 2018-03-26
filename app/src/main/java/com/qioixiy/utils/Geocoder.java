package com.qioixiy.utils;

import android.os.Message;
import android.util.Log;

import com.qioixiy.app.nfcStudentManagement.model.NetDataModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.qioixiy.utils.ConstString.getServerString;

public class Geocoder {

    private static String TAG = NetDataModel.class.getSimpleName();

    public List<String> getFromLocation(String lng, String lat) {
        List<String> ret = new ArrayList<>();

        String url = "http://localhost:8080/asystem/utils/geo?func=getAddress&location=31.2325992433,111.5045631813";
        String location = lng + "," + lat;
        url = ConstString.getServerPrefix() + "/asystem/utils/geo";

        FormBody.Builder builder = new FormBody.Builder()
                .add("func", "getAddress")
                .add("location", location);

        RequestBody formBody = builder.build();

        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        Response response = null;
        try {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(3, TimeUnit.SECONDS)
                    .readTimeout(3, TimeUnit.SECONDS).build();
            response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String rep = response.body().string();
                Log.i(TAG, "response:" + rep);

                String rep2 = rep.replace("\\", "");
                String rep3 = rep2.substring(rep2.indexOf("{"), rep2.lastIndexOf("}") + 1);

                JSONObject jsonObject = new JSONObject(rep3);
                String address = null;
                JSONObject obj1 = jsonObject.getJSONObject("result");
                address = obj1.getString("formatted_address");

                ret.add(address);
            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ret;
    }
}
