package com.example.deardiary;

public class Meeting {
    private String subject;
    private String date;
    private String description;
    private Address address;
    private String timeStart;
    private double duration;


    public Meeting(String subject, String date, String description,
                   Address address, String timeStart, double duration) {
        this.subject = subject;
        this.date = date;
        this.description = description;
        this.address = address;
        this.timeStart=timeStart;
        this.duration=duration;
    }
    public Meeting() { }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }
}
