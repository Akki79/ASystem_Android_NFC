package com.qioixiy.utils;

import android.os.Message;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Geocoder {
    List<String> getFromLocation(String lng, String lat) {
        List<String> ret = new ArrayList<>();

        String url = "http://localhost:8080/asystem/utils/geo?func=getAddress&location=31.2325992433,111.5045631813";
        url = "http://localhost:8080/asystem/utils/geo?func=getAddress&location=" + lng + ","+ lat;

        OkHttpClient client = new OkHttpClient.Builder().readTimeout(5, TimeUnit.SECONDS).build();
        Request request = new Request.Builder().url(url)
                .get().build();
        Call call = client.newCall(request);
        try {
            Response response = call.execute();
            System.out.println(response.body().string());

            JSONObject jsonObject = new JSONObject(response.body().string());
            // 获取result节点下的位置信息
            JSONArray resultArray = jsonObject.getJSONArray("result");
            if (resultArray.length() > 0) {
                JSONObject subObject = resultArray.getJSONObject(0);
                // 取出格式化后的位置信息
                String address = subObject.getString("formatted_address");
                ret.add(address);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ret;
    }
}
