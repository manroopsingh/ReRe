
package com.example.manroop.ReRe.pojos.yelpBusinessDetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Open implements Serializable {

    @SerializedName("is_overnight")
    @Expose
    private Boolean isOvernight;
    @SerializedName("end")
    @Expose
    private String end;
    @SerializedName("day")
    @Expose
    private Integer day;
    @SerializedName("start")
    @Expose
    private String start;

    public Boolean getIsOvernight() {
        return isOvernight;
    }

    public void setIsOvernight(Boolean isOvernight) {
        this.isOvernight = isOvernight;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

}
