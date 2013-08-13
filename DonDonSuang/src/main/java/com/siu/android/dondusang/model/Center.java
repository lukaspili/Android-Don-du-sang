package com.siu.android.dondusang.model;

import com.siu.android.dondusang.util.DateUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by lukas on 8/13/13.
 */
public class Center implements Serializable {

    private String city;
    private String description;
    private double latitude;
    private double longitude;
    private String phone;
    private String email;
    private String region;
    private Date date;
    private Date start;
    private Date end;
    private String type;


    public boolean isPermanent() {
        return null != region;
    }

    public boolean isTemporal() {
        return null != type;
    }

    public String getTitle() {
        if (isPermanent()) {
            return region;
        }

        return "Centre mobile";
    }

    public String getSubtitle() {
        if (isPermanent()) {
            return "Situé à " + city;
        }

        return "Le " + DateUtils.formatAsFull(date) + ", à " + city;
    }


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Center center = (Center) o;

        if (Double.compare(center.latitude, latitude) != 0) return false;
        if (Double.compare(center.longitude, longitude) != 0) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(latitude);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(longitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
