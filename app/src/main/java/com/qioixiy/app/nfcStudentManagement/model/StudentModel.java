package com.qioixiy.app.nfcStudentManagement.model;

import java.util.ArrayList;
import java.util.List;

public class StudentModel {

    public List<String> getSelfInfo() {
        List<String> ret = new ArrayList<String>();

        ret.add("查看学员动态信息");
        ret.add("分析统计学员动态信息");

        return ret;
    }
}
