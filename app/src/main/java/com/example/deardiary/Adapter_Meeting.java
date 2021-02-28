package com.example.deardiary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.List;


public class Adapter_Meeting extends RecyclerView.Adapter<Adapter_Meeting.MyViewHolder> {

    private AllMeetings allMeetings;
    private LayoutInflater mInflater;
    private MyItemClickListener mClickListener;
    private Gson gson;

    // data is passed into the constructor
    Adapter_Meeting(Context context) {
        this.mInflater = LayoutInflater.from(context);
        gson= new Gson();
        readAllMeetings_SP();
    }


    private void saveAllMeetings_SP() {
        String allMeetings_text=gson.toJson(allMeetings);
        MySP.getInstance().putString(Fragment_New_Meeting.MEETINGS,allMeetings_text);
    }

    public void readAllMeetings_SP() {
        String allMeetings_text=MySP.getInstance().getString(Fragment_New_Meeting.MEETINGS,Fragment_New_Meeting.NO_MEETINGS);
        if(allMeetings_text.equals(Fragment_New_Meeting.NO_MEETINGS))
            allMeetings=new AllMeetings();
        else
            allMeetings=gson.fromJson(allMeetings_text,AllMeetings.class);
    }

    // inflates the row layout from xml when needed
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_meeting, parent, false);
        return new MyViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Meeting meeting = allMeetings.getAllMeetings().get(position);
        holder.list_meeting_LBL_date.setText(meeting.getDate());
        holder.list_meeting_LBL_time.setText(meeting.getTimeStart());
        holder.list_meeting_LBL_subject.setText(meeting.getSubject());

        holder.list_meeting_BTN_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mClickListener != null){
                    mClickListener.onRemoveClick(view,position);
                }
            }
        });

    }

    // total number of rows
    @Override
    public int getItemCount() {
        return allMeetings.getAllMeetings().size();
    }


    // parent activity will implement this method to respond to click events
    public interface MyItemClickListener {
        public void onItemClick(View view, Meeting meeting);
        public void onRemoveClick(View view,int position);
    }

    // allows clicks events to be caught
    void setClickListener(MyItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // stores and recycles views as they are scrolled off screen
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView list_meeting_BTN_remove;
        TextView list_meeting_LBL_subject;
        TextView list_meeting_LBL_time;
        TextView list_meeting_LBL_date;

        MyViewHolder(View itemView) {
            super(itemView);
            list_meeting_BTN_remove = itemView.findViewById(R.id.list_meeting_BTN_remove);
            list_meeting_LBL_subject = itemView.findViewById(R.id.list_meeting_LBL_subject);
            list_meeting_LBL_time = itemView.findViewById(R.id.list_meeting_LBL_time);
            list_meeting_LBL_date = itemView.findViewById(R.id.list_meeting_LBL_date);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mClickListener != null) {

                        mClickListener.onItemClick(view, allMeetings.getAllMeetings().get(getAdapterPosition()));
                    }
                }
            });

        }

        // convenience method for getting data at click position
        Meeting getItem(int id) {

            return allMeetings.getAllMeetings().get(id);
        }

    }
}