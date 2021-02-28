package com.example.deardiary;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.List;

public class Fragment_My_Meetings extends Fragment_Base {

   private RecyclerView my_meetings_LST_all_meetings;
   private  ImageView my_meetings_IMG_background;
   private AllMeetings allMeetings;
   private Adapter_Meeting adapter_meeting;

   private Gson gson;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_meetings,container,false);
        findVies(view);
        initView();
        return view;
    }

    private void findVies(View view) {
        my_meetings_IMG_background=view.findViewById(R.id.my_meetings_IMG_background);
        my_meetings_LST_all_meetings=view.findViewById(R.id.my_meetings_LST_all_meetings);
    }

    private void initView() {
        updateImage(this.getResources().getIdentifier("background",
                "drawable", this.getContext().getPackageName()), my_meetings_IMG_background);
        gson=new Gson();
        readAllMeetings_SP();


        my_meetings_LST_all_meetings.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter_meeting = new Adapter_Meeting(getContext());
        adapter_meeting.setClickListener(new Adapter_Meeting.MyItemClickListener() {
            @Override
            public void onItemClick(View view, Meeting meeting) {
                openMeetingInfo(meeting);
            }

            @Override
            public void onRemoveClick(View view, int position) {
                try {
                    allMeetings.remove(position);
                    my_meetings_LST_all_meetings.removeViewAt(position);
                    adapter_meeting.notifyItemRemoved(position);
                    saveAllMeetings_SP();
                    adapter_meeting.readAllMeetings_SP();
                    adapter_meeting.notifyItemRangeChanged(position, allMeetings.getAllMeetings().size());
                }catch (Exception e){
                }
            }
        });
        my_meetings_LST_all_meetings.setAdapter(adapter_meeting);
    }

    public void updateMeetingListView(){
        adapter_meeting.readAllMeetings_SP();
        adapter_meeting.notifyDataSetChanged();
    }

    private void openMeetingInfo(Meeting meeting) {
        AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                .setTitle(meeting.getSubject())
                .setMessage("Duration: "+meeting.getDuration()+"\nDescription: "+meeting.getDescription()+"\nLocation: "+meeting.getAddress().toString())
                .setPositiveButton("CLose", null).show();


    }


    private void saveAllMeetings_SP() {
        String allMeetings_text=gson.toJson(allMeetings);
        MySP.getInstance().putString(Fragment_New_Meeting.MEETINGS,allMeetings_text);
    }

    private void readAllMeetings_SP() {
        String allMeetings_text=MySP.getInstance().getString(Fragment_New_Meeting.MEETINGS,Fragment_New_Meeting.NO_MEETINGS);
        if(allMeetings_text.equals(Fragment_New_Meeting.NO_MEETINGS))
            allMeetings=new AllMeetings();
        else
            allMeetings=gson.fromJson(allMeetings_text,AllMeetings.class);
    }
}
