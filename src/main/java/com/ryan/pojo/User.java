package com.ryan.pojo;

/**
 * @author Ryan Tao
 * @github lemonjing
 */
public class User {
    private String userId;
    private String birthday;
    private String city;
    private int credit;
    private String email;
    private String gender;
    private int gold;
    private String intro;
    private String password;
    private String qq;
    private String username;
    private String userstate;
    private String wechat;
    private String image_id;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserstate() {
        return userstate;
    }

    public void setUserstate(String userstate) {
        this.userstate = userstate;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public String getImage_id() {
        return image_id;
    }

    public void setImage_id(String image_id) {
        this.image_id = image_id;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", birthday='" + birthday + '\'' +
                ", city='" + city + '\'' +
                ", credit=" + credit +
                ", email='" + email + '\'' +
                ", gender='" + gender + '\'' +
                ", gold=" + gold +
                ", intro='" + intro + '\'' +
                ", password='" + password + '\'' +
                ", qq='" + qq + '\'' +
                ", username='" + username + '\'' +
                ", userstate='" + userstate + '\'' +
                ", wechat='" + wechat + '\'' +
                ", image_id='" + image_id + '\'' +
                '}';
    }
}
