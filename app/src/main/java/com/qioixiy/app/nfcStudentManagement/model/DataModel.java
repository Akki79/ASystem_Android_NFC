package com.qioixiy.app.nfcStudentManagement.model;

import android.os.Message;

import com.qioixiy.app.nfcStudentManagement.view.student.StudentListInfoViewAdapter;
import com.qioixiy.utils.Geocoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DataModel {
    public static BaseModel request(String token){
        // 声明一个空的BaseModel
        BaseModel model = null;
        try {
            //利用反射机制获得对应Model对象的引用
            model = (BaseModel)Class.forName(token).newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return model;
    }

    public static List<Student> getStudentList() {
        String result = NetDataModel.sendHttpRequestSync("student", "viewall");

        ArrayList<Student> mStudentList = new ArrayList<Student>();

        JSONObject json = null;
        try {
            json = new JSONObject(result);
            JSONArray array = json.getJSONArray("items");

            for (int index = 0; index < array.length(); index++) {
                JSONObject obj = array.getJSONObject(index);

                int id = obj.getInt("id");
                String name = obj.getString("name");
                String number = obj.getString("number");
                String telphone = obj.getString("telphone");
                String email = obj.getString("email");

                Student student = new Student();
                student.setId(id);
                student.setName(name);
                student.setNumber(number);
                student.setTelphone(telphone);
                student.setEmail(email);

                mStudentList.add(student);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return mStudentList;
    }

    public static List<NfcTag> getNfcTagList() {
        String responseNfcTag = NetDataModel.sendHttpRequestSync("nfc", "viewall");
        JSONObject json = null;
        // NfcTags
        List<NfcTag> mNfcList = new ArrayList<>();
        try {
            json = new JSONObject(responseNfcTag);
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
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return mNfcList;
    }

    public static List<DynInfo> getDynInfoList() {
        Geocoder geocoder = new Geocoder();

        String response = NetDataModel.sendHttpRequestSync("dyn_info", "viewall");

        List<DynInfo> info = new ArrayList<DynInfo>();

        try {
            // NfcTags
            List<NfcTag> mNfcList = getNfcTagList();

            JSONObject json = new JSONObject(response);
            JSONArray array = json.getJSONArray("items");

            for (int index = 0; index < array.length(); index++) {
                JSONObject obj = array.getJSONObject(index);

                int id = obj.getInt("id");
                int studentId = obj.getInt("studentId");
                String nfcTag = obj.getString("nfcTag");
                String geo = obj.getString("geo");
                String type = obj.getString("type");
                String createTimestamp = obj.getString("createTimestamp");

                DynInfo dynInfo = new DynInfo();
                dynInfo.setId(id);
                dynInfo.setStudentId(studentId);
                dynInfo.setNfcTag(nfcTag);
                dynInfo.setGeo(geo);
                dynInfo.setType(type);
                dynInfo.setLocation("未知");
                SimpleDateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");
                Date date =  formatter.parse(createTimestamp);
                dynInfo.setCreateTimestamp(date);

                String str = new String();
                str += createTimestamp;
                for (NfcTag nfcTag1 : mNfcList) {
                    if (nfcTag1.getTag().equals(nfcTag)) {
                        str += nfcTag1.getDefine();
                        break;
                    }
                }

                String[] s = geo.split(",");
                if (s.length == 2) {
                    List<String> address = geocoder.getFromLocation(s[0], s[1]);
                    if (address.size() > 0) {
                        //str += "(" + address.get(0) + ")";
                        dynInfo.setLocation(address.get(0));
                    }
                }
                if (type.equals("check_in")) {
                    str += "签入";
                } else {
                    str += "签出";
                }
                dynInfo.setCheckState(str);

                info.add(dynInfo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return info;
    }
}