package com.example.easeme;

import java.util.List;

public class QsetModel {
    public QsetModel() {
    }

   List<String> qlist,anslist;
    boolean role;

    public QsetModel(List<String> qlist, List<String> anslist, boolean role) {
        this.qlist = qlist;
        this.anslist = anslist;
        this.role = role;
    }


    public List<String> getQlist() {
        return qlist;
    }

    public void setQlist(List<String> qlist) {
        this.qlist = qlist;
    }

    public List<String> getAnslist() {
        return anslist;
    }

    public void setAnslist(List<String> anslist) {
        this.anslist = anslist;
    }

    public boolean isRole() {
        return role;
    }

    public void setRole(boolean role) {
        this.role = role;
    }
}
