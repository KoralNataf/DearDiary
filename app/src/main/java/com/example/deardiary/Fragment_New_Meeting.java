package com.example.deardiary;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Fragment_New_Meeting extends Fragment_Base {

    public static final String MEETINGS="MEETINGS";
    public static final String NO_MEETINGS="NO_MEETINGS";
    private ImageView new_meeting_IMG_background;
    private EditText new_meeting_EDT_subject;
    private TextView new_meeting_LBL_date;
    private TextView new_meeting_LBL_time;
    private EditText new_meeting_EDT_duration;
    private TextInputLayout new_meeting_EDT_description;
    private EditText new_meeting_EDT_city;
    private EditText new_meeting_EDT_street;
    private EditText new_meeting_EDT_number;
    private TextView new_meeting_LBL_create;

    private int hours,minutes;

    DatePickerDialog.OnDateSetListener setListener;
    private Calendar calendar=Calendar.getInstance();
    private final int year = calendar.get(Calendar.YEAR);
    private final int month = calendar.get(Calendar.MONTH);
    private final int day = calendar.get(Calendar.DAY_OF_MONTH);

    private AllMeetings allMeetings;
    private boolean toast=false;
    private Gson gson;

    private  CallBack callBack;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_meeting,container,false);
        findVies(view);
        initView();
        return view;
    }

    private void findVies(View view) {
        new_meeting_IMG_background=view.findViewById(R.id.new_meeting_IMG_background);
        new_meeting_EDT_subject=view.findViewById(R.id.new_meeting_EDT_subject);
        new_meeting_LBL_date=view.findViewById(R.id.new_meeting_LBL_date);
        new_meeting_LBL_time=view.findViewById(R.id.new_meeting_LBL_time);
        new_meeting_EDT_duration=view.findViewById(R.id.new_meeting_EDT_duration);
        new_meeting_EDT_description=view.findViewById(R.id.new_meeting_EDT_description);
        new_meeting_EDT_city=view.findViewById(R.id.new_meeting_EDT_city);
        new_meeting_EDT_street=view.findViewById(R.id.new_meeting_EDT_street);
        new_meeting_EDT_number=view.findViewById(R.id.new_meeting_EDT_number);
        new_meeting_LBL_create=view.findViewById(R.id.new_meeting_LBL_create);
    }

    private void initView() {
        updateImage(this.getResources().getIdentifier("background",
                "drawable", this.getContext().getPackageName()), new_meeting_IMG_background);
        gson=new Gson();

        new_meeting_LBL_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getContext(), android.R.style.Theme_Holo_Light_Dialog_MinWidth
                        ,setListener,year,month,day);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });
        setListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month=month+1;
                String date = dayOfMonth+"/"+month+"/"+year+"   ";
                new_meeting_LBL_date.setText(date);
            }
        };
        new_meeting_LBL_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Initialize time picker dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        getContext(), android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                //Initialize hour and minute
                                hours = hourOfDay;
                                minutes = minute;
                                //Store hour and minute in string
                                String time = hours + ":"+minutes;
                                //Initialize 24 hours time format
                                SimpleDateFormat f24Hours = new SimpleDateFormat(
                                        "HH:mm"
                                );
                                try {
                                    Date date = f24Hours.parse(time);
                                    //Initialize 12 hours time format
                                    SimpleDateFormat f12Hours = new SimpleDateFormat(
                                            "hh:mm aa"
                                    );
                                    //Set selected time on text view
                                    new_meeting_LBL_time.setText(f24Hours.format(date)+"   ");
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, 12, 0, false
                );
                //Set transparent background
                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                // Displayed previous selected time
                timePickerDialog.updateTime(hours, minutes);
                //show dialog
                timePickerDialog.show();
            }
        });

        new_meeting_LBL_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String subject = new_meeting_EDT_subject.getText().toString();
                String date = new_meeting_LBL_date.getText().toString();
                String timeStart = new_meeting_LBL_time.getText().toString();
                String duration = new_meeting_EDT_duration.getText().toString();
                String description = new_meeting_EDT_description.getEditText().getText().toString();
                String city = new_meeting_EDT_city.getText().toString();
                String street = new_meeting_EDT_street.getText().toString();
                String number = new_meeting_EDT_number.getText().toString();

                if (TextUtils.isEmpty(subject) ||TextUtils.isEmpty(duration) || TextUtils.isEmpty(city) || TextUtils.isEmpty(street) || TextUtils.isEmpty(number) )
                {
                    Toast.makeText(getContext(), "Please Fill All Fields", Toast.LENGTH_SHORT).show();
                    toast=true;
                    return;
                }
                else if (date.equals(getString(R.string.date_format)) || timeStart.equals(getString(R.string.time_format))){
                    Toast.makeText(getContext(), "Date And Time Can Not Be Empty..", Toast.LENGTH_SHORT).show();
                    toast=true;
                    return;
                }

                double durationNum=-1;
                int num=-1;
                try {
                    durationNum = Double.parseDouble(duration);
                    num = Integer.parseInt(number);
                }catch (NumberFormatException e){
                    if(!toast){
                        Toast.makeText(getContext(), "Duration And Location Number Must Be A Numbers..", Toast.LENGTH_SHORT).show();
                        toast=false;
                    }
                    return;
                }

                Meeting meeting = new Meeting(subject,date, description, new Address(city,street,num),timeStart, durationNum);
                readAllMeetings_SP();
                if(!allMeetings.add(meeting))
                    Toast.makeText(getContext(), "Can Not Create A New Meeting..\nThis Time Is Not Available! ", Toast.LENGTH_SHORT).show();
                else{
                    saveAllMeetings_SP();
                    if(callBack != null)
                        callBack.updateList();
                    Toast.makeText(getContext(), "Meeting Created Successfully ", Toast.LENGTH_SHORT).show();
                }
                clearAllFields();

            }
        });
    }

    private void clearAllFields() {
        new_meeting_EDT_subject.getText().clear();
        new_meeting_LBL_date.setText(R.string.date_format);
        new_meeting_LBL_time.setText(R.string.time_format);
        new_meeting_EDT_duration.getText().clear();
        new_meeting_EDT_description.getEditText().getText().clear();
        new_meeting_EDT_city.getText().clear();
        new_meeting_EDT_street.getText().clear();
        new_meeting_EDT_number.getText().clear();

    }

    private void saveAllMeetings_SP() {
        String allMeetings_text=gson.toJson(allMeetings);
        MySP.getInstance().putString(MEETINGS,allMeetings_text);
    }

    private void readAllMeetings_SP() {
        String allMeetings_text=MySP.getInstance().getString(MEETINGS,NO_MEETINGS);
        if(allMeetings_text.equals(NO_MEETINGS))
            allMeetings=new AllMeetings();
        else
            allMeetings=gson.fromJson(allMeetings_text,AllMeetings.class);
    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }
}
