package com.immo2n.bytelover.Objects;

public class User {
    private String fullname;
    private String image;
    private String email;
    private String phone;
    private String email_verified;
    private String institute;
    private String profession;
    private String degree;
    private String joined;
    private String birthday;
    public User(String fullname, String image, String email, String phone, String email_verified,
                String institute, String profession, String degree, String joined, String birthday){
        this.fullname = fullname;
        this.image = image;
        this.email = email;
        this.phone = phone;
        this.email_verified = email_verified;
        this.institute = institute;
        this.profession = profession;
        this.degree = degree;
        this.joined = joined;
        this.birthday = birthday;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail_verified() {
        return email_verified;
    }

    public void setEmail_verified(String email_verified) {
        this.email_verified = email_verified;
    }

    public String getInstitute() {
        return institute;
    }

    public void setInstitute(String institute) {
        this.institute = institute;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getJoined() {
        return joined;
    }

    public void setJoined(String joined) {
        this.joined = joined;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
}
