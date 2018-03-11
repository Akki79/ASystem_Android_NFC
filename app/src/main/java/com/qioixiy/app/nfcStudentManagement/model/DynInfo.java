package com.qioixiy.app.nfcStudentManagement.model;

import java.util.Date;

public class DynInfo {
    private Integer id;

    private Integer studentId;

    private String nfcTag;

    private String geo;

    private String type;

    private Date createTimestamp;

    private String checkState;

    private String location;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public String getNfcTag() {
        return nfcTag;
    }

    public void setNfcTag(String nfcTag) {
        this.nfcTag = nfcTag == null ? null : nfcTag.trim();
    }

    public String getGeo() {
        return geo;
    }

    public void setGeo(String geo) {
        this.geo = geo == null ? null : geo.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public Date getCreateTimestamp() {
        return createTimestamp;
    }

    public void setCreateTimestamp(Date createTimestamp) {
        this.createTimestamp = createTimestamp;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public void setCheckState(String checkState) {
        this.checkState = checkState;
    }

    public String getCheckState() {
        return checkState;
    }
}