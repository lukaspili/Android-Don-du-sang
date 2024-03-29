package com.siu.android.dondusang.dao.model;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table BLOOD_EVENT.
 */
public class BloodEvent implements java.io.Serializable {

    private Long id;
    private Integer type;
    private java.util.Date date;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public BloodEvent() {
    }

    public BloodEvent(Long id) {
        this.id = id;
    }

    public BloodEvent(Long id, Integer type, java.util.Date date) {
        this.id = id;
        this.type = type;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public java.util.Date getDate() {
        return date;
    }

    public void setDate(java.util.Date date) {
        this.date = date;
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}
