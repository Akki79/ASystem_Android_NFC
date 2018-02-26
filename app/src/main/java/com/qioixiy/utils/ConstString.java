package com.qioixiy.utils;

public class ConstString {

    static String sServerPrefix = "http://192.168.2.153:8080";

    public static void setServerPrefix(String pref) {
        sServerPrefix = pref;
    }

    public static String getServerString() {
        return sServerPrefix + "/asystem/api/StudentManagerNFC.do";
    }
}
