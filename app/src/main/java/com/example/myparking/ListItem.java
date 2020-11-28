package com.example.myparking;

public class ListItem {
    String name;
    String ownername;
    String opentime;
    String closingtime;
    String contactinfo;
    String parkingaddress;
    String parkingimage;

    public ListItem() {
    }

    public ListItem(String name, String ownername, String opentime, String closingtime, String contactinfo, String parkingaddress, String parkingimage) {
        this.name = name;
        this.ownername = ownername;
        this.opentime = opentime;
        this.closingtime = closingtime;
        this.contactinfo = contactinfo;
        this.parkingaddress = parkingaddress;
        this.parkingimage = parkingimage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwnername() {
        return ownername;
    }

    public void setOwnername(String ownername) {
        this.ownername = ownername;
    }

    public String getOpentime() {
        return opentime;
    }

    public void setOpentime(String opentime) {
        this.opentime = opentime;
    }

    public String getClosingtime() {
        return closingtime;
    }

    public void setClosingtime(String closingtime) {
        this.closingtime = closingtime;
    }

    public String getContactinfo() {
        return contactinfo;
    }

    public void setContactinfo(String contactinfo) {
        this.contactinfo = contactinfo;
    }

    public String getParkingaddress() {
        return parkingaddress;
    }

    public void setParkingaddress(String parkingaddress) {
        this.parkingaddress = parkingaddress;
    }

    public String getParkingimage() {
        return parkingimage;
    }

    public void setParkingimage(String parkingimage) {
        this.parkingimage = parkingimage;
    }
}
