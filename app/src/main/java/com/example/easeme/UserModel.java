package com.example.easeme;
// 1==PATIENT TRUE, 0==HELPER FALSE

//1== MALE TRUE, 0==FEMALE FALSE

public class UserModel {
    public String fname,email,pass,uid,phoneno,age,profession;
    public boolean status,role;
    public String gender;


    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public UserModel() {
    }

    public String getPhoneno() {
        return phoneno;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean isRole() {
        return role;
    }

    public void setRole(boolean role) {
        this.role = role;
    }



    public UserModel(String fname, String lname, String email, String pass, String uid, boolean role, String age) {
        this.fname = fname;

        this.email = email;
        this.pass = pass;
        this.uid = uid;
        this.role = role;
    }
}
