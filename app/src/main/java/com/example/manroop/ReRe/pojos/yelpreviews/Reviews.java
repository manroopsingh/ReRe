
package com.example.manroop.ReRe.pojos.yelpreviews;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Reviews {

    @SerializedName("reviews")
    @Expose
    private List<Review> reviews = null;
    @SerializedName("total")
    @Expose
    private Integer total;

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

}
