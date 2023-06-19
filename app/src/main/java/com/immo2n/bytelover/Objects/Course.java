package com.immo2n.bytelover.Objects;

public class Course {

    private String id, name, price, actual_price, level, batch, slot, course_code, short_description, main_description,
            duration, start_date, end_date, record_expiry_months, course_language, teacher_id, status;

    public Course(String id, String name, String price, String actual_price, String level, String batch,
                  String slot, String course_code, String short_description, String main_description,
                  String duration, String start_date, String end_date, String record_expiry_months,
                  String course_language, String teacher_id, String status) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.actual_price = actual_price;
        this.level = level;
        this.batch = batch;
        this.slot = slot;
        this.course_code = course_code;
        this.short_description = short_description;
        this.main_description = main_description;
        this.duration = duration;
        this.start_date = start_date;
        this.end_date = end_date;
        this.record_expiry_months = record_expiry_months;
        this.course_language = course_language;
        this.teacher_id = teacher_id;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getActualPrice() {
        return actual_price;
    }

    public void setActualPrice(String actual_price) {
        this.actual_price = actual_price;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getSlot() {
        return slot;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }

    public String getCourseCode() {
        return course_code;
    }

    public void setCourseCode(String course_code) {
        this.course_code = course_code;
    }

    public String getShortDescription() {
        return short_description;
    }

    public void setShortDescription(String short_description) {
        this.short_description = short_description;
    }

    public String getMainDescription() {
        return main_description;
    }

    public void setMainDescription(String main_description) {
        this.main_description = main_description;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getStartDate() {
        return start_date;
    }

    public void setStartDate(String start_date) {
        this.start_date = start_date;
    }

    public String getEndDate() {
        return end_date;
    }

    public void setEndDate(String end_date) {
        this.end_date = end_date;
    }

    public String getRecordExpiryMonths() {
        return record_expiry_months;
    }

    public void setRecordExpiryMonths(String record_expiry_months) {
        this.record_expiry_months = record_expiry_months;
    }

    public String getCourseLanguage() {
        return course_language;
    }

    public void setCourseLanguage(String course_language) {
        this.course_language = course_language;
    }

    public String getTeacherId() {
        return teacher_id;
    }

    public void setTeacherId(String teacher_id) {
        this.teacher_id = teacher_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}