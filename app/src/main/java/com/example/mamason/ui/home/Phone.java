package com.example.mamason.ui.home;

public class Phone {
    private String index;
    private String pname;
    private String pnum;

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getPnum() {
        return pnum;
    }

    public void setPnum(String pnum) {
        this.pnum = pnum;
    }

    public Phone(String pname, String pnum) {
        //this.index = index;
        this.pname = pname;
        this.pnum = pnum;
    }

}

