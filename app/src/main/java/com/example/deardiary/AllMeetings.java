package com.example.deardiary;

import android.widget.Toast;

import java.util.ArrayList;

public class AllMeetings {
    private ArrayList<Meeting> allMeetings;

    public AllMeetings() {
        allMeetings= new ArrayList<>();
    }

    public boolean add(Meeting meeting){
        if(checkAvailability(meeting)) {
            allMeetings.add(meeting);
            return true;
        }
        return false;

    }

    public void setAllMeetings(ArrayList<Meeting> allMeetings) {
        this.allMeetings = allMeetings;
    }

    public ArrayList<Meeting> getAllMeetings() {
        return allMeetings;
    }

    public void remove(int index){
        allMeetings.remove(index);
    }

    private boolean checkAvailability(Meeting m1) {
        if(allMeetings.size() == 0 )
            return true;
        for (Meeting m2 :allMeetings) {
            if(m1.getDate().equals(m2.getDate())){
                double startTime1=makeTimeDouble(m1.getTimeStart());
                double startTime2=makeTimeDouble(m2.getTimeStart());
                double endTime1=startTime1+m1.getDuration();
                double endTime2=startTime2+m2.getDuration();
                if(startTime1 > startTime2 && startTime1 < endTime2)
                    return false;
                else if(startTime1 < startTime2 && startTime1 > endTime2)
                    return false;
            }
        }
        return true;
    }

    private double makeTimeDouble(String timeStart) {
        String time [] = timeStart.split(":");
        double hours= Double.parseDouble(time[0]);
        double minutes=Double.parseDouble(time[1])/60;
        return hours+minutes;

    }


}
