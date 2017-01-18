package com.example.manroop.ReRe.pojos.reservation;

/**
 * Created by INSPIRON on 15-Jan-17.
 */

public class Reservation1 {
    private String username;
    private String restaurantName;
    private String restaurantId;
    private String date;
    private String time;
    private int partySize;
    private int tableNo;
    private String source;
    private String status;

    public Reservation1(String username, String restaurantName, String restaurantId, String date, String time, int partySize, int tableNo, String source, String status, String generationTime) {
        this.username = username;
        this.restaurantName = restaurantName;
        this.restaurantId = restaurantId;
        this.date = date;
        this.time = time;
        this.partySize = partySize;
        this.tableNo = tableNo;
        this.source = source;
        this.status = status;
        this.generationTime = generationTime;
    }

    public String getGenerationTime() {
        return generationTime;
    }

    public void setGenerationTime(String generationTime) {
        this.generationTime = generationTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getPartySize() {
        return partySize;
    }

    public void setPartySize(int partySize) {
        this.partySize = partySize;
    }

    public int getTableNo() {
        return tableNo;
    }

    public void setTableNo(int tableNo) {
        this.tableNo = tableNo;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String generationTime;


}
