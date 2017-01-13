
package com.example.manroop.ReRe.pojos.yelpBusinesses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Region implements Serializable {

    @SerializedName("center")
    @Expose
    private Center center;

    public Center getCenter() {
        return center;
    }

    public void setCenter(Center center) {
        this.center = center;
    }

}
